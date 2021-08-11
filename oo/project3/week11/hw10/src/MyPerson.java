import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Person,Integer> acquaintance = new HashMap<>();
    private int socialValue;
    private LinkedList<Message> messages = new LinkedList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            return (((Person) obj).getId() == id);
        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }

        return acquaintance.containsKey(person);
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person)) {
            return acquaintance.get(person);
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getReceivedMessages() {
        LinkedList<Message> messages1 = new LinkedList<>();
        for (int i = 0;i < messages.size() && i <= 3;i++) {
            messages1.add(messages.get(i));
        }
        return messages1;
    }

    public void addAcquaintance(Person person,int value) {
        acquaintance.put(person,value);
    }

    public HashMap<Person, Integer> getAcquaintance() {
        return acquaintance;
    }

    public void addMessage(Message message) {
        messages.add(0,message);
    }
}
