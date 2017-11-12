import java.util.Map;

public class SemiSpaceCopyingMemory implements Collector, Memory {

    private final static int STRING_LENGTH_OFFSET = 1;
    private final static int NULL_PTR = 0;

    private final int halfOfHeap;
    private int fromSpaceIndex;
    private int toSpaceIndex = 1;
    private int allocationIndex;

    public SemiSpaceCopyingMemory(int heapSize) {
        halfOfHeap = heapSize / 2;
        fromSpaceIndex = halfOfHeap + 1;
        allocationIndex = toSpaceIndex;
    }

    @Override
    public int getToSpaceIndex() {
        return toSpaceIndex;
    }

    @Override
    public int allocateT(int[] heap, Map<Object, Object> objects) {
        int index = allocate(heap, objects, Type.T.baseSize);
        heap[index] = Type.T.code;
        heap[index + 1] = heap[index + 2] = NULL_PTR;
        heap[index + 3] = 0;
        return index;
    }

    @Override
    public int allocateS(int[] heap, Map<Object, Object> objects, int stringLength) {
        int index = allocate(heap, objects, Type.S.baseSize + stringLength);
        heap[index] = Type.S.code;
        heap[index + STRING_LENGTH_OFFSET] = stringLength;
        return index;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void collect(int[] heap, Map<Object, Object> params) {
        int tmp = fromSpaceIndex;
        fromSpaceIndex = toSpaceIndex;
        toSpaceIndex = tmp;
        allocationIndex = toSpaceIndex;

        for (Variable info : ((Map<String, Variable>) (Map) params).values()) {
            info.setIndex(copy(heap, info.getIndex()));
        }

        int zeroEndIdx = fromSpaceIndex + halfOfHeap;
        for (int i = fromSpaceIndex; i < zeroEndIdx; i++) {
            heap[i] = 0;
        }
    }

    private int allocate(int[] heap, Map<Object, Object> objects, int size) {
        if (!isAllocatable(size)) {
            collect(heap, objects);
        }
        if (isAllocatable(size)) {
            throw new Error("Interpreter ran out of memory.");
        }

        int index = allocationIndex;
        allocationIndex += size;
        return index;
    }

    private int copy(int[] heap, int index) {
        if (heap[index] < 0) {
            int newIndex = allocationIndex;
            int variableSize = getSize(heap, index);
            allocationIndex += variableSize;
            System.arraycopy(heap, index, heap, newIndex, variableSize);
            heap[index] = newIndex;

            if (Type.forCode(heap[newIndex]) == Type.T) {
                int f1 = heap[index + 1];
                int f2 = heap[index + 2];
                heap[newIndex + 1] = copy(heap, f1);
                heap[newIndex + 2] = copy(heap, f2);
            }
            return newIndex;
        }
        return heap[index];
    }

    private int getSize(int[] heap, int index) {
        Type type = Type.forCode(heap[index]);

        if (type == Type.T) {
            return type.baseSize;
        }
        if (type == Type.S) {
            return type.baseSize + heap[index + STRING_LENGTH_OFFSET];
        }

        throw new RuntimeException("Undefined variable size.");
    }

    private boolean isAllocatable(int size) {
        if (allocationIndex + size > toSpaceIndex + halfOfHeap) {
            return false;
        }
        return true;
    }
}
