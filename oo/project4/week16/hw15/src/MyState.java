import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlState;

import java.util.ArrayList;
import java.util.List;

public class MyState {
    private UmlElement state;
    private List<MyState> states = new ArrayList<>();

    public MyState(UmlElement state) {
        if (state instanceof UmlState || state instanceof UmlFinalState
                || state instanceof UmlPseudostate) {
            this.state = state;
        }
    }

    public String getId() {
        return state.getId();
    }

    public ElementType getType() {
        return state.getElementType();
    }

    public String getName() {
        return state.getName();
    }

    public void addSubsequentState(MyState state) {
        states.add(state);
    }

    public List<MyState> getStates() {
        return states;
    }

    public int getTransitionSum() {
        return states.size();
    }
}
