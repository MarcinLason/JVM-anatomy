import npj.generated.NPJBaseListener;
import npj.generated.NPJParser;
import utils.Type;
import utils.Variable;

import java.util.*;

public class NPJInterpreter extends NPJBaseListener {

    private final static int STRING_LENGTH_OFFSET = 1;
    private final static int NULL_PTR = 0;
    private final Memory memory;
    private final HashMap<String, Variable> variables;
    private final int[] heap;

    public NPJInterpreter(Memory memory, int[] heap) {
        this.memory = memory;
        this.heap = heap;
        variables = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void exitVarDeclT(NPJParser.VarDeclTContext context) {
        final int index = memory.allocateT(heap, (Map) variables);
        final String name = context.STRING().getText();
        final Variable variable = new Variable(Type.T, index);
        variables.put(name, variable);
    }

    @Override
    public void exitVarDeclSConst(NPJParser.VarDeclSConstContext context) {
        final String name = context.STRING(0).getText();
        final String value = context.STRING(1).getText();
        allocateString(name, value);
    }

    @Override
    public void exitVarDeclSNull(NPJParser.VarDeclSNullContext context) {
        final String name = context.STRING().getText();
        final Variable variable = new Variable(Type.S, NULL_PTR);
        variables.put(name, variable);
    }

    @Override
    public void exitPrintStringMessage(NPJParser.PrintStringMessageContext context) {
        NPJ.print(context.STRING().getText());
    }

    @Override
    public void exitPrintQuoted(NPJParser.PrintQuotedContext context) {
        String original = context.QUOTED().getText();
        NPJ.print(original.substring(1, original.length() - 1));
    }

    @Override
    public void exitPrintStringConst(NPJParser.PrintStringConstContext context) {
        final Variable variable = variables.get(context.STRING().getText());
        if (variable != null && variable.getType() != Type.S) {
            throw new RuntimeException("Could not print non-string value.");
        }

        if (variable != null) {
            NPJ.print(readString(variable.getIndex()));
        } else {
            NPJ.print("NULL");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void exitCollect(NPJParser.CollectContext context) {
        NPJ.collect(heap, memory, (Map) variables);
    }

    @Override
    public void exitAssignment(NPJParser.AssignmentContext context) {
        final String left = context.lValue().getText();
        final String right = context.rValue().getText();
        final int leftIndex = findIndex(left);

        if ("NULL".equals(right)) {
            heap[leftIndex] = NULL_PTR;
            return;
        } else if (right.startsWith("\"")) {
            allocateString(left, right.substring(1, right.length() - 1));
            return;
        }
        try {
            heap[leftIndex] = Integer.parseInt(right);
        } catch (NumberFormatException e) {
            heap[leftIndex] = findIndex(right);
        }
    }

    @Override
    public void exitHeapAnalyze(NPJParser.HeapAnalyzeContext context) {
        final List<Integer> tVariables = new ArrayList<>();
        final List<String> sVariables = new ArrayList<>();
        final int toSpaceIndex = memory.getToSpaceIndex();
        int index = toSpaceIndex;

        while (index < toSpaceIndex + (heap.length / 2)) {

            int code = heap[index];
            Type type = code < 0 ? Type.forCode(code) : null;
            if (type != null) {
                switch (type) {
                    case S:
                        sVariables.add(readString(index));
                        index += Type.S.baseSize + heap[index + STRING_LENGTH_OFFSET] - 1;
                        break;
                    case T:
                        index += 3;
                        tVariables.add(heap[index]);
                }
            }
            index++;
        }
        NPJ.heapAnalyze(tVariables, sVariables);
    }

    @SuppressWarnings("unchecked")
    private void allocateString(String name, String value) {
        final int index = memory.allocateS(heap, (Map) variables, value.length());

        for (int i = 0; i < value.length(); i++) {
            heap[index + Type.S.baseSize + i] = value.charAt(i);
        }
        variables.put(name, new Variable(Type.S, index));
    }

    private String readString(int idx) {
        if (heap[idx] == NULL_PTR) {
            return "NULL";
        }

        final int length = heap[idx + STRING_LENGTH_OFFSET];
        final StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++)
            builder.append((char) heap[idx + Type.S.baseSize + i]);

        return builder.toString();
    }

    private int findIndex(String value) {
        List<String> parts = Arrays.asList(value.split("\\."));
        Iterator<String> iterator = parts.iterator();
        int index = variables.get(iterator.next()).getIndex();

        while (iterator.hasNext()) {
            switch (iterator.next()) {
                case "f1":
                    index += 1;
                    break;
                case "f2":
                    index += 2;
                    break;
                case "data":
                    index += 3;
                    break;
                default:
                    throw new RuntimeException("Invalid reference");
            }
            if (iterator.hasNext()) {
                index = heap[index];
            }
        }
        return index;
    }
}
