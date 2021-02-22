package babycino;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// A MiniJava method.
public class Method {
    // The name of the method.
    private String name;
    // The return type of the method.
    private Type ret;
    // The parent class of the method.
    private Class parent;
    // The point in the parse tree where the class is defined.
    private MiniJavaParser.MethodDeclarationContext ctx;
    // A map of the method's local variable names and types.
    private LinkedHashMap<String, Type> vars;
    // A corresponding map of the method's local variable names and indices.
    private HashMap<String, Integer> varidx;
    // A map of the method's parameter names and types.
    private LinkedHashMap<String, Type> params;

    // At construction time, we need to know the name, return type, parent class
    // and source location of the method.
    public Method(String name, Type ret, Class parent, MiniJavaParser.MethodDeclarationContext ctx) {
        // Save the passed parameters.
        this.name = name;
        this.ret = ret;
        this.parent = parent;
        this.ctx = ctx;

        // Initialise the hashtables.
        this.vars = new LinkedHashMap<String, Type>();
        this.varidx = new HashMap<String, Integer>();
        this.params = new LinkedHashMap<String, Type>();

        // Add "this" as 1st variable, as it works as an implicit parameter,
        // stored in vl0. Don't put it in params, or it would have to be
        // passed in explicitly to method calls.
        this.addVar("this", new Type(parent));
    }

    // Simple accessors:

    // Return the name of the method.
    public String getName() {
        return this.name;    
    }

    // Return the parse tree Context of this class.
    public MiniJavaParser.MethodDeclarationContext getCtx() {
        return this.ctx;
    }
    
    // Return the qualified name of the method (including class name).
    public String getQualifiedName() {
        return this.parent.getName() + "." + this.name;
    }

    // Return the return type of the method.    
    public Type getReturnType() {
        return this.ret;
    }

    // Add and retrieve variables:

    // Add a variable of a specific type to the method.
    public void addVar(String name, Type type) {
        this.varidx.put(name, this.vars.size());
        this.vars.put(name, type);
    }

    // Check whether the method has a variable with a given name.
    public boolean hasVar(String name) {
        return this.vars.containsKey(name);
    }

    // Get the type of a named variable.
    public Type getVarType(String name) {
        // Note: During semantic analysis, this could be null if the variable
        // had an invalid type. Use hasVar() to check existence of variables.
        return this.vars.get(name);
    }

    // Get the index of a named variable.
    public int getVarIndex(String name) {
        return this.varidx.get(name);
    }

    // Add and retrieve parameters, which are also stored as variables:

    // Add a parameter of a specific type to the method.
    public void addParam(String name, Type type) {
        this.params.put(name, type);
        // Parameters are treated as local variables within the method body.
        this.addVar(name, type);
    }

    // Get the EntrySet view of the parameters.
    public Set<Map.Entry<String, Type>> getParams() {
        return this.params.entrySet();
    }

    // Return the method signature as a string.
    // Mainly used for debug info.
    public String signature() {
        String sig = "    public " + this.ret.toString() + " " + this.name + "(";
        boolean first = true;
        for (Map.Entry e : this.params.entrySet()) {
            if (first) {
                first = false;
            }
            else {
                sig += ", ";
            }
            sig += e.getValue().toString() + " " + e.getKey().toString();
        }
        sig += ");\n";
        return sig;
    }
}

