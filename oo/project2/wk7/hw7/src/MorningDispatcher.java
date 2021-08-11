import java.util.ArrayList;

public class MorningDispatcher extends Dispatcher {
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
            notifyAll();
        }
    }

    public void addC(Person person) {
        synchronized (this) {
            requestC.add(person);
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
