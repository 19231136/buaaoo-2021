public class Producer extends Thread{
    private int num;
    private Dispatcher dispatcher;

    Producer(Dispatcher dispatcher) {
        num = 1;
        this.dispatcher = dispatcher;
    }

    public void run() {
        while (num <= 10) {
            synchronized (dispatcher) {
                if (!dispatcher.isHas()) {
                    dispatcher.add(num);
                    System.out.println("Producer put:" + num);
                    num++;
                    try {
                        sleep((int)(Math.random() * 100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
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
