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

    private static final String NO_MATCHING_METHOD = "No matching method found!";
    private final JavaClass sourceClass;
    private ConstantPoolGen modifyingClassConstPool;

    public ProblemSixSolver(JavaClass sourceClass) {
        this.sourceClass = sourceClass;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Invalid amount of arguments!");
            return;
        }
        String toModifyClassName = args[0] + ".class";

        JavaClass toModifyClass = parseForClassName(toModifyClassName);
        JavaClass inlineSourceClass = parseForClassName(args[1] + ".class");

        ProblemSixSolver solver = new ProblemSixSolver(inlineSourceClass);
        JavaClass modifiedClass = solver.modifyMethods(toModifyClass);

        modifiedClass.dump(toModifyClassName);
    }

    private JavaClass modifyMethods(JavaClass classToModify) {
        ClassGen modifyingClass = new ClassGen(classToModify);
        modifyingClassConstPool = modifyingClass.getConstantPool();

        for (Method method : modifyingClass.getMethods()) {
            MethodGen modifiedMethod = new MethodGen(method, classToModify.getClassName(), modifyingClassConstPool);
            inlineUsages(modifiedMethod);
            modifyingClass.replaceMethod(method, modifiedMethod.getMethod());
        }
        return modifyingClass.getJavaClass();
    }

    private void inlineUsages(MethodGen modifiedMethod) {
        InstructionList instructions = modifiedMethod.getInstructionList();
        InstructionHandle instructionHandle = instructions.getStart();

        do {
            InstructionHandle nextHandle = instructionHandle.getNext();
            Instruction instruction = instructionHandle.getInstruction();

            if (instruction instanceof InvokeInstruction && shouldBeInlined((InvokeInstruction) instruction)) {
                InstructionList inlinedInstructions = inlineMethod(modifiedMethod, (InvokeInstruction) instruction,
                        nextHandle);
                instructions.insert(instructionHandle, inlinedInstructions);

                try {
                    instructions.delete(instructionHandle, instructionHandle);
                } catch (TargetLostException e) {
                }
            }
            instructionHandle = nextHandle;
        } while (instructionHandle != null);
        instructions.setPositions();
    }

    private InstructionList inlineMethod(MethodGen modifyingMethod, InvokeInstruction instruction,
                                         InstructionHandle nextInstruction) {
        INVOKEVIRTUAL methodInvocation = (INVOKEVIRTUAL) instruction;
        Method matchingMethod = findMatchingMethod(methodInvocation);
        int modifyingMethodLocalsOffset = modifyingMethod.getMaxLocals();

        updateCallersMaxStackAndLocals(modifyingMethod, matchingMethod);

        InstructionList stackInstructions = loadLocals(matchingMethod, modifyingMethodLocalsOffset);
        InstructionList checkInstructions = new InstructionList();
        BranchHandle checkJumpInstruction = prepareCheckInstructions(checkInstructions, modifyingMethodLocalsOffset,
                matchingMethod, methodInvocation, nextInstruction);

        Map<InstructionHandle, InstructionHandle> handleMapper = new HashMap<InstructionHandle, InstructionHandle>();
        Map<BranchHandle, InstructionHandle> branchToHandle = new HashMap<BranchHandle, InstructionHandle>();
        InstructionList sourceMethodCode = new InstructionList(matchingMethod.getCode().getCode());

        sourceMethodCode.replaceConstantPool(new ConstantPoolGen(matchingMethod.getConstantPool()),
                modifyingClassConstPool);
        InstructionList translatedMethodInstructions = new InstructionList();
        Iterator sourceMethodCodeIterator = sourceMethodCode.iterator();

        while (sourceMethodCodeIterator.hasNext()) {

            InstructionHandle handle = (InstructionHandle) sourceMethodCodeIterator.next();
            Instruction sourceMethod = handle.getInstruction();
            InstructionHandle newHandle;

            if (sourceMethod instanceof ReturnInstruction) {
                newHandle = translatedMethodInstructions.append(new GOTO(nextInstruction));
            } else if (sourceMethod instanceof LocalVariableInstruction) {
                LocalVariableInstruction localVariableInstruction = (LocalVariableInstruction) sourceMethod;
                localVariableInstruction.setIndex(localVariableInstruction.getIndex() + modifyingMethodLocalsOffset);
                newHandle = translatedMethodInstructions.append(localVariableInstruction);
            } else {
                if (sourceMethod instanceof BranchInstruction) {

                    BranchInstruction branchInstruction = (BranchInstruction) sourceMethod;
                    InstructionHandle target = branchInstruction.getTarget();
                    newHandle = translatedMethodInstructions.append(branchInstruction);
                    branchToHandle.put((BranchHandle) newHandle, target);
                } else {
                    newHandle = translatedMethodInstructions.append(sourceMethod);
                }
            }
            handleMapper.put(handle, newHandle);
        }

        for (Map.Entry<BranchHandle, InstructionHandle> branchToTarget : branchToHandle.entrySet()) {
            InstructionHandle newTarget = handleMapper.get(branchToTarget.getValue());
            branchToTarget.getKey().setTarget(newTarget);
        }

        checkJumpInstruction.setTarget(translatedMethodInstructions.getInstructionHandles()[0]);
        stackInstructions.append(checkInstructions);
        stackInstructions.append(translatedMethodInstructions);

        return stackInstructions;
    }

    private BranchHandle prepareCheckInstructions(InstructionList instructions, int offset, Method matchingMethod,
                                                  INVOKEVIRTUAL invocation, InstructionHandle nextInstruction) {
        instructions.append(new ALOAD(offset));
        instructions.append(new INVOKEVIRTUAL(modifyingClassConstPool
                .addMethodref("java/lang/Object", "getClass", "()Ljava/lang/Class;")));
        instructions.append(new PUSH(modifyingClassConstPool, sourceClass.getClassName()));
        instructions.append(new INVOKESTATIC(modifyingClassConstPool
                .addMethodref("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;")));
        BranchHandle checkJumpInstruction = instructions.append(new IF_ACMPEQ(null));
        instructions.append(unloadLocals(matchingMethod, offset));
        instructions.append(invocation);
        instructions.append(new GOTO(nextInstruction));
        return checkJumpInstruction;
    }

    private InstructionList unloadLocals(Method matchingMethod, int offset) {
        InstructionList instructions = new InstructionList();
        Type[] argumentTypes = matchingMethod.getArgumentTypes();
        int nextLocalIndex = offset;
        instructions.append(new ALOAD(nextLocalIndex++));

        for (int i = 0; i < argumentTypes.length; i++) {
            Type argumentType = argumentTypes[i];
            if (argumentType instanceof BasicType) {
                instructions.append(new ILOAD(nextLocalIndex++));
            } else {
                instructions.append(new ALOAD(nextLocalIndex++));
            }
        }
        return instructions;
    }

    private InstructionList loadLocals(Method matchingMethod, int offset) {
        InstructionList instructions = new InstructionList();
        Type[] argumentTypes = matchingMethod.getArgumentTypes();
        int argumentTypesLength = argumentTypes.length;
        int nextLocalIndex = offset + argumentTypesLength;

        for (int i = argumentTypesLength - 1; i >= 0; i--) {
            Type argumentType = argumentTypes[i];

            if (argumentType instanceof BasicType) {
                instructions.append(new ISTORE(nextLocalIndex--));
            } else {
                instructions.append(new ASTORE(nextLocalIndex--));
            }
        }
        instructions.append(new ASTORE(nextLocalIndex));
        return instructions;
    }

    private Method findMatchingMethod(INVOKEVIRTUAL methodInvocation) {
        Method matchingMethod = null;

        for (Method method : sourceClass.getMethods()) {
            if (method.getName().equals(methodInvocation.getMethodName(modifyingClassConstPool))
                    && method.getSignature().equals(methodInvocation.getSignature(modifyingClassConstPool))) {
                matchingMethod = method;
            }
        }
        if (matchingMethod == null) {
            throw new IllegalStateException(NO_MATCHING_METHOD);
        }
        return matchingMethod;
    }

    private void updateCallersMaxStackAndLocals(MethodGen modifyingMethod, Method matchingMethod) {
        Code matchingMethodCode = matchingMethod.getCode();

        modifyingMethod.setMaxStack(modifyingMethod.getMaxStack() + matchingMethodCode.getMaxStack());
        modifyingMethod.setMaxLocals(modifyingMethod.getMaxLocals() + matchingMethodCode.getMaxLocals());
    }

    private boolean shouldBeInlined(InvokeInstruction instruction) {
        boolean isCallForInlinedClass = instruction.getClassName(modifyingClassConstPool)
                .equals(sourceClass.getClassName());

        return isCallForInlinedClass && !(instruction instanceof INVOKESPECIAL || instruction instanceof INVOKESTATIC);
    }

    private static JavaClass parseForClassName(String callerClassFile) throws IOException {
        return new ClassParser(callerClassFile).parse();
    }
}
