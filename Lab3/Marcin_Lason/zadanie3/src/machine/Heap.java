package machine;

import org.apache.bcel.generic.RETURN;

import javax.naming.SizeLimitExceededException;

public class Heap {
	private final int halfHeapSize;
	private final int[] heap;
	private final int size;

	private int usedHalfOffset = 1;
	private int nextAllocationOffsetWithinHalf = 0;

	public Heap(int size) {
		this.size = size;
		this.halfHeapSize = size / 2;
		this.heap = new int[size];
	}

	public int[] getHeap() {
		return heap;
	}

	public int allocate(int nBytes) {
		if (notEnoughSpace(nBytes)) {
			return -1;
		}

		int address = usedHalfOffset + nextAllocationOffsetWithinHalf;
		nextAllocationOffsetWithinHalf += nBytes;
		return address;
	}

	private boolean notEnoughSpace(int nBytes) {
		return nextAllocationOffsetWithinHalf + nBytes > halfHeapSize;
	}

	public void set(int addr, int[] value) {
		System.arraycopy(value, 0, heap, addr, value.length);
	}

	public int[] get(int addr, int length) {
		int[] value = new int[length];
		System.arraycopy(heap, addr, value, 0, length);
		return value;
	}

	public void swapHalves() {
		if(usedHalfOffset == 1) {
			usedHalfOffset = 1 + halfHeapSize;
		} else {
			usedHalfOffset = 1;
		}
		nextAllocationOffsetWithinHalf = 0;
	}

	public int getStartIndex() {
		return usedHalfOffset;
	}

	public int getEndIndex() {
		return usedHalfOffset + halfHeapSize;
	}

	public int getLimit() {
		return size;
	}
}
