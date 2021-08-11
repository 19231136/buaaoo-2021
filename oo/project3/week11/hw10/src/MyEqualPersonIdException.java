import com.oocourse.spec2.exceptions.EqualPersonIdException;

import java.util.HashMap;
import java.util.Set;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static HashMap<Integer,Integer> hashMap = new HashMap<>();
    private int id;
    private static int count;

    public MyEqualPersonIdException(int id) {
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
        System.out.println("epi-" + count + ", " + id + "-" + hashMap.get(id));
    }
}
