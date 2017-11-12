import npj.generated.NPJBaseListener;
import npj.generated.NPJParser;

import java.util.*;

public class NPJInterpreter extends NPJBaseListener {

    private final static int STRING_LENGTH_OFFSET = 1;
    private final static int NULL_PTR = 0;
    private final Memory memory;
    private final int[] heap;
    private final HashMap<String, Variable> nameToVariableInfo;

    public NPJInterpreter(Memory memory, int[] heap) {
        this.memory = memory;
        this.heap = heap;
        nameToVariableInfo = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void exitVarDeclT(NPJParser.VarDeclTContext ctx) {
        final int idx = memory.allocateT(heap, (Map) nameToVariableInfo);
        final String name = ctx.STRING().getText();
        final Variable info = new Variable(Type.T, idx);
        nameToVariableInfo.put(name, info);
    }

    @Override
    public void exitVarDeclSConst(NPJParser.VarDeclSConstContext ctx) {
        final String name = ctx.STRING(0).getText();
        final String value = ctx.STRING(1).getText();
        allocateString(name, value);
    }

    @Override
    public void exitVarDeclSNull(NPJParser.VarDeclSNullContext ctx) {
        final String name = ctx.STRING().getText();
        final Variable date = new Variable(Type.S, NULL_PTR);
        nameToVariableInfo.put(name, date);
    }

    @Override
    public void exitPrintStringMessage(NPJParser.PrintStringMessageContext ctx) {
        NPJ.print(ctx.STRING().getText());
    }

    @Override
    public void exitPrintQuoted(NPJParser.PrintQuotedContext ctx) {
        String original = ctx.QUOTED().getText();
        NPJ.print(original.substring(1, original.length() - 1));
    }

    @Override
    public void exitPrintStringConst(NPJParser.PrintStringConstContext ctx) {
        final Variable info = nameToVariableInfo.get(ctx.STRING().getText());
        if (info.getType() != Type.S) {
            throw new RuntimeException("Could not print non-string value.");
        }

        NPJ.print(readString(info.getIndex()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void exitCollect(NPJParser.CollectContext ctx) {
        NPJ.collect(heap, memory, (Map) nameToVariableInfo);
    }

    @Override
    public void exitAssignment(NPJParser.AssignmentContext ctx) {
        final String lValue = ctx.lValue().getText();
        final String rValue = ctx.rValue().getText();
        final int leftIdx = findIdx(lValue);
        if ("NULL".equals(rValue)) {
            heap[leftIdx] = NULL_PTR;
            return;
        } else if (rValue.startsWith("\"")) {
            allocateString(lValue, rValue.substring(1, rValue.length() - 1));
            return;
        }
        try {
            heap[leftIdx] = Integer.parseInt(rValue);
        } catch (NumberFormatException nfe) {
            heap[leftIdx] = findIdx(rValue);
        }
    }

    @Override
    public void exitHeapAnalyze(NPJParser.HeapAnalyzeContext ctx) {
        final ArrayList<Integer> typeTVariables = new ArrayList<>();
        final ArrayList<String> typeSVariables = new ArrayList<>();
        final int toSpaceIdx = memory.getToSpaceIndex();
        int idx = toSpaceIdx;
        while (idx < toSpaceIdx + (heap.length / 2)) {
            int code = heap[idx];
            Type type = code < 0 ? Type.forCode(code) : null;
            if (type != null) {
                switch (type) {
                    case S:
                        typeSVariables.add(readString(idx));
                        idx += Type.S.baseSize + heap[idx + STRING_LENGTH_OFFSET] - 1;
                        break;
                    case T:
                        idx += 3;
                        typeTVariables.add(heap[idx]);
                }
            }
            idx++;
        }
        NPJ.heapAnalyze(typeTVariables, typeSVariables);
    }

    @SuppressWarnings("unchecked")
    private void allocateString(String name, String value) {
        final int idx = memory.allocateS(heap, (Map) nameToVariableInfo, value.length());
        for (int i = 0; i < value.length(); i++) {
            heap[idx + Type.S.baseSize + i] = value.charAt(i);
        }
        nameToVariableInfo.put(name, new Variable(Type.S, idx));
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

    private int findIdx(String value) {
        List<String> parts = Arrays.asList(value.split("\\."));
        Iterator<String> it = parts.iterator();
        int idx = nameToVariableInfo.get(it.next()).getIndex();
        while (it.hasNext()) {
            switch (it.next()) {
                case "f1":
                    idx += 1;
                    break;
                case "f2":
                    idx += 2;
                    break;
                case "data":
                    idx += 3;
                    break;
                default:
                    throw new RuntimeException("Invalid reference");
            }
            if (it.hasNext()) {
                idx = heap[idx];
            }
        }
        return idx;
    }
}
