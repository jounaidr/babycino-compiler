package babycino;

import java.util.ArrayList;

// To add a new peephole optimisation:
//
// 1. Write a class that implements Peephole.
//   * The method optimise() gets called with n set to every index in a
//   block in turn.
//   * Your method should check if the code at n can be optimised. If it can be,
//   your method should do so by updating the code block.
//   * Your method should return true if it changed the code and false if it
//   did not. This lets the optimiser know whether it is worth trying more
//   cycles of optimisation.
//
// 2. In the constructor for TACPeepholeOptimiser, add an instance of your new
// class to the list.

// Peephole optimiser.
public class TACPeepholeOptimiser implements TACBlockOptimiser {

    // Impose a limit on the number of cycles of optimisationn.
    // If the limit is reached, it probably means there is a bug in one of
    // the optimisers.
    private static final int LIMIT = 100;

    // Interface for a single optimisation.
    private interface Peephole {
        // Optimise a block of code, looking at operation n.
        // Return whether the code was changed.
        public boolean optimise(TACBlock code, int n);
    }

    // List of all optimisations to try.
    private ArrayList<Peephole> optimisations;

    public TACPeepholeOptimiser() {
        // Initialise the list of optimisations.
        this.optimisations = new ArrayList<Peephole>();
        this.optimisations.add(new ImmedBinop());
        this.optimisations.add(new ImmedMov());
        this.optimisations.add(new ImmedJz());
        this.optimisations.add(new JumpNext());
    }

    // Optimise a block of code by checking every location.
    // Keep optimising until no more optimisations are applicable.
    // Return the modified block, or null if no changes were made.
    public TACBlock optimise(TACBlock code) {
        // Track how many cycles of optimisation have been attempted.
        int cycle = 0;
        // Track whether any cycle has changed the code.
        boolean changedEver = false;

        // Keep optimising until nothing changes or the hard limit is reached.
        while (cycle < LIMIT) {
            // Track whether the current cycle has changed the code.
            boolean changedRecently = false;
            // Try every location in the code block.
            for (int n = 0; n < code.size(); n++) {
                // Try every enabled optimisation.
                for (Peephole p : this.optimisations) {
                    // Apply one optimisation.
                    changedRecently = p.optimise(code, n) || changedRecently;
                }
            }
            // Update the record of whether the code has ever changed.
            changedEver = changedEver || changedRecently;
            // Break out if nothing changed this cycle.
            if (!changedRecently) {
                break;
            }
            cycle++;
        }

        // Give a warning if the cycle limit was reached.
        if (cycle == LIMIT) {
            System.err.println("Warning: TAC peephole optimiser reached cycle limit");
        }
        // Return the code block passed in, but modified, if any optimisations
        // were made.
        if (changedEver) {
            return code;
        }
        else {
            return null;
        }
    }

    // Helper function for precomputing the results of binary operations.
    private static int precompute(int op, int arg1, int arg2) {
        switch (TACOp.codeToBinop(op)) {
            case "<":
                return (arg1 < arg2) ? 1 : 0;
            case "+":
                return arg1 + arg2;
            case "-":
                return arg1 - arg2;
            case "*":
                return arg1 * arg2;
            // We should never encounter "offset", as we don't know any memory
            // addresses at compile-time.
            case "offset":
                assert(false);
                return 0;
            default:
                assert(false);
                return 0;
        }
    }

    // If the arguments to a binary operation are constants set in the
    // preceding code, compute the result and set it immediately.
    private class ImmedBinop implements Peephole {
        public boolean optimise(TACBlock code, int n) {
            // Check there are 3 instructions.
            if (n + 2 >= code.size()) {
                return false;
            }
            TACOp op1 = code.get(n);
            TACOp op2 = code.get(n+1);
            TACOp op3 = code.get(n+2);

            // Optimise: r1 = k1; r2 = k2; r3 = r1 op r2;
            if ((op1.getType() == TACOpType.IMMED) &&
                (op2.getType() == TACOpType.IMMED) &&
                (op3.getType() == TACOpType.BINOP) &&
                (op1.getR1().equals(op3.getR2())) &&
                (op2.getR1().equals(op3.getR3())) &&
                (op3.getN() != TACOp.binopToCode("offset"))) {
                code.set(n+2, TACOp.immed(op3.getR1(), precompute(op3.getN(), op1.getN(), op2.getN())));
                return true;
            }
            // Optimise: r2 = k2; r1 = k1; r3 = r1 op r2;
            else if
               ((op1.getType() == TACOpType.IMMED) &&
                (op2.getType() == TACOpType.IMMED) &&
                (op3.getType() == TACOpType.BINOP) &&
                (op2.getR1().equals(op3.getR2())) &&
                (op1.getR1().equals(op3.getR3())) &&
                (op3.getN() != TACOp.binopToCode("offset"))) {
                code.set(n+2, TACOp.immed(op3.getR1(), precompute(op3.getN(), op2.getN(), op1.getN())));
                return true;
            }
            else {
                return false;
            }
        }
    }

    // If a constant is loaded into a register and then moved into another,
    // set it directly in the second register.
    private class ImmedMov implements Peephole {
        public boolean optimise(TACBlock code, int n) {
            // Check there are 2 instructions.
            if (n + 1 >= code.size()) {
                return false;
            }
            TACOp op1 = code.get(n);
            TACOp op2 = code.get(n+1);

            // Optimise: r1 = k; r2 = r1;
            if ((op1.getType() == TACOpType.IMMED) &&
                (op2.getType() == TACOpType.MOV) &&
                (op1.getR1().equals(op2.getR2()))) {
                code.set(n+1, TACOp.immed(op2.getR1(), op1.getN()));
                return true;
            }
            else {
                return false;
            }
        }
    }

    // If a constant 0 is loaded into a register and then used for a
    // conditional jump, turn it into an unconditional jump.
    private class ImmedJz implements Peephole {
        public boolean optimise(TACBlock code, int n) {
            // Check there are 2 instructions.
            if (n + 1 >= code.size()) {
                return false;
            }
            TACOp op1 = code.get(n);
            TACOp op2 = code.get(n+1);
            
            // Check the instructions hae form: mov r1, k; if (r1 = 0) jmp lab;
            if (!((op1.getType() == TACOpType.IMMED) && (op2.getType() == TACOpType.JZ) && (op1.getR1().equals(op2.getR1())))) {
                return false;
            }
            // Optimise: mov r1, 0; if (r1 = 0) jmp lab;
            if (op1.getN() == 0) {
                code.set(n+1, TACOp.jmp(op2.getLabel()));
                return true;
            }
            // if r1 = 0, remove n+1 instruction;
            if (op1.getN() == 1) {
            	code.remove(n+1);
            	return true;
            }
            else {
                return false;
            }
        }
    }

    // If the target of a jump is a label immediately afterwards, remove the jump.
    private class JumpNext implements Peephole {
        public boolean optimise(TACBlock code, int n) {
            // Check there are 2 instructions.
            if (n + 1 >= code.size()) {
                return false;
            }
            TACOp op1 = code.get(n);
            TACOp op2 = code.get(n+1);

            // Optimise: jmp lab; lab: ;
            if (((op1.getType() == TACOpType.JMP) || (op1.getType() == TACOpType.JZ)) &&
                (op2.getType() == TACOpType.LABEL) &&
                (op1.getLabel().equals(op2.getLabel()))) {
                code.set(n, TACOp.nop());
                return true;
            }
            else {
                return false;
            }
        }
    }
    
}

