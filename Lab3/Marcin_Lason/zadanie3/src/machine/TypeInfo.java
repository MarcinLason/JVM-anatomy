package machine;

public class TypeInfo {
	public static final int NULL = 0;
	public static final int INTEGER = 4;
	public static final int STRING = 5;
	public static final int REFERENCE = 6;


	public static final int FORWARDING_FLAG = 	0x80000000;
	public static final int VISITED_FLAG = 		0x40000000;
	public static final int TYPE_T_HEADER = 	0x20000000;
	public static final int TYPE_S_HEADER = 	0x10000000;

	private static final int FLAGS_MASK = 0xC0000000;
	private static final int TYPE_MASK = 0x30000000;
	private static final int ADDR_MASK = 0x0fffffff;

	public static boolean isOfTypeS(int[] allocatedBlob) {
		return (allocatedBlob[0] & TYPE_MASK) == TYPE_S_HEADER;
	}

	public static boolean isOfTypeT(int[] allocatedBlob) {
		return (allocatedBlob[0] & TYPE_MASK) == TYPE_T_HEADER;
	}

	public static boolean isForwarding(int[] allocatedBlob) {
		return (allocatedBlob[0] & FLAGS_MASK) == FORWARDING_FLAG;
	}

	public static boolean wasVisited(int[] allocatedBlob) {
		return (allocatedBlob[0] & FLAGS_MASK) == VISITED_FLAG;
	}

	public static int extractDestination(int[] ints) {
		return (ints[0] & ADDR_MASK);
	}
}