package utils;

public class Variable {
    private final Type type;
    private int index;

    public Variable(Type type, int index) {
        this.index = index;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
