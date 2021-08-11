import java.util.ArrayList;
import java.util.List;

public class Bus implements Cloneable {
    private List<Person> personList;

    public Bus() {
        personList = new ArrayList<>();
    }

    public void addPerson(Person person) {
        personList.add(person);
    }

    public void removePerson(Person person) {
        personList.remove(person);
    }

    public List<Person> getPersonList() {
        return (List<Person>) clone();
    }

    public Object clone() {
        try {
            Bus bus = (Bus) super.clone();
            bus.personList = new ArrayList<>();
            for (int i = 0;i < personList.size();i++) {
                Person person = personList.get(i).clone();
                bus.personList.add(person);
            }
            return bus.personList;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

}
