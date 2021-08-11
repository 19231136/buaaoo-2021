import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.models.elements.UmlStateMachine;

import java.util.List;

public class MyStateMachine {
    private UmlStateMachine umlStateMachine;
    private MyRegion region;

    public MyStateMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
    }

    public void setRegion(MyRegion myRegion) {
        this.region = myRegion;
    }

    public String getName() {
        return umlStateMachine.getName();
    }

    public int getStateCount() {
        return region.getStateCount();
    }

    public int getSubsequentStateCount(String stateName)
            throws StateDuplicatedException, StateNotFoundException {
        return region.getSubsequentStateCount(stateName);
    }

    public List<String> getTransitionTrigger(String stateFrom,String stateTo)
            throws StateDuplicatedException, StateNotFoundException, TransitionNotFoundException {
        return region.getTransitionTrigger(stateFrom,stateTo);
    }
}
