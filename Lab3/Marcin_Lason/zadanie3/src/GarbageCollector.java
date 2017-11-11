import machine.Heap;
import machine.TypeInfo;

import java.util.Map;

import static machine.TypeInfo.*;

public class GarbageCollector implements Collector {
    private final Heap heap;
    private final Map<String, Integer> variables;

    public GarbageCollector(Heap heap, Map<String, Integer> variables) {
        this.heap = heap;
        this.variables = variables;
    }

    @Override
    public void collect(int[] h, Map<Object, Object> params) {
        heap.swapHalves();
        for (Map.Entry<String, Integer> variable : variables.entrySet()) {
            int varAddress = variable.getValue();

            int newAddress = moveToSecondHalf(varAddress);
            variable.setValue(newAddress);
        }

        int scannedIndex = heap.getStartIndex();
        int endIndex = heap.getEndIndex();
        while (scannedIndex < endIndex) {
            int[] header = heap.get(scannedIndex, 1);
            if (header[0] == 0) {
                break;
            }
            if (isOfTypeT(header)) {
                int[] refs = heap.get(scannedIndex + 1, 2);
                for (int i = 0; i < refs.length; i++) {
                    refs[i] = moveToSecondHalf(refs[i]);
                }
                heap.set(scannedIndex + 1, refs);
                scannedIndex += 4;
            } else {
                int stringLength = heap.get(scannedIndex + 1, 1)[0];
                scannedIndex += stringLength + 2;
            }
        }
    }

    private int moveToSecondHalf(int varAddress) {
        if (varAddress == 0) {
            return 0;
        }

        int[] header = heap.get(varAddress, 2);

        if (isForwarding(header)) {
            return TypeInfo.extractDestination(header);
        }

        int newAddress;
        int[] oldData;

        if (isOfTypeS(header)) {
            oldData = heap.get(varAddress, 2 + header[1]);
            newAddress = heap.allocate(2 + header[1]);
        } else {
            oldData = heap.get(varAddress, 4);
            newAddress = heap.allocate(4);
        }

        heap.set(newAddress, oldData);
        if (newAddress + oldData.length < heap.getLimit()) {
            heap.set(newAddress + oldData.length, new int[]{0}); // finding a 0 after the last item on the heap is the requirement for halting sweep
        }

        heap.set(varAddress, new int[]{newAddress | TypeInfo.FORWARDING_FLAG});

        return newAddress;
    }
}
