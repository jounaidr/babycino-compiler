package babycino;

import java.util.ArrayList;

// Register conventions:
//
// vg0, vg1, ... - global registers; not used
// vl0 - pointer to "this" object, or 0 for main()
// vl1, vl2, ... - method local variables
// r0 - holds return value from method calls
// r1, r2, ... - local temporary registers
//
// All registers except r0 are assumed to be preserved across calls.
//
// Integer arrays:
//
// An integer array value is a pointer to a block of memory.
// The 1st word is the length of the array.
// The remaining words are the array contents, in order.
//
// Objects:
//
// An object value is a pointer to a block of memory.
// The 1st word is a pointer to a "vtable".
// Next come the properties (fields) of the parent class.
// Then come the properties for this class.
//
// A vtable is a block of memory.
// All objects of the same class share the same vtable.
// The words are pointers to the methods of the class.
// Methods inherited from the parent class come first.
// Then come the methods new to this class.

public class TACGenerator extends MiniJavaBaseVisitor<TACBlock> {

    SymbolTable sym;
    int regs;
    int labels;
    Class current;
    Method method;

    public TACGenerator(SymbolTable sym, Class current) {
        this.sym = sym;
        this.regs = 1;
        this.labels = 0;
        this.current = current;
        this.method = null;
    }

    // Generate code for method's statements and return expression.
    public TACBlock visitMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {
        // It's inefficient to generate each statement/expression recursively
        // as an ArrayList and combine them into a larger block.
        // However, it's the simplest way to write it, so that's what we do.
        // The Java 8 Streams API would probably be simplest way to improve it.
        TACBlock result = new TACBlock();

        // Record the method for which code is being generated.
        this.method = this.current.getOwnMethod(ctx.identifier(0).getText());
        // Reset the count of temporaries.
        this.regs = 1;

        // Add a label to the code to indicate the start of the method.
        result.add(TACOp.label(this.method.getQualifiedName()));

        // Generate and sequence code for all the statements.
        for (MiniJavaParser.StatementContext s : ctx.statement()) {
            result.addAll(this.visit(s));
        }
        // Generate code for the return expression.
        TACBlock ret = this.visit(ctx.expression());
        result.addAll(ret);
        // Move the result of the return expression into the distinguished
        // register r0 for function results.
        result.add(TACOp.mov("r0", ret.getResult()));
        result.add(TACOp.ret());

        return result;
    }

    // ------------------------------------------------------------------------
    // Generate code for statements:

    public TACBlock visitStmtBlock(MiniJavaParser.StmtBlockContext ctx) {
        TACBlock result = new TACBlock();

        // Generate and sequence code for all the statements.
        for (MiniJavaParser.StatementContext s : ctx.statement()) {
            result.addAll(this.visit(s));
        }
        
        return result;
    }

    public TACBlock visitStmtIf(MiniJavaParser.StmtIfContext ctx) {
        TACBlock result = new TACBlock();
        TACBlock expr = this.visit(ctx.expression());
        TACBlock ifTrue = this.visit(ctx.statement(0));
        String labelElse = this.genlab();
        TACBlock ifFalse = this.visit(ctx.statement(1));
        String labelEnd = this.genlab();
        
        result.addAll(expr);
        result.add(TACOp.jz(expr.getResult(), labelElse));
        result.addAll(ifTrue);
        result.add(TACOp.jmp(labelEnd));
        result.add(TACOp.label(labelElse));
        result.addAll(ifFalse);
        result.add(TACOp.label(labelEnd));
        
        return result;
    }

    public TACBlock visitStmtDoWhile(MiniJavaParser.StmtDoWhileContext ctx) {
        TACBlock result = new TACBlock();
        String labelStart = this.genlab();
        TACBlock body = this.visit(ctx.statement());
        TACBlock expr = this.visit(ctx.expression());
        String labelEnd = this.genlab();

        result.add(TACOp.label(labelStart));
        result.addAll(body);
        result.addAll(expr);
        result.add(TACOp.jz(expr.getResult(), labelEnd));
        result.add(TACOp.jmp(labelStart));
        result.add(TACOp.label(labelEnd));

        return result;
    }

