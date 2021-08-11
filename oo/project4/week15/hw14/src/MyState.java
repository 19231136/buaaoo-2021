import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlState;

import java.util.HashSet;
import java.util.Set;

public class MyState {
    private UmlElement state;
    private Set<MyState> states = new HashSet<>();

    public MyState(UmlElement state) {
        if (state instanceof UmlState || state instanceof UmlFinalState
                || state instanceof UmlPseudostate) {
            this.state = state;
        }
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

    public Set<MyState> getStates() {
        return states;
    }
}
