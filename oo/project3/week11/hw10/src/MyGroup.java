import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashSet;
import java.util.Set;

public class MyGroup implements Group {
    private int id;
    private Set<Person> people = new HashSet<>();
    private int valueSum;
    private int ageSum;
    private int ageSqSum;

    public MyGroup(int id) {
        this.id = id;
        this.valueSum = 0;
        this.ageSum = 0;
        this.ageSqSum = 0;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return (((Group) obj).getId() == id);
        }
        return false;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addPerson(Person person) {
        people.add(person);
        ageSum += person.getAge();
        ageSqSum += person.getAge() * person.getAge();
        for (Person person1 : people) {
            if (!person1.equals(person) && person1.isLinked(person)) {
                valueSum += 2 * person.queryValue(person1);
            }
        }
    }

    public void cacheLink(Person person1, Person person2) {
        valueSum += 2 * person1.queryValue(person2);
    }

    @Override
    public boolean hasPerson(Person person) {
        if (people.contains(person)) {
            return true;
        }
        return false;
    }

    @Override
    public int getValueSum() {
        return valueSum;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int mean = getAgeMean();
        int size = getSize();
        return (ageSqSum + size * mean * mean - 2 * mean * ageSum) / size;
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person);
        ageSum -= person.getAge();
        ageSqSum -= person.getAge() * person.getAge();
        for (Person person1 : people) {
            if (person.isLinked(person1)) {
                valueSum -= 2 * person.queryValue(person1);
            }
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public Set<Person> getPeople() {
        return people;
    }
}
