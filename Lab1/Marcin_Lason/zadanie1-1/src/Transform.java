import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Transform {

    public static void main(String[] args) {
        String testClassName = "";
        JavaClass testClass = null;

        if (args.length > 0) {
            testClassName = args[0].replace(".class", "");
        }

        try {
            testClass = Repository.lookupClass(testClassName);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        if (testClass == null) return;
        List<Method> methods = Arrays.asList(testClass.getMethods());


        ClassGen testClassModification = new ClassGen(testClass);
        ConstantPoolGen constantPoolGen = testClassModification.getConstantPool();
        InstructionFactory instructionFactory = new InstructionFactory(testClassModification);

        for (Method method : methods) {
            MethodGen methodModification = new MethodGen(method, testClassModification.getClassName(), constantPoolGen);
            System.out.println("\nMethod's name: " + method.getName().toString());
            System.out.println("Return type: " + method.getReturnType().toString());

            InstructionList instructionList = methodModification.getInstructionList();
            InstructionHandle[] instructionHandles = instructionList.getInstructionHandles();

            for (InstructionHandle handle : instructionHandles) {
                if (handle.getInstruction() instanceof InvokeInstruction) {
                    if (!(handle.getInstruction() instanceof INVOKESPECIAL)) {
                        System.out.println(handle.getInstruction().getName());
                    }
                }
            }

        }


        String classFilePath = Repository.lookupClassFile(testClassModification.getClassName()).getPath();
        try {
            testClassModification.getJavaClass().dump(classFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
