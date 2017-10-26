/**
 * Created by Zuzanna on 01.11.2016.
 */
public class Test02 {
    public static void main(String[] args) {
        try {
            throw new NullPointerException();
        } catch(NullPointerException e){
            System.out.println("Got it.");
        }
    }
}
