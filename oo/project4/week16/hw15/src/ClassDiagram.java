import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.OperationParamInformation;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.models.common.NameableType;
import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassDiagram {
    private HashMap<String, List<MyClass>> name2class = new HashMap<>();
    private List<MyClass> classes = new ArrayList<>();
    private List<MyInterface> interfaces = new ArrayList<>();
    private HashMap<String,Object> id2elem;
    private HashMap<String,String> end2id = new HashMap<>();

    public ClassDiagram(HashMap<String,Object> id2elem) {
        this.id2elem = id2elem;
    }

    public void parseUmlClass(UmlElement e) {
        MyClass myClass = new MyClass((UmlClass) e);
        id2elem.put(e.getId(),myClass);
        if (name2class.containsKey(e.getName())) {
            name2class.get(e.getName()).add(myClass);
        } else {
            List<MyClass> list = new ArrayList<>();
            list.add(myClass);
            name2class.put(e.getName(),list);
        }
        classes.add(myClass);
    }

    public void parseInterface(UmlElement e) {
        MyInterface myInterface = new MyInterface((UmlInterface) e);
        id2elem.put(e.getId(),myInterface);
        interfaces.add(myInterface);
    }

    public void parseUmlAssociationEnd(UmlElement e) {
        end2id.put(e.getId(),((UmlAssociationEnd) e).getReference());
        id2elem.put(e.getId(), e);
    }

    public void parseUmlAttribute(UmlElement e) {
        Object obj = id2elem.get(e.getParentId());
        if (obj instanceof Operable) {
            Operable target = (Operable) obj;
            target.addAttribute((UmlAttribute) e);
        }
        id2elem.put(e.getId(), e);
    }

    public void parseUmlGeneralization(UmlElement e) {
        Object source = id2elem.get(((UmlGeneralization) e).getSource());
        Object target = id2elem.get(((UmlGeneralization) e).getTarget());
        if (source instanceof MyClass && target instanceof MyClass) {
            ((MyClass) source).setParent((MyClass) target);
        } else if (source instanceof MyInterface && target instanceof MyInterface) {
            ((MyInterface) source).setParents((MyInterface) target);
        }
    }

    public void parseUmlInterfaceRealization(UmlElement e) {
        Operable source = (Operable) id2elem.get(((UmlInterfaceRealization) e).getSource());
        Operable target = (Operable) id2elem.get(((UmlInterfaceRealization) e).getTarget());
        if (source instanceof MyClass && target instanceof MyInterface) {
            ((MyClass) source).setImplementInterface((MyInterface) target);
        }
    }

    public void parseUmlOperation(UmlElement e) {
        Object obj = id2elem.get(e.getParentId());
        MyOperation operation = new MyOperation((UmlOperation) e);
        if (obj instanceof Operable) {
            Operable myClass = (Operable) obj;
            myClass.addOperation(operation);
        }
        id2elem.put(e.getId(),operation);
    }

    public void parseUmlAssociation(UmlElement e) {
        Object end1 = id2elem.get(end2id.get(((UmlAssociation) e).getEnd1()));
        Object end2 = id2elem.get(end2id.get(((UmlAssociation) e).getEnd2()));
        Associated e1 = (Associated) end1;
        Associated e2 = (Associated) end2;
        e1.addAssociatedEnd(e2);
        e2.addAssociatedEnd(e1);
        UmlAssociationEnd endOne = (UmlAssociationEnd) id2elem.get(((UmlAssociation) e).getEnd1());
        UmlAssociationEnd endTwo = (UmlAssociationEnd) id2elem.get(((UmlAssociation) e).getEnd2());
        e1.addAssociated(endTwo);
        e2.addAssociated(endOne);
    }

    public void parseUmlParameter(UmlElement e) {
        MyOperation operation = (MyOperation) id2elem.get(e.getParentId());
        operation.addParameters((UmlParameter) e);
    }

    public MyClass findClass(String name) throws ClassNotFoundException,
            ClassDuplicatedException {
        if (!name2class.containsKey(name)) {
            throw new ClassNotFoundException(name);
        }
        List<MyClass> classes = name2class.get(name);
        if (classes.size() > 1) {
            throw new ClassDuplicatedException(name);
        }
        return classes.get(0);
    }

    public int getClassCount() {
        return classes.size();
    }

    public int getClassOperationCount(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getOperationCnt();
    }

    public int getClassAttributeCount(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getAttributeCount();
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getOperationVisibility(s1);
    }

    public Visibility getClassAttributeVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getAttrVisibility(s1);
    }

    public String getClassAttributeType(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        MyClass myClass = findClass(s);
        String type = myClass.getAttributeType(s1);
        if (!type.equals("byte") && !type.equals("short") && !type.equals("int")
                && !type.equals("long") && !type.equals("float") && !type.equals("double")
                && !type.equals("char") && !type.equals("boolean") && !type.equals("String")) {
            if (id2elem.containsKey(type)) {
                return ((Operable) id2elem.get(type)).getName();
            } else {
                throw new AttributeWrongTypeException(myClass.getName(),s1);
            }
        }
        return type;
    }

    public boolean judge(String name) {
        if (name.equals("byte") ||
                name.equals("short") ||
                name.equals("int") ||
                name.equals("long") ||
                name.equals("float") ||
                name.equals("double") ||
                name.equals("char")) {
            return true;
        }
        return name.equals("boolean") ||
                name.equals("String");
    }

    public List<OperationParamInformation> getClassOperationParamType(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        MyClass myClass = findClass(s);
        ArrayList<MyOperation> operations = myClass.getOperations();
        ArrayList<OperationParamInformation> informations = new ArrayList<>();
        for (MyOperation op : operations) {
            if (op.getName().equals(s1)) {
                ArrayList<UmlParameter> parameters = op.getParameters();
                ArrayList<UmlParameter> returns = op.getReturns();
                String returnType = null;
                if (!returns.isEmpty()) {
                    NameableType type = returns.get(0).getType();
                    if (type instanceof NamedType) {
                        if (judge(((NamedType) type).getName()) ||
                                ((NamedType) type).getName().equals("void")) {
                            returnType = ((NamedType) type).getName();
                        } else {
                            throw new MethodWrongTypeException(myClass.getName(),s1);
                        }
                    } else if (type instanceof ReferenceType) {
                        if (id2elem.containsKey(((ReferenceType) type).getReferenceId())) {
                            returnType = ((Operable)id2elem.get(((ReferenceType)
                                    type).getReferenceId())).getName();
                        } else {
                            throw new MethodWrongTypeException(myClass.getName(),s1);
                        }
                    }
                }
                List<String> parameter = new ArrayList<>();
                if (!parameters.isEmpty()) {
                    for (UmlParameter para : parameters) {
                        NameableType type = para.getType();
                        if (type instanceof NamedType) {
                            if (judge(((NamedType) type).getName())) {
                                parameter.add(((NamedType) type).getName());
                            } else {
                                throw new MethodWrongTypeException(myClass.getName(),s1);
                            }
                        } else if (type instanceof ReferenceType) {
                            if (id2elem.containsKey(((ReferenceType) type).getReferenceId())) {
                                parameter.add(((Operable)id2elem.
                                        get(((ReferenceType) type).getReferenceId())).getName());
                            } else {
                                throw new MethodWrongTypeException(myClass.getName(),s1);
                            }
                        }
                    }
                }
                OperationParamInformation n = new OperationParamInformation(parameter, returnType);
                informations.add(n);
            }
        }
        for (int i = 0;i < informations.size() - 1;i++) {
            for (int j = i + 1;j < informations.size();j++) {
                if (informations.get(i).compareTo(informations.get(j)) == 0) {
                    throw new MethodDuplicatedException(myClass.getName(),s1);
                }
            }
        }
        return informations;
    }

    public List<String> getClassAssociatedClassList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getAssociatedClassList();
    }

    public String getTopParentClass(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getTopParentClass();
    }

    public List<String> getImplementInterfaceList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getImplementedInterfList();
    }

    public List<AttributeClassInformation> getInformationNotHidden(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getAttributeNotHidden();
    }

    public void checkForUml001() throws UmlRule001Exception {
        Set<AttributeClassInformation> faults = new HashSet();
        for (MyClass myClass : classes) {
            List<String> duplicateName = myClass.checkForUml001();
            for (String name : duplicateName) {
                faults.add(new AttributeClassInformation(name,myClass.getName()));
            }
        }
        if (!faults.isEmpty()) {
            throw new UmlRule001Exception(faults);
        }
    }

    public void checkForUml002() throws UmlRule002Exception {
        Set<UmlClassOrInterface> faults = new HashSet<>();
        for (MyClass myClass : classes) {
            if (myClass.checkForUml002()) {
                faults.add(myClass.getUmlClass());
            }
        }
        for (MyInterface myInterface : interfaces) {
            if (myInterface.checkForUml002()) {
                faults.add(myInterface.getUmlInterface());
            }
        }
        if (!faults.isEmpty()) {
            throw new UmlRule002Exception(faults);
        }
    }

    public void checkForUml003() throws UmlRule003Exception {
        Set<UmlClassOrInterface> faults = new HashSet<>();
        for (MyInterface myInterface : interfaces) {
            if (!myInterface.checkForUml003()) {
                faults.add(myInterface.getUmlInterface());
            }
        }
        if (!faults.isEmpty()) {
            throw new UmlRule003Exception(faults);
        }
    }

    public void checkForUml004() throws UmlRule004Exception {
        Set<UmlClass> faults = new HashSet<>();
        for (MyClass myClass : classes) {
            if (!myClass.checkForUml004()) {
                faults.add(myClass.getUmlClass());
            }
        }
        if (!faults.isEmpty()) {
            throw new UmlRule004Exception(faults);
        }
    }

    public void checkForUml005() throws UmlRule005Exception {
        for (MyClass myClass : classes) {
            myClass.checkForUml005();
        }
        for (MyInterface myInterface : interfaces) {
            myInterface.checkForUml005();
        }
    }

    public void checkForUml006() throws UmlRule006Exception {
        for (MyInterface myInterface : interfaces) {
            myInterface.checkForUml006();
        }
    }
}
