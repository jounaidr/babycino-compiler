// Generated from babycino/MiniJava.g4 by ANTLR 4.7.2
package babycino;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MiniJavaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MiniJavaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#goal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGoal(MiniJavaParser.GoalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#mainClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainClass(MiniJavaParser.MainClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(MiniJavaParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeIntArray}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeIntArray(MiniJavaParser.TypeIntArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeBoolean}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeBoolean(MiniJavaParser.TypeBooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeInt}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeInt(MiniJavaParser.TypeIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeObject}
	 * labeled alternative in {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeObject(MiniJavaParser.TypeObjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtBlock}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtBlock(MiniJavaParser.StmtBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtIf}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtIf(MiniJavaParser.StmtIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtWhile}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtWhile(MiniJavaParser.StmtWhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtDoWhile}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtDoWhile(MiniJavaParser.StmtDoWhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtPrint}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtPrint(MiniJavaParser.StmtPrintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtAssign}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtAssign(MiniJavaParser.StmtAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StmtArrayAssign}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtArrayAssign(MiniJavaParser.StmtArrayAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpConstTrue}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpConstTrue(MiniJavaParser.ExpConstTrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpArrayLength}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpArrayLength(MiniJavaParser.ExpArrayLengthContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpBinOp}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpBinOp(MiniJavaParser.ExpBinOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpConstInt}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpConstInt(MiniJavaParser.ExpConstIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpMethodCall}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpMethodCall(MiniJavaParser.ExpMethodCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpConstFalse}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpConstFalse(MiniJavaParser.ExpConstFalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpArrayIndex}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpArrayIndex(MiniJavaParser.ExpArrayIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpNewObject}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpNewObject(MiniJavaParser.ExpNewObjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpNewArray}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpNewArray(MiniJavaParser.ExpNewArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpNot}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpNot(MiniJavaParser.ExpNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpGroup}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpGroup(MiniJavaParser.ExpGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpLocalVar}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpLocalVar(MiniJavaParser.ExpLocalVarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpThis}
	 * labeled alternative in {@link MiniJavaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpThis(MiniJavaParser.ExpThisContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(MiniJavaParser.IdentifierContext ctx);
}