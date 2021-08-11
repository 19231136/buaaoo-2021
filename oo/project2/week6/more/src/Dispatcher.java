import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.oocourse.elevator2.PersonRequest;

public class Dispatcher {
    private ArrayList<PersonRequest> request = new ArrayList();
    private boolean end = false;
    private int strategy = 0;//1 Random 2 Morning 3 Night
    private boolean nightStart = false;

    public void add(PersonRequest personRequest) {
        synchronized (this) {
            request.add(personRequest);
            if (strategy == 3) {
                sort();
            }
            notifyAll();
        }
    }

    public ArrayList<PersonRequest> get() {
        synchronized (this) {
            return request;
        }
    }

    public synchronized ArrayList<PersonRequest> read() {
        return request;
    }

    public void setStrategy(String pattern) {
        if (pattern.equals("Random")) {
            strategy = 1;
        } else if (pattern.equals("Morning")) {
            strategy = 2;
        } else {
            strategy = 3;
        }
    }

    public int getStrategy() {
        return strategy;
    }

    public void setEnd() {
        synchronized (this) {
            this.end = true;
            this.notifyAll();
        }
    }

    public synchronized boolean getEnd() {
        return end;
    }

    public void sort() {
        Comparator<PersonRequest> comparator = new Comparator<PersonRequest>() {
            @Override
            public int compare(PersonRequest o1, PersonRequest o2) {
                if (o1.getFromFloor() == o2.getFromFloor()) {
                    return 0;
                } else if (o1.getFromFloor() > o2.getFromFloor()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        Collections.sort(request,comparator);
    }
}
