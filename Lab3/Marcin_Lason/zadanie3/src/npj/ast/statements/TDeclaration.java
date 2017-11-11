package npj.ast.statements;

import npj.interpreter.InterpretingVisitor;

public class TDeclaration implements Declaration {
    public String name;

    private TDeclaration(String name) {
        this.name = name;
    }

    public static TDeclaration of(String name) {
        return new TDeclaration(name);
    }

    @Override
    public void accept(InterpretingVisitor visitor) {
        visitor.visitTDeclaration(name);
    }
}
