public class Test {
    static {
        System.out.println("Hello world");
    }

    public static void someMethod(int i) {
        while (i != 1) {
            i = i % 2 == 0 ? i / 2 : 3 * i + 1;
        }
    }

    public static void main(String[] args) {
        someMethod(50);
    }
}

