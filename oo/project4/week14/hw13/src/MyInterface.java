import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyInterface implements Operable,Associated {
    private UmlInterface umlInterface;
    private ArrayList<MyInterface> parents = new ArrayList<>();
    private ArrayList<MyOperation> operations = new ArrayList<>();
    private ArrayList<MyClass> associateClass = new ArrayList<>();
    private ArrayList<MyInterface> associateInterface = new ArrayList<>();

    public MyInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }

    @Override
    public void addAttribute(UmlAttribute umlAttribute) {

    }

    @Override
    public void addOperation(MyOperation myOperation) {

    }

    public void setParents(MyInterface parent) {
        parents.add(parent);
    }

    @Override
    public void addAssociatedEnd(Associated end) {

    }

    public List<MyInterface> getParents() {
        return parents;
    }

    public String getName() {
        return this.umlInterface.getName();
    }
}
