package babycino;

import java.util.List;

// Optimiser to remove unreachable, pointless or redundant code.
public class TACDeadCodeOptimiser implements TACBlockOptimiser {
    public TACDeadCodeOptimiser() {
    
    }

    // Remove dead (unreachable) and redundant (unused) code from a TACBlock.
    public TACBlock optimise(TACBlock code) {
        // Use the flow analysis to discover what is dead.
        TACFlowAnalysis flow = new TACFlowAnalysis(code);
        TACBlock result = new TACBlock();

        // Copy just the reachable, nonredundant code into the result.
        for (int n = 0; n < code.size(); n++) {
            // Eliminate unreachable code.
            if (!flow.reachable(n)) {
                continue;
            }
            // Eliminate unused labels.
            // Be careful not to eliminate the label at the start of a block.
            if (code.get(n).getType() == TACOpType.LABEL && n > 0) {
                List<Integer> preds = flow.pred(n);
                if (preds.size() == 1 && (preds.get(0) == (n-1))) {
                    continue;
                }
            }
            // Eliminate definitions with no uses.
            if (flow.redundant(n)) {
                continue;
            }
            
            // Preserve everything else.
            result.add(code.get(n));
        }

        // If no code was removed, there was no optimisation.
        if (result.size() == code.size()) {
            return null;
        }

        // Otherwise, return the optimised code.
        return result;
    }

}

