import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test {
    private Test test;

    private void returnNothing() {
        String a = "nothing";
    }

    private Integer sum (int a, int b) {
        return a + b;
    }

    double multiply(int a, int b){
        return a*b;
    }

    private Integer proxyInvoker (int a) {
        returnNothing();
        sum (a, 10);
        multiply(a, a + 17);
        return a + 1;
    }

    public static void main(String[] args){
        Test test = new Test();
        test.hi();
        test.bigbig(16);
        test.hello("lala");
        int r = test.natural(5, 2);
        test.arr();
        test.multiply(r,20);
        test.getScanner();
    }

    Integer natural(int a, int b){
        test = new Test();
        test.hi();
        return a + b;
    }

    long bigbig(int a){
        return a*a*a*1000;
    }



    Scanner getScanner(){
        return new Scanner("nanananaananaa");
    }

    String hi(){
        String s = "HI naturally!";
        return s;
    }
    float[][] arr(){
        return new float[3][4];
    }


    List<String> hello(String s){
        return Arrays.asList(s);
    }
}
