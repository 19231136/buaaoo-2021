import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

import java.util.HashMap;
import java.util.Set;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private int id;
    private static HashMap<Integer,Integer> hashMap = new HashMap<>();
    private static int count;

    public MyGroupIdNotFoundException(int id) {
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
        System.out.println("ginf-" + count + ", " + id + "-" + hashMap.get(id));
    }
}