    public TACBlock visitStmtWhile(MiniJavaParser.StmtWhileContext ctx) {
        TACBlock result = new TACBlock();
        String labelStart = this.genlab();
        TACBlock expr = this.visit(ctx.expression());
        TACBlock body = this.visit(ctx.statement());
        String labelEnd = this.genlab();
        
        result.add(TACOp.label(labelStart));
        result.addAll(expr);
        result.add(TACOp.jz(expr.getResult(), labelEnd));
        result.addAll(body);
        result.add(TACOp.jmp(labelStart));
        result.add(TACOp.label(labelEnd));
        
        return result;
    }
    
    public TACBlock visitStmtPrint(MiniJavaParser.StmtPrintContext ctx) {
        TACBlock result = new TACBlock();
        TACBlock expr = this.visit(ctx.expression());

        result.addAll(expr);
        result.add(TACOp.write(expr.getResult()));
        
        return result;
    }

    public TACBlock visitStmtAssign(MiniJavaParser.StmtAssignContext ctx) {
        TACBlock result = new TACBlock();

        // Generate code for the expression to assign.
        TACBlock expr = this.visit(ctx.expression());
        result.addAll(expr);

        // Is the identifier a method local variable or a class instance variable?
        String id = ctx.identifier().getText();
        if (this.method.hasVar(id)) {
            // Variable is stored in vl register.
            result.add(TACOp.mov("vl" + this.method.getVarIndex(id), expr.getResult()));
        }
        else if (this.current.hasAnyVar(id)) {
            // Variable is stored in memory, indexed by this (vl0).
            String dest = this.genreg();
            String idx = this.genreg();
            result.add(TACOp.immed(idx, this.current.getVarIndex(id)));
            result.add(TACOp.offset(dest, "vl0", idx));
            result.add(TACOp.store(dest, expr.getResult()));
        }
        else {
            System.err.println("Unrecognised variable: " + id);
            assert(false);
        }
        
        return result;
    }

    public TACBlock visitStmtArrayAssign(MiniJavaParser.StmtArrayAssignContext ctx) {
        TACBlock result = new TACBlock();

        // Generate code for array base, index and expression to assign.
        TACBlock base = this.lookupIdentifier(ctx.identifier());
        TACBlock index = this.visit(ctx.expression(0));
        TACBlock expr = this.visit(ctx.expression(1));

        result.addAll(base);
        result.addAll(index);
        result.addAll(expr);

        // Calculate the address of the array element.
        String one = this.genreg();
        String int0 = this.genreg();
        String dest = this.genreg();

        result.add(TACOp.immed(one, 1));
        result.add(TACOp.offset(int0, base.getResult(), one));
        result.add(TACOp.offset(dest, int0, index.getResult()));
        result.add(TACOp.store(dest, expr.getResult()));
        
        return result;
    }

    // ------------------------------------------------------------------------
    // Generate code for expressions:

    public TACBlock visitExpConstTrue(MiniJavaParser.ExpConstTrueContext ctx) {
        TACBlock result = new TACBlock();
        result.add(TACOp.immed(this.genreg(), 1));
        return result;
    }

    public TACBlock visitExpArrayLength(MiniJavaParser.ExpArrayLengthContext ctx) {
        TACBlock result = new TACBlock();
        TACBlock expr = this.visit(ctx.expression());

        String res = this.genreg();

        result.addAll(expr);
        result.add(TACOp.load(res, expr.getResult()));
        
        return result;
    }

    public TACBlock visitExpBinOp(MiniJavaParser.ExpBinOpContext ctx) {
        TACBlock result = new TACBlock();
        TACBlock expr1 = this.visit(ctx.expression(0));
        TACBlock expr2 = this.visit(ctx.expression(1));

        String op = ctx.getChild(1).getText();
        if (op.equals("&&")) {
            // && should short-circuit.
            String end = this.genlab();
            String res = this.genreg();

            result.addAll(expr1);
            result.add(TACOp.mov(res, expr1.getResult()));
            result.add(TACOp.jz(res, end));
            result.addAll(expr2);
            result.add(TACOp.mov(res, expr2.getResult()));
            result.add(TACOp.label(end));

            result.setResult(res);            
            return result;
        }

        // Generate the correct code for the operation.
        int n = TACOp.binopToCode(op);
        result.addAll(expr1);
        result.addAll(expr2);
        result.add(TACOp.binop(this.genreg(), expr1.getResult(), expr2.getResult(), n));
        
        return result;
    }


    public TACBlock visitExpConstInt(MiniJavaParser.ExpConstIntContext ctx) {
        TACBlock result = new TACBlock();
        int n = Integer.parseInt(ctx.INT().getText());
        result.add(TACOp.immed(this.genreg(), n));
        return result;
    }

    public TACBlock visitExpMethodCall(MiniJavaParser.ExpMethodCallContext ctx) {
        TACBlock result = new TACBlock();

        // Evaluate call "receiver" and arguments.
        ArrayList<String> results = new ArrayList<String>();
        for (MiniJavaParser.ExpressionContext e : ctx.expression()) {
            TACBlock expr = this.visit(e);
            result.addAll(expr);
            results.add(expr.getResult());
        }

        // Get a pointer to the method.
        String vtbl = this.genreg();
        String idx = this.genreg();
        String method = this.genreg();
        String dst = this.genreg();
        String res = this.genreg();

        Class receiver = this.sym.getStaticType(ctx).getObject();
        String methodName = ctx.identifier().getText();
        int methodIdx = receiver.getAnyMethodIndex(methodName);
        
        result.add(TACOp.load(vtbl, results.get(0))); // vtbl = [obj]
        result.add(TACOp.immed(idx, methodIdx));
        result.add(TACOp.offset(method, vtbl, idx)); // method = [obj] + idx
        result.add(TACOp.load(dst, method)); // dest = [[obj] + idx]

        // Push the parameters, including implicit "this".
        for (int n = 0; n < results.size(); n++) {
            result.add(TACOp.param(results.get(n)));
        }

        // Make the call and save the result.
        result.add(TACOp.call(dst));
        result.add(TACOp.mov(res, "r0"));
        
        return result;
    }

    public TACBlock visitExpConstFalse(MiniJavaParser.ExpConstFalseContext ctx) {
        TACBlock result = new TACBlock();
        result.add(TACOp.immed(this.genreg(), 0));
        return result;
    }
    
    public TACBlock visitExpArrayIndex(MiniJavaParser.ExpArrayIndexContext ctx) {
        TACBlock result = new TACBlock();
        TACBlock array = this.visit(ctx.expression(0));
        TACBlock index = this.visit(ctx.expression(1));
        String one = this.genreg();
        String int0 = this.genreg();
        String src = this.genreg();
        String res = this.genreg();
        
        result.addAll(array);
        result.addAll(index);

        result.add(TACOp.immed(one, 1));
        result.add(TACOp.offset(int0, array.getResult(), one));
        result.add(TACOp.offset(src, int0, index.getResult()));
        result.add(TACOp.load(res, src));
        
        return result;
    }
    
    public TACBlock visitExpNewObject(MiniJavaParser.ExpNewObjectContext ctx) {
        TACBlock result = new TACBlock();
        String size = this.genreg();
//        String vtbl = this.genreg();
        String res = this.genreg();
        
        Class c = sym.get(ctx.identifier().getText());

        // Allocate a block of memory.        
        result.add(TACOp.immed(size, c.size()));
        result.add(TACOp.malloc(res, size));
        // Set the 1st word to be the vtable of the object's class.
//        result.add(TACOp.addrof(vtbl, c.getName() + ".vtbl"));
//        result.add(TACOp.store(res, vtbl));
        result.add(TACOp.store(res, "vg" + c.getClassIndex()));
        
        return result;
    }

