import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test {

    private void returnNothing() {
        String a = "nothing";
    }

    private Integer sum (int a, int b) {
        return a + b;
    }

    private double multiply(int a, int b){
        return a*b;
    }

    private String concatStrings (String a, String b) {
        return a + b;
    }
    private List<String> getList(String string1, String string2, String string3, String string4) {
        return Arrays.asList(concatStrings(string1, string2), concatStrings(string3, string4));
    }

    Scanner getScanner(){
        return new Scanner("source");
    }

    private Integer proxyInvoker (int a) {
        returnNothing();
        sum (a, 10);
        multiply(a, a + 17);
        return a + 1;
    }

    public static void main(String[] args){
        Test test = new Test();
        test.proxyInvoker(30);
        test.getList("a", "b", "c", "d");
        test.getScanner();
    }
}
