package npj.ast.data;

public class Nil implements RValue {
    private static final Nil value = new Nil();

    public static Nil value() {
        return value;
    }
}
