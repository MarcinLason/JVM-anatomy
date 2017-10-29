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

    private static final String CONSTRUCTOR = "<init>";
    private static final String STATIC_INITIALIZER = "<clinit>";
    private static final String STATISTICS_FIELD = "executionStatistics";
    private static final String COUNTER = "counter";

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
                .addMethodref("java/lang/Object", CONSTRUCTOR, "()V")));
        instructions.append(new RETURN());

        MethodGen newMethod = new MethodGen(ACC_PUBLIC, Type.VOID, new Type[0], new String[0], CONSTRUCTOR,
                helperClass.getClassName(), instructions, helperClassConstantPool);
        newMethod.setMaxStack();
        newMethod.setMaxLocals();
        return newMethod.getMethod();
    }

    private static Method createCounterMethod(String helperClassName, ConstantPoolGen helperClassConstantPool) {
        InstructionList instructions = new InstructionList();
        instructions.append(new ALOAD(0));
        instructions.append(new GETSTATIC(helperClassConstantPool
                .addFieldref(helperClassName, STATISTICS_FIELD, "Ljava/util/Map;")));
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
                new String[]{"instrName"}, COUNTER, helperClassName, instructions, helperClassConstantPool);
        runMethod.setMaxStack();
        return runMethod.getMethod();
    }

    private static void addStatisticsMapAndShutdownHook(ClassGen helperClass, ConstantPoolGen helperClassConstantPool) {
        String className = helperClass.getClassName();
        InstructionList instructions = new InstructionList();

        instructions.append(new NEW(helperClassConstantPool.addClass("java.util.TreeMap")));
        instructions.append(new DUP());
        instructions.append(new INVOKESPECIAL(helperClassConstantPool
                .addMethodref("java/util/TreeMap", CONSTRUCTOR, "()V")));
        instructions.append(new PUTSTATIC(helperClassConstantPool
                .addFieldref(className, STATISTICS_FIELD, "Ljava/util/Map;")));
        instructions.append(new INVOKESTATIC(helperClassConstantPool
                .addMethodref("java/lang/Runtime", "getRuntime", "()Ljava/lang/Runtime;")));
        instructions.append(new NEW(helperClassConstantPool.addClass("java.lang.Thread")));
        instructions.append(new DUP());
        instructions.append(new NEW(helperClassConstantPool.addClass(className)));
        instructions.append(new DUP());
        instructions.append(new INVOKESPECIAL(helperClassConstantPool
                .addMethodref(className, CONSTRUCTOR, "()V")));
        instructions.append(new INVOKESPECIAL(helperClassConstantPool
                .addMethodref("java/lang/Thread", CONSTRUCTOR, "(Ljava/lang/Runnable;)V")));
        instructions.append(new INVOKEVIRTUAL(helperClassConstantPool
                .addMethodref("java/lang/Runtime", "addShutdownHook", "(Ljava/lang/Thread;)V")));
        instructions.append(new RETURN());

        MethodGen newMethod = new MethodGen(ACC_STATIC, Type.VOID, Type.NO_ARGS, new String[0], STATIC_INITIALIZER,
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
                addCounterBeforeInstruction(helperClassName, instructions, instructionHandle,
                        method.getExceptionHandlers(), constantPool);
            }
            instructionHandle = nextHandle;
        } while (instructionHandle != null);

        instructions.setPositions();
    }

    private static void addCounterBeforeInstruction(String helperClassName, InstructionList instructions,
                                                    InstructionHandle instructionHandle,
                                                    CodeExceptionGen[] exceptionHandlers,
                                                    ConstantPoolGen constantPool) {
        InstructionList bumpingInstructions = new InstructionList();
        Instruction instruction = instructionHandle.getInstruction();

        bumpingInstructions.append(new PUSH(constantPool, instruction.getName()));
        bumpingInstructions.append(new INVOKESTATIC(constantPool
                .addMethodref(helperClassName, COUNTER, "(Ljava/lang/String;)V")));

        InstructionHandle branchTarget = instructions.insert(instructionHandle, bumpingInstructions);
        instructions.redirectBranches(instructionHandle, branchTarget);

        for (CodeExceptionGen exceptionHandler : exceptionHandlers) {
            if (exceptionHandler.getStartPC() == instructionHandle) {
                exceptionHandler.setStartPC(branchTarget);
            }
            if (exceptionHandler.getHandlerPC() == instructionHandle) {
                exceptionHandler.setHandlerPC(branchTarget);
            }
        }
    }

    private static Method createStatisticsPrintingMethod(ClassGen helperClass,
                                                         ConstantPoolGen helperClassConstantPool) {
        InstructionList instructions = new InstructionList();
        int mapIndex = 0;
        int iteratorIndex = 1;
        int keyIndex = 2;
        int valueIndex = 3;

        instructions.append(new GETSTATIC(helperClassConstantPool
                .addFieldref(helperClass.getClassName(), STATISTICS_FIELD, "Ljava/util/Map;")));
        instructions.append(new DUP());
        instructions.append(new ASTORE(mapIndex));
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Map", "keySet", "()Ljava/util/Set;"), 1));
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Set", "iterator", "()Ljava/util/Iterator;"), 1));
        instructions.append(new DUP());
        instructions.append(new ASTORE(iteratorIndex));

        InstructionHandle loopEntry = instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Iterator", "hasNext", "()Z"), 1));
        instructions.append(new ICONST(0));

        BranchHandle loopExitJump = instructions.append(new IF_ICMPEQ(null));
        instructions.append(new ALOAD(iteratorIndex));
        instructions.append(new ALOAD(mapIndex));
        instructions.append(new ALOAD(iteratorIndex));
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Iterator", "next", "()Ljava/lang/Object;"), 1));
        instructions.append(new DUP());
        instructions.append(new ASTORE(keyIndex));
        instructions.append(new INVOKEINTERFACE(helperClassConstantPool
                .addInterfaceMethodref("java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;"), 2));
        instructions.append(new DUP());
        instructions.append(new ASTORE(valueIndex));
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
        instructions.append(new ALOAD(keyIndex));
        instructions.append(new AASTORE());
        instructions.append(new DUP());
        instructions.append(new ICONST(1));
        instructions.append(new ALOAD(valueIndex));
        instructions.append(new AASTORE());
        instructions.append(new INVOKEVIRTUAL(helperClassConstantPool
                .addMethodref("java/io/PrintStream", "printf",
                        "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream;")));
        instructions.append(new POP());
        instructions.append(new GOTO(loopEntry));
        InstructionHandle loopExit = instructions.append(new RETURN());
        loopExitJump.setTarget(loopExit);

        MethodGen methodGen = new MethodGen(ACC_STATIC | ACC_PUBLIC, Type.VOID, Type.NO_ARGS, new String[0],
                "printStatistics", helperClass.getClassName(), instructions, helperClassConstantPool);
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
                .addMethodref(helperClass.getClassName(), "printStatistics", "()V")));
        instructions.append(new RETURN());

        MethodGen runMethod = new MethodGen(ACC_PUBLIC | ACC_SYNTHETIC, Type.VOID, new Type[0], new String[0], "run",
                helperClass.getClassName(), instructions, helperClassConstantPool);
        runMethod.setMaxStack();
        return runMethod.getMethod();
    }

    private static Field createStatisticsMap(ConstantPoolGen helperClassConstantPool) {
        return new FieldGen(ACC_PUBLIC | ACC_STATIC, Type.getType(Map.class), STATISTICS_FIELD,
                helperClassConstantPool).getField();
    }

    private static boolean isConstructor(Method method) {
        if (method.getName().equals(CONSTRUCTOR) || method.getName().equals(STATIC_INITIALIZER)) {
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