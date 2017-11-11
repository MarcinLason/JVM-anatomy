package npj.ast.data;

public class Dereference implements RValue {
	public final String path;

	private Dereference(String path) {
		this.path = path;
	}

	public static Dereference of(String name) {
		return new Dereference(name);
	}
}
