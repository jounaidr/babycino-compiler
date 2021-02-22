package babycino;

import java.util.HashMap;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.ListIterator;

// Collect information about the contents of all classes.
public class ClassAnalysis extends MiniJavaBaseListener {

    // The symbol table, where class information is stored.
    private SymbolTable sym;
    // The class currently being analysed.
    private Class current;
    // The method currently being analysed.
    private Method method;

    // Flag: Have any errors occurred so far?
    private boolean errors;

    // Construct the Listener.
    public ClassAnalysis(SymbolTable sym) {
        this.sym = sym;
        this.current = null;
        this.method = null;
        this.errors = false;
    }
    
    public void enterMainClass(MiniJavaParser.MainClassContext ctx) {
        // Record the static main() method.
        sym.setMain(ctx.statement());
    }
    
    
    public void enterClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        // Save the class for use in later calls.
        String name = ctx.identifier(0).getText();
        this.current = this.sym.get(name);
        
        // Finish here if no specified base class.
        if (ctx.identifier(1) == null) {
            // Use Object as the base class.
            this.current.setBase(this.sym.get("Object"));
            return;
        }

        // Look up the base class.
        String base = ctx.identifier(1).getText();
        Class baseClass = this.sym.get(base);

        // Check the base class exists.
        if (baseClass == null) {
            this.error(ctx, "Class " + name + " has non-existent base class: " + base);
            return;
        }
        
        // Check this won't create a cycle in the class hierarchy.
        if (baseClass.isa(this.current)) {
            this.error(ctx, "Making class " + name + " extend " + base + " would create inheritance hierarchy cycle.");
            return;
        }
        
        this.current.setBase(baseClass);
    }
    
    public void exitClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        // Clear the current class. Just for cleanliness.
        this.current = null;
    }

    public void enterVarDeclaration(MiniJavaParser.VarDeclarationContext ctx) {
        Type type = this.extractType(this.sym, ctx.type());
        String name = ctx.identifier().getText();

        if (this.method == null) {
            // If we're not inside a method declaration, this is a class variable.
            // Check the variable doesn't already exist in this class.
            this.check(!this.current.hasOwnVar(name), ctx, "Duplicate variable in class " + this.current.getName() + ": " + name);
            // Add it to the class.
            this.current.addVar(name, type);
        }
        else {        
            // If we are inside a method declaration, this is a method variable.
            // Check the variable doesn't already exist in this method, either locally or as a parameter.
            this.check(!this.method.hasVar(name), ctx, "Duplicate variable in method " + this.method.getQualifiedName() + ": " + name);
            // Add it to the method.
            this.method.addVar(name, type);
        }
    }

    public void exitVarDeclaration(MiniJavaParser.VarDeclarationContext ctx) {
        // Nothing to do here.
    }

    public void enterMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {
        // Check the method name is not a duplicate.
        String name = ctx.identifier(0).getText();
        this.check(this.current.getOwnMethod(name) == null, ctx, "Duplicate method name in class " + this.current.getName() + ": " + name);
        
        // Create a new Method object and add it to the class.
        Method method = new Method(name, this.extractType(this.sym, ctx.type(0)), this.current, ctx);
        this.current.addMethod(method);
        // Save the method for use in later calls.
        // (Specifically, variable declarations.)
        this.method = method;

        // Process the types and names of parameters.
        ListIterator<MiniJavaParser.TypeContext> types = ctx.type().listIterator();
        ListIterator<MiniJavaParser.IdentifierContext> ids = ctx.identifier().listIterator();
        // Discard the return type.
        types.next();
        // Discard the method name.
        ids.next();
        while (types.hasNext()) {
            assert(ids.hasNext());
            MiniJavaParser.TypeContext tctx = types.next();
            Type paramType = this.extractType(this.sym, tctx);
            String paramId = ids.next().getText();
            // Check the names of parameters are distinct.
            this.check(!this.method.hasVar(paramId), tctx, "Duplicate parameter to method " + method.getQualifiedName() + ": " + paramId);
            // Record their names and types.
            this.method.addParam(paramId, paramType);
        }
    }

    public void exitMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {
        // Clear the current method.
        // This ensures subsequent property declarations are handled correctly.
        this.method = null;
    }

    public void enterTypeObject(MiniJavaParser.TypeObjectContext ctx) {
        // All object types must refer to classes that exist.
        this.check(this.sym.get(ctx.getText()) != null, ctx, "Invalid type: " + ctx.getText());
    }

    private Type extractType(SymbolTable sym, MiniJavaParser.TypeContext ctx) {
        Type ret = Type.extractType(sym, ctx);
        if (ret == null) {
            this.error(ctx, "Unrecognised type: " + ctx.getText());
            return new Type(sym.get("Object"));
        }
        return ret;
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

