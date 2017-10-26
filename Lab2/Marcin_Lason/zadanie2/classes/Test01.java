/**
 * Created by Zuzanna on 01.11.2016.
 */
public class Test01 {
    static { System.out.println("GO!"); }
    public static void f(int i) {
        while(i != 1) {   i = i % 2 == 0 ? i/2 : 3*i + 1;   }
    }
    public static void main(String[] args) { f(50);   }
}

