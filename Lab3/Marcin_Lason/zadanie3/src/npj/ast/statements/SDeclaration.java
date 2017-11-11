package npj.ast.statements;

import npj.interpreter.InterpretingVisitor;

public class SDeclaration implements Declaration {
	public String name;
	public String initialValue;

	private SDeclaration(String name, String initialValue) {
		this.name = name;
		this.initialValue = initialValue;
	}

	public static SDeclaration of(String name) {
		return new SDeclaration(name, null);
	}

	public static SDeclaration of(String name, String initialValue) {
		return new SDeclaration(name, initialValue);
	}

	@Override
	public void accept(InterpretingVisitor visitor) {
		visitor.visitSDeclaration(name, initialValue);
	}
}
