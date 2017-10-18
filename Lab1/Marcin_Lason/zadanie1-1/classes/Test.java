import java.io.IOException;

public class Test {
    private int someInt;

    private int getSomeInt () {
        someInt = 30;
        return someInt;
    }

    private void returnNothing () {
        int number = 30;
        String text = "text";
    }

    private double getSomeDouble() {
        return someInt + 0.5;
    }


    public static void main(String[] argv) throws IOException {
        Test test = new Test();
        System.out.println("Hello world");
        System.out.println(test.getSomeInt());
        System.out.println(test.getSomeDouble());
    }
}
