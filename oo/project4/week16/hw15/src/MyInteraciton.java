import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyInteraciton {
    private UmlInteraction umlInteraction;
    private int participantCount = 0;
    private HashMap<String, List<MyLifeline>> name2LifeLine = new HashMap<>();

    public MyInteraciton(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
    }

    public String getName() {
        return umlInteraction.getName();
    }

    public void addParticipant(MyLifeline myLifeline) {
        participantCount++;
        if (name2LifeLine.containsKey(myLifeline.getName())) {
            name2LifeLine.get(myLifeline.getName()).add(myLifeline);
        } else {
            List<MyLifeline> list = new ArrayList<>();
            list.add(myLifeline);
            name2LifeLine.put(myLifeline.getName(),list);
        }
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public int getIncomingMessageCount(String name)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2LifeLine.containsKey(name)) {
            throw new LifelineNotFoundException(getName(),name);
        }
        List<MyLifeline> list = name2LifeLine.get(name);
        if (list.size() > 1) {
            throw new LifelineDuplicatedException(getName(),name);
        }
        return list.get(0).getincomingMessageCount();
    }

    public void addSentMessage(UmlMessage message,Object source) {
        if (source instanceof MyLifeline) {
            ((MyLifeline) source).addSentMessage(message);
        }
    }

    public void addMessage(UmlMessage message,Object target) {
        if (target instanceof MyLifeline) {
            ((MyLifeline) target).addIncomingMessages(message);
        }
    }

    public int getSentMessageCount(String name, MessageSort messageSort)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2LifeLine.containsKey(name)) {
            throw new LifelineNotFoundException(getName(),name);
        }
        List<MyLifeline> list = name2LifeLine.get(name);
        if (list.size() > 1) {
            throw new LifelineDuplicatedException(getName(),name);
        }
        return list.get(0).getSentMessageCount(messageSort);
    }

    public String getParentId() {
        return umlInteraction.getParentId();
    }
}
