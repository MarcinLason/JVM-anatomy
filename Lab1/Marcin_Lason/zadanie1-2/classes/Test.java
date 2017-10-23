import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Test {
    private int someInt = 23;
    private int biggerInt = 40;
    private String someString = "";
    private double someDouble = 34.0;
    private double secondDouble = 36.1;
    private List<String> listOfString = Arrays.asList("String1", "String2");

    public String concat (List<String> listOfString) {
        String result = "";
        for (int i = 0; i < listOfString.size(); i++) {
            result = result + listOfString.get(i);
        }
        return result;
    }


    public int sumInts () {
        return someInt + biggerInt;
    }

    public double multiplyDoubles () {
        return someDouble * secondDouble;
    }

    public String getOneString() {
        return someString + concat(listOfString);
    }

    public static void main(String[] argv) throws IOException {
        Test test = new Test();
        test.sumInts();
        test.multiplyDoubles();
        test.getOneString();
    }
}
