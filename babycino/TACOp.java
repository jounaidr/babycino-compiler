package babycino;

// A Three Address Code operation.
public class TACOp {
    // Rather than having 15-20 tiny subclasses to cover each kind of TAC operation,
    // have a single giant "struct" with all possible TAC fields.

    // The TACOpType determines the actual instruction.
    TACOpType type;
    // Up to 3 register arguments.
    String r1, r2, r3;
    // A label argument.
    String label;
    // An immediate integer constant argument.
    int n;

    // The constructor is private, so TACOps can only be created by the static
    // convenience factory methods.
    private TACOp(TACOpType type, String r1, String r2, String r3, String label, int n) {
        // Just save all the arguments in the fields.
        this.type = type;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.label = label;
        this.n = n;
    }

    // Accessors to return the values of the TACOp fields:

    public TACOpType getType() {
        return this.type;
    }

    public String getR1() {
        return this.r1;
    }
    
    public String getR2() {
        return this.r2;
    }

    public String getR3() {
        return this.r3;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public int getN() {
        return this.n;
    }

    // Convenience static factory methods to return TACOps of a specific type:
    
    public static TACOp mov(String r1, String r2) {
        return new TACOp(TACOpType.MOV, r1, r2, null, null, 0);
    }

    public static TACOp immed(String r1, int n) {
        return new TACOp(TACOpType.IMMED, r1, null, null, null, n);
    }
    
    public static TACOp load(String r1, String r2) {
        return new TACOp(TACOpType.LOAD, r1, r2, null, null, 0);
    }

    public static TACOp store(String r1, String r2) {
        return new TACOp(TACOpType.STORE, r1, r2, null, null, 0);
    }

    public static TACOp binop(String r1, String r2, String r3, int n) {
        return new TACOp(TACOpType.BINOP, r1, r2, r3, null, n);
    }

    // Include a convenience method for this binop, as size addition is so common.
    public static TACOp add(String r1, String r2, String r3) {
        return TACOp.binop(r1, r2, r3, TACOp.binopToCode("+"));
    }

    // Include a convenience method for this binop, as offset addition is so common.
    // "offset" doesn't actually occur in MiniJava code, but is needed in the
    // generated TAC for pointer arithmetic. The difference from "+" is that
    // r3 holds an offset in machine words, not bytes. So "offset 1" may mean
    // "+ 4" on 32-bit machines or "+ 8" on 64-bit machines, rather than "+ 1".
    public static TACOp offset(String r1, String r2, String r3) {
        return TACOp.binop(r1, r2, r3, TACOp.binopToCode("offset"));
    }

    public static TACOp param(String r1) {
        return new TACOp(TACOpType.PARAM, r1, null, null, null, 0);
    }

    public static TACOp call(String r1) {
        return new TACOp(TACOpType.CALL, r1, null, null, null, 0);
    }
    
    public static TACOp ret() {
        return new TACOp(TACOpType.RET, null, null, null, null, 0);
    }
    
    public static TACOp label(String label) {
        return new TACOp(TACOpType.LABEL, null, null, null, label, 0);
    }

    public static TACOp jmp(String label) {
        return new TACOp(TACOpType.JMP, null, null, null, label, 0);
    }

    public static TACOp jz(String r1, String label) {
        return new TACOp(TACOpType.JZ, r1, null, null, label, 0);
    }

    public static TACOp malloc(String r1, String r2) {
        return new TACOp(TACOpType.MALLOC, r1, r2, null, null, 0);
    }

    public static TACOp read(String r1) {
        return new TACOp(TACOpType.READ, r1, null, null, null, 0);
    }

    public static TACOp write(String r1) {
        return new TACOp(TACOpType.WRITE, r1, null, null, null, 0);
    }

    public static TACOp addrof(String r1, String label) {
        return new TACOp(TACOpType.ADDROF, r1, null, null, label, 0);
    }
    
    public static TACOp nop() {
        return new TACOp(TACOpType.NOP, null, null, null, null, 0);
    }

    // Convert between string representations of binary operations, as they
    // occur in MiniJava programs, and an integer encoding for use in a TACOp:

    public static int binopToCode(String op) {
        switch (op) {
            case "<":
                return 0;
            case "+":
                return 1;
            case "-":
                return 2;
            case "*":
                return 3;
            case "offset":
                return 4;
            default:
                assert(false);
                return -1;
        }
    }

    public static String codeToBinop(int n) {
        switch (n) {
            case 0:
                return "<";
            case 1:
                return "+";
            case 2:
                return "-";
            case 3:
                return "*";
            case 4:
                return "offset";
            default:
                assert(false);
                return "";
        }
    }

    // Pretty-print TACOps.    
    public String toString() {
        switch (this.type) {
            case MOV:
                return "    " + this.r1 + " = " + this.r2;
            case IMMED:
                return "    " + this.r1 + " = " + this.n;
            case LOAD:
                return "    " + this.r1 + " = [" + this.r2 + "]";
            case STORE:
                return "    " + "[" + this.r1 + "] = " + this.r2;
            case BINOP:
                return "    " + this.r1 + " = " + this.r2 + " " + TACOp.codeToBinop(this.n) + " " + this.r3;
            case PARAM:
                return "    " + "param " + this.r1;
            case CALL:
                return "    " + "call " + this.r1;
            case RET:
                return "    " + "return";
            case LABEL:
                return this.label + ":";
            case JMP:
                return "    " + "jmp " + this.label;
            case JZ:
                return "    " + "if (" + this.r1 + "=0) jmp " + this.label;
            case MALLOC:
                return "    " + this.r1 + " = malloc " + this.r2;
            case READ:
                return "    " + "read " + this.r1;
            case WRITE:
                return "    " + "write " + this.r1;
            case ADDROF:
                return "    " + this.r1 + " = " + this.label;
            case NOP:
                return "    ";
            default:
                assert(false);
                return "";
        }
    }

}

