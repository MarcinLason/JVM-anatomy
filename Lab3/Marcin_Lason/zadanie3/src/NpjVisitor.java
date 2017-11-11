import machine.Heap;
import machine.TypeInfo;
import npj.ast.data.*;
import npj.interpreter.InterpretingVisitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static machine.TypeInfo.isOfTypeS;

class NpjVisitor implements InterpretingVisitor {
	private static final Map<String, Integer> tComponentsToOffset = new HashMap<String, Integer>() {{
		put("f1", 1);
		put("f2", 2);
		put("data", 3);
	}};
	private final Heap heap;
	private final Map<String, Integer> variables;

	NpjVisitor(Heap heap) {
		this.heap = heap;
		this.variables = new HashMap<>();
	}

	@Override
	public void visitCollect() {
		NPJ.collect(heap.getHeap(), new GarbageCollector(heap, variables), null);
	}

	@Override
	public void visitAssignment(Dereference target, RValue value) {
		int address = resolveTargetAddress(target);
		int[] rvalue = toData(value);
		if(targetIsNamedVariable(target)) {
			variables.put(target.path, rvalue[0]);
		} else {
			heap.set(address, rvalue);
		}
	}

	private boolean targetIsNamedVariable(Dereference target) {
		return !target.path.contains(".");
	}

	private int[] toData(RValue value) {
		if (value instanceof Nil) {
			return new int[]{0};
		}

		if (value instanceof StringLiteral) {
			StringLiteral v = (StringLiteral) value;
			int stringLength = v.getLength();
			int[] data = new int[stringLength + 2];
			data[0] = TypeInfo.TYPE_S_HEADER;
			data[1] = stringLength;
			for (int i = 0; i < stringLength; i++) {
				data[2 + i] = v.value.charAt(i);
			}
			int allocatedAddress = allocate(2 + stringLength);
			heap.set(allocatedAddress, data);
			return new int[]{allocatedAddress};
		}

		if (value instanceof IntLiteral) {
			IntLiteral v = (IntLiteral) value;
			return new int[]{v.value};
		}

		if (value instanceof Dereference) {
			int address = resolveTargetAddress((Dereference) value);
			int type = resolveTargetType((Dereference) value);
			if (type == TypeInfo.INTEGER) {
				return heap.get(address, 1);
			} else {
				return new int[]{address};
			}
		}

		return new int[0];
	}

	private int resolveTargetAddress(Dereference target) {
		String[] segments = target.path.split("\\.");

		Integer address = variables.get(segments[0]);
		if (isOfTypeS(heap.get(address, 1))) {
			return address;
		}

		for (int i = 1; i < segments.length; i++) {
			int offset = tComponentsToOffset.get(segments[i]);
			address = address + offset;
		}

		return address;
	}

	private int resolveTargetType(Dereference target) {
		String[] segments = target.path.split("\\.");

		Integer address = variables.get(segments[0]);
		if (isOfTypeS(heap.get(address, 1))) {
			return TypeInfo.STRING;
		}

		String lastSegment = segments[segments.length - 1];
		if (lastSegment.equals("data")) {
			return TypeInfo.INTEGER;
		}

		return TypeInfo.REFERENCE;
	}

	@Override
	public void visitPrintDereferenced(String dereferenceToPrint) {
		Dereference target = Dereference.of(dereferenceToPrint);
		int type = resolveTargetType(target);
		int address = resolveTargetAddress(target);
		String toBePrinted;

		if (address != 0 && type == TypeInfo.STRING) {
			toBePrinted = readString(address);
		} else { //int or reference
			toBePrinted = "NULL";
		}

		NPJ.print(toBePrinted);
	}

	private String readString(int address) {
		String toBePrinted;
		int stringLength = heap.get(address + 1, 1)[0];
		StringBuilder builder = new StringBuilder(stringLength);
		int[] intizedString = heap.get(address + 2, stringLength);
		for (int i = 0; i < intizedString.length; i++) {
			builder.append(Character.toString((char) intizedString[i]));
		}
		toBePrinted = builder.toString();
		return toBePrinted;
	}

	@Override
	public void visitPrintLiteral(String literalToPrint) {
		NPJ.print(literalToPrint);
	}

	@Override
	public void visitSDeclaration(String name, String initialValue) {
		allocateS(name, initialValue);
	}

	@Override
	public void visitTDeclaration(String name) {
		allocateT(name);
	}

	@Override
	public void visitHeapAnalyze() {
		Set<String> typeSVariables = new HashSet<>();
		Set<Integer> typeTVariables = new HashSet<>();
		for (Integer address : variables.values()) {
			if (isOfTypeS(heap.get(address, 1))) {
				String s = readString(address);
				typeSVariables.add(s);
			} else {
				int value = heap.get(address + 3, 1)[0];
				typeTVariables.add(value);
			}
		}

		NPJ.heapAnalyze(typeTVariables, typeSVariables);
	}

	private void allocateT(String name) {
		int addr = allocate(4);

		int[] value = new int[]{TypeInfo.TYPE_T_HEADER, TypeInfo.NULL, TypeInfo.NULL, 0};
		heap.set(addr, value);

		variables.put(name, addr);
	}

	private void allocateS(String name, String initialValue) {
		int stringLength = initialValue.length();
		int addr = allocate(stringLength + 2);

		int[] value = new int[stringLength + 2];
		value[0] = TypeInfo.TYPE_S_HEADER;
		value[1] = stringLength;
		for (int i = 0; i < stringLength; i++) {
			value[2 + i] = initialValue.charAt(i);
		}

		heap.set(addr, value);
		variables.put(name, addr);
	}

	private int allocate(int nBytes) {
		int addr = heap.allocate(nBytes);
		if (addr < 0) {
			visitCollect();
			addr = heap.allocate(nBytes);
			if(addr < 0) {
				throw new OutOfMemoryError();
			}
		}
		return addr;
	}
}
