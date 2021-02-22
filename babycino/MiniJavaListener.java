// Generated from babycino/MiniJava.g4 by ANTLR 4.7.2
package babycino;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MiniJavaParser}.
 */
public interface MiniJavaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#goal}.
	 * @param ctx the parse tree
	 */
	void enterGoal(MiniJavaParser.GoalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#goal}.
	 * @param ctx the parse tree
	 */
	void exitGoal(MiniJavaParser.GoalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#mainClass}.
	 * @param ctx the parse tree
	 */
	void enterMainClass(MiniJavaParser.MainClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#mainClass}.
	 * @param ctx the parse tree
	 */
	void exitMainClass(MiniJavaParser.MainClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(MiniJavaParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(MiniJavaParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeIntArray}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeIntArray(MiniJavaParser.TypeIntArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeIntArray}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeIntArray(MiniJavaParser.TypeIntArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeBoolean}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeBoolean(MiniJavaParser.TypeBooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeBoolean}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeBoolean(MiniJavaParser.TypeBooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeInt}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeInt(MiniJavaParser.TypeIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeInt}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeInt(MiniJavaParser.TypeIntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeObject}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTypeObject(MiniJavaParser.TypeObjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeObject}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTypeObject(MiniJavaParser.TypeObjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtBlock}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStmtBlock(MiniJavaParser.StmtBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtBlock}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStmtBlock(MiniJavaParser.StmtBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtIf}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStmtIf(MiniJavaParser.StmtIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtIf}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStmtIf(MiniJavaParser.StmtIfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtWhile}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStmtWhile(MiniJavaParser.StmtWhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtWhile}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStmtWhile(MiniJavaParser.StmtWhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtDoWhile}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStmtDoWhile(MiniJavaParser.StmtDoWhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtDoWhile}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStmtDoWhile(MiniJavaParser.StmtDoWhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtPrint}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStmtPrint(MiniJavaParser.StmtPrintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtPrint}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStmtPrint(MiniJavaParser.StmtPrintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtAssign}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStmtAssign(MiniJavaParser.StmtAssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtAssign}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStmtAssign(MiniJavaParser.StmtAssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StmtArrayAssign}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStmtArrayAssign(MiniJavaParser.StmtArrayAssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StmtArrayAssign}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStmtArrayAssign(MiniJavaParser.StmtArrayAssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpConstTrue}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpConstTrue(MiniJavaParser.ExpConstTrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpConstTrue}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpConstTrue(MiniJavaParser.ExpConstTrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpArrayLength}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpArrayLength(MiniJavaParser.ExpArrayLengthContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpArrayLength}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpArrayLength(MiniJavaParser.ExpArrayLengthContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpBinOp}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpBinOp(MiniJavaParser.ExpBinOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpBinOp}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpBinOp(MiniJavaParser.ExpBinOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpConstInt}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpConstInt(MiniJavaParser.ExpConstIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpConstInt}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpConstInt(MiniJavaParser.ExpConstIntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpMethodCall}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpMethodCall(MiniJavaParser.ExpMethodCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpMethodCall}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpMethodCall(MiniJavaParser.ExpMethodCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpConstFalse}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpConstFalse(MiniJavaParser.ExpConstFalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpConstFalse}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpConstFalse(MiniJavaParser.ExpConstFalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpArrayIndex}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpArrayIndex(MiniJavaParser.ExpArrayIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpArrayIndex}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpArrayIndex(MiniJavaParser.ExpArrayIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpNewObject}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpNewObject(MiniJavaParser.ExpNewObjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpNewObject}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpNewObject(MiniJavaParser.ExpNewObjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpNewArray}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpNewArray(MiniJavaParser.ExpNewArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpNewArray}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpNewArray(MiniJavaParser.ExpNewArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpNot}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpNot(MiniJavaParser.ExpNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpNot}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpNot(MiniJavaParser.ExpNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpGroup}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpGroup(MiniJavaParser.ExpGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpGroup}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpGroup(MiniJavaParser.ExpGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpLocalVar}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpLocalVar(MiniJavaParser.ExpLocalVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpLocalVar}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpLocalVar(MiniJavaParser.ExpLocalVarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpThis}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpThis(MiniJavaParser.ExpThisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpThis}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpThis(MiniJavaParser.ExpThisContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(MiniJavaParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(MiniJavaParser.IdentifierContext ctx);
}