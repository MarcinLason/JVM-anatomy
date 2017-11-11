package npj.ast.statements;

import npj.interpreter.InterpretingVisitor;

public class Collect implements Statement {
    private static final Collect instance = new Collect();

    private Collect() {

    }

    public static Collect instance() {
        return instance;
    }

    @Override
    public void accept(InterpretingVisitor visitor) {
        visitor.visitCollect();
    }
}
