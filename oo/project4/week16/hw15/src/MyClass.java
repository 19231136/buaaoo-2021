import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.NameableType;
import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MyClass implements Operable,Associated {
    private UmlClass umlClass;
    private MyClass parent = null;
    private ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private ArrayList<MyOperation> operations = new ArrayList<>();
    private ArrayList<UmlAssociationEnd> umlAssociationEnds = new ArrayList<>();
    private ArrayList<MyClass> associateClass = new ArrayList<>();
    private ArrayList<MyInterface> associateInterface = new ArrayList<>();
    private ArrayList<MyInterface> implementInterface = new ArrayList<>();

    public MyClass(UmlClass umlClass) {
        this.umlClass = umlClass;
    }

    public String getName() {
        return umlClass.getName();
    }

    public int getOperationCnt() {
        return operations.size();
    }

    public UmlClass getUmlClass() {
        return umlClass;
    }

    @Override
    public void addAttribute(UmlAttribute umlAttribute) {
        attributes.add(umlAttribute);
    }

    @Override
    public void addOperation(MyOperation myOperation) {
        operations.add(myOperation);
    }

    public void setParent(MyClass parent) {
        this.parent = parent;
    }

    public void setImplementInterface(MyInterface interf) {
        this.implementInterface.add(interf);
    }

    @Override
    public void addAssociatedEnd(Associated end) {
        if (end instanceof MyClass) {
            associateClass.add((MyClass) end);
        } else if (end instanceof MyInterface) {
            associateInterface.add((MyInterface) end);
        }
    }

    @Override
    public void addAssociated(UmlAssociationEnd end) {
        umlAssociationEnds.add(end);
    }

    public int getAttributeCount() {
        if (parent == null) {
            return attributes.size();
        }
        return attributes.size() + parent.getAttributeCount();
    }

    public Map<Visibility, Integer> getOperationVisibility(String name) {
        Map<Visibility, Integer> visibilityIntegerMap = new EnumMap<>(Visibility.class);
        for (MyOperation op : operations) {
            if (op.getName().equals(name)) {
                visibilityIntegerMap.merge(op.getVisibility(), 1, Integer::sum);
            }
        }
        return visibilityIntegerMap;
    }

    public Visibility getAttrVisibility(String attributeName)
            throws AttributeDuplicatedException, AttributeNotFoundException {
        List<UmlAttribute> attributesAll = new ArrayList<>();
        MyClass cur = this;
        while (cur != null) {
            attributesAll.addAll(cur.attributes);
            cur = cur.parent;
        }
        int count = 0;
        Visibility visibility = null;
        for (UmlAttribute attribute : attributesAll) {
            if (attribute.getName().equals(attributeName)) {
                count++;
                visibility = attribute.getVisibility();
            }
        }
        if (count == 0) {
            throw new AttributeNotFoundException(umlClass.getName(),attributeName);
        }
        if (count > 1) {
            throw new AttributeDuplicatedException(umlClass.getName(),attributeName);
        }
        return visibility;
    }

    public String getAttributeType(String attributeName)
            throws AttributeDuplicatedException, AttributeNotFoundException,
            AttributeWrongTypeException {
        List<UmlAttribute> attributesAll = new ArrayList<>();
        MyClass cur = this;
        while (cur != null) {
            attributesAll.addAll(cur.attributes);
            cur = cur.parent;
        }
        int count = 0;
        NameableType type = null;
        for (UmlAttribute attribute : attributesAll) {
            if (attribute.getName().equals(attributeName)) {
                count++;
                type = attribute.getType();
            }
        }
        if (count == 0) {
            throw new AttributeNotFoundException(umlClass.getName(),attributeName);
        }
        if (count > 1) {
            throw new AttributeDuplicatedException(umlClass.getName(),attributeName);
        }
        if (type instanceof NamedType) {
            if (((NamedType) type).getName().equals("byte") ||
                    ((NamedType) type).getName().equals("short") ||
                    ((NamedType) type).getName().equals("int") ||
                    ((NamedType) type).getName().equals("long") ||
                    ((NamedType) type).getName().equals("float") ||
                    ((NamedType) type).getName().equals("double") ||
                    ((NamedType) type).getName().equals("char")) {
                return ((NamedType) type).getName();
            } else if (((NamedType) type).getName().equals("boolean") ||
                    ((NamedType) type).getName().equals("String")) {
                return ((NamedType) type).getName();
            } else {
                throw new AttributeWrongTypeException(umlClass.getName(),attributeName);
            }
        } else if (type instanceof ReferenceType) {
            return ((ReferenceType) type).getReferenceId();
        }
        throw new AttributeWrongTypeException(umlClass.getName(),attributeName);
    }

    public List<String> getAssociatedClassList() {
        HashSet<MyClass> classes = new HashSet<>();
        MyClass cur = this;
        while (cur != null) {
            classes.addAll(cur.associateClass);
            cur = cur.parent;
        }

        List<String> list = new ArrayList<>();
        for (MyClass myClass : classes) {
            list.add(myClass.getName());
        }
        return list;
    }

    public String getTopParentClass() {
        MyClass cur = this;
        while (cur.parent != null) {
            cur = cur.parent;
        }
        return cur.getName();
    }

    public List<String> getImplementedInterfList() {
        HashSet<MyInterface> set = new HashSet<>();
        MyClass cur = this;
        while (cur != null) {
            set.addAll(cur.implementInterface);
            cur = cur.parent;
        }
        Queue<MyInterface> queue = new LinkedList<>(set);
        HashSet<MyInterface> superSet = new HashSet<>();

        while (!queue.isEmpty()) {
            MyInterface curInterface = queue.poll();
            if (!superSet.contains(curInterface)) {
                superSet.add(curInterface);
                queue.addAll(curInterface.getParents());
            }
        }

        List<String> list = new ArrayList<>();
        for (MyInterface myInterface : superSet) {
            list.add(myInterface.getName());
        }
        return list;
    }

    public List<AttributeClassInformation> getAttributeNotHidden() {
        List<AttributeClassInformation> list = new ArrayList<>();
        MyClass cur = this;
        while (cur != null) {
            for (UmlAttribute attr : cur.attributes) {
                if (attr.getVisibility() != Visibility.PRIVATE) {
                    list.add(new AttributeClassInformation(attr.getName(),
                            cur.getName()));
                }
            }
            cur = cur.parent;
        }
        return list;
    }

    public ArrayList<MyOperation> getOperations() {
        return operations;
    }

    public List<String> checkForUml001() {
        ArrayList<String> duplicated = new ArrayList<>();
        Set<String> name = new HashSet<>();
        for (UmlAttribute attribute : attributes) {
            if (attribute.getName() != null) {
                if (!name.add(attribute.getName())) {
                    duplicated.add(attribute.getName());
                }
            }
        }
        for (UmlAssociationEnd end : umlAssociationEnds) {
            if (end.getName() != null) {
                if (!name.add(end.getName())) {
                    duplicated.add(end.getName());
                }
            }
        }
        return duplicated;
    }

    public boolean checkForUml002() {
        Set<UmlClass> circular = new HashSet<>();
        MyClass cur = this;
        while (cur != null) {
            if (!circular.add(cur.umlClass)) {
                return false;
            }
            cur = cur.parent;
            if (cur == this) {
                return true;
            }
        }
        return false;
    }

    public boolean checkForUml004() {
        Set<MyInterface> relization = new HashSet<>();
        MyClass cur = this;
        while (cur != null) {
            for (MyInterface myInterface : cur.implementInterface) {
                if (!relization.add(myInterface)) {
                    return false;
                }
            }
            cur = cur.parent;
        }
        Queue<MyInterface> interfaceQueue = new LinkedList<>(relization);
        Set<MyInterface> parentInterface = new HashSet<>();
        while (!interfaceQueue.isEmpty()) {
            MyInterface current = interfaceQueue.poll();
            if (!parentInterface.add(current)) {
                return false;
            } else {
                interfaceQueue.addAll(current.getParents());
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
}
