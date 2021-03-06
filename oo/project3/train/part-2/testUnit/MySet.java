import java.util.ArrayList;
import java.util.HashSet;

public class MySet implements IntSet {
    private ArrayList<Integer> arr;
    private int count;

    public MySet() {
        arr = new ArrayList<>();
        count = 0;
    }

    public void insert(int x) {
        int L = 0;
        int R = count - 1;
        while (L <= R) {
            int mid = (L + R) / 2;
            if (arr.get(mid) < x) {
                L = mid + 1;
            } else if (arr.get(mid) > x) {
                R = mid - 1;
            } else {
                L = mid;
                break;
            }
        }
        if (L < 0 || arr.get(L) != x) {
            arr.add(L+1, x);
            count++;
        }
    }

    public void delete(int x) {
        int L = 0;
        int R = count - 1;
        while (L < R) {
            int mid = (L + R) / 2;
            if (arr.get(mid) < x) {
                L = mid + 1;
            } else if (arr.get(mid) > x) {
                R = mid - 1;
            } else {
                L = mid;
                break;
            }
        }
        if (arr.get(L) == x) {
            arr.remove(L);
            count--;
        }
    }

    public boolean isIn(int x) {
        return arr.contains(x);
    }

    public int size() {
        return count;
    }

    public void elementSwap(IntSet a) {
    }

    public IntSet symmetricDifference(IntSet a) throws NullPointerException {
        return null;
    }

    //@ ensures \result == (ia != null) && (\forall int i, j ; 0 <= i && i < j && j < ia.length; ia[i] != ia[j]);
    public boolean repOK() {
        //	TODO
        if (arr != null) {
            int j;
            for (j = 0;j < arr.size();j++) {
                for (int i = 0;i < j;i++) {
                    if (arr.get(i) == arr.get(j)) {
                        break;
                    }
                }
            }
            return j == arr.size();
        }
        return false;
    }
}
