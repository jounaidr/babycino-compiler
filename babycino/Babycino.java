package babycino;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Almost every Java-related tool seems to have a coffee-related name.
public class Babycino {
    public static final int LIMIT = 100;

    public static void main(String args[]) {

        // Check the command-line arguments.
        if (args.length != 2) {
            System.err.println("Usage: babycino in.java out.c");
            System.exit(1);
        }

        // Read the input file.
        ANTLRInputStream input = null;
        try {
            input = new ANTLRFileStream(args[0]);
        }
        catch (IOException e) {
            System.err.println("Error reading input file: " + e);
            System.exit(1);
        }

        // Create the output file.
        FileWriter output = null;
        try {
            output = new FileWriter(args[1]);
        }
        catch (IOException e) {
            System.err.println("Error writing output file: " + e);
            System.exit(1);
        }
        
        try {
            // Call each stage of the compiler in sequence.
            ParseTree tree = parse(input);
            SymbolTable sym = semantic(tree);
            List<TACBlock> tac = generateTAC(tree, sym);
            System.out.println("UNOPTIMISED INTERMEDIATE CODE:");
            dumpTAC(tac);
            tac = optimiseTAC(tac);
            System.out.println("OPTIMISED INTERMEDIATE CODE:");
            dumpTAC(tac);
            generateCCode(tac, output);
        }

        catch (CompilerException e) {
            System.err.println("Exiting due to earlier error.");
            System.exit(1);
        }
    }


    // LEXICAL AND SYNTAX ANALYSIS:
    public static ParseTree parse(ANTLRInputStream input) throws CompilerException {
        // Set up the ANTLR lexer and parser. Parse the input.
        MiniJavaLexer lexer = new MiniJavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiniJavaParser parser = new MiniJavaParser(tokens);
        ParseTree tree = parser.goal();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("Syntax errors encountered during parsing.");
            throw new CompilerException();
        }
        return tree;
    }


    // SEMANTIC ANALYSIS:
    public static SymbolTable semantic(ParseTree tree) throws CompilerException {
        // Create the symbol table.
        SymbolTable sym = new SymbolTable();
        // Prepare to walk the parse tree.
        ParseTreeWalker walker = new ParseTreeWalker();

        // Populate the symbol table with the names of all the classes.
        {
            ClassFinder finder = new ClassFinder(sym);
            walker.walk(finder, tree);
            finder.die();
        }
        
        // Populate the symbol table with the members of classes.
        // Also build the inheritance hierarchy.
        {
            ClassAnalysis analysis = new ClassAnalysis(sym);
            walker.walk(analysis, tree);
            analysis.die();
        }

        // Resolve the inheritance hierarchy.
        for (Class c : sym.values()) {
            c.inherit();
        }

        // Uncomment this to dump a summary of the symbol table.
        System.out.println("SYMBOL TABLE SUMMARY:");
        sym.dump();

        // Typechecking.
        {
            TypeChecker typechecker = new TypeChecker(sym);
            walker.walk(typechecker, tree);
            typechecker.die();
        }
        
        return sym;
    }


    // INTERMEDIATE CODE GENERATION:
    public static List<TACBlock> generateTAC(ParseTree tree, SymbolTable sym) {
        List<TACBlock> tac = new ArrayList<TACBlock>();

        // Generate code for vtables.
        tac.add(TACGenerator.generateVTables(sym));

        // Iterate over all classes.
        Iterator<Class> classes = sym.values().iterator();
        classes.next();
        // Generate code for main() in first class.
        {
            Class main = classes.next();
            TACGenerator gen = new TACGenerator(sym, main);
            TACBlock mainBlock = new TACBlock();
            mainBlock.add(TACOp.label("MAIN"));
            mainBlock.addAll(gen.visit(sym.getMain()));
            mainBlock.add(TACOp.ret());
            tac.add(mainBlock);
        }
        // Generate code for every other class.
        while (classes.hasNext()) {
            Class c = classes.next();
            TACGenerator gen = new TACGenerator(sym, c);
            for (Method m : c.ownMethods()) {
                tac.add(gen.visit(m.getCtx()));
            }
        }
        
        return tac;
    }


    // Dump Three Address Code to standard output.
    public static void dumpTAC(List<TACBlock> tac) {
        for (TACBlock b : tac) {
            b.dump();
        }
    }


    // INTERMEDIATE CODE OPTIMISATION:
    public static List<TACBlock> optimiseTAC(List<TACBlock> tac) {
        TACBlockOptimiser peephole = new TACPeepholeOptimiser();
        TACBlockOptimiser deadcode = new TACDeadCodeOptimiser();

        for (int n = 0; n < tac.size(); n++) {
            int cycle = 0;
            while (cycle < LIMIT) {
                boolean changed = false;

                {
                    TACBlock opt = peephole.optimise(tac.get(n));
                    if (opt != null) {
                        tac.set(n, opt);
                        changed = true;
                    }
                }
                {
                    TACBlock opt = deadcode.optimise(tac.get(n));
                    if (opt != null) {
                        tac.set(n, opt);
                        changed = true;
                    }
                }

                if (!changed) {
                    break;
                }
                cycle++;
            }
            if (cycle == LIMIT) {
                System.err.println("Warning: Optimisation reached cycle limit.");
            }
        }

        return tac;
    }


    // MACHINE CODE GENERATION:
    public static void generateCCode(List<TACBlock> tac, Writer output) {
        MachineGenerator gen = new CGenerator();
        PrintWriter writer = new PrintWriter(output);
        gen.generate(writer, tac);
        writer.close();
    }
    
}

