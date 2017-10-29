import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.*;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.DUP2;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IADD;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.POP;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTSTATIC;
import org.apache.bcel.generic.RETURN;
import org.apache.bcel.generic.SWAP;

import java.io.IOException;
import java.util.Map;

import static org.apache.bcel.Constants.*;


public class Transform {
    private static final int LOCAL_MAP_INDEX = 0;
    private static final int LOCAL_ITER_INDEX = 1;
    private static final int LOCAL_KEY_INDEX = 2;
    private static final int LOCAL_VALUE_INDEX = 3;
    private static final int COUNT_THRESHOLD = 5;
    private static final String EXECUTION_COUNT_FORMAT = "%s    %d%n";
    private static final String EXECUTION_STATS_FIELD_NAME = "$executionStats";
    private static final String CONSTRUCTOR_NAME = "<init>";
    private static final String STATIC_CONSTRUCTOR_NAME = "<clinit>";
    private static final String PRINT_STATS_METHOD_NAME = "$printStats";
    private static final String BUMP_COUNTER = "$bumpCounter";
    private static final String INSTRUCTION_COUNTER_CLASS_NAME = "$InstructionCounter";

    public static void main(String[] args) throws IOException {
        String className = getClassName(args);
        ClassParser parser = new ClassParser(className);
        if (parser == null) {
            return;
        }

        JavaClass parsedClass = parser.parse();
        ClassGen helperClass = new ClassGen(INSTRUCTION_COUNTER_CLASS_NAME,
                "java/lang/Object", INSTRUCTION_COUNTER_CLASS_NAME + ".java", ACC_PUBLIC, new String[0]);
        JavaClass modifiedClass = modifyClass(parsedClass, helperClass);

        modifiedClass.dump(className);
        helperClass.getJavaClass().dump(INSTRUCTION_COUNTER_CLASS_NAME + ".class");
    }

    private static JavaClass modifyClass(JavaClass originalClass, ClassGen helperClass) {
        ClassGen modifiedClass = new ClassGen(originalClass);
        ConstantPoolGen constPool = modifiedClass.getConstantPool();
        ConstantPoolGen counterClassConstPool = helperClass.getConstantPool();

        for (Method method : originalClass.getMethods()) {
            if (isStaticOrNormalConstructor(method)) {
                continue;
            }
            MethodGen newMethod = new MethodGen(method, modifiedClass.getClassName(), constPool);
            addStatistics(modifiedClass.getClassName(), helperClass.getClassName(), newMethod, constPool);
            newMethod.setMaxStack();
            newMethod.setMaxLocals();
            modifiedClass.replaceMethod(method, newMethod.getMethod());
        }
        helperClass.addField(createStatsMap(counterClassConstPool));
        helperClass.addInterface("java.lang.Runnable");
        helperClass.addMethod(createRunMethod(helperClass, counterClassConstPool));
        helperClass.addMethod(defaultConstructorForCounter(helperClass, counterClassConstPool));
        createStatsMapInitializerAndHookRegistrar(helperClass, counterClassConstPool);
        helperClass.addMethod(createInstructionCounterMethod(helperClass.getClassName(), counterClassConstPool));
        helperClass.addMethod(createStatisticsPrintingMethod(helperClass, counterClassConstPool));
        return modifiedClass.getJavaClass();
    }

    private static Method defaultConstructorForCounter(ClassGen counterClass, ConstantPoolGen counterClassConstPool) {
        InstructionList instructionList = new InstructionList();
        instructionList.append(new ALOAD(0));
        instructionList.append(new INVOKESPECIAL(counterClassConstPool.addMethodref("java/lang/Object", CONSTRUCTOR_NAME, "()V")));
        instructionList.append(new RETURN());
        MethodGen methodGen = new MethodGen(ACC_PUBLIC, Type.VOID, new Type[0], new String[0], CONSTRUCTOR_NAME, counterClass.getClassName(), instructionList, counterClassConstPool);
        methodGen.setMaxStack();
        methodGen.setMaxLocals();
        return methodGen.getMethod();
    }

