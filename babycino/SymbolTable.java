package babycino;

import java.util.HashMap;

// The "symbol table" holds information about all entities defined in a program.
// For this compiler, it is primarily a record of all the classes, but
// additionally stores the location of main() and static types of method calls.
//
// For convenience, this just extends an existing library class.
// Object orientation purists may argue we should create a class containing
// a HashMap and wrapper methods for just those HashMap methods we need.
public class SymbolTable extends java.util.LinkedHashMap<String, Class> {

    // The statement in the static main() method.
    MiniJavaParser.StatementContext main;
    
    // Static types of method calls.
    HashMap<MiniJavaParser.ExpMethodCallContext, Type> types;

    // Create a new symbol table.
    public SymbolTable() {
        this.main = null;
        this.types = new HashMap<MiniJavaParser.ExpMethodCallContext, Type>();
        // Make Object, the root of the inheritance hierarchy, the first object.
        // It has no point of definition, so the class has no associated context.
        this.put("Object", new Class("Object", null));
    }

    // Add a class to the symbol table.
    public Class put(String key, Class val) {
        // Record the index of the class within the Class object.
        // This serves as a unique ID for the Class.
        // The global register with the same number will hold the Class vtable.
        val.setClassIndex(this.size());
        return super.put(key, val);
    }

    // Record the main() method of the program.
    public void setMain(MiniJavaParser.StatementContext main) {
        this.main = main;
    }

    // Return the main() method of the program.
    public MiniJavaParser.StatementContext getMain() {
        return this.main;
    }

    // Record the static type of the receiver in a method call.
    // The Context identifies the location of the method call in the parse tree
    // and hence the program text.
    public void setStaticType(MiniJavaParser.ExpMethodCallContext ctx, Type t) {
        this.types.put(ctx, t);
    }

    // Return the static type of the receiver in a method call.
    public Type getStaticType(MiniJavaParser.ExpMethodCallContext ctx) {
        return this.types.get(ctx);
    }

    // For informational/debugging purposes, dump the classes/methods in a
    // program to standard output.
    public void dump() {
        // Iterate over all the classes and print their signatures.
        for (Class c : this.values()) {
            // Skip Object and the first class (which holds main()).
            if (c.getBase() == null) {
                continue;
            }
            System.out.println(c.signature());
            System.out.println();
        }
    }
}

