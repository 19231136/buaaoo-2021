public class MainClass {
    public static void main(String[] argc) {
        Dispatcher dispatcher = new Dispatcher();
        Producer producer = new Producer(dispatcher);
        Consumer consumer = new Consumer(dispatcher);
        producer.start();
        consumer.start();
    }
}
