// Generated from NPJ.g4 by ANTLR 4.5.3
package npj.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link NPJParser}.
 */
public interface NPJListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link NPJParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(NPJParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(NPJParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(NPJParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(NPJParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(NPJParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(NPJParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(NPJParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(NPJParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#varDeclT}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclT(NPJParser.VarDeclTContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#varDeclT}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclT(NPJParser.VarDeclTContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#varDeclSConst}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclSConst(NPJParser.VarDeclSConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#varDeclSConst}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclSConst(NPJParser.VarDeclSConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#varDeclSNull}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclSNull(NPJParser.VarDeclSNullContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#varDeclSNull}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclSNull(NPJParser.VarDeclSNullContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(NPJParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(NPJParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#lValue}.
	 * @param ctx the parse tree
	 */
	void enterLValue(NPJParser.LValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#lValue}.
	 * @param ctx the parse tree
	 */
	void exitLValue(NPJParser.LValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#rValue}.
	 * @param ctx the parse tree
	 */
	void enterRValue(NPJParser.RValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#rValue}.
	 * @param ctx the parse tree
	 */
	void exitRValue(NPJParser.RValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#print}.
	 * @param ctx the parse tree
	 */
	void enterPrint(NPJParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#print}.
	 * @param ctx the parse tree
	 */
	void exitPrint(NPJParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#printStringConst}.
	 * @param ctx the parse tree
	 */
	void enterPrintStringConst(NPJParser.PrintStringConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#printStringConst}.
	 * @param ctx the parse tree
	 */
	void exitPrintStringConst(NPJParser.PrintStringConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#printStringMessage}.
	 * @param ctx the parse tree
	 */
	void enterPrintStringMessage(NPJParser.PrintStringMessageContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#printStringMessage}.
	 * @param ctx the parse tree
	 */
	void exitPrintStringMessage(NPJParser.PrintStringMessageContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#printQuoted}.
	 * @param ctx the parse tree
	 */
	void enterPrintQuoted(NPJParser.PrintQuotedContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#printQuoted}.
	 * @param ctx the parse tree
	 */
	void exitPrintQuoted(NPJParser.PrintQuotedContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#heapAnalyze}.
	 * @param ctx the parse tree
	 */
	void enterHeapAnalyze(NPJParser.HeapAnalyzeContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#heapAnalyze}.
	 * @param ctx the parse tree
	 */
	void exitHeapAnalyze(NPJParser.HeapAnalyzeContext ctx);
	/**
	 * Enter a parse tree produced by {@link NPJParser#collect}.
	 * @param ctx the parse tree
	 */
	void enterCollect(NPJParser.CollectContext ctx);
	/**
	 * Exit a parse tree produced by {@link NPJParser#collect}.
	 * @param ctx the parse tree
	 */
	void exitCollect(NPJParser.CollectContext ctx);
}