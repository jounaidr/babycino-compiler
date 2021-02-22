package babycino;

import org.antlr.v4.runtime.ParserRuleContext;
import java.util.LinkedHashMap;

// Do an initial scan of the parse tree to find all the classes.
public class ClassFinder extends MiniJavaBaseListener {

    // Store the name of each class and a Class object for info about the class.
    private SymbolTable sym;
    // Flag: Have any errors occurred so far?
    private boolean errors;

    public ClassFinder(SymbolTable sym) {
        // Store the symbol table for use during walking.
        this.sym = sym;
    }

    public void enterMainClass(MiniJavaParser.MainClassContext ctx) {
        // Record the first class, which in MiniJava has only a static main method.
        String name = ctx.identifier(0).getText();
        this.sym.put(name, new Class(name, null));
    }
    
    public void enterClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        // Record all other classes.
        String name = ctx.identifier(0).getText();
        // Give an error for duplicate classes.
        this.check(!this.sym.containsKey(name), ctx, "Duplicate class: " + name);
        this.sym.put(name, new Class(name, ctx));
    }

    // Error logging and recording:

    // Assert condition. Print error if false. Record occurrence of error.
    private void check(boolean condition, ParserRuleContext ctx, String error) {
        if (!condition) {
            System.err.println(error);
            System.err.println("Context: " + ctx.getText());
            this.errors = true;
        }
    }

    // Assert false. Print error. Record occurrence of error.
    private void error(ParserRuleContext ctx, String error) {
        System.err.println(error);
        System.err.println("Context: " + ctx.getText());
        this.errors = true;
    }

    // Throw an exception if an error previously occurred.
    public void die() throws CompilerException {
        if (this.errors) {
            throw new CompilerException();
        }
    }
}

