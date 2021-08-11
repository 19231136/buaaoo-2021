import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class MyNetwork implements Network {
    private HashMap<Integer,MyPerson> peopleMap = new HashMap<>();
    private HashMap<Integer,Integer> father = new HashMap<>();
    private HashMap<Integer,MyGroup> groups = new HashMap<>();
    private HashMap<Integer,MyMessage> messages = new HashMap<>();
    private HashMap<Integer,Integer> emojis = new HashMap<>();//emojiid,heat

    public MyNetwork() {

    }

    @Override
    public boolean contains(int id) {
        return peopleMap.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            return peopleMap.get(id);
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (!contains(person.getId())) {
            peopleMap.put(person.getId(),(MyPerson) person);
            father.put(person.getId(),person.getId());
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
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            for (Integer i : groups.keySet()) {
                if (groups.get(i).hasPerson(person1) && groups.get(i).hasPerson(person2)) {
                    groups.get(i).cacheLink(person1,person2);
                }
            }
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
        return peopleMap.size();
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

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), (MyGroup) group);
    }

    @Override
    public Group getGroup(int id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        return null;
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!peopleMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        if (getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }

    @Override
    public int queryGroupSum() {
        return groups.size();
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getValueSum();
    }

    @Override
    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeMean();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!peopleMap.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        getGroup(id2).delPerson(getPerson(id1));
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException,
            EqualPersonIdException, EmojiIdNotFoundException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            MyEmojiMessage myEmojiMessage = (MyEmojiMessage) message;
            throw new MyEmojiIdNotFoundException(myEmojiMessage.getEmojiId());
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), (MyMessage) message);
    }

    @Override
    public Message getMessage(int id) {
        if (messages.containsKey(id)) {
            return messages.get(id);
        }
        return null;
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        MyMessage myMessage = messages.get(id);
        Person person1 = myMessage.getPerson1();
        if (myMessage.getType() == 0) {
            MyPerson person2 = (MyPerson) myMessage.getPerson2();
            if (!person1.isLinked(person2)) {
                throw new MyRelationNotFoundException(person1.getId(),person2.getId());
            }
            person1.addSocialValue(myMessage.getSocialValue());
            person2.addSocialValue(myMessage.getSocialValue());
            messages.remove(id);
            person2.addMessage(myMessage);
            if (myMessage instanceof RedEnvelopeMessage) {
                MyRedEnvelopeMessage message = (MyRedEnvelopeMessage) myMessage;
                person1.addMoney(-message.getMoney());
                person2.addMoney(message.getMoney());
            } else if (myMessage instanceof EmojiMessage) {
                MyEmojiMessage message = (MyEmojiMessage) myMessage;
                emojis.put(message.getEmojiId(),emojis.get(message.getEmojiId()) + 1);
            }
        }
        if (myMessage.getType() == 1) {
            if (!myMessage.getGroup().hasPerson(person1)) {
                throw new MyPersonIdNotFoundException(person1.getId());
            }
            MyGroup myGroup = (MyGroup) myMessage.getGroup();
            if (myMessage instanceof RedEnvelopeMessage) {
                int num = myGroup.getSize();
                int money = ((RedEnvelopeMessage) myMessage).getMoney() / num;
                person1.addMoney(-money * (num - 1));
                for (Person person : myGroup.getPeople()) {
                    person.addSocialValue(myMessage.getSocialValue());
                    if (!person.equals(person1)) {
                        person.addMoney(money);
                    }
                }
            } else if (myMessage instanceof EmojiMessage) {
                for (Person person : myGroup.getPeople()) {
                    person.addSocialValue(myMessage.getSocialValue());
                }
                MyEmojiMessage message = (MyEmojiMessage) myMessage;
                emojis.put(message.getEmojiId(),emojis.get(message.getEmojiId()) + 1);
            } else {
                for (Person person : myGroup.getPeople()) {
                    person.addSocialValue(myMessage.getSocialValue());
                }
            }
            messages.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!peopleMap.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }

    @Override
    public boolean containsEmojiId(int id) {
        return emojis.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojis.put(id,0);
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojis.get(id);
    }

    @Override
    public int deleteColdEmoji(int limit) {
        Iterator<Map.Entry<Integer,Integer>> iterator = emojis.entrySet().iterator();
        Map.Entry<Integer,Integer> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue() < limit) {
                iterator.remove();
            }
        }
        Iterator<Map.Entry<Integer,MyMessage>> iterator1 = messages.entrySet().iterator();
        Map.Entry<Integer,MyMessage> entry1;
        while (iterator1.hasNext()) {
            entry1 = iterator1.next();
            if (entry1.getValue() instanceof EmojiMessage &&
                    !containsEmojiId(((EmojiMessage) entry1.getValue()).getEmojiId())) {
                iterator1.remove();
            }
        }
        return emojis.size();
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!containsMessage(id) ||
                (containsMessage(id) && getMessage(id).getType() == 1)) {
            throw new MyMessageIdNotFoundException(id);
        }
        MyMessage message = (MyMessage) getMessage(id);
        try {
            if (!isCircle(message.getPerson1().getId(),message.getPerson2().getId())) {
                return -1;
            }
        } catch (PersonIdNotFoundException e) {
            return -1;
        }
        messages.remove(id);
        MyPerson person1 = (MyPerson) message.getPerson1();
        MyPerson person2 = (MyPerson) message.getPerson2();
        person1.addSocialValue(message.getSocialValue());
        person2.addSocialValue(message.getSocialValue());
        person2.addMessage(message);
        if (message instanceof RedEnvelopeMessage) {
            MyRedEnvelopeMessage m = (MyRedEnvelopeMessage) message;
            person1.addMoney(-m.getMoney());
            person2.addMoney(m.getMoney());
        } else if (message instanceof EmojiMessage) {
            MyEmojiMessage m = (MyEmojiMessage) message;
            emojis.put(m.getEmojiId(),emojis.get(m.getEmojiId()) + 1);
        }

        int personCount = 0;
        HashMap<Integer,Path> paths = new HashMap();
        PriorityQueue<Path> candidates = new PriorityQueue<>();
        candidates.add(new Path(0,person1,person1));

        while (personCount < queryPeopleSum() && !candidates.isEmpty()) {
            Path path = candidates.poll();
            int distance = path.getDistance();
            MyPerson min = (MyPerson) path.getEnd();
            if (paths.get(min.getId()) != null) {
                continue;
            }
            paths.put(min.getId(), path);
            if (min.getId() == person2.getId()) {
                break;
            }

            Iterator<Person> p = min.getAcquaintance().keySet().iterator();
            while (p.hasNext()) {
                Person next = p.next();
                Path path1 = paths.get(next.getId());
                int value = min.queryValue(next);
                if (path1 == null) {
                    candidates.add(new Path(distance + value,min,next));
                }
            }
            personCount++;
        }
        return paths.get(person2.getId()).getDistance();
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
