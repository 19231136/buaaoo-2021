import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;

public class MyOperation {
    private UmlOperation umlOperation;
    private ArrayList<UmlParameter> parameters = new ArrayList<>();
    private ArrayList<UmlParameter> returns = new ArrayList<>();

    public MyOperation(UmlOperation umlOperation) {
        this.umlOperation = umlOperation;
    }

    public ArrayList<UmlParameter> getParameters() {
        return parameters;
    }

    public ArrayList<UmlParameter> getReturns() {
        return returns;
    }

    public void addParameters(UmlParameter parameter) {
        if (parameter.getDirection() == Direction.IN) {
            parameters.add(parameter);
        }
        if (parameter.getDirection() == Direction.RETURN) {
            returns.add(parameter);
        }
    }

    public String getName() {
        return umlOperation.getName();
    }

    public Visibility getVisibility() {
        return umlOperation.getVisibility();
    }

    public void checkForUml005() throws UmlRule005Exception {
        if (this.getName() == null) {
            throw new UmlRule005Exception();
        }
        for (UmlParameter parameter : parameters) {
            if (parameter.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
    }

}
