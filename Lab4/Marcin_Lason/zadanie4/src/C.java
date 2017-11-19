import java.util.*;
public class C {
    public static void main(String[] args) {
        Collection<A> objs = new ArrayList<A>();
        objs.add(new A(4));
        objs.add(new B(4));
        objs.add(new B(4));
        objs.add(new A(4));

        for (Iterator<A> it = objs.iterator(); it.hasNext();) {
            A obj = it.next();
            System.out.println("obj: " + obj.getSomething());
        }
    }
}