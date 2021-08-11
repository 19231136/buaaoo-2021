import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class MyInterface implements Operable,Associated {
    private UmlInterface umlInterface;
    private ArrayList<MyInterface> parents = new ArrayList<>();
    private ArrayList<MyOperation> operations = new ArrayList<>();
    private ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private ArrayList<MyClass> associateClass = new ArrayList<>();
    private ArrayList<MyInterface> associateInterface = new ArrayList<>();

    public MyInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
    }

    public UmlInterface getUmlInterface() {
        return this.umlInterface;
    }

    @Override
    public void addAttribute(UmlAttribute umlAttribute) {
        attributes.add(umlAttribute);
    }

    @Override
    public void addOperation(MyOperation myOperation) {
        operations.add(myOperation);
    }

    public void setParents(MyInterface parent) {
        parents.add(parent);
    }

    @Override
    public void addAssociatedEnd(Associated end) {

    }

    @Override
    public void addAssociated(UmlAssociationEnd end) {

    }

    public List<MyInterface> getParents() {
        return parents;
    }

    public String getName() {
        return this.umlInterface.getName();
    }

    public boolean checkForUml002() {
        Stack<MyInterface> stack = new Stack<>();
        Stack<Integer> indexStack = new Stack<>();
        stack.add(this);
        indexStack.add(0);
        HashSet<MyInterface> visit = new HashSet<>();
        visit.add(this);
        while (!stack.isEmpty()) {
            int i = indexStack.pop();
            MyInterface cur = stack.pop();
            if (i < cur.parents.size()) {
                MyInterface next = cur.parents.get(i);
                if (next == this) {
                    return true;
                }
                indexStack.push(i + 1);
                stack.push(cur);
                if (visit.add(next)) {
                    stack.add(next);
                    indexStack.add(0);
                }
            }
        }
        return false;
    }

    public boolean checkForUml003() {
        Set<MyInterface> generalized = new HashSet<>();
        Queue<MyInterface> queue = new LinkedList<>(parents);
        while (!queue.isEmpty()) {
            MyInterface cur = queue.poll();
            if (!generalized.add(cur)) {
                return false;
            } else {
                queue.addAll(cur.getParents());
            }
        }
        return true;
    }

    public void checkForUml005() throws UmlRule005Exception {
        if (getName() == null) {
            throw new UmlRule005Exception();
        }
        for (UmlAttribute attribute : attributes) {
            if (attribute.getName() == null) {
                throw new UmlRule005Exception();
            }
        }
        for (MyOperation operation : operations) {
            operation.checkForUml005();
        }
    }

    public void checkForUml006() throws UmlRule006Exception {
        for (UmlAttribute attribute : attributes) {
            if (attribute.getVisibility() != Visibility.PUBLIC) {
                throw new UmlRule006Exception();
            }
        }
    }
}
