import java.util.ArrayList;

public class Server implements Observerable {
    private ArrayList<Observer> users = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        users.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        users.remove(observer);
    }

    @Override
    public void notifyObserver(String msg) {
        System.out.println(msg);
        for (Observer observer : users) {
            observer.update(msg);
        }
    }
}
