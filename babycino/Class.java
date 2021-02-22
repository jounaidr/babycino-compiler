package babycino;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

// Information about a MiniJava class.
public class Class {
    // The name of the class.
    private String name;
    // The point in the parse tree where the class is defined.
    private MiniJavaParser.ClassDeclarationContext ctx;
    // The base class of the class.
    private Class base;
    // The index of the class within the symbol table.
    private int classIdx;

    // Map from class variable name to type.
    private Map<String, Type> vars;
    // A corresponding map from class variable names to indices.
    private Map<String, Integer> varIdx;

    // Map from class method name to method.
    private Map<String, Method> ownMethods;
    // Map from class method name to method, including inherited methods.
    private Map<String, Method> allMethods;
    // A corresponding map from method names to indices.
    private Map<String, Integer> allMethodIdx;

    // To construct a class, we just need its name and source location.
    public Class(String name, MiniJavaParser.ClassDeclarationContext ctx) {
        // Only name of class and source location are initially known.
        this.name = name;
        this.ctx = ctx;
        // Base class and index within symbol table are set later.
        this.base = null;
        this.classIdx = -1;

        // Initialise all the maps.
        this.vars = new LinkedHashMap<>();
        this.varIdx = new HashMap<>();
        this.ownMethods = new LinkedHashMap<>();
        // Leave these until inheritance is resolved.
        this.allMethods = null;
        this.allMethodIdx = null;
    }

    // ------------------------------------------------------------------------
    // Simple accessors:

    // Return the name of the class.
    public String getName() {
        return this.name;
    }
    
    // Return the parse tree Context of this class.
    // Returns null for Object or main() class.
    public MiniJavaParser.ClassDeclarationContext getCtx() {
        return this.ctx;
    }

    // Get the base class of the class.
    public Class getBase() {
        return this.base;
    }
    
    // Set the base class of the class.
    public void setBase(Class base) {
        // This should only be done once, during semantic analysis.
        assert(this.base == null);
        this.base = base;
    }

    // Return the index of this class within the symbol table.
    // This is also the number of the vg register that holds a pointer to the
    // vtable for objects of this class.
    public int getClassIndex() {
        return this.classIdx;
    }
    
    // Set the index of this class within the symbol table.
    public void setClassIndex(int idx) {
        // Should only be called once, when the class is added to the table.
        assert (this.classIdx == -1);
        this.classIdx = idx;
    }

    // ------------------------------------------------------------------------
    // Add and retrieve instance variables (properties):

    // Add a variable of a specific type to the class.
    public void addVar(String name, Type type) {
        this.varIdx.put(name, this.vars.size());
        this.vars.put(name, type);
    }

    // Check whether the class has a variable with a given name.
    public boolean hasOwnVar(String name) {
        return this.vars.containsKey(name);
    }

    // Check whether the class or its ancestors have a variable with a given name.
    public boolean hasAnyVar(String name) {
        if (this.base == null) {
            return this.hasOwnVar(name);
        }
        else {
            return this.hasOwnVar(name) || this.base.hasAnyVar(name);
        }
    }

    // Get the index of a named variable.
    // This is the offset of the variable in an object's block of memory.
    public int getVarIndex(String name) {
        if (this.base == null) {
            return this.varIdx.get(name) + 1;
        }
        else if (this.hasOwnVar(name)) {
            return this.base.size() + this.varIdx.get(name);
        }
        else {
            return this.base.getVarIndex(name);
        }
    }

    // Get the type of a named variable.
    public Type getVarType(String name) {
        // Note: During semantic analysis, this could be null if the variable
        // had an invalid type. Use hasVar() to check existence of variables.
        if (this.base == null) {
            return this.vars.get(name);
        }
        else if (this.hasOwnVar(name)) {
            return this.vars.get(name);
        }
        else {
            return this.base.getVarType(name);
        }
    }

    // ------------------------------------------------------------------------
    // Add and retrieve methods:

    // Add a method to the class.    
    public void addMethod(Method method) {
        this.ownMethods.put(method.getName(), method);
    }

    // Look up a method defined in the class by name.
    public Method getOwnMethod(String name) {
        return this.ownMethods.get(name);
    }

    // Look up a method by name, including inherited methods.
    public Method getAnyMethod(String name) {
        return this.allMethods.get(name);
    }

    // Get the index of a named method within the class vtable.
    public int getAnyMethodIndex(String name) {
        return this.allMethodIdx.get(name);
    }

    // Return a Collection of all methods defined in the class.
    public Collection<Method> ownMethods() {
        return this.ownMethods.values();
    }

    // Return a Collection of all methods in the class, including inherited ones.
    public Collection<Method> allMethods() {
        return this.allMethods.values();
    }

