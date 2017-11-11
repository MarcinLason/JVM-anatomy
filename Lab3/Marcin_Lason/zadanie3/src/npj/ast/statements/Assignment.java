package npj.ast.statements;

import npj.ast.data.Dereference;
import npj.ast.data.RValue;
import npj.interpreter.InterpretingVisitor;

public class Assignment implements Statement {
    Dereference target;
    RValue value;

    private Assignment(Dereference target, RValue value) {
        this.target = target;
        this.value = value;
    }

    public static Assignment of(Dereference target, RValue value) {
        return new Assignment(target, value);
    }

    @Override
    public void accept(InterpretingVisitor visitor) {
        visitor.visitAssignment(target, value);
    }
}