    private static Method createInstructionCounterMethod(String counterClassName, ConstantPoolGen counterClassConstPool) {
        InstructionList instructions = new InstructionList();
        instructions.append(new ALOAD(0));
        instructions.append(new GETSTATIC(counterClassConstPool.addFieldref(counterClassName, EXECUTION_STATS_FIELD_NAME, "Ljava/util/Map;")));
        instructions.append(new SWAP());
        instructions.append(new DUP2());
        instructions.append(new INVOKEINTERFACE(counterClassConstPool.addInterfaceMethodref("java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;"), 2));
        instructions.append(new DUP());
        BranchHandle ifNonNullJump = instructions.append(new IFNONNULL(null));
        instructions.append(new POP());
        instructions.append(new ICONST(0));
        instructions.append(new INVOKESTATIC(counterClassConstPool.addMethodref("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;")));
        InstructionHandle ifNonNullDest = instructions.append(new CHECKCAST(counterClassConstPool.addClass("java.lang.Integer")));
        instructions.append(new INVOKEVIRTUAL(counterClassConstPool.addMethodref("java/lang/Integer", "intValue", "()I")));
        ifNonNullJump.setTarget(ifNonNullDest);
        instructions.append(new ICONST(1));
        instructions.append(new IADD());
        instructions.append(new INVOKESTATIC(counterClassConstPool.addMethodref("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;")));
        instructions.append(new INVOKEINTERFACE(counterClassConstPool.addInterfaceMethodref("java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), 3));
        instructions.append(new POP());
        instructions.append(new RETURN());
        MethodGen runMethod = new MethodGen(ACC_PUBLIC | ACC_STATIC | ACC_SYNTHETIC, Type.VOID, new Type[]{Type.STRING}, new String[]{"instrName"}, BUMP_COUNTER, counterClassName, instructions, counterClassConstPool);
        runMethod.setMaxStack();
        return runMethod.getMethod();
    }

    private static void createStatsMapInitializerAndHookRegistrar(ClassGen counterClass, ConstantPoolGen counterClassConstPool) {
        String className = counterClass.getClassName();
        InstructionList instructions = new InstructionList();
        instructions.append(new NEW(counterClassConstPool.addClass("java.util.TreeMap")));
        instructions.append(new DUP());
        instructions.append(new INVOKESPECIAL(counterClassConstPool.addMethodref("java/util/TreeMap", CONSTRUCTOR_NAME, "()V")));
        instructions.append(new PUTSTATIC(counterClassConstPool.addFieldref(className, EXECUTION_STATS_FIELD_NAME, "Ljava/util/Map;")));
        instructions.append(new INVOKESTATIC(counterClassConstPool.addMethodref("java/lang/Runtime", "getRuntime", "()Ljava/lang/Runtime;")));
        instructions.append(new NEW(counterClassConstPool.addClass("java.lang.Thread")));
        instructions.append(new DUP());
        instructions.append(new NEW(counterClassConstPool.addClass(className)));
        instructions.append(new DUP());
        instructions.append(new INVOKESPECIAL(counterClassConstPool.addMethodref(className, CONSTRUCTOR_NAME, "()V")));
        instructions.append(new INVOKESPECIAL(counterClassConstPool.addMethodref("java/lang/Thread", CONSTRUCTOR_NAME, "(Ljava/lang/Runnable;)V")));
        instructions.append(new INVOKEVIRTUAL(counterClassConstPool.addMethodref("java/lang/Runtime", "addShutdownHook", "(Ljava/lang/Thread;)V")));
        instructions.append(new RETURN());
        MethodGen methodGen = new MethodGen(ACC_STATIC, Type.VOID, Type.NO_ARGS, new String[0], STATIC_CONSTRUCTOR_NAME, className, instructions, counterClassConstPool);
        methodGen.setMaxStack();
        counterClass.addMethod(methodGen.getMethod());
    }

    private static void addStatistics(String className, String counterClassName, MethodGen method, ConstantPoolGen constPool) {
        InstructionList methodInstructions = method.getInstructionList();
        InstructionHandle inspectedInstructionHandle = methodInstructions.getStart();
        do {
            InstructionHandle nextHandle = inspectedInstructionHandle.getNext();
            Instruction instruction = inspectedInstructionHandle.getInstruction();
            if (isNotCallToExternalMethodOrOneStartingWithM(className, instruction, constPool)) {
                injectCounterBeforeInstruction(counterClassName, methodInstructions, inspectedInstructionHandle, method.getExceptionHandlers(), constPool);
            }
            inspectedInstructionHandle = nextHandle;
        } while (inspectedInstructionHandle != null);
        methodInstructions.setPositions();
    }

    private static void injectCounterBeforeInstruction(String counterClassName, InstructionList methodInstructions, InstructionHandle inspectedInstructionHandle, CodeExceptionGen[] exceptionHandlers, ConstantPoolGen constPool) {
        InstructionList bumpingInstructions = new InstructionList();
        Instruction instruction = inspectedInstructionHandle.getInstruction();
        bumpingInstructions.append(new PUSH(constPool, instruction.getName()));
        bumpingInstructions.append(new INVOKESTATIC(constPool.addMethodref(counterClassName, BUMP_COUNTER, "(Ljava/lang/String;)V")));
        InstructionHandle newBranchTarget = methodInstructions.insert(inspectedInstructionHandle, bumpingInstructions);
        methodInstructions.redirectBranches(inspectedInstructionHandle, newBranchTarget);
        for (CodeExceptionGen exceptionHandler : exceptionHandlers) {
            if (exceptionHandler.getStartPC() == inspectedInstructionHandle) {
                exceptionHandler.setStartPC(newBranchTarget);
            }
            if (exceptionHandler.getHandlerPC() == inspectedInstructionHandle) {
                exceptionHandler.setHandlerPC(newBranchTarget);
            }
        }
    }

    private static Method createStatisticsPrintingMethod(ClassGen newClass, ConstantPoolGen counterClassConstPool) {
        InstructionList instructions = new InstructionList();
        instructions.append(new GETSTATIC(counterClassConstPool.addFieldref(newClass.getClassName(), EXECUTION_STATS_FIELD_NAME, "Ljava/util/Map;")));
        instructions.append(new DUP());
        instructions.append(new ASTORE(LOCAL_MAP_INDEX));
        instructions.append(new INVOKEINTERFACE(counterClassConstPool.addInterfaceMethodref("java/util/Map", "keySet", "()Ljava/util/Set;"), 1));
        instructions.append(new INVOKEINTERFACE(counterClassConstPool.addInterfaceMethodref("java/util/Set", "iterator", "()Ljava/util/Iterator;"), 1));
        instructions.append(new DUP());
        instructions.append(new ASTORE(LOCAL_ITER_INDEX));
        InstructionHandle loopEntry = instructions.append(new INVOKEINTERFACE(counterClassConstPool.addInterfaceMethodref("java/util/Iterator", "hasNext", "()Z"), 1));
        instructions.append(new ICONST(0));
        BranchHandle loopExitJump = instructions.append(new IF_ICMPEQ(null));
        instructions.append(new ALOAD(LOCAL_ITER_INDEX));
        instructions.append(new ALOAD(LOCAL_MAP_INDEX));
        instructions.append(new ALOAD(LOCAL_ITER_INDEX));
        instructions.append(new INVOKEINTERFACE(counterClassConstPool.addInterfaceMethodref("java/util/Iterator", "next", "()Ljava/lang/Object;"), 1));
        instructions.append(new DUP());
        instructions.append(new ASTORE(LOCAL_KEY_INDEX));
        instructions.append(new INVOKEINTERFACE(counterClassConstPool.addInterfaceMethodref("java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;"), 2));
        instructions.append(new DUP());
        instructions.append(new ASTORE(LOCAL_VALUE_INDEX));
        instructions.append(new CHECKCAST(counterClassConstPool.addClass("java.lang.Integer")));
        instructions.append(new INVOKEVIRTUAL(counterClassConstPool.addMethodref("java/lang/Integer", "intValue", "()I")));
        instructions.append(new ICONST(COUNT_THRESHOLD));
        instructions.append(new IF_ICMPLT(loopEntry));
        instructions.append(new GETSTATIC(counterClassConstPool.addFieldref("java/lang/System", "out", "Ljava/io/PrintStream;")));
        instructions.append(new PUSH(counterClassConstPool, EXECUTION_COUNT_FORMAT));
        instructions.append(new ICONST(2));
        instructions.append(new ANEWARRAY(counterClassConstPool.addClass(ObjectType.OBJECT)));
        instructions.append(new DUP());
        instructions.append(new ICONST(0));
        instructions.append(new ALOAD(LOCAL_KEY_INDEX));
        instructions.append(new AASTORE());
        instructions.append(new DUP());
        instructions.append(new ICONST(1));
        instructions.append(new ALOAD(LOCAL_VALUE_INDEX));
        instructions.append(new AASTORE());
        instructions.append(new INVOKEVIRTUAL(counterClassConstPool.addMethodref("java/io/PrintStream", "printf", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream;")));
        instructions.append(new POP());
        instructions.append(new GOTO(loopEntry));
        InstructionHandle loopExit = instructions.append(new RETURN());
        loopExitJump.setTarget(loopExit);
        MethodGen methodGen = new MethodGen(ACC_STATIC | ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[0], PRINT_STATS_METHOD_NAME, newClass.getClassName(), instructions, counterClassConstPool);
        methodGen.setMaxLocals();
        methodGen.setMaxStack();
        return methodGen.getMethod();
    }

    private static boolean isNotCallToExternalMethodOrOneStartingWithM(String className, Instruction instruction, ConstantPoolGen constPool) {
        if (!(instruction instanceof InvokeInstruction)) {
            return true;
        }
        InvokeInstruction invokeInstruction = (InvokeInstruction) instruction;
        ObjectType calledMethodClassType = invokeInstruction.getLoadClassType(constPool);
        if (!className.equals(calledMethodClassType.getClassName())) {
            return false;
        }
        String methodName = invokeInstruction.getMethodName(constPool);
        if (methodName.startsWith("m") || methodName.startsWith("M")) {
            return false;
        }
        return true;
    }

    private static Method createRunMethod(ClassGen counterClass, ConstantPoolGen counterClassConstPool) {
        InstructionList instructions = new InstructionList();
        instructions.append(new INVOKESTATIC(counterClassConstPool.addMethodref(counterClass.getClassName(), PRINT_STATS_METHOD_NAME, "()V")));
        instructions.append(new RETURN());
        MethodGen runMethod = new MethodGen(ACC_PUBLIC | ACC_SYNTHETIC, Type.VOID, new Type[0], new String[0], "run", counterClass.getClassName(), instructions, counterClassConstPool);
        runMethod.setMaxStack();
        return runMethod.getMethod();
    }

    private static Field createStatsMap(ConstantPoolGen counterClassConstPool) {
        return new FieldGen(ACC_PUBLIC | ACC_STATIC, Type.getType(Map.class), EXECUTION_STATS_FIELD_NAME, counterClassConstPool).getField();
    }

    private static boolean isStaticOrNormalConstructor(Method method) {
        if (method.getName().equals(CONSTRUCTOR_NAME) || method.getName().equals(STATIC_CONSTRUCTOR_NAME)) {
            return true;
        }
        return false;
    }

    private static String getClassName(String[] args) {
        String className = "";
        if (args.length > 0) {
            className = args[0];
        }
        return className;
    }
}