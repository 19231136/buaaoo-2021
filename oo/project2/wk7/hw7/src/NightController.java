import java.util.ArrayList;

public class NightController extends Controller {
    private NightDispatcher nightDispatcher;
    private ArrayList<Person> people = new ArrayList<>();

    public NightController(NightDispatcher nightDispatcher) {
        this.nightDispatcher = nightDispatcher;
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
            nightDispatcher.addC(person);
            //System.out.println(person.getId() + "C");
        } else {
            nightDispatcher.addAB(person);
            //System.out.println(person.getId() + "AB");
        }
    }

    public synchronized void remove(Person person) {
        people.remove(person);
        nightDispatcher.notifyAll();
    }
}
