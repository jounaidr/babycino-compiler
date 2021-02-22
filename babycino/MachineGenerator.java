package babycino;

import java.io.PrintWriter;
import java.util.List;

// Interface for machine code/assembly language generators.
public interface MachineGenerator {
    // Translate intermediate "code" into machine code and write it to "out".
    public void generate(PrintWriter out, List<TACBlock> code);
}

