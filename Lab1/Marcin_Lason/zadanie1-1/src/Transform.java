import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;

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
        ConstantPoolGen poolGen = testClassModification.getConstantPool();

        for (Method m : methods) {
            MethodGen method = new MethodGen(m, testClassModification.getClassName(), poolGen);


        }



        String classFilePath = Repository.lookupClassFile(testClassModification.getClassName()).getPath();
        try {
            testClassModification.getJavaClass().dump(classFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
