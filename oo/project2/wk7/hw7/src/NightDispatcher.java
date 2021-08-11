import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NightDispatcher extends Dispatcher {
    private boolean end = false;
    private ArrayList<Person> requestAB = new ArrayList();
    private ArrayList<Person> requestC = new ArrayList();
    private static final int[] floorB = {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0};
    private static final int[] floorC = {1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1};

    public void setEnd() {
        synchronized (this) {
            this.end = true;
            this.notifyAll();
        }
    }

    public void addAB(Person person) {
        synchronized (this) {
            requestAB.add(person);
            sort();
            notifyAll();
        }
    }

    public void addC(Person person) {
        synchronized (this) {
            requestC.add(person);
            sort();
            notifyAll();
        }
    }

    public ArrayList<Person> get(String type) {
        synchronized (this) {
            if (type.equals("C")) {
                return requestC;
            } else {
                return requestAB;
            }
        }
    }

    public void sort() {
        Comparator<Person> comparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getFrom() == o2.getFrom()) {
                    return 0;
                } else if (o1.getFrom() > o2.getFrom()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        Collections.sort(requestAB,comparator);
        Collections.sort(requestC,comparator);
    }

    public synchronized boolean getEnd() {
        return end;
    }

    public synchronized boolean isEmpty(String type) {
        if (type.equals("B")) {
            for (int i = 0;i < requestAB.size();i++) {
                Person person = requestAB.get(i);
                int from = person.getFrom();
                int to = person.getTempDes();
                if (floorB[from - 1] == 1 && floorB[to - 1] == 1) {
                    return false;
                }
            }
            return true;
        } else {
            return get(type).size() == 0;
        }
    }
}
