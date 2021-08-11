import com.oocourse.spec2.exceptions.EqualMessageIdException;

import java.util.HashMap;
import java.util.Set;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static HashMap<Integer,Integer> hashMap = new HashMap<>();
    private int id;
    private static int count;

    public MyEqualMessageIdException(int id) {
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
        System.out.println("emi-" + count + ", " + id + "-" + hashMap.get(id));
    }
}
