package npj.interpreter;

import npj.ast.data.Dereference;
import npj.ast.data.RValue;

public interface InterpretingVisitor {

    void visitCollect();

    void visitAssignment(Dereference target, RValue value);

    void visitPrintDereferenced(String dereferenceToPrint);

    void visitPrintLiteral(String literalToPrint);

    void visitSDeclaration(String name, String initialValue);

    void visitTDeclaration(String name);

    void visitHeapAnalyze();
}