    public TACBlock visitExpNewArray(MiniJavaParser.ExpNewArrayContext ctx) {
        TACBlock result = new TACBlock();
        TACBlock expr = this.visit(ctx.expression());
        String size = this.genreg();
        String one = this.genreg();
        String res = this.genreg();
        
        // Size of memory block to allocate is length of array + 1.
        result.addAll(expr);
        result.add(TACOp.immed(one, 1));
        result.add(TACOp.add(size, expr.getResult(), one));
        result.add(TACOp.malloc(res, size));
        // 1st word in block stores length of array.
        result.add(TACOp.store(res, expr.getResult()));
        
        return result;
    }

    public TACBlock visitExpNot(MiniJavaParser.ExpNotContext ctx) {
        TACBlock result = new TACBlock();
        TACBlock expr = this.visit(ctx.expression());
        String labelFalse = this.genlab();
        String labelEnd = this.genlab();
        String res = this.genreg();
        
        result.addAll(expr);
        result.add(TACOp.jz(expr.getResult(), labelFalse));
        result.add(TACOp.immed(res, 0));
        result.add(TACOp.jmp(labelEnd));
        result.add(TACOp.label(labelFalse));
        result.add(TACOp.immed(res, 1));
        result.add(TACOp.label(labelEnd));
        result.setResult(res);
        
        return result;
    }

    public TACBlock visitExpGroup(MiniJavaParser.ExpGroupContext ctx) {
        // Grouping just changes the order of parsing, so no need to generate code.
        return this.visit(ctx.expression());
    }
    
    public TACBlock visitExpLocalVar(MiniJavaParser.ExpLocalVarContext ctx) {
        return this.lookupIdentifier(ctx.identifier());
    }

    public TACBlock visitExpThis(MiniJavaParser.ExpThisContext ctx) {
        // "this" is always in vl0, so no need to generate any code.
        TACBlock result = new TACBlock();
        result.setResult("vl0");
        return result;
    }

    // ------------------------------------------------------------------------

    private TACBlock lookupIdentifier(MiniJavaParser.IdentifierContext ctx) {
        TACBlock result = new TACBlock();

        // Is the identifier a method local variable or a class instance variable?
        String id = ctx.getText();
        if (this.method.hasVar(id)) {
            // Variable is stored in vl register.
            // No need to generate any code; just return the register.
            result.setResult("vl" + this.method.getVarIndex(id));
        }
        else if (this.current.hasAnyVar(id)) {
            // Variable is stored in memory, indexed by this (vl0).
            String idx = this.genreg();
            String field = this.genreg();
            String res = this.genreg();
            result.add(TACOp.immed(idx, this.current.getVarIndex(id)));
            result.add(TACOp.offset(field, "vl0", idx));
            result.add(TACOp.load(res, field));
        }
        else {
            System.err.println("Unrecognised variable: " + id);
            assert(false);
        }

        return result;
    }

    // ------------------------------------------------------------------------
    
    private String genreg() {
        int n = this.regs;
        this.regs++;
        return "r" + n;
    }
    
    private String genlab() {
        String label = this.method.getQualifiedName() + "@" + this.labels;
        this.labels++;
        return label;
    }

    public static TACBlock generateVTables(SymbolTable sym) {
        TACBlock result = new TACBlock();
        result.add(TACOp.label("INIT"));

        String one = "r1";
        result.add(TACOp.immed(one, 1));
        
        for (Class c : sym.values()) {
            int n = c.getClassIndex();
            String base = "vg" + n;
            String size = "r2";
            String entry = "r3";
            String method = "r4";
            result.add(TACOp.immed(size, c.allMethods().size()));
            result.add(TACOp.malloc(base, size));
            result.add(TACOp.mov(entry, base));
            for (Method m : c.allMethods()) {
                result.add(TACOp.addrof(method, m.getQualifiedName()));
                result.add(TACOp.store(entry, method));
                result.add(TACOp.offset(entry, entry, one));
            }
        }
        result.add(TACOp.ret());
        return result;
    }

    
}

