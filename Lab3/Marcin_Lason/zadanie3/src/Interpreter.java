import npj.generated.NPJLexer;
import npj.generated.NPJParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.FileInputStream;
import java.io.IOException;

public class Interpreter {

    public static final String HEAP_SIZE_PROPERTY = "npj.heap.size";
    private final String programFilename;
    private final int heapSize;
    private final int[] heap;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong number of arguments - pass program file name");
            System.exit(1);
        }
        new Interpreter(args[0], Integer.valueOf(System.getProperty(HEAP_SIZE_PROPERTY))).run();
    }

    public Interpreter(String programFilename, int heapSize) {
        this.programFilename = programFilename;
        this.heapSize = heapSize;
        this.heap = new int[heapSize];
    }

    public void run() {
        NPJParser npjParser = null;
        try {
            NPJLexer npjLexer = new NPJLexer(new ANTLRInputStream(new FileInputStream(programFilename)));
            TokenStream tokenStream = new CommonTokenStream(npjLexer);
            npjParser = new NPJParser(tokenStream);
        } catch (IOException e) {
            System.out.println("Error while reading " + programFilename + ".");
            System.exit(1);
        }
        npjParser.addParseListener(new NPJInterpreter(new SemiSpaceCopyingMemory(heapSize), heap));
        npjParser.program();
    }
}
