package babycino;

// A MiniJava type.
public class Type {
    // The kind (type of the type).
    private Kind k;
    // If the kind is OBJECT, the class of the object.
    private Class c;

    // Construct a type of non-object kind.
    public Type(Kind k) {
        assert(k != Kind.OBJECT);
        this.k = k;
    }

    // Construct a type of object kind.
    public Type(Class c) {
        this.k = Kind.OBJECT;
        this.c = c;
    }

    // Accessors to check the kind/object of the type:
    
    public boolean isIntArray() {
        return this.k == Kind.INTARRAY;
    }
    
    public boolean isBoolean() {
        return this.k == Kind.BOOLEAN;
    }
    
    public boolean isInt() {
        return this.k == Kind.INT;
    }
    
    public boolean isObject() {
        return this.k == Kind.OBJECT;
    }

    public Class getObject() {
        if (this.k != Kind.OBJECT) {
            return null;
        }
        return this.c;
    }

    // Pretty-print a type.
    public String toString() {
        switch (this.k) {
            case INTARRAY:
                return "int[]";
            case BOOLEAN:
                return "boolean";
            case INT:
                return "int";
            case OBJECT:
                return this.c.getName();
            default:
                assert(false);
                return "";
        }
    }

    // Use the symbol table to turn a parse tree type into a Type object.
    public static Type extractType(SymbolTable sym, MiniJavaParser.TypeContext ctx) {
        TypeExtractor v = new TypeExtractor(sym);
        return v.visit(ctx);
    }

    // Can a value of type t be assigned to a variable of this type?
    public boolean compatibleWith(Type t) {
        if (this.isIntArray()) {
            return t.isIntArray();
        }
        else if (this.isBoolean()) {
            return t.isBoolean();
        }
        else if (this.isInt()) {
            return t.isInt();
        }
        
        // Object type.
        assert(t.isObject());
        if (!this.isObject()) {
            return false;
        }
        return t.getObject().isa(this.getObject());
    }

    // Check equality of two types.
    public static boolean equals(Type t1, Type t2) {
        if (t1 == null && t2 == null) {
            return true;
        }
        else if (t1 == null || t2 == null) {
            return false;
        }
        else if (t1.isIntArray()) {
            return t2.isIntArray();
        }
        else if (t1.isBoolean()) {
            return t2.isBoolean();
        }
        else if (t1.isInt()) {
            return t2.isInt();
        }
        else {
            assert(t1.isObject());
            if (!t2.isObject()) {
                return false;
            }
            else if (t1.getObject() == null && t2.getObject() == null) {
                return true;
            }
            else if (t1.getObject() == null || t2.getObject() == null) {
                return false;
            }
            else {
                return t1.getObject().getName().equals(t2.getObject().getName());
            }
        }
    }
}

