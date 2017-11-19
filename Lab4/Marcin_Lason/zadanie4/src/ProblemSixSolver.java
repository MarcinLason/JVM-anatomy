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
        if (args.length != 2) {
            System.err.println("Invalid amount of arguments!");
            return;
        }

        String toModifyClassName = args[0] + ".class";
        String inlineSourceClassName = args[1] + ".class";

        JavaClass toModifyClass = parseForClassName(toModifyClassName);
        JavaClass inlineSourceClass = parseForClassName(inlineSourceClassName);

        ProblemSixSolver solver = new ProblemSixSolver(inlineSourceClass);
        JavaClass modifiedClass = solver.inlineMethodCalls(toModifyClass);

        modifiedClass.dump(toModifyClassName);
    }

    private JavaClass inlineMethodCalls(JavaClass classToModify) {
        ClassGen modifyingClass = new ClassGen(classToModify);
        modifiedClassConstPool = modifyingClass.getConstantPool();

        for (Method method : modifyingClass.getMethods()) {
            MethodGen modifiedMethod = new MethodGen(method, classToModify.getClassName(), modifiedClassConstPool);
            inlineCalls(modifiedMethod);
            modifyingClass.replaceMethod(method, modifiedMethod.getMethod());
        }
        return modifyingClass.getJavaClass();
    }

    private void inlineCalls(MethodGen modifiedMethod) {
        InstructionList instructions = modifiedMethod.getInstructionList();
        InstructionHandle inspectedInstructionHandle = instructions.getStart();
        do {
            InstructionHandle nextHandle = inspectedInstructionHandle.getNext();
            Instruction instruction = inspectedInstructionHandle.getInstruction();

            if (isMethodCall(instruction) && shouldBeInlined(instruction)) {
                InstructionList instructionsToPrepend = inlineMethod(modifiedMethod, (InvokeInstruction) instruction, nextHandle);
                instructions.insert(inspectedInstructionHandle, instructionsToPrepend);
                try {
                    instructions.delete(inspectedInstructionHandle, inspectedInstructionHandle);
                } catch (TargetLostException e) {
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

    private static JavaClass parseForClassName(String callerClassFile) throws IOException {
        return new ClassParser(callerClassFile).parse();
    }
}
