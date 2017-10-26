/**
 * Created by Zuzanna on 01.11.2016.
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Test03 {

    private Test03() {
    }

    public void tryCatchTest() throws IOException {
        String s = "Fuck";

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(s);
        } catch (FileNotFoundException e1) {
            System.out.println("Got exception: " + e1.getMessage());
        } finally {
            if (fis != null) {
                fis.close();
            } else {
                throw new IOException();
            }
        }

        System.out.println(s + "^13");
    }

    public static void main(String[] args) {
        Test03 t = new Test03();

        try {
            t.tryCatchTest();
        } catch (IOException e) {
            System.out.println("Got exception: " + e.getMessage());
        }
    }

}
