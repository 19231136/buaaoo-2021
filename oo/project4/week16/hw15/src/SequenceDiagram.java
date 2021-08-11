import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SequenceDiagram {
    private HashMap<String, List<MyInteraciton>> name2interaction = new HashMap<>();
    private HashMap<String,Object> id2elem;
    private ArrayList<MyLifeline> lifelines = new ArrayList<>();

    public SequenceDiagram(HashMap<String,Object> id2elem) {
        this.id2elem = id2elem;
    }

    public void parseUmlInteraction(UmlElement e) {
        MyInteraciton interaciton = new MyInteraciton((UmlInteraction) e);
        id2elem.put(e.getId(),interaciton);
        if (name2interaction.containsKey(interaciton.getName())) {
            name2interaction.get(interaciton.getName()).add(interaciton);
        } else {
            List<MyInteraciton> list = new ArrayList<>();
            list.add(interaciton);
            name2interaction.put(interaciton.getName(),list);
        }
    }

    public void parseUmlLifeLine(UmlElement e) {
        MyLifeline myLifeline = new MyLifeline((UmlLifeline) e);
        lifelines.add(myLifeline);
        id2elem.put(e.getId(),myLifeline);
        MyInteraciton interaciton = (MyInteraciton) id2elem.get(e.getParentId());
        interaciton.addParticipant(myLifeline);
    }

    public void parseUmlMessage(UmlElement e) {
        Object obj = id2elem.get(((UmlMessage)e).getTarget());
        Object source = id2elem.get(((UmlMessage)e).getSource());
        MyInteraciton myInteraciton = (MyInteraciton) id2elem.get(e.getParentId());
        myInteraciton.addMessage((UmlMessage) e,obj);
        myInteraciton.addSentMessage((UmlMessage) e,source);
    }

    public MyInteraciton findInteraction(String name)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2interaction.containsKey(name)) {
            throw new InteractionNotFoundException(name);
        }
        List<MyInteraciton> interacitons = name2interaction.get(name);
        if (interacitons.size() > 1) {
            throw new InteractionDuplicatedException(name);
        }
        return interacitons.get(0);
    }

    public int getParticipantCount(String s)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyInteraciton interaciton = findInteraction(s);
        return interaciton.getParticipantCount();
    }

    public int getIncomingMessageCount(String s, String s1)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyInteraciton interaciton = findInteraction(s);
        return interaciton.getIncomingMessageCount(s1);
    }

    public int getSentMessageCount(String s, String s1, MessageSort messageSort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyInteraciton interaciton = findInteraction(s);
        return interaciton.getSentMessageCount(s1,messageSort);
    }

    public void checkForUml007() throws UmlRule007Exception {
        for (MyLifeline lifeline : lifelines) {
            String id = lifeline.getRepresent();
            if (id2elem.get(id) == null) {
                throw new UmlRule007Exception();
            }
            if (!(id2elem.get(id) instanceof UmlAttribute)) {
                throw new UmlRule007Exception();
            }
            UmlAttribute umlAttribute = (UmlAttribute) id2elem.get(id);
            MyInteraciton umlInteraction = (MyInteraciton) id2elem.get(lifeline.getParentId());
            if (!umlInteraction.getParentId().equals(umlAttribute.getParentId())) {
                throw new UmlRule007Exception();
            }
        }
    }
}