    // ------------------------------------------------------------------------
    // Handling inheritance:
    
    // Check whether the given class is this class or one of its ancestors.
    public boolean isa(Class ancestor) {
        Class parent = this;

        // Assume class hierarchy is a DAG (no cycles).
        do {
            if (parent == ancestor) {
                return true;
            }
            parent = parent.base;
        } while (parent != null);

        return false;
    }

    // Return the size of an object in this class, in words.
    // This is the size of the variables, plus the size of the pointer to the
    // vtable. Note it doesn't include the vtable itself, which is shared
    // between all objects belonging to a class.
    public int size() {
        if (this.base == null) {
            return 1 + this.vars.size();
        }
        else {
            return this.base.size() + this.vars.size();
        }
    }

    // Resolve method inheritance for this class.
    public void inherit() throws CompilerException {
        // If already resolved, return immediately.
        if (this.allMethods != null) {
            return;
        }

        // Initialise the hashtables.
        this.allMethods = new LinkedHashMap<String, Method>();
        this.allMethodIdx = new HashMap<String, Integer>();

        // Resolve base class first, if necessary.
        if (this.base != null) {
            this.base.inherit();

            // All methods of the base class become ours by default.
            for (Method m : this.base.allMethods()) {
                this.allMethods.put(m.getName(), m);
            }
        }

        // Add our own methods to the list populated from the base class.
        for (Method m : this.ownMethods.values()) {
            if (!this.allMethods.containsKey(m.getName())) {
                // If the method is not overriding, this is simple.
                this.allMethodIdx.put(m.getName(), this.allMethodIdx.size());
                this.allMethods.put(m.getName(), m);
                continue;
            }

            // If a method with the same name already exists, check the signatures match.
            // If not, as MiniJava forbids overloading, this is an error.
            Method old = this.allMethods.get(m.getName());
            Set<Map.Entry<String, Type>> oldParams = old.getParams();
            Set<Map.Entry<String, Type>> newParams = m.getParams();

            if (!Type.equals(old.getReturnType(), m.getReturnType())) {
                System.err.println("Method " + m.getQualifiedName() + " overloads method " + old.getQualifiedName() +
                    ": return types " + m.getReturnType() + " and " + old.getReturnType() + " differ");
                throw new CompilerException();
            }
            
            if (oldParams.size() != newParams.size()) {
                System.err.println("Method " + m.getQualifiedName() + " overloads method " + old.getQualifiedName() +
                    ": parameter list lengths differ");
                throw new CompilerException();
            }

            Iterator<Map.Entry<String, Type>> oldIt = oldParams.iterator();
            Iterator<Map.Entry<String, Type>> newIt = newParams.iterator();
            while (oldIt.hasNext()) {
                Type t1 = oldIt.next().getValue();
                Type t2 = newIt.next().getValue();
                if (!Type.equals(t1, t2)) {
                    System.err.println("Method " + m.getQualifiedName() + " overloads method " + old.getQualifiedName() +
                        ": parameter types " + t1 + " and " + t2 + " differ");
                    throw new CompilerException();
                }
            }

            // Otherwise, it's a legal overriding. Our method replaces the parent's.
            // Note the code is actually the same as without overriding, as
            // the update behaviour of put() replaces a value without changing order.
            this.allMethodIdx.put(m.getName(), this.allMethodIdx.size());
            this.allMethods.put(m.getName(), m);
        }
        
        // For properties, conceptually:
        //
        // Add our properties to the parent's.
        // If a property with the same name already exists, leave it.
        // Our properties are always added to the end.
        // Any "overridden" properties are shadowed.
        //
        // It's easier to calculate this as needed than to precompute it.
        // We choose the easy implementation rather than worrying about speed.
        // Observe that hasAnyVar/getVarIndex/getVarType are more complex than
        // getAnyMethod/getAnyMethodIndex, but less complex than inherit.
    }

    // ------------------------------------------------------------------------
    // Printing and logging:

    // When printing a Class, just show its name.
    public String toString() {
        return this.name;
    }

    // Return a "signature" summarising the class and its members.
    public String signature() {
        String signature = "class " + this.name;
        if (this.base != null) {
            signature += " extends " + this.base.getName();
        }
        signature += " {\n";
        
        // Summarise the properties of the class.
        for (Map.Entry e : this.vars.entrySet()) {
            signature += "    " + e.getValue().toString() + " " + e.getKey() + ";\n";
        }

        // Summarise the methods of the class.
        for (Method m : this.ownMethods.values()) {
            signature += m.signature();
        }
        
        signature += "}\n";
        return signature;
    }

}

