import com.oocourse.spec1.exceptions.EqualRelationException;

import java.util.HashMap;
import java.util.Set;

public class MyEqualRelationException extends EqualRelationException {
    private int idmax;
    private int idmin;
    private static int count;
    private static HashMap<Integer,Integer> hashMap = new HashMap<>();

    public MyEqualRelationException(int id1,int id2) {
        int max = 0;
        int min = 0;
        count++;
        if (id1 >= id2) {
            idmax = id1;
            idmin = id2;
        } else {
            idmax = id2;
            idmin = id1;
        }
        Set<Integer> keyset = hashMap.keySet();
        for (Integer id : keyset) {
            if (id == idmax) {
                max = 1;
                hashMap.put(id,hashMap.get(id) + 1);
            }
        }
        if (max == 0) {
            hashMap.put(idmax,1);
        }
        for (Integer id : keyset) {
            if (id == idmin) {
                min = 1;
                hashMap.put(id,hashMap.get(id) + 1);
            }
        }
        if (min == 0) {
            hashMap.put(idmin,1);
        }
        if (idmin == idmax) {
            hashMap.put(idmax,hashMap.get(idmax) - 1);
        }
    }

    public void print() {
        System.out.println("er-" + count + ", " + idmin + "-" +
                hashMap.get(idmin) + ", " + idmax + "-" + hashMap.get(idmax));
    }
}

