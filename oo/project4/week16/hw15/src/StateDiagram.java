import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StateDiagram {
    private HashMap<String, Object> id2elem;
    private HashMap<String, List<MyStateMachine>> name2state = new HashMap<>();
    private List<MyStateMachine> stateMachines = new ArrayList<>();

    public StateDiagram(HashMap<String,Object> id2elem) {
        this.id2elem = id2elem;
    }

    public void parseUmlStateMachine(UmlElement e) {
        MyStateMachine stateMachine = new MyStateMachine((UmlStateMachine) e);
        id2elem.put(e.getId(),stateMachine);
        if (name2state.containsKey(stateMachine.getName())) {
            name2state.get(stateMachine.getName()).add(stateMachine);
        } else {
            List<MyStateMachine> list = new ArrayList<>();
            list.add(stateMachine);
            name2state.put(stateMachine.getName(),list);
        }
        stateMachines.add(stateMachine);
    }

    public void parseUmlRegion(UmlElement e) {
        MyStateMachine machine = (MyStateMachine) id2elem.get(e.getParentId());
        MyRegion myRegion = new MyRegion((UmlRegion) e);
        id2elem.put(e.getId(),myRegion);
        machine.setRegion(myRegion);
        myRegion.setParent(machine);
    }

    public void parseState(UmlElement e) {
        MyRegion myRegion = (MyRegion) id2elem.get(e.getParentId());
        MyState myState = new MyState(e);
        id2elem.put(e.getId(),myState);
        myRegion.addState(myState);
    }

    public void parseUmlTransition(UmlElement e) {
        String source = ((UmlTransition)e).getSource();
        String target = ((UmlTransition)e).getTarget();
        MyState sourceState = (MyState) id2elem.get(source);
        MyState targetstate = (MyState) id2elem.get(target);
        MyRegion myRegion = (MyRegion) id2elem.get(e.getParentId());
        myRegion.addTransition((UmlTransition) e,sourceState,targetstate);
        id2elem.put(e.getId(),e);
    }

    public void parseUmlEvent(UmlElement e) {
        UmlTransition transition = (UmlTransition) id2elem.get(e.getParentId());
        MyRegion myRegion = (MyRegion) id2elem.get(transition.getParentId());
        myRegion.addTriggers((UmlEvent) e);
    }

    public MyStateMachine findStateMachine(String name)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!name2state.containsKey(name)) {
            throw new StateMachineNotFoundException(name);
        }
        List<MyStateMachine> stateMachines = name2state.get(name);
        if (stateMachines.size() > 1) {
            throw new StateMachineDuplicatedException(name);
        }
        return stateMachines.get(0);
    }

    public int getStateCount(String s)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyStateMachine stateMachine = findStateMachine(s);
        return stateMachine.getStateCount();
    }

    public int getSubsequentStateCount(String s, String s1)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        MyStateMachine stateMachine = findStateMachine(s);
        return stateMachine.getSubsequentStateCount(s1);
    }

    public List<String> getTransitionTrigger(String s, String s1, String s2)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        MyStateMachine stateMachine = findStateMachine(s);
        return stateMachine.getTransitionTrigger(s1,s2);
    }

    public void checkForUml008() throws UmlRule008Exception {
        for (MyStateMachine machine : stateMachines) {
            machine.checkForUml008();
        }
    }
}
