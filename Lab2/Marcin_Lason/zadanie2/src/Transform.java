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

    private static final int NUMBER_OF_INVOCATIONS_TO_PRINT = 5;

    private static final int LOCAL_MAP_INDEX = 0;
    private static final int LOCAL_ITER_INDEX = 1;
    private static final int LOCAL_KEY_INDEX = 2;
    private static final int LOCAL_VALUE_INDEX = 3;
    private static final String EXECUTION_STATS_FIELD_NAME = "$executionStats";
    private static final String CONSTRUCTOR_NAME = "<init>";
    private static final String STATIC_CONSTRUCTOR_NAME = "<clinit>";
    private static final String PRINT_STATS_METHOD_NAME = "$printStats";
    private static final String BUMP_COUNTER = "$bumpCounter";

    public static void main(String[] args) throws IOException {
        String className = getClassName(args);
        String helperClassName = "Helper";
        ClassParser parser = new ClassParser(className);
        if (parser == null) {
            return;
        }

        JavaClass parsedClass = parser.parse();
        ClassGen helperClass = new ClassGen(helperClassName, "java/lang/Object", helperClassName + ".java",
                ACC_PUBLIC, new String[0]);
        JavaClass modifiedClass = modifyClass(parsedClass, helperClass);

        modifiedClass.dump(className);
        helperClass.getJavaClass().dump(helperClassName + ".class");
    }

    private static JavaClass modifyClass(JavaClass originalClass, ClassGen helperClass) {
        ClassGen modifiedClass = new ClassGen(originalClass);
        ConstantPoolGen constantPool = modifiedClass.getConstantPool();
        ConstantPoolGen helperClassConstantPool = helperClass.getConstantPool();

        for (Method method : originalClass.getMethods()) {
            if (isConstructor(method)) {
                continue;
            }

            MethodGen modifiedMethod = new MethodGen(method, modifiedClass.getClassName(), constantPool);
            addStatistics(modifiedClass.getClassName(), helperClass.getClassName(), modifiedMethod, constantPool);
            modifiedMethod.setMaxStack();
            modifiedMethod.setMaxLocals();
            modifiedClass.replaceMethod(method, modifiedMethod.getMethod());
        }

        helperClass.addField(createStatisticsMap(helperClassConstantPool));
        helperClass.addInterface("java.lang.Runnable");
        helperClass.addMethod(createRunMethod(helperClass, helperClassConstantPool));
        helperClass.addMethod(createConstructorForHelperClass(helperClass, helperClassConstantPool));
        addStatisticsMapAndShutdownHook(helperClass, helperClassConstantPool);
        helperClass.addMethod(createCounterMethod(helperClass.getClassName(), helperClassConstantPool));
        helperClass.addMethod(createStatisticsPrintingMethod(helperClass, helperClassConstantPool));
        return modifiedClass.getJavaClass();
    }

    private static Method createConstructorForHelperClass(ClassGen helperClass,
                                                          ConstantPoolGen helperClassConstantPool) {
        InstructionList instructions = new InstructionList();
        instructions.append(new ALOAD(0));
        instructions.append(new INVOKESPECIAL(helperClassConstantPool
                .addMethodref("java/lang/Object", CONSTRUCTOR_NAME, "()V")));
        instructions.append(new RETURN());

        MethodGen newMethod = new MethodGen(ACC_PUBLIC, Type.VOID, new Type[0], new String[0], CONSTRUCTOR_NAME,
                helperClass.getClassName(), instructions, helperClassConstantPool);
        newMethod.setMaxStack();
        newMethod.setMaxLocals();
        return newMethod.getMethod();
    }

    private static Method createCounterMethod(String helperClassName, ConstantPoolGen helperClassConstantPool) {
        InstructionList instructions = new InstructionList();
        instructions.append(new ALOAD(0));
        instructions.append(new GETSTATIC(helperClassConstantPool
                .addFieldref(helperClassName, EXECUTION_STATS_FIELD_NAME, "Ljava/util/Map;")));
        instructions.append(new SWAP());
        instructions.append(new DUP2());
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;"), 2));
        instructions.append(new DUP());

        BranchHandle ifNonNullJump = instructions.append(new IFNONNULL(null));
        instructions.append(new POP());
        instructions.append(new ICONST(0));
        instructions.append(new INVOKESTATIC(helperClassConstantPool
                .addMethodref("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;")));

        InstructionHandle ifNonNullDest = instructions
                .append(new CHECKCAST(helperClassConstantPool.addClass("java.lang.Integer")));
        instructions.append(new INVOKEVIRTUAL(helperClassConstantPool
                .addMethodref("java/lang/Integer", "intValue", "()I")));

        ifNonNullJump.setTarget(ifNonNullDest);
        instructions.append(new ICONST(1));
        instructions.append(new IADD());
        instructions.append(new INVOKESTATIC(helperClassConstantPool
                .addMethodref("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;")));
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Map", "put",
                        "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), 3));
        instructions.append(new POP());
        instructions.append(new RETURN());

        MethodGen runMethod = new MethodGen(ACC_PUBLIC | ACC_STATIC | ACC_SYNTHETIC, Type.VOID, new Type[]{Type.STRING},
                new String[]{"instrName"}, BUMP_COUNTER, helperClassName, instructions, helperClassConstantPool);
        runMethod.setMaxStack();
        return runMethod.getMethod();
    }

    private static void addStatisticsMapAndShutdownHook(ClassGen helperClass, ConstantPoolGen helperClassConstantPool) {
        String className = helperClass.getClassName();
        InstructionList instructions = new InstructionList();

        instructions.append(new NEW(helperClassConstantPool.addClass("java.util.TreeMap")));
        instructions.append(new DUP());
        instructions.append(new INVOKESPECIAL(helperClassConstantPool
                .addMethodref("java/util/TreeMap", CONSTRUCTOR_NAME, "()V")));
        instructions.append(new PUTSTATIC(helperClassConstantPool
                .addFieldref(className, EXECUTION_STATS_FIELD_NAME, "Ljava/util/Map;")));
        instructions.append(new INVOKESTATIC(helperClassConstantPool
                .addMethodref("java/lang/Runtime", "getRuntime", "()Ljava/lang/Runtime;")));
        instructions.append(new NEW(helperClassConstantPool.addClass("java.lang.Thread")));
        instructions.append(new DUP());
        instructions.append(new NEW(helperClassConstantPool.addClass(className)));
        instructions.append(new DUP());
        instructions.append(new INVOKESPECIAL(helperClassConstantPool
                .addMethodref(className, CONSTRUCTOR_NAME, "()V")));
        instructions.append(new INVOKESPECIAL(helperClassConstantPool
                .addMethodref("java/lang/Thread", CONSTRUCTOR_NAME, "(Ljava/lang/Runnable;)V")));
        instructions.append(new INVOKEVIRTUAL(helperClassConstantPool
                .addMethodref("java/lang/Runtime", "addShutdownHook", "(Ljava/lang/Thread;)V")));
        instructions.append(new RETURN());

        MethodGen newMethod = new MethodGen(ACC_STATIC, Type.VOID, Type.NO_ARGS, new String[0], STATIC_CONSTRUCTOR_NAME,
                className, instructions, helperClassConstantPool);
        newMethod.setMaxStack();
        helperClass.addMethod(newMethod.getMethod());
    }

    private static void addStatistics(String className, String helperClassName, MethodGen method,
                                      ConstantPoolGen constantPool) {
        InstructionList instructions = method.getInstructionList();
        InstructionHandle instructionHandle = instructions.getStart();

        do {
            InstructionHandle nextHandle = instructionHandle.getNext();
            Instruction instruction = instructionHandle.getInstruction();
            if (isValidMethod(className, instruction, constantPool)) {
                injectCounterBeforeInstruction(helperClassName, instructions, instructionHandle,
                        method.getExceptionHandlers(), constantPool);
            }
            instructionHandle = nextHandle;
        } while (instructionHandle != null);

        instructions.setPositions();
    }

    private static void injectCounterBeforeInstruction(String helperClassName, InstructionList methodInstructions, InstructionHandle inspectedInstructionHandle, CodeExceptionGen[] exceptionHandlers, ConstantPoolGen constantPool) {
        InstructionList bumpingInstructions = new InstructionList();
        Instruction instruction = inspectedInstructionHandle.getInstruction();
        bumpingInstructions.append(new PUSH(constantPool, instruction.getName()));
        bumpingInstructions.append(new INVOKESTATIC(constantPool
                .addMethodref(helperClassName, BUMP_COUNTER, "(Ljava/lang/String;)V")));
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

    private static Method createStatisticsPrintingMethod(ClassGen newClass, ConstantPoolGen helperClassConstantPool) {
        InstructionList instructions = new InstructionList();
        instructions.append(new GETSTATIC(helperClassConstantPool
                .addFieldref(newClass.getClassName(), EXECUTION_STATS_FIELD_NAME, "Ljava/util/Map;")));
        instructions.append(new DUP());
        instructions.append(new ASTORE(LOCAL_MAP_INDEX));
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Map", "keySet", "()Ljava/util/Set;"), 1));
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Set", "iterator", "()Ljava/util/Iterator;"), 1));
        instructions.append(new DUP());
        instructions.append(new ASTORE(LOCAL_ITER_INDEX));

        InstructionHandle loopEntry = instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Iterator", "hasNext", "()Z"), 1));
        instructions.append(new ICONST(0));

        BranchHandle loopExitJump = instructions.append(new IF_ICMPEQ(null));
        instructions.append(new ALOAD(LOCAL_ITER_INDEX));
        instructions.append(new ALOAD(LOCAL_MAP_INDEX));
        instructions.append(new ALOAD(LOCAL_ITER_INDEX));
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Iterator", "next", "()Ljava/lang/Object;"), 1));
        instructions.append(new DUP());
        instructions.append(new ASTORE(LOCAL_KEY_INDEX));
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;"), 2));
        instructions.append(new DUP());
        instructions.append(new ASTORE(LOCAL_VALUE_INDEX));
        instructions.append(new CHECKCAST(helperClassConstantPool.addClass("java.lang.Integer")));
        instructions.append(new INVOKEVIRTUAL(helperClassConstantPool
                .addMethodref("java/lang/Integer", "intValue", "()I")));
        instructions.append(new ICONST(NUMBER_OF_INVOCATIONS_TO_PRINT));
        instructions.append(new IF_ICMPLT(loopEntry));
        instructions.append(new GETSTATIC(helperClassConstantPool
                .addFieldref("java/lang/System", "out", "Ljava/io/PrintStream;")));
        instructions.append(new PUSH(helperClassConstantPool, "%s    %d%n"));
        instructions.append(new ICONST(2));
        instructions.append(new ANEWARRAY(helperClassConstantPool.addClass(ObjectType.OBJECT)));
        instructions.append(new DUP());
        instructions.append(new ICONST(0));
        instructions.append(new ALOAD(LOCAL_KEY_INDEX));
        instructions.append(new AASTORE());
        instructions.append(new DUP());
        instructions.append(new ICONST(1));
        instructions.append(new ALOAD(LOCAL_VALUE_INDEX));
        instructions.append(new AASTORE());
        instructions.append(new INVOKEVIRTUAL(helperClassConstantPool
                .addMethodref("java/io/PrintStream", "printf",
                        "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream;")));
        instructions.append(new POP());
        instructions.append(new GOTO(loopEntry));
        InstructionHandle loopExit = instructions.append(new RETURN());
        loopExitJump.setTarget(loopExit);

        MethodGen methodGen = new MethodGen(ACC_STATIC | ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[0],
                PRINT_STATS_METHOD_NAME, newClass.getClassName(), instructions, helperClassConstantPool);
        methodGen.setMaxLocals();
        methodGen.setMaxStack();
        return methodGen.getMethod();
    }

    private static boolean isValidMethod(String className, Instruction instruction, ConstantPoolGen constantPool) {
        if (!(instruction instanceof InvokeInstruction)) {
            return true;
        }

        InvokeInstruction invokeInstruction = (InvokeInstruction) instruction;
        ObjectType classType = invokeInstruction.getLoadClassType(constantPool);
        String methodName = invokeInstruction.getMethodName(constantPool);

        if (!className.equals(classType.getClassName()) || methodName.startsWith("m") || methodName.startsWith("M")) {
            return false;
        }

        return true;
    }

    private static Method createRunMethod(ClassGen helperClass, ConstantPoolGen helperClassConstantPool) {
        InstructionList instructions = new InstructionList();
        instructions.append(new INVOKESTATIC(helperClassConstantPool
                .addMethodref(helperClass.getClassName(), PRINT_STATS_METHOD_NAME, "()V")));
        instructions.append(new RETURN());

        MethodGen runMethod = new MethodGen(ACC_PUBLIC | ACC_SYNTHETIC, Type.VOID, new Type[0], new String[0], "run",
                helperClass.getClassName(), instructions, helperClassConstantPool);
        runMethod.setMaxStack();
        return runMethod.getMethod();
    }

    private static Field createStatisticsMap(ConstantPoolGen helperClassConstantPool) {
        return new FieldGen(ACC_PUBLIC | ACC_STATIC, Type.getType(Map.class), EXECUTION_STATS_FIELD_NAME,
                helperClassConstantPool).getField();
    }

    private static boolean isConstructor(Method method) {
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