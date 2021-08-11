import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.*;

public class MyNetwork implements Network {
    private HashMap<Integer,MyPerson> peopleMap = new HashMap<>();
    //public ArrayList<MyPerson> people = new ArrayList<>();
    private HashMap<Integer,Integer> father = new HashMap<>();

    public MyNetwork() {

    }

    @Override
    public boolean contains(int id) {
        for (Integer i : peopleMap.keySet()) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            for (Integer i : peopleMap.keySet()) {
                if (i == id) {
                    return peopleMap.get(i);
                }
            }
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (!contains(person.getId())) {
            peopleMap.put(person.getId(),(MyPerson) person);
            father.put(person.getId(),person.getId());
            //people.add((MyPerson) person);
            return;
        }
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            ((MyPerson)getPerson(id1)).addAcquaintance(getPerson(id2),value);
            ((MyPerson)getPerson(id2)).addAcquaintance(getPerson(id1),value);
            addFather(id1,id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1,id2);
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
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
        return peopleMap.keySet().size();
    }

    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            int cnt = 0;
            for (Integer i : peopleMap.keySet()) {
                if (compareName(id,i) > 0) {
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
            return findFather(id1) == findFather(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            throw new MyPersonIdNotFoundException(id2);
        }
    }

    @Override
    public int queryBlockSum() {
        Set<Integer> set = new HashSet<>();
        int cnt = 0;
        for (Integer i : peopleMap.keySet()) {
            int father = findFather(i);
            if (!set.contains(father)) {
                cnt++;
                set.add(father);
            }
        }
        return cnt;
    }

    public int findFather(int id) {
        if (id == father.get(id)) {
            return id;
        }
        int root = findFather(father.get(id));
        father.put(id,root);
        return root;
    }

    public void addFather(int id1,int id2) {
        if (findFather(id1) == findFather(id2)) {
            return;
        }
        father.put(findFather(id1),findFather(id2));
    }
}
