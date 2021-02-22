package babycino;

// Interface for all optimisations on blocks of TAC.
public interface TACBlockOptimiser {
    // Optimise a TACBlock. Return the optimised block, or null if no change.
    // May modify the TACBlock passed in.
    TACBlock optimise(TACBlock code);
}

