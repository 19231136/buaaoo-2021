public class Dispatcher {
    private boolean has;
    private int num;

    Dispatcher() {
        this.has = false;
    }

    public boolean isHas() {
        return has;
    }

    public void add(int num) {
        synchronized (this) {
            this.num = num;
            this.has = true;
            notifyAll();
        }
    }

    public int get() {
        synchronized (this) {
            this.has = false;
            notifyAll();
            return num;
        }
    }
}
