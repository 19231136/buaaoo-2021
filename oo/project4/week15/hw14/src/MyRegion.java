import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class MyRegion {
    private UmlRegion umlRegion;
    private MyStateMachine parent;
    private List<MyState> states = new ArrayList<>();
    private HashMap<String,List<MyState>> name2state = new HashMap<>();
    private HashMap<String,List<String>> transitionid2statename = new HashMap<>();
    //private List<UmlTransition> transitions = new ArrayList<>();
    //private HashMap<String,UmlTransition> id2transition = new HashMap<>();
    //private HashMap<String,MyState> transitionid2target = new HashMap();
    //private HashMap<MyState,List<String>> sourceid2transitionid = new HashMap<>();
    private HashMap<String,List<String>> transitionid2triggers = new HashMap<>();

    public MyRegion(UmlRegion umlRegion) {
        this.umlRegion = umlRegion;
    }

    public void setParent(MyStateMachine machine) {
        this.parent = machine;
    }

    public void addState(MyState myState) {
        if (myState.getType() == ElementType.UML_STATE) {
            states.add(myState);
            if (name2state.containsKey(myState.getName())) {
                name2state.get(myState.getName()).add(myState);
            } else {
                List<MyState> list = new ArrayList<>();
                list.add(myState);
                name2state.put(myState.getName(),list);
            }
        } else if (myState.getType() == ElementType.UML_FINAL_STATE ||
                myState.getType() == ElementType.UML_PSEUDOSTATE) {
            states.add(myState);
        }
    }

    public void addTriggers(UmlEvent trigger) {
        String transitionId = trigger.getParentId();
        if (transitionid2triggers.containsKey(transitionId)) {
            transitionid2triggers.get(transitionId).add(trigger.getName());
        } else {
            List<String> list = new ArrayList<>();
            list.add(trigger.getName());
            transitionid2triggers.put(transitionId,list);
        }
    }

    public void addTransition(UmlTransition umlTransition,MyState source,MyState target) {
        //transitions.add(umlTransition);
        List<String> stateName = new ArrayList<>();
        stateName.add(source.getName());
        stateName.add(target.getName());
        transitionid2statename.put(umlTransition.getId(),stateName);
        source.addSubsequentState(target);
    }

    public int getStateCount() {
        return states.size();
    }

    public int getSubsequentStateCount(String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        if (!name2state.containsKey(stateName)) {
            throw new StateNotFoundException(parent.getName(), stateName);
        }
        List<MyState> list = name2state.get(stateName);
        if (list.size() > 1) {
            throw new StateDuplicatedException(parent.getName(), stateName);
        }
        Set<MyState> subsequentSet = new HashSet<>();
        Queue<MyState> queue = new LinkedList<>(list.get(0).getStates());
        while (!queue.isEmpty()) {
            MyState cur = queue.poll();
            if (!subsequentSet.contains(cur)) {
                subsequentSet.add(cur);
                queue.addAll(cur.getStates());
            }
        }
        return subsequentSet.size();
    }

    public List<String> getTransitionTrigger(String stateFrom,String stateTo)
            throws StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        if (!name2state.containsKey(stateFrom)) {
            throw new StateNotFoundException(parent.getName(), stateFrom);
        }
        List<MyState> list = name2state.get(stateFrom);
        if (list.size() > 1) {
            throw new StateDuplicatedException(parent.getName(), stateFrom);
        }
        if (!name2state.containsKey(stateTo)) {
            throw new StateNotFoundException(parent.getName(), stateTo);
        }
        List<MyState> list2 = name2state.get(stateFrom);
        if (list2.size() > 1) {
            throw new StateDuplicatedException(parent.getName(), stateTo);
        }
        boolean haveTransition = false;
        String transitionId;
        List<String> result = new ArrayList<>();
        for (String id : transitionid2statename.keySet()) {
            List<String> list1 = transitionid2statename.get(id);
            if (list1.get(0) != null && list1.get(1) != null
                    && list1.get(0).equals(stateFrom) && list1.get(1).equals(stateTo)) {
                haveTransition = true;
                transitionId = id;
                if (transitionid2triggers.containsKey(transitionId)) {
                    result.addAll(transitionid2triggers.get(transitionId));
                }
            }
        }
        if (!haveTransition) {
            throw new TransitionNotFoundException(parent.getName(),stateFrom,stateTo);
        }
        return result;
    }
}
