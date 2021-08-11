import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

import java.util.HashMap;
import java.util.Set;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private int id;
    private static HashMap<Integer,Integer> hashMap = new HashMap<>();
    private static int count;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        count++;
        Set<Integer> keyset = hashMap.keySet();
        for (Integer i : keyset) {
            if (i == id) {
                hashMap.put(id,hashMap.get(id) + 1);
                return;
            }
        }
        hashMap.put(id,1);
    }

    public void print() {
        System.out.println("minf-" + count + ", " + id + "-" + hashMap.get(id));
    }
}
