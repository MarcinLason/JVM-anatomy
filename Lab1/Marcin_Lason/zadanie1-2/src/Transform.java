import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Transform {
    private static final List<BasicType> NUMERIC_TYPES = Arrays.asList(Type.BYTE, Type.SHORT, Type.INT, Type.LONG,
            Type.FLOAT, Type.DOUBLE);
    private static final String GETFIELD_MESSAGE = "Before getfield:%n    %s%n    %s%n    %s%n    %s%n";
    private static final String BIGGER_VALUE_MESSAGE = "    !the value is greater than 30!";

    public static void main(String[] args) throws IOException {
        String className = getClassName(args);
        ClassParser parser = new ClassParser(className);
        if (parser == null) {
            return;
        }

        JavaClass parsedClass = parser.parse();
        JavaClass modifiedClass = modifyClass(parsedClass);
        modifiedClass.dump(className);
    }

    private static JavaClass modifyClass(JavaClass originalClass) {
        ClassGen modifiedClass = new ClassGen(originalClass);
        ConstantPoolGen constantPool = modifiedClass.getConstantPool();

        for (Method method : originalClass.getMethods()) {
            MethodGen modifiedMethod = new MethodGen(method, modifiedClass.getClassName(), constantPool);
            InstructionList modifiedMethodInstructions = modifiedMethod.getInstructionList();
            InstructionHandle inspectedInstructionHandle = modifiedMethodInstructions.getStart();

            modifiedMethod.setMaxStack(modifiedMethod.getMaxStack() + 6);
            modifiedMethod.setMaxLocals(modifiedMethod.getMaxLocals() + 1);
            int localVariableIndex = modifiedMethod.getMaxLocals() - 1;

            do {
                InstructionHandle nextHandle = inspectedInstructionHandle.getNext();
                Instruction instruction = inspectedInstructionHandle.getInstruction();

                if (isPrimitive(instruction, constantPool)) {
                    GETFIELD getFieldInstruction = (GETFIELD) instruction;
                    Type accessedFieldType = getFieldInstruction.getFieldType(constantPool);

                    InstructionList instructionsToPrepend = printFieldInfo(localVariableIndex,
                            getFieldInstruction, accessedFieldType, constantPool);
                    modifiedMethodInstructions.insert(instruction, instructionsToPrepend);

                    if (NUMERIC_TYPES.contains(accessedFieldType)) {
                        InstructionList valueCheckInstructions = checkResultsValue(inspectedInstructionHandle,
                                accessedFieldType, constantPool);
                        modifiedMethodInstructions.append(instruction, valueCheckInstructions);
                    }
                }
                inspectedInstructionHandle = nextHandle;
            } while (inspectedInstructionHandle != null);

            modifiedMethodInstructions.setPositions();
            modifiedClass.replaceMethod(method, modifiedMethod.getMethod());
        }
        return modifiedClass.getJavaClass();
    }

    private static InstructionList printFieldInfo(int localVariableIndex, GETFIELD getFieldInstruction,
                                                  Type accessedFieldType, ConstantPoolGen constantPool) {
        InstructionList instructions = new InstructionList();
        String targetType = getFieldInstruction.getLoadClassType(constantPool).getClassName();
        String fieldType = accessedFieldType.toString();
        String fieldName = getFieldInstruction.getFieldName(constantPool);

        instructions.append(new DUP());
        instructions.append(new ASTORE(localVariableIndex));
        instructions.append(new GETSTATIC(getSystemOutIndex(constantPool)));
        instructions.append(new PUSH(constantPool, GETFIELD_MESSAGE));
        instructions.append(new PUSH(constantPool, 4));
        instructions.append(new ANEWARRAY(getObjectClassIndex(constantPool)));

        insertStringIntoArray(instructions, constantPool, 0, targetType);
        insertStringIntoArray(instructions, constantPool, 1, fieldType);
        insertStringIntoArray(instructions, constantPool, 2, fieldName);
        insertFieldValueIntoArray(instructions, constantPool, 3, localVariableIndex, getFieldInstruction);

        instructions.append(new INVOKEVIRTUAL(getPrintStreamPrintfIndex(constantPool)));
        instructions.append(new POP());
        return instructions;
    }

    private static InstructionList checkResultsValue(InstructionHandle instructionHandle, Type accessedFieldType,
                                                     ConstantPoolGen constantPool) {
        InstructionList instructions = new InstructionList();
        instructions.append(InstructionFactory.createDup(accessedFieldType.getSize()));

        if (accessedFieldType.equals(Type.FLOAT)) {
            floatAppend(instructions, constantPool);
            numericAppend(instructions, instructionHandle);
        } else if (accessedFieldType.equals(Type.DOUBLE)) {
            doubleAppend(instructions, constantPool);
            numericAppend(instructions, instructionHandle);
        } else if (accessedFieldType.equals(Type.LONG)) {
            longAppend(instructions, constantPool);
            numericAppend(instructions, instructionHandle);
        } else {
            instructions.append(new PUSH(constantPool, 30));
            instructions.append(new IF_ICMPLE(instructionHandle.getNext()));
        }

        instructions.append(new GETSTATIC(getSystemOutIndex(constantPool)));
        instructions.append(new PUSH(constantPool, BIGGER_VALUE_MESSAGE));
        instructions.append(new INVOKEVIRTUAL(getPrintStreamPrintlnIndex(constantPool)));
        return instructions;
    }

    private static String getClassName(String[] args) {
        String className = "";
        if (args.length > 0) {
            className = args[0];
        }
        return className;
    }

    private static void insertStringIntoArray(InstructionList instructions, ConstantPoolGen constantPool, int index,
                                              String value) {
        instructions.append(new DUP());
        instructions.append(new PUSH(constantPool, index));
        instructions.append(new PUSH(constantPool, value));
        instructions.append(new AASTORE());
    }

    private static void insertFieldValueIntoArray(InstructionList instructions, ConstantPoolGen constantPool,
                                                  int index, int targetObjectLocalIndex, GETFIELD getFieldInstruction) {
        instructions.append(new DUP());
        instructions.append(new PUSH(constantPool, index));
        instructions.append(new ALOAD(targetObjectLocalIndex));
        instructions.append(getFieldInstruction);
        getStringValueOfPrimitiveType(instructions, constantPool, getFieldInstruction);
        instructions.append(new AASTORE());
    }

    private static boolean isPrimitive(Instruction instruction, ConstantPoolGen constantPool) {
        if (instruction instanceof GETFIELD &&
                ((GETFIELD) instruction).getFieldType(constantPool) instanceof BasicType) {
            return true;
        }
        return false;
    }

    private static void getStringValueOfPrimitiveType(InstructionList instructions, ConstantPoolGen constantPool,
                                                      GETFIELD getFieldInstruction) {
        Type printedType = getFieldInstruction.getFieldType(constantPool);
        if (printedType.equals(Type.BYTE) || printedType.equals(Type.SHORT)) {
            printedType = Type.INT;
        }
        instructions.append(new INVOKESTATIC(getStringValueOfIndex(printedType, constantPool)));
    }

    private static int getSystemOutIndex(ConstantPoolGen constantPool) {
        return constantPool.addFieldref("java/lang/System", "out",
                "Ljava/io/PrintStream;");
    }

    private static int getPrintStreamPrintlnIndex(ConstantPoolGen constantPool) {
        return constantPool.addMethodref("java/io/PrintStream", "println",
                "(Ljava/lang/String;)V");
    }

    private static int getPrintStreamPrintfIndex(ConstantPoolGen constantPool) {
        return constantPool.addMethodref("java/io/PrintStream", "printf",
                "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream;");
    }

    private static int getStringValueOfIndex(Type type, ConstantPoolGen constantPool) {
        return constantPool.addMethodref("java/lang/String", "valueOf",
                String.format("(%s)Ljava/lang/String;", type.getSignature()));
    }

    private static int getObjectClassIndex(ConstantPoolGen constantPool) {
        return constantPool.addClass(ObjectType.OBJECT);
    }

    private static void floatAppend(InstructionList instructions, ConstantPoolGen constantPool) {
        instructions.append(new PUSH(constantPool, 30.0f));
        instructions.append(new FCMPL());
    }

    private static void doubleAppend(InstructionList instructions, ConstantPoolGen constantPool) {
        instructions.append(new PUSH(constantPool, 30.0));
        instructions.append(new DCMPL());
    }

    private static void longAppend(InstructionList instructions, ConstantPoolGen constantPool) {
        instructions.append(new PUSH(constantPool, 30L));
        instructions.append(new LCMP());
    }

    private static void numericAppend(InstructionList instructions, InstructionHandle instructionHandle) {
        instructions.append(new ICONST(1));
        instructions.append(new IF_ICMPLT(instructionHandle.getNext()));
    }
}