import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.*;

public class MyNetwork implements Network {
    public ArrayList<MyPerson> people = new ArrayList<>();

    public MyNetwork() {

    }

    @Override
    public boolean contains(int id) {
        for (int i = 0;i < people.size();i++) {
            if (people.get(i).getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            for (int i = 0;i < people.size();i++) {
                if (people.get(i).getId() == id) {
                    return people.get(i);
                }
            }
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (!contains(person.getId())) {
            people.add((MyPerson) person);
            return;
        }
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            ((MyPerson)getPerson(id1)).addAcquaintance(getPerson(id2),value);
            ((MyPerson)getPerson(id2)).addAcquaintance(getPerson(id1),value);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1,id2);
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1,id2);
        }
        return 0;
    }

    @Override
    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        if (contains(id1) && contains(id2)) {
            return getPerson(id1).compareTo(getPerson(id2));
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return 0;
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            int cnt = 0;
            for (int i = 0;i < people.size();i++) {
                if (compareName(id,people.get(i).getId()) > 0) {
                    cnt++;
                }
            }
            return cnt + 1;
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (contains(id1) && contains(id2)) {
            Set<Integer> set = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();
            queue.add(id1);
            set.add(id1);
            while (!queue.isEmpty()) {
                int temp = queue.poll();
                if (temp == id2) {
                    return true;
                }
                Person person = getPerson(temp);
                ArrayList<Person> arrayList = ((MyPerson) person).getAcquaintance();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!set.contains(arrayList.get(i).getId())) {
                        set.add(arrayList.get(i).getId());
                        queue.offer(arrayList.get(i).getId());
                    }
                }
            }
            return false;
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            throw new MyPersonIdNotFoundException(id2);
        }
    }

    @Override
    public int queryBlockSum() {
        int cnt = 0;
        for (int i = 0;i < people.size();i++) {
            int j;
            for (j = 0;j < i;j++) {
                try {
                    if (isCircle(people.get(i).getId(),people.get(j).getId())) {
                        break;
                    }
                } catch (PersonIdNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (j == i) {
                cnt++;
            }
        }
        return cnt;
    }
}
