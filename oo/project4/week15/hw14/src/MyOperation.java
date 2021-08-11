import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

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

}
