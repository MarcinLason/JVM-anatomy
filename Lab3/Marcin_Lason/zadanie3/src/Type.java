import java.util.HashMap;
import java.util.Map;

public enum Type {
    S(-2, 2), T(-4, 4);

    private static final Map<Integer, Type> codeToType = new HashMap<>();

    static {
        for (Type type : values()) {
            codeToType.put(type.code, type);
        }
    }

    public final int code;
    public final int baseSize;

    Type(final int code, final int baseSize) {
        this.code = code;
        this.baseSize = baseSize;
    }

    public static Type forCode(int code) {
        Type type = codeToType.get(code);
        if (type == null) {
            throw new RuntimeException("Unsupported variable type code.");
        }
        return type;
    }
}
