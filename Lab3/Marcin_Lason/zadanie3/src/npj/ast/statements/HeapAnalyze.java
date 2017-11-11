package npj.ast.statements;

import npj.interpreter.InterpretingVisitor;

import java.lang.reflect.Method;
import java.util.Collection;

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
