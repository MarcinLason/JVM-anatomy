import java.util.Map;

public class SemiSpaceCopyingMemory implements Collector, Memory {

    private final static int STRING_LENGTH_OFFSET = 1;
    private final static int NULL_PTR = 0;
    private final int halfSize;
    private int toSpaceIdx = 1;
    private int fromSpaceIdx;
    private int allocationIdx;

    public SemiSpaceCopyingMemory(int heapSize) {
        halfSize = heapSize / 2;
        fromSpaceIdx = halfSize + 1;
        allocationIdx = toSpaceIdx;
    }

    @Override
    public int getToSpaceIndex() {
        return toSpaceIdx;
    }

    @Override
    public int allocateT(int[] heap, Map<Object, Object> objects) {
        int idx = allocate(heap, objects, Type.T.baseSize);
        heap[idx] = Type.T.code;
        heap[idx + 1] = heap[idx + 2] = NULL_PTR;
        heap[idx + 3] = 0;
        return idx;
    }

    @Override
    public int allocateS(int[] heap, Map<Object, Object> objects, int stringLength) {
        int idx = allocate(heap, objects, Type.S.baseSize + stringLength);
        heap[idx] = Type.S.code;
        heap[idx + STRING_LENGTH_OFFSET] = stringLength;
        return idx;
    }

    private boolean cannotAllocate(int size) {
        return allocationIdx + size > toSpaceIdx + halfSize;
    }

    private int allocate(int[] heap, Map<Object, Object> objects, int size) {
        if (cannotAllocate(size)) collect(heap, objects);
        if (cannotAllocate(size)) throw new Error("Interpreter ran out of memory.");
        int idx = allocationIdx;
        allocationIdx += size;
        return idx;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void collect(int[] heap, Map<Object, Object> params) {
        int temp = fromSpaceIdx;
        fromSpaceIdx = toSpaceIdx;
        toSpaceIdx = temp;

        allocationIdx = toSpaceIdx;

        for (VariableInfo info : ((Map<String, VariableInfo>) (Map) params).values())
            info.index = copy(heap, info.index);

        int zeroEndIdx = fromSpaceIdx + halfSize;

        for (int i = fromSpaceIdx; i < zeroEndIdx; i++)
            heap[i] = 0;

    }

    private int copy(int[] heap, int idx) {
        if (heap[idx] < 0) {
            int newIdx = allocationIdx;
            int variableSize = getSize(heap, idx);
            allocationIdx += variableSize;
            System.arraycopy(heap, idx, heap, newIdx, variableSize);
            heap[idx] = newIdx;
            if (Type.forCode(heap[newIdx]) == Type.T) {
                int f1 = heap[idx + 1];
                int f2 = heap[idx + 2];
                heap[newIdx + 1] = copy(heap, f1);
                heap[newIdx + 2] = copy(heap, f2);
            }
            return newIdx;
        } else return heap[idx];
    }

    private int getSize(int[] heap, int idx) {
        Type type = Type.forCode(heap[idx]);
        switch (type) {
            case T:
                return type.baseSize;
            case S:
                return type.baseSize + heap[idx + STRING_LENGTH_OFFSET];
        }
        throw new RuntimeException("Undefined variable size.");
    }
}
