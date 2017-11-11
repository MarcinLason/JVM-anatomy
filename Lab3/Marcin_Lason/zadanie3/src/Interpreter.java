import npj.generated.NPJLexer;
import npj.generated.NPJParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.FileInputStream;
import java.io.IOException;

public class Interpreter {

    public static final String NPJ_HEAP_SIZE_PROPERTY = "npj.heap.size";
    private final String programFile;
    private final int heapSize;
    private final int[] heap;

    public Interpreter(String programFile, int heapSize) {
        this.programFile = programFile;
        this.heapSize = heapSize;
        this.heap = new int[heapSize];
    }

    public static void main(String[] args) {
        new Interpreter(args[0], Integer.valueOf(System.getProperty(NPJ_HEAP_SIZE_PROPERTY))).run();
    }

    private NPJParser createParser() throws IOException {
        NPJLexer lexer = new NPJLexer(new ANTLRInputStream(new FileInputStream(programFile)));
        TokenStream tokens = new CommonTokenStream(lexer);
        return new NPJParser(tokens);
    }

    public void run() {
        NPJParser parser = null;
        try {
            parser = createParser();
        } catch (IOException e) {
            System.out.println("Cannot read " + programFile);
            System.exit(1);
        }
        parser.addParseListener(new NPJInterpreter(new SemiSpaceCopyingMemory(heapSize), heap));
        parser.program();
    }

}
