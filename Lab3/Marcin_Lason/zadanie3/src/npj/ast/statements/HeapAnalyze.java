package npj.ast.statements;

import npj.interpreter.InterpretingVisitor;

public class HeapAnalyze implements Statement {
    private static final HeapAnalyze instance = new HeapAnalyze();

    private HeapAnalyze() {

    }

    public static HeapAnalyze instance() {
        return instance;
    }

    @Override
    public void accept(InterpretingVisitor visitor) {
        visitor.visitHeapAnalyze();
    }
}
