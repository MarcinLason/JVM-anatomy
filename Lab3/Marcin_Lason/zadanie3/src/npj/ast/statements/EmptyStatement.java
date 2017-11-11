package npj.ast.statements;

import npj.interpreter.InterpretingVisitor;

public class EmptyStatement implements Statement {
    private static final EmptyStatement instance = new EmptyStatement();

    private EmptyStatement() {

    }

    public static EmptyStatement instance() {
        return instance;
    }

    @Override
    public void accept(InterpretingVisitor visitor) {

    }
}
