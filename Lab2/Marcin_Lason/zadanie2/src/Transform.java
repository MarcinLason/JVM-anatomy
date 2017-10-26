import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

import java.io.IOException;

/**
 * Created by Zuzanna on 01.11.2016.
 */
public class Transform {

    public static void main(String[] args) {

        String className = (args.length >= 1) ? args[0] : "";

        if(className.endsWith(".class")){
            className = className.substring(0, className.length() - 6);
        }

        JavaClass clazz = null;
        ClassGen cg;
        ConstantPoolGen cpg;

        try {
            clazz = Repository.lookupClass(className);
//            System.out.println("Class name: " + className);
            System.out.println("Class: " + clazz.toString());
        } catch (Exception e) {
            System.err.println("Could not find class " + className);
        }

        cg = new ClassGen(clazz);
        cpg = cg.getConstantPool();

        Method[] methods = clazz.getMethods();

        for(Method method : methods) {
            String methodName = method.getName();

            /*
            <init> is the (or one of the) constructor(s) for the instance, and non-static field initialization.
            <clinit> are the static initialization blocks for the class, and static field initialization.
             */

            if(methodName.equals("<init>") || methodName.equals("<clinit>")) continue;

            MethodGen methodGen = new MethodGen(method, cg.getClassName(), cpg);
            InstructionFactory factory = new InstructionFactory(cg);
            InstructionList il = methodGen.getInstructionList();
            InstructionHandle[] ih = il.getInstructionHandles();

            if (method.getName().equals("main") && method.getReturnType().equals(Type.VOID)){
                il.insert(factory.createInvoke(Helper.class.getCanonicalName(),
                        "addShutdownCall", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));
            }

            for(InstructionHandle handle : ih) {
                if(isDeclaredHere(handle, clazz, cpg)){

                    String name = handle.getInstruction().getName();

                    if (!(name.startsWith("m") || name.startsWith("M"))) {
                        il.insert(handle, factory.createConstant(name));
                        il.insert(handle, factory.createInvoke(
                                Helper.class.getCanonicalName(),
                                "increment",
                                Type.VOID,
                                new Type[]{Type.STRING},
                                Constants.INVOKESTATIC)
                        );
                    }
                }
            }

            methodGen.setMaxStack();
            cg.replaceMethod(method, methodGen.getMethod());
        }

        try {
            String classFilePath = Repository.lookupClassFile(cg.getClassName()).getPath();
            cg.getJavaClass().dump(classFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isDeclaredHere(InstructionHandle handle, JavaClass clazz, ConstantPoolGen cpg) {

        if(!(handle.getInstruction() instanceof InvokeInstruction)) return true;

        InvokeInstruction instr = (InvokeInstruction) handle.getInstruction();

        for (Method m : clazz.getMethods()) {
            if (m.getSignature().equals(instr.getSignature(cpg)) && m.getName().equals(instr.getName(cpg).toString())) {
                System.out.println("signature: " + m.getSignature() + ", " + instr.getSignature(cpg));
                System.out.println("name: " + m.getName() + ", " + instr.getName(cpg).toString());
                return true;
            }
        }

        return false;
    }
}
