public class Consumer extends Thread{
    private Dispatcher dispatcher;

    Consumer(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void run() {
        int n = 1;
        while (n <= 10) {
            synchronized (dispatcher) {
                if (dispatcher.isHas()) {
                    System.out.println("Consumer get:" + dispatcher.get());
                    n++;
                } else {
                    try {
                        dispatcher.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
