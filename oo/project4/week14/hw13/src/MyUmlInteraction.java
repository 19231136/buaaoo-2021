import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.OperationParamInformation;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlInteraction implements UmlInteraction {
    private int classCount = 0;
    private HashMap<String,Object> id2elem = new HashMap<>();
    private HashMap<String,List<MyClass>> name2class = new HashMap<>();
    private HashMap<String,String> end2id = new HashMap<>();

    public MyUmlInteraction(UmlElement... elements) {
        for (UmlElement e : elements) {
            if (e instanceof UmlClass) {
                MyClass myClass = new MyClass((UmlClass) e);
                classCount++;
                id2elem.put(e.getId(),myClass);
                if (name2class.containsKey(e.getName())) {
                    name2class.get(e.getName()).add(myClass);
                } else {
                    List<MyClass> list = new ArrayList<>();
                    list.add(myClass);
                    name2class.put(e.getName(),list);
                }
            } else if (e instanceof UmlInterface) {
                MyInterface myInterface = new MyInterface((UmlInterface) e);
                id2elem.put(e.getId(),myInterface);
            }
        }

        for (UmlElement e : elements) {
            if (e instanceof UmlAssociationEnd) {
                end2id.put(e.getId(),((UmlAssociationEnd) e).getReference());
            } else if (e instanceof UmlAttribute) {
                Operable myClass = (Operable) id2elem.get(e.getParentId());
                myClass.addAttribute((UmlAttribute) e);
            } else if (e instanceof UmlGeneralization) {
                Operable source = (Operable) id2elem.get(((UmlGeneralization) e).getSource());
                Operable target = (Operable) id2elem.get(((UmlGeneralization) e).getTarget());
                if (source instanceof MyClass && target instanceof MyClass) {
                    ((MyClass) source).setParent((MyClass) target);
                } else if (source instanceof MyInterface && target instanceof MyInterface) {
                    ((MyInterface) source).setParents((MyInterface) target);
                }
            } else if (e instanceof UmlInterfaceRealization) {
                Operable source = (Operable) id2elem.get(((UmlInterfaceRealization) e).getSource());
                Operable target = (Operable) id2elem.get(((UmlInterfaceRealization) e).getTarget());
                if (source instanceof MyClass && target instanceof MyInterface) {
                    ((MyClass) source).setImplementInterface((MyInterface) target);
                }
            } else if (e instanceof UmlOperation) {
                Operable myClass = (Operable) id2elem.get(e.getParentId());
                MyOperation operation = new MyOperation((UmlOperation) e);
                myClass.addOperation(operation);
                id2elem.put(e.getId(),operation);
            }
        }

        for (UmlElement e : elements) {
            if (e instanceof UmlAssociation) {
                Object end1 = id2elem.get(end2id.get(((UmlAssociation) e).getEnd1()));
                Object end2 = id2elem.get(end2id.get(((UmlAssociation) e).getEnd2()));
                Associated e1 = (Associated) end1;
                Associated e2 = (Associated) end2;
                e1.addAssociatedEnd(e2);
                e2.addAssociatedEnd(e1);
            } else if (e instanceof UmlParameter) {
                MyOperation operation = (MyOperation) id2elem.get(e.getParentId());
                operation.addParameters((UmlParameter) e);
            }
        }
    }

    public MyClass findClass(String name) throws ClassNotFoundException,
            ClassDuplicatedException {
        if (!name2class.containsKey(name)) {
            throw new ClassNotFoundException(name);
        }
        List<MyClass> classes = name2class.get(name);
        if (classes.size() == 0) {
            throw new ClassNotFoundException(name);
        }
        if (classes.size() > 1) {
            throw new ClassDuplicatedException(name);
        }
        return classes.get(0);
    }

    @Override
    public int getClassCount() {
        return classCount;
    }

    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(className);
        return myClass.getOperationCnt();
    }

    @Override
    public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(className);
        return myClass.getAttributeCount();
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String className,
                                                                String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(className);
        return myClass.getOperationVisibility(operationName);
    }

    @Override
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyClass myClass = findClass(className);
        return myClass.getAttrVisibility(attributeName);
    }

    @Override
    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException, AttributeNotFoundException,
            AttributeDuplicatedException, AttributeWrongTypeException {
        MyClass myClass = findClass(className);
        String type = myClass.getAttributeType(attributeName);
        if (!type.equals("byte") && !type.equals("short") && !type.equals("int")
            && !type.equals("long") && !type.equals("float") && !type.equals("double")
            && !type.equals("char") && !type.equals("boolean") && !type.equals("String")) {
            if (id2elem.containsKey(type)) {
                return ((Operable) id2elem.get(type)).getName();
            } else {
                throw new AttributeWrongTypeException(myClass.getName(),attributeName);
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

    @Override
    public List<OperationParamInformation> getClassOperationParamType(String className,
                                                                      String operationName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        MyClass myClass = findClass(className);
        ArrayList<MyOperation> operations = myClass.getOperations();
        ArrayList<OperationParamInformation> informations = new ArrayList<>();
        for (MyOperation op : operations) {
            if (op.getName().equals(operationName)) {
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
                            throw new MethodWrongTypeException(myClass.getName(),operationName);
                        }
                    } else if (type instanceof ReferenceType) {
                        if (id2elem.containsKey(((ReferenceType) type).getReferenceId())) {
                            returnType = ((Operable)id2elem.get(((ReferenceType)
                                    type).getReferenceId())).getName();
                        } else {
                            throw new MethodWrongTypeException(myClass.getName(),operationName);
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
                                throw new MethodWrongTypeException(myClass.getName(),operationName);
                            }
                        } else if (type instanceof ReferenceType) {
                            if (id2elem.containsKey(((ReferenceType) type).getReferenceId())) {
                                parameter.add(((Operable)id2elem.
                                        get(((ReferenceType) type).getReferenceId())).getName());
                            } else {
                                throw new MethodWrongTypeException(myClass.getName(),operationName);
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
                    throw new MethodDuplicatedException(myClass.getName(),operationName);
                }
            }
        }
        return informations;
    }

    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(className);
        return myClass.getAssociatedClassList();
    }

    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(className);
        return myClass.getTopParentClass();
    }

    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(className);
        return myClass.getImplementedInterfList();
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(className);
        return myClass.getAttributeNotHidden();
    }
}
