import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.List;

public class MyLifeline {
    private UmlLifeline umlLifeline;
    private List<UmlMessage> incomingMessages = new ArrayList<>();
    private List<UmlMessage> sentMessages = new ArrayList<>();

    public MyLifeline(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
    }

    public String getName() {
        return umlLifeline.getName();
    }

    public void addIncomingMessages(UmlMessage message) {
        incomingMessages.add(message);
    }

    public int getincomingMessageCount() {
        return incomingMessages.size();
    }

    public void addSentMessage(UmlMessage message) {
        sentMessages.add(message);
    }

    public int getSentMessageCount(MessageSort messageSort) {
        int count = 0;
        for (UmlMessage message : sentMessages) {
            if (message.getMessageSort().equals(messageSort)) {
                count++;
            }
        }
        return count;
    }

    public String getRepresent() {
        return umlLifeline.getRepresent();
    }

    public String getParentId() {
        return umlLifeline.getParentId();
    }
}
