import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProblemSixSolver {

    private final JavaClass classWithMethodsToInline;
    private ConstantPoolGen modifiedClassConstPool;

    public ProblemSixSolver(JavaClass classWithMethodsToInline) {
        this.classWithMethodsToInline = classWithMethodsToInline;
    }

    public static void main(String[] args) throws IOException {
        String callerClassFile = args[0] + ".class";
        ClassParser parser = new ClassParser(callerClassFile);
        JavaClass classBeingRewritten = parser.parse();

        String classToInlineFile = args[1] + ".class";
        JavaClass inlinedClass = new ClassParser(classToInlineFile).parse();

        ProblemSixSolver callInliner = new ProblemSixSolver(inlinedClass);
        JavaClass inlinedCaller = callInliner.inlineMethodCalls(classBeingRewritten);

        inlinedCaller.dump(callerClassFile);
    }

    private JavaClass inlineMethodCalls(JavaClass classBeingRewritten) {
        ClassGen newClass = new ClassGen(classBeingRewritten);
        modifiedClassConstPool = newClass.getConstantPool();

        for (Method rewrittenMethod : newClass.getMethods()) {
            MethodGen newMethod = new MethodGen(rewrittenMethod, classBeingRewritten.getClassName(), modifiedClassConstPool);
            inlineCalls(newMethod);
            newClass.replaceMethod(rewrittenMethod, newMethod.getMethod());
        }
        return newClass.getJavaClass();
    }

    private void inlineCalls(MethodGen newMethod) {
        InstructionList instructions = newMethod.getInstructionList();
        InstructionHandle inspectedInstructionHandle = instructions.getStart();
        do {
            InstructionHandle nextHandle = inspectedInstructionHandle.getNext();
            Instruction instruction = inspectedInstructionHandle.getInstruction();

            if (isMethodCall(instruction) && shouldBeInlined(instruction)) {
                InstructionList instructionsToPrepend = inlineMethod(newMethod, (InvokeInstruction) instruction, nextHandle);
                instructions.insert(inspectedInstructionHandle, instructionsToPrepend);
                try {
                    instructions.delete(inspectedInstructionHandle, inspectedInstructionHandle);
                } catch (TargetLostException e) {
                    //ignore
                }
            }
            inspectedInstructionHandle = nextHandle;
        } while (inspectedInstructionHandle != null);

        instructions.setPositions();
    }

    private InstructionList inlineMethod(MethodGen callingMethod, InvokeInstruction instruction, InstructionHandle instructionAfterInlinedCall) {
        INVOKEVIRTUAL methodInvocation = (INVOKEVIRTUAL) instruction;

        //find the method

        Method matchingMethod = getCalledMethod(methodInvocation);
        // change max locals and max stack of the calling method

        int calledMethodLocalsOffset = callingMethod.getMaxLocals();
        updateCallersMaxStackAndLocals(callingMethod, matchingMethod);

        // find number of arguments (+ this)
        // load vars from stack (note the types)

        InstructionList stackLoadingInstructions = loadLocalsFromStack(matchingMethod, calledMethodLocalsOffset);

        // add guard code

        InstructionList checkInstructions = new InstructionList();
        checkInstructions.append(new ALOAD(calledMethodLocalsOffset));
        checkInstructions.append(new INVOKEVIRTUAL(modifiedClassConstPool.addMethodref("java/lang/Object", "getClass", "()Ljava/lang/Class;")));
        checkInstructions.append(new PUSH(modifiedClassConstPool, classWithMethodsToInline.getClassName()));
        checkInstructions.append(new INVOKESTATIC(modifiedClassConstPool.addMethodref("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;")));
        BranchHandle checkJumpInstruction = checkInstructions.append(new IF_ACMPEQ(null));
        checkInstructions.append(unloadLocalsToStack(matchingMethod, calledMethodLocalsOffset));
        checkInstructions.append(methodInvocation);
        checkInstructions.append(new GOTO(instructionAfterInlinedCall));

        // renumber local vars accesses
        // replace returns with gotos

        Map<InstructionHandle, InstructionHandle> oldHandleToNewHandle = new HashMap<InstructionHandle, InstructionHandle>();
        Map<BranchHandle, InstructionHandle> branchToNeededHandle = new HashMap<BranchHandle, InstructionHandle>();

        InstructionList inlinedMethodCode = new InstructionList(matchingMethod.getCode().getCode());
        inlinedMethodCode.replaceConstantPool(new ConstantPoolGen(matchingMethod.getConstantPool()), modifiedClassConstPool);

        InstructionList translatedMethodInstructions = new InstructionList();
        Iterator inlinedMethodCodeIterator = inlinedMethodCode.iterator();

        while (inlinedMethodCodeIterator.hasNext()) {
            InstructionHandle instructionHandle = (InstructionHandle) inlinedMethodCodeIterator.next();
            Instruction instructionFromInlinedMethod = instructionHandle.getInstruction();
            InstructionHandle newHandle;
            if (isReturnInstruction(instructionFromInlinedMethod)) {
                newHandle = translatedMethodInstructions.append(new GOTO(instructionAfterInlinedCall));
            } else if (instructionFromInlinedMethod instanceof LocalVariableInstruction) {
                LocalVariableInstruction localVarInstr = (LocalVariableInstruction) instructionFromInlinedMethod;
                localVarInstr.setIndex(localVarInstr.getIndex() + calledMethodLocalsOffset);
                newHandle = translatedMethodInstructions.append(localVarInstr);
            } else {
                if (instructionFromInlinedMethod instanceof BranchInstruction) {
                    BranchInstruction branchInstruction = (BranchInstruction) instructionFromInlinedMethod;
                    InstructionHandle neededTarget = branchInstruction.getTarget();
                    newHandle = translatedMethodInstructions.append(branchInstruction);
                    branchToNeededHandle.put((BranchHandle) newHandle, neededTarget);
                } else {
                    newHandle = translatedMethodInstructions.append(instructionFromInlinedMethod);
                }
            }
            oldHandleToNewHandle.put(instructionHandle, newHandle);
        }

        for (Map.Entry<BranchHandle, InstructionHandle> branchToTarget : branchToNeededHandle.entrySet()) {
            InstructionHandle newTarget = oldHandleToNewHandle.get(branchToTarget.getValue());
            branchToTarget.getKey().setTarget(newTarget);
        }

        checkJumpInstruction.setTarget(translatedMethodInstructions.getInstructionHandles()[0]);

        stackLoadingInstructions.append(checkInstructions);
        stackLoadingInstructions.append(translatedMethodInstructions);

        return stackLoadingInstructions;
    }

    private InstructionList unloadLocalsToStack(Method matchingMethod, int calledMethodLocalsOffset) {
        InstructionList localsUnloadingInstructions = new InstructionList();
        Type[] argumentTypes = matchingMethod.getArgumentTypes();
        int nextLocalIndex = calledMethodLocalsOffset;

        localsUnloadingInstructions.append(new ALOAD(nextLocalIndex++));
        for (int i = 0; i < argumentTypes.length; i++) {
            Type argumentType = argumentTypes[i];

            if (argumentType instanceof BasicType) {
                localsUnloadingInstructions.append(new ILOAD(nextLocalIndex++));
            } else {
                localsUnloadingInstructions.append(new ALOAD(nextLocalIndex++));
            }
        }

        return localsUnloadingInstructions;
    }

    private InstructionList loadLocalsFromStack(Method matchingMethod, int calledMethodLocalsOffset) {
        InstructionList stackLoadingInstructions = new InstructionList();
        Type[] argumentTypes = matchingMethod.getArgumentTypes();
        int nextLocalIndex = calledMethodLocalsOffset + argumentTypes.length;

        for (int i = argumentTypes.length - 1; i >= 0; i--) {
            Type argumentType = argumentTypes[i];
            if (argumentType instanceof BasicType) {
                stackLoadingInstructions.append(new ISTORE(nextLocalIndex--));
            } else {
                stackLoadingInstructions.append(new ASTORE(nextLocalIndex--));
            }
        }
        stackLoadingInstructions.append(new ASTORE(nextLocalIndex));

        return stackLoadingInstructions;
    }

    private boolean isReturnInstruction(Instruction instruction) {
        return instruction instanceof ReturnInstruction;
    }

    private void updateCallersMaxStackAndLocals(MethodGen callingMethod, Method matchingMethod) {
        Code matchingMethodCode = matchingMethod.getCode();
        callingMethod.setMaxStack(callingMethod.getMaxStack() + matchingMethodCode.getMaxStack());
        callingMethod.setMaxLocals(callingMethod.getMaxLocals() + matchingMethodCode.getMaxLocals());
    }

    private Method getCalledMethod(INVOKEVIRTUAL methodInvocation) {
        Method matchingMethod = null;
        for (Method method : classWithMethodsToInline.getMethods()) {
            boolean namesMatch = method.getName().equals(methodInvocation.getMethodName(modifiedClassConstPool));
            boolean signatureMatches = method.getSignature().equals(methodInvocation.getSignature(modifiedClassConstPool));

            if (namesMatch && signatureMatches) {
                matchingMethod = method;
            }
        }

        if (matchingMethod == null) {
            throw new IllegalStateException("No matching method found");
        }
        return matchingMethod;
    }

    private boolean shouldBeInlined(Instruction methodCall) {
        InvokeInstruction instruction = (InvokeInstruction) methodCall;

        boolean isCallForInlinedClass = instruction.getClassName(modifiedClassConstPool).equals(classWithMethodsToInline.getClassName());
        return isCallForInlinedClass && !(instruction instanceof INVOKESPECIAL || instruction instanceof INVOKESTATIC);

    }

    private boolean isMethodCall(Instruction instruction) {
        return instruction instanceof InvokeInstruction;
    }
}
