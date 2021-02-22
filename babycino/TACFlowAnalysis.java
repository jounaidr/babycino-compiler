package babycino;

import java.util.*;

// Control and data flow analyses for Three Address Code.
public class TACFlowAnalysis {

    // The block of code being analysed.
    private TACBlock code;
    // Map giving the location of each label.
    private Map<String, Integer> labelLocs;
    // For each location, is it reachable?
    private List<Boolean> reachables;
    // For each location, which locations can precede it in execution?
    private List<ArrayList<Integer>> preds;

    // Set up analysis for a code block.
    public TACFlowAnalysis(TACBlock code) {
        this.code = code;
        // Locations of labels are cheap to find and nearly always needed.
        this.buildLabelLocs();
    }

    // Build a map from label names to locations.
    private void buildLabelLocs() {
        this.labelLocs = new HashMap<String, Integer>();
        for (int n = 0; n < this.code.size(); n++) {
            TACOp op = this.code.get(n);
            if (op.getType() == TACOpType.LABEL) {
                this.labelLocs.put(op.getLabel(), n);
            }
        }
    }

    // Return a list of locations that can succeed (follow) a location in execution?
    public List<Integer> succ(int n) {
        // Don't generate a non-existent "next" instruction for the last in a block.
        // For sensibly generated code, this ought not even to be possible,
        // as the last operation will be return or unconditional jump.
        boolean last = (n == this.code.size() - 1) ? true : false;
        TACOp op = this.code.get(n);

        switch (op.getType()) {
            // Unconditional jumps can't fall through to the next operation.
            case JMP:
                return Collections.singletonList(this.labelLocs.get(op.getLabel()));
            // Conditional jumps can fall through.
            case JZ:
                int dest = this.labelLocs.get(op.getLabel());
                if (last) {
                    return Collections.singletonList(dest);
                }
                else {
                    return pairList(dest, n+1);
                }
            // Returns can't go anywhere (within the block).
            case RET:
                return Collections.emptyList();
            // All other operations fall through.
            // (Even for calls, execution resumes at the following operation.)
            default:
                if (last) {
                    return Collections.emptyList();
                }
                else {
                    return Collections.singletonList(n+1);
                }
        }
    }

    // Return a list of instructions that can precede this instruction.
    public List<Integer> pred(int n) {
        // Compute this once, then cache it.
        if (this.preds == null) {
            this.buildPred();
        }
        return this.preds.get(n);
    }

    // Helper method: return a list containing two elements.
    private static <T> List<T> pairList(T a, T b) {
        ArrayList<T> res = new ArrayList<T>();
        res.add(a);
        res.add(b);
        return res;
    }

    // Return whether the operation at index n is reachable.
    public boolean reachable(int n) {
        // Compute this once, then cache it.
        if (this.reachables == null) {
            this.buildReachable();
        }
        return this.reachables.get(n);
    }

    // Compute whether each operation is reachable.
    private void buildReachable() {
        // This is a simple search algorithm.
        // It doesn't matter whether it's depth-first or breadth-first.
        this.reachables = new ArrayList<Boolean>();
        for (int n = 0; n < code.size(); n++) {
            this.reachables.add(false);
        }

        ArrayDeque<Integer> next = new ArrayDeque<Integer>();
        if (!code.isEmpty()) {
            next.add(0);
        }
        
        while (!next.isEmpty()) {
            int here = next.remove();
            
            if (this.reachables.get(here)) {
                continue;
            }
            this.reachables.set(here, true);
            next.addAll(this.succ(here));
        }
    }

    // Compute the possible predecessors of each operation.
    public void buildPred() {
        // Effectively, succ() gives us an adjacency list representation of the
        // block's flow graph. Reverse every edge to get the backwards graph.
        // Note that, while the forwards flow graph can have at most 2 out-edges
        // from every node, the backwards flow graph could have arbitrarily many,
        // although most nodes will have 1 and few will have more than 2.
        this.preds = new ArrayList<ArrayList<Integer>>();
        for (int n = 0; n < code.size(); n++) {
            this.preds.add(new ArrayList<Integer>());
        }
        for (int from = 0; from < code.size(); from++) {
            for (Integer to : this.succ(from)) {
                if (!this.preds.get(to).contains(from)) {
                    this.preds.get(to).add(from);
                }
            }
        }
    }

    private List<String> uses(int n) {
        TACOp op = this.code.get(n);
        switch (op.getType()) {
            // Uses r1 only:
            case PARAM:
            case CALL:
            case JZ:
            case WRITE:
                return Collections.singletonList(op.getR1());
            
            // Uses r2 only:
            case MOV:
            case LOAD:
            case MALLOC:
                return Collections.singletonList(op.getR2());

            // Uses r1 and r2:
            case STORE:
                return pairList(op.getR1(), op.getR2());

            // Uses r2 and r3:
            case BINOP:
                return pairList(op.getR2(), op.getR3());

            // A return implicitly uses r0.
            case RET:
                return Collections.singletonList("r0");
            
            // Anything else uses nothing:
            default:
                return Collections.emptyList();
        }
    }

    // Return a list of registers operation n defines.
    private List<String> defs(int n) {
        TACOp op = this.code.get(n);
        switch (op.getType()) {
            // These operations define r1.
            case MOV:
            case IMMED:
            case LOAD:
            case BINOP:
            case MALLOC:
            case READ:
            case ADDROF:
                return Collections.singletonList(op.getR1());

            // A call may put a result in r0.
            case CALL:
                return Collections.singletonList("r0");

            // Everything else defines nothing.
            default:
                return Collections.emptyList();
        }
    }

    // Is the operation at index n redundant?
    // That is, does it make a definition that is never used?
    public boolean redundant(int n) {
        // A more efficient route might be to work backwards from uses to
        // definitions, particularly as every use ought to have a definition
        // and often these are close together. When working forwards from
        // definitions, we can stop at the first use, but if there is none,
        // we may traverse the whole block.

        TACOp op = this.code.get(n);
        // Many operations are executed for side effects, so check against
        // this whitelist before proceeding.
        switch (op.getType()) {
            case MOV:
            case IMMED:
            case LOAD:
            case BINOP:
            case ADDROF:
                break;
            default:
                return false;
        }

        // Now we do a breadth-first search for uses.
        for (String def : this.defs(n)) {
            // Assignments to global variables are harder to flag as redundant,
            // as we would need to check that all paths make a new definition
            // before any use or potential use (through a call).
            // So assume they are always needed.
            // (This never happens for our code anyway, as all assignments to
            // global variables are the result of memory allocation.)
            if (def.startsWith("vg")) {
                return false;
            }

            boolean[] seen = new boolean[(this.code.size())];
            ArrayDeque<Integer> next = new ArrayDeque<Integer>();
            
            next.addAll(this.succ(n));
            while (!next.isEmpty()) {
                int here = next.remove();
                // Process each location only once to avoid an infinite loop.
                if (seen[here]) {
                    continue;
                }
                seen[here] = true;

                // A use of the register means the definition was not redundant.
                if (this.uses(here).contains(def)) {
                    return false;
                }

                // A new definition means, on this path, the original was redundant.
                if (this.defs(here).contains(def)) {
                    continue;
                }
                
                // Keep searching.
                next.addAll(this.succ(here));
            }
        }
        
        // If no use of the definition was found, the code is redundant.
        return true;
    }
}

