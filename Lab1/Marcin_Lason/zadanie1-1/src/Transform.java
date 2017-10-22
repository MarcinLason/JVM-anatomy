import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.io.IOException;
import java.io.PrintStream;

public class Transform {
    private static final String PREFIX = "Method to be called: ";
    private static final String RESULT = "Got result: ";

    public static void main(String[] args) {
        JavaClass testClass = getTestClass(args);
        if (testClass == null) {
            return;
        }
        ClassGen modifiedClass = modifyTestClass(testClass);
        saveModifiedClass(modifiedClass);
    }

    private static ClassGen modifyTestClass(JavaClass originalClass) {
        ClassGen modifiedClass = new ClassGen(originalClass);
        InstructionFactory instructionFactory = new InstructionFactory(modifiedClass);
        ConstantPoolGen constantPool = modifiedClass.getConstantPool();

        for (Method method : originalClass.getMethods()) {
            MethodGen modifiedMethod = new MethodGen(method, method.getName(), constantPool);
            InstructionList instructionList = modifiedMethod.getInstructionList();

            for (InstructionHandle handle : instructionList.getInstructionHandles()) {
                if (handle.getInstruction() instanceof InvokeInstruction) {
                    InvokeInstruction instruction = (InvokeInstruction) handle.getInstruction();
                    Type instructionReturnType = instruction.getReturnType(constantPool);

                    if (instructionReturnType != Type.VOID) {
                        String methodMessage = prepareMessage(instruction, constantPool);
                        instructionList.insert(handle, instructionFactory.createPrintln(methodMessage));

                        if (!(instruction instanceof INVOKESPECIAL)) {
                            instructionList.append(handle, buildInstructionList(instructionFactory, instructionReturnType));
                        }
                    }
                }
            }
            modifiedMethod.setMaxStack();
            modifiedClass.replaceMethod(method, modifiedMethod.getMethod());
        }
        return modifiedClass;
    }

    private static InstructionList buildInstructionList(InstructionFactory factory, Type returnType) {
        InstructionList modifiedInstructionList = new InstructionList();

        modifiedInstructionList.append(getSystemOutInstruction(factory));
        modifiedInstructionList.append(factory.createConstant(RESULT));
        modifiedInstructionList.append(getPrintInstruction(factory));
        modifiedInstructionList.append(InstructionFactory.createDup(returnType.getSize()));
        modifiedInstructionList.append(getSystemOutInstruction(factory));

        if (returnType.equals(Type.LONG) || returnType.equals(Type.DOUBLE)) {
            modifiedInstructionList.append(InstructionFactory.DUP_X2);
            modifiedInstructionList.append(InstructionFactory.POP);
        } else {
            modifiedInstructionList.append(InstructionFactory.SWAP);
        }

        modifiedInstructionList.append(getPrintlnInstruction(factory, checkReturnedType(returnType)));
        return modifiedInstructionList;
    }

    private static String getTestClassName(String[] args) {
        String testClassName = "";
        if (args.length > 0) {
            testClassName = args[0].replace(".class", "");
        }
        return testClassName;
    }

    private static JavaClass getTestClass(String[] args) {
        JavaClass testClass = null;
        try {
            testClass = Repository.lookupClass(getTestClassName(args));
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return testClass;
    }

    private static void saveModifiedClass(ClassGen modifiedClass) {
        String path = Repository.lookupClassFile(modifiedClass.getClassName()).getPath();
        try {
            modifiedClass.getJavaClass().dump(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Type checkReturnedType(Type returnType) {
        if (returnType.getSignature().contains("/") || returnType.getSignature().contains("[")) {
            return new ObjectType("java.lang.Object");
        }
        return returnType;
    }

    private static String prepareMessage(InvokeInstruction instruction, ConstantPoolGen cp) {
        return PREFIX + instruction.getMethodName(cp) + instruction.getSignature(cp);
    }

    private static Instruction getSystemOutInstruction(InstructionFactory factory) {
        return factory.createFieldAccess(
                System.class.getCanonicalName(), "out", new ObjectType(PrintStream.class.getCanonicalName()),
                Constants.GETSTATIC);
    }

    private static Instruction getPrintInstruction(InstructionFactory factory) {
        return factory.createInvoke(
                PrintStream.class.getCanonicalName(), "print", Type.VOID, new Type[]{new ObjectType("java.lang.String")},
                Constants.INVOKEVIRTUAL);
    }

    private static Instruction getPrintlnInstruction(InstructionFactory factory, Type type) {
        return factory.createInvoke(
                PrintStream.class.getCanonicalName(),"println", Type.VOID, new Type[]{type},
                Constants.INVOKEVIRTUAL);
    }
}
