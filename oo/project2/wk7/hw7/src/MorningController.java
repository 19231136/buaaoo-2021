import java.util.ArrayList;

public class MorningController extends Controller {
    private MorningDispatcher morningDispatcher;
    private ArrayList<Person> people = new ArrayList<>();

    public MorningController(MorningDispatcher morningDispatcher) {
        this.morningDispatcher = morningDispatcher;
    }

    public synchronized ArrayList<Person> getPeople() {
        return people;
    }

    public synchronized void remove(Person person) {
        people.remove(person);
        morningDispatcher.notifyAll();
    }

    public void allocate(Person person) {
        synchronized (people) {
            people.add(person);
        }
        String first = person.getFirst();
        if (first.equals("C")) {
            morningDispatcher.addC(person);
            //System.out.println(person.getId() + "C");
        } else {
            morningDispatcher.addAB(person);
            //System.out.println(person.getId() + "AB");
        }
        System.out.println();
    }

}
