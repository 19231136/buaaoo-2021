import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

import java.util.HashMap;
import java.util.Set;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private int id;
    private static HashMap<Integer,Integer> hashMap = new HashMap<>();
    private static int count;

    public MyPersonIdNotFoundException(int id) {
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
        System.out.println("pinf-" + count + ", " + id + "-" + hashMap.get(id));
    }
}
