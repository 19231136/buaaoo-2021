import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.OperationParamInformation;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.common.NamedType;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    //类图
    private HashMap<String,List<MyClass>> name2class = new HashMap<>();
    private int classCount = 0;
    //状态图
    private HashMap<String,List<MyStateMachine>> name2state = new HashMap<>();
    //顺序图
    private HashMap<String,List<MyInteraciton>> name2interaction = new HashMap<>();
    private HashMap<String,Object> id2elem = new HashMap<>();
    private HashMap<String,String> end2id = new HashMap<>();

    public MyUmlGeneralInteraction(UmlElement[] elements) {
        for (UmlElement e : elements) {
            if (e instanceof UmlClass) {
                parseUmlClass(e);
            } else if (e instanceof UmlInterface) {
                parseInterface(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlAssociationEnd) {
                parseUmlAssociationEnd(e);
            } else if (e instanceof UmlAttribute) {
                parseUmlAttribute(e);
            } else if (e instanceof UmlGeneralization) {
                parseUmlGeneralization(e);
            } else if (e instanceof UmlInterfaceRealization) {
                parseUmlInterfaceRealization(e);
            } else if (e instanceof UmlOperation) {
                parseUmlOperation(e);
            } else if (e instanceof UmlStateMachine) {
                parseUmlStateMachine(e);
            } else if (e instanceof UmlInteraction) {
                parseUmlInteraction(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlAssociation) {
                parseUmlAssociation(e);
            } else if (e instanceof UmlParameter) {
                parseUmlParameter(e);
            } else if (e instanceof UmlRegion) {
                parseUmlRegion(e);
            } else if (e instanceof UmlLifeline) {
                parseUmlLifeLine(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlState || e instanceof UmlFinalState
                    || e instanceof UmlPseudostate) {
                parseState(e);
            } else if (e instanceof UmlMessage) {
                parseUmlMessage(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlTransition) {
                parseUmlTransition(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlEvent) {
                parseUmlEvent(e);
            }
        }
    }

    public void parseUmlClass(UmlElement e) {
        classCount++;
        MyClass myClass = new MyClass((UmlClass) e);
        id2elem.put(e.getId(),myClass);
        if (name2class.containsKey(e.getName())) {
            name2class.get(e.getName()).add(myClass);
        } else {
            List<MyClass> list = new ArrayList<>();
            list.add(myClass);
            name2class.put(e.getName(),list);
        }
    }

    public void parseInterface(UmlElement e) {
        MyInterface myInterface = new MyInterface((UmlInterface) e);
        id2elem.put(e.getId(),myInterface);
    }

    public void parseUmlAssociationEnd(UmlElement e) {
        end2id.put(e.getId(),((UmlAssociationEnd) e).getReference());
    }

    public void parseUmlAttribute(UmlElement e) {
        Object obj = id2elem.get(e.getParentId());
        if (obj instanceof Operable) {
            Operable target = (Operable) obj;
            target.addAttribute((UmlAttribute) e);
        }
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

    public void parseUmlStateMachine(UmlElement e) {
        MyStateMachine stateMachine = new MyStateMachine((UmlStateMachine) e);
        id2elem.put(e.getId(),stateMachine);
        if (name2state.containsKey(stateMachine.getName())) {
            name2state.get(stateMachine.getName()).add(stateMachine);
        } else {
            List<MyStateMachine> list = new ArrayList<>();
            list.add(stateMachine);
            name2state.put(stateMachine.getName(),list);
        }
    }

    public void parseUmlInteraction(UmlElement e) {
        MyInteraciton interaciton = new MyInteraciton((UmlInteraction) e);
        id2elem.put(e.getId(),interaciton);
        if (name2interaction.containsKey(interaciton.getName())) {
            name2interaction.get(interaciton.getName()).add(interaciton);
        } else {
            List<MyInteraciton> list = new ArrayList<>();
            list.add(interaciton);
            name2interaction.put(interaciton.getName(),list);
        }
    }

    public void parseUmlAssociation(UmlElement e) {
        Object end1 = id2elem.get(end2id.get(((UmlAssociation) e).getEnd1()));
        Object end2 = id2elem.get(end2id.get(((UmlAssociation) e).getEnd2()));
        Associated e1 = (Associated) end1;
        Associated e2 = (Associated) end2;
        e1.addAssociatedEnd(e2);
        e2.addAssociatedEnd(e1);
    }

    public void parseUmlParameter(UmlElement e) {
        MyOperation operation = (MyOperation) id2elem.get(e.getParentId());
        operation.addParameters((UmlParameter) e);
    }

    public void parseUmlRegion(UmlElement e) {
        MyStateMachine machine = (MyStateMachine) id2elem.get(e.getParentId());
        MyRegion myRegion = new MyRegion((UmlRegion) e);
        id2elem.put(e.getId(),myRegion);
        machine.setRegion(myRegion);
        myRegion.setParent(machine);
    }

    public void parseUmlLifeLine(UmlElement e) {
        MyLifeline myLifeline = new MyLifeline((UmlLifeline) e);
        id2elem.put(e.getId(),myLifeline);
        MyInteraciton interaciton = (MyInteraciton) id2elem.get(e.getParentId());
        interaciton.addParticipant(myLifeline);
    }

    public void parseState(UmlElement e) {
        MyRegion myRegion = (MyRegion) id2elem.get(e.getParentId());
        MyState myState = new MyState(e);
        id2elem.put(e.getId(),myState);
        myRegion.addState(myState);
    }

    public void parseUmlMessage(UmlElement e) {
        Object obj = id2elem.get(((UmlMessage)e).getTarget());
        Object source = id2elem.get(((UmlMessage)e).getSource());
        MyInteraciton myInteraciton = (MyInteraciton) id2elem.get(e.getParentId());
        myInteraciton.addMessage((UmlMessage) e,obj);
        myInteraciton.addSentMessage((UmlMessage) e,source);
    }

    public void parseUmlTransition(UmlElement e) {
        String source = ((UmlTransition)e).getSource();
        String target = ((UmlTransition)e).getTarget();
        MyState sourceState = (MyState) id2elem.get(source);
        MyState targetstate = (MyState) id2elem.get(target);
        MyRegion myRegion = (MyRegion) id2elem.get(e.getParentId());
        myRegion.addTransition((UmlTransition) e,sourceState,targetstate);
        id2elem.put(e.getId(),e);
    }

    public void parseUmlEvent(UmlElement e) {
        UmlTransition transition = (UmlTransition) id2elem.get(e.getParentId());
        MyRegion myRegion = (MyRegion) id2elem.get(transition.getParentId());
        myRegion.addTriggers((UmlEvent) e);
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

    public MyStateMachine findStateMachine(String name)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!name2state.containsKey(name)) {
            throw new StateMachineNotFoundException(name);
        }
        List<MyStateMachine> stateMachines = name2state.get(name);
        if (stateMachines.size() > 1) {
            throw new StateMachineDuplicatedException(name);
        }
        return stateMachines.get(0);
    }

    public MyInteraciton findInteraction(String name)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2interaction.containsKey(name)) {
            throw new InteractionNotFoundException(name);
        }
        List<MyInteraciton> interacitons = name2interaction.get(name);
        if (interacitons.size() > 1) {
            throw new InteractionDuplicatedException(name);
        }
        return interacitons.get(0);
    }

    @Override
    public int getClassCount() {
        return classCount;
    }

    @Override
    public int getClassOperationCount(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getOperationCnt();
    }

    @Override
    public int getClassAttributeCount(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getAttributeCount();
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getOperationVisibility(s1);
    }

    @Override
    public Visibility getClassAttributeVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getAttrVisibility(s1);
    }

    @Override
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

    @Override
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

    @Override
    public List<String> getClassAssociatedClassList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getAssociatedClassList();
    }

    @Override
    public String getTopParentClass(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getTopParentClass();
    }

    @Override
    public List<String> getImplementInterfaceList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getImplementedInterfList();
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = findClass(s);
        return myClass.getAttributeNotHidden();
    }

    @Override
    public int getParticipantCount(String s)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyInteraciton interaciton = findInteraction(s);
        return interaciton.getParticipantCount();
    }

    @Override
    public int getIncomingMessageCount(String s, String s1)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyInteraciton interaciton = findInteraction(s);
        return interaciton.getIncomingMessageCount(s1);
    }

    @Override
    public int getSentMessageCount(String s, String s1, MessageSort messageSort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
                LifelineNotFoundException, LifelineDuplicatedException {
        MyInteraciton interaciton = findInteraction(s);
        return interaciton.getSentMessageCount(s1,messageSort);
    }

    @Override
    public int getStateCount(String s)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyStateMachine stateMachine = findStateMachine(s);
        return stateMachine.getStateCount();
    }

    @Override
    public int getSubsequentStateCount(String s, String s1)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        MyStateMachine stateMachine = findStateMachine(s);
        return stateMachine.getSubsequentStateCount(s1);
    }

    @Override
    public List<String> getTransitionTrigger(String s, String s1, String s2)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        MyStateMachine stateMachine = findStateMachine(s);
        return stateMachine.getTransitionTrigger(s1,s2);
    }
}
