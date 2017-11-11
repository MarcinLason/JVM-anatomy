package npj.ast.statements;

import npj.interpreter.InterpretingVisitor;

public interface Statement {
    void accept(InterpretingVisitor visitor);
}
