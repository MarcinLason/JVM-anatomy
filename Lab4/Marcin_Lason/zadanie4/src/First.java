public class First {
    public static void main(String... args) {
        Second second = new Second();
        second.incCounter();
        second.incCounter();
        int counter = second.getCounter();
        System.out.println(counter);
    }
}