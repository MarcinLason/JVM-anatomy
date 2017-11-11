import machine.Heap;
import npj.ast.parser.NPJParser;
import npj.ast.statements.Statement;
import npj.interpreter.InterpretingVisitor;
import org.antlr.runtime.RecognitionException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Interpreter {

    private static final String NPJ_HEAP_SIZE = "npj.heap.size";
    private final int heapSize;
    private final List<Statement> program;

    public static void main(String[] args) throws IOException, RecognitionException {
        // make sure we got one and only one parameter
        if (args.length != 1) {
            System.out.println("Wrong number of arguments - pass program file name");
            System.exit(1);
        }

        // get heap size
        int heapSize = getHeapSize();

        // initialize Njp VM with program file name, heap size and GC
        Interpreter interpreter = new Interpreter(args[0], heapSize);
        interpreter.run();
    }

    /**
     * Initializes Njp interpreter with specified program and heap size.
     *
     * @param programFile path to program file
     * @param heapSize    heap size
     */
    public Interpreter(String programFile, int heapSize) throws IOException, RecognitionException {
        InputStream programStream = getProgramStream(programFile);
        this.program = NPJParser.parse(programStream);
        this.heapSize = heapSize;
    }

    /**
     * Opens program file and returns program code input stream.
     *
     * @param programFile path to program file
     * @return input stream for program code
     */
    private InputStream getProgramStream(String programFile) {
        InputStream programStream = null;
        try {
            programStream = new FileInputStream(programFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open program file: " + programFile + ", exiting.");
            System.exit(2);
        }

        return programStream;
    }

    private static int getHeapSize() {
        String heapSize = System.getProperty(NPJ_HEAP_SIZE);
        if (heapSize == null) {
            throw new IllegalStateException(String.format("No %s property present", NPJ_HEAP_SIZE));
        }
        return Integer.parseInt(heapSize); //dummy implementation
    }

    private void run() {
        Heap heap = new Heap(heapSize);
        InterpretingVisitor interpretingVisitor = new NpjVisitor(heap);
        for (Statement statement : program) {
            statement.accept(interpretingVisitor);
        }
    }
}
