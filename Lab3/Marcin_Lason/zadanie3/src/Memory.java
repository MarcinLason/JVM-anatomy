import java.util.Map;

public interface Memory extends Collector {

    int allocateT(int[] heap, Map<Object, Object> objects);

    int allocateS(int[] heap, Map<Object, Object> objects, int stringLength);

    int getToSpaceIndex();
}
