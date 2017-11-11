package npj.ast.data;

public class StringLiteral implements RValue {
    public final String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    public static StringLiteral of(String value) {
        return new StringLiteral(value);
    }

    public int getLength() {
        return value.length();
    }

    public String getValue() {
        return value;
    }
}
