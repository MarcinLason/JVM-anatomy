import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Transform {
    private static final String THRESHOLD_MESSAGE = "    !the value is greater than 30!";
    private static final String GETFIELD_INFO_MESSAGE = "Before getfield:%n    %s%n    %s%n    %s%n    %s%n";
    private static final List<BasicType> NUMERIC_TYPES = Arrays.asList(Type.BYTE, Type.SHORT, Type.INT, Type.LONG, Type.FLOAT,
            Type.DOUBLE);

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
        ConstantPoolGen constantPoolGen = modifiedClass.getConstantPool();

        for (Method method : originalClass.getMethods()) {
            MethodGen modifiedMethod = new MethodGen(method, modifiedClass.getClassName(), constantPoolGen);
            modifiedMethod.setMaxStack(modifiedMethod.getMaxStack() + 6);
            modifiedMethod.setMaxLocals(modifiedMethod.getMaxLocals() + 1);
            int tmpLocalVariableIndex = modifiedMethod.getMaxLocals() - 1;
            InstructionList modifiedMethodInstructions = modifiedMethod.getInstructionList();
            InstructionHandle inspectedInstructionHandle = modifiedMethodInstructions.getStart();

            do {
                InstructionHandle nextHandle = inspectedInstructionHandle.getNext();
                Instruction instruction = inspectedInstructionHandle.getInstruction();
                if (isPrimitiveFieldRead(instruction, constantPoolGen)) {
                    GETFIELD getFieldInstruction = (GETFIELD) instruction;
                    Type accessedFieldType = getFieldInstruction.getFieldType(constantPoolGen);
                    InstructionList instructionsToPrepend = printAccessedFieldInfo(tmpLocalVariableIndex, getFieldInstruction, accessedFieldType, constantPoolGen);
                    modifiedMethodInstructions.insert(instruction, instructionsToPrepend);
                    if (NUMERIC_TYPES.contains(accessedFieldType)) {
                        InstructionList thresholdCheck = checkIfResultIsLargerThanThreshold(inspectedInstructionHandle, accessedFieldType, constantPoolGen);
                        modifiedMethodInstructions.append(instruction, thresholdCheck);
                    }
                }
                inspectedInstructionHandle = nextHandle;
            } while (inspectedInstructionHandle != null);


            modifiedMethodInstructions.setPositions();
            modifiedClass.replaceMethod(method, modifiedMethod.getMethod());
        }
        return modifiedClass.getJavaClass();
    }

    private static InstructionList printAccessedFieldInfo(int tmpLocalVariableIndex, GETFIELD getFieldInstruction, Type accessedFieldType, ConstantPoolGen constantPoolGen) {
        InstructionList instructionsToPrepend = new InstructionList();
        String targetType = getFieldInstruction.getLoadClassType(constantPoolGen).getClassName();
        String fieldType = accessedFieldType.toString();
        String fieldName = getFieldInstruction.getFieldName(constantPoolGen);
        instructionsToPrepend.append(new DUP());
        instructionsToPrepend.append(new ASTORE(tmpLocalVariableIndex));
        instructionsToPrepend.append(new GETSTATIC(getSystemOutIndex(constantPoolGen)));
        instructionsToPrepend.append(new PUSH(constantPoolGen, GETFIELD_INFO_MESSAGE));
        createNewArray(instructionsToPrepend, constantPoolGen, getObjectClassIndex(constantPoolGen), 4);
        insertStringIntoArray(instructionsToPrepend, constantPoolGen, 0, targetType);
        insertStringIntoArray(instructionsToPrepend, constantPoolGen, 1, fieldType);
        insertStringIntoArray(instructionsToPrepend, constantPoolGen, 2, fieldName);
        insertFieldValueObjectIntoArray(instructionsToPrepend, constantPoolGen, 3, tmpLocalVariableIndex, getFieldInstruction);
        instructionsToPrepend.append(new INVOKEVIRTUAL(getPrintStreamPrintfIndex(constantPoolGen)));
        instructionsToPrepend.append(new POP());
        return instructionsToPrepend;
    }

    private static InstructionList checkIfResultIsLargerThanThreshold(InstructionHandle instructionHandle, Type accessedFieldType, ConstantPoolGen constantPoolGen) {
        InstructionList instructionsToAppend = new InstructionList();
        instructionsToAppend.append(InstructionFactory.createDup(accessedFieldType.getSize()));
        if (accessedFieldType.equals(Type.FLOAT)) {
            instructionsToAppend.append(new PUSH(constantPoolGen, 30.0f));
            instructionsToAppend.append(new FCMPL());
            instructionsToAppend.append(new ICONST(1));
            instructionsToAppend.append(new IF_ICMPLT(instructionHandle.getNext()));
        } else if (accessedFieldType.equals(Type.DOUBLE)) {
            instructionsToAppend.append(new PUSH(constantPoolGen, 30.0));
            instructionsToAppend.append(new DCMPL());
            instructionsToAppend.append(new ICONST(1));
            instructionsToAppend.append(new IF_ICMPLT(instructionHandle.getNext()));
        } else if (accessedFieldType.equals(Type.LONG)) {
            instructionsToAppend.append(new PUSH(constantPoolGen, 30L));
            instructionsToAppend.append(new LCMP());
            instructionsToAppend.append(new ICONST(1));
            instructionsToAppend.append(new IF_ICMPLT(instructionHandle.getNext()));
        } else {
            instructionsToAppend.append(new PUSH(constantPoolGen, 30));
            instructionsToAppend.append(new IF_ICMPLE(instructionHandle.getNext()));
        }
        instructionsToAppend.append(new GETSTATIC(getSystemOutIndex(constantPoolGen)));
        instructionsToAppend.append(new PUSH(constantPoolGen, THRESHOLD_MESSAGE));
        instructionsToAppend.append(new INVOKEVIRTUAL(getPrintStreamPrintlnIndex(constantPoolGen)));
        return instructionsToAppend;
    }

    private static void createNewArray(InstructionList instructionList, ConstantPoolGen constantPoolGen, int elementClassConstIndex, int size) {
        instructionList.append(new PUSH(constantPoolGen, size));
        instructionList.append(new ANEWARRAY(elementClassConstIndex));
    }

    private static void insertFieldValueObjectIntoArray(InstructionList instructionList, ConstantPoolGen constantPoolGen, int index, int targetObjectLocalIndex, GETFIELD getFieldInstruction) {
        instructionList.append(new DUP());
        instructionList.append(new PUSH(constantPoolGen, index));
        instructionList.append(new ALOAD(targetObjectLocalIndex));
        instructionList.append(getFieldInstruction);
        stringValueOfPrimitiveType(instructionList, constantPoolGen, getFieldInstruction);
        instructionList.append(new AASTORE());
    }

    private static void insertStringIntoArray(InstructionList instructionList, ConstantPoolGen constantPoolGen, int index, String value) {
        instructionList.append(new DUP());
        instructionList.append(new PUSH(constantPoolGen, index));
        instructionList.append(new PUSH(constantPoolGen, value));
        instructionList.append(new AASTORE());
    }

    private static void stringValueOfPrimitiveType(InstructionList instructionList, ConstantPoolGen constantPoolGen,
                                                   GETFIELD getFieldInstruction) {
        Type printedType = getFieldInstruction.getFieldType(constantPoolGen);
        if (printedType.equals(Type.BYTE) || printedType.equals(Type.SHORT)) {
            printedType = Type.INT;
        }
        instructionList.append(new INVOKESTATIC(getStringValueOfIndex(printedType, constantPoolGen)));
    }

    private static String getClassName(String[] args) {
        String className = "";
        if (args.length > 0) {
            className = args[0];
        }
        return className;
    }

    private static boolean isPrimitiveFieldRead(Instruction instruction, ConstantPoolGen constantPool) {
        if (instruction instanceof GETFIELD &&
                ((GETFIELD) instruction).getFieldType(constantPool) instanceof BasicType) {
            return true;
        }
        return false;
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

    private static int getStringValueOfIndex(Type argType, ConstantPoolGen constantPool) {
        return constantPool.addMethodref("java/lang/String", "valueOf",
                String.format("(%s)Ljava/lang/String;", argType.getSignature()));
    }

    private static int getObjectClassIndex(ConstantPoolGen constantPool) {
        return constantPool.addClass(ObjectType.OBJECT);
    }
}