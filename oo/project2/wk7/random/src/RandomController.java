import java.util.ArrayList;

public class RandomController extends Controller {
    private RandomDispatcher randomDispatcher;
    private ArrayList<Person> people = new ArrayList<>();

    public RandomController(RandomDispatcher randomDispatcher) {
        this.randomDispatcher = randomDispatcher;
    }

    public synchronized ArrayList<Person> getPeople() {
        return people;
    }

    public void allocate(Person person) {
        synchronized (people) {
            people.add(person);
        }
        String first = person.getFirst();
        if (first.equals("C")) {
            randomDispatcher.addC(person);
            //System.out.println(person.getId() + "C");
        } else {
            randomDispatcher.addAB(person);
            //System.out.println(person.getId() + "AB");
        }
    }

    public synchronized void remove(Person person) {
        people.remove(person);
        randomDispatcher.notifyAll();
    }
}
