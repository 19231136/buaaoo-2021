import java.util.ArrayList;

import com.oocourse.elevator2.PersonRequest;

public class Dispatcher {
    private ArrayList<PersonRequest> request = new ArrayList();
    private boolean end = false;
    private int strategy = 0;//1 Random 2 Morning 3 Night

    public void add(PersonRequest personRequest) {
        synchronized (this) {
            request.add(personRequest);
            notifyAll();
        }
    }

    public ArrayList<PersonRequest> get() {
        synchronized (this) {
            while (request.size() == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return request;
        }
    }

    public ArrayList<PersonRequest> read() {
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
            notifyAll();
        }
    }

    public boolean getEnd() {
        return end;
    }
}
