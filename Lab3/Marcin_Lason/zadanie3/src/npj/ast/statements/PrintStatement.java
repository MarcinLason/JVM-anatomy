package npj.ast.statements;

import npj.interpreter.InterpretingVisitor;

public class PrintStatement implements Statement {
	public String literalToPrint;
	public String dereferenceToPrint;

	private PrintStatement() {

	}

	public static PrintStatement ofLiteral(String literalToPrint) {
		PrintStatement printStatement = new PrintStatement();
		printStatement.literalToPrint = literalToPrint;
		return printStatement;
	}

	public static PrintStatement ofDereference(String dereferenceToPrint) {
		PrintStatement printStatement = new PrintStatement();
		printStatement.dereferenceToPrint = dereferenceToPrint;
		return printStatement;
	}

	@Override
	public void accept(InterpretingVisitor visitor) {
		if(literalToPrint != null) {
			visitor.visitPrintLiteral(literalToPrint);
		} else {
			visitor.visitPrintDereferenced(dereferenceToPrint);
		}
	}
}
