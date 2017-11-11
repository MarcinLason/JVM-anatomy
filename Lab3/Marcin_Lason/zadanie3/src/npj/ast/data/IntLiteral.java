package npj.ast.data;

public class IntLiteral implements RValue {
    public final int value;

    private IntLiteral(int value) {
        this.value = value;
    }

    public static IntLiteral of(int value) {
        return new IntLiteral(value);
    }
}
