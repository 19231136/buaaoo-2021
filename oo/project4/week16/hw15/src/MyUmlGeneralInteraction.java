import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.OperationParamInformation;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.PreCheckRuleException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.format.UmlGeneralInteraction;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    //类图
    private ClassDiagram classDiagram;
    //状态图
    private StateDiagram stateDiagram;
    //顺序图
    private SequenceDiagram sequenceDiagram;
    private HashMap<String,Object> id2elem = new HashMap<>();

    public MyUmlGeneralInteraction(UmlElement[] elements) {
        classDiagram = new ClassDiagram(id2elem);
        stateDiagram = new StateDiagram(id2elem);
        sequenceDiagram = new SequenceDiagram(id2elem);
        for (UmlElement e : elements) {
            if (e instanceof UmlClass) {
                classDiagram.parseUmlClass(e);
            } else if (e instanceof UmlInterface) {
                classDiagram.parseInterface(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlAssociationEnd) {
                classDiagram.parseUmlAssociationEnd(e);
            } else if (e instanceof UmlAttribute) {
                classDiagram.parseUmlAttribute(e);
            } else if (e instanceof UmlGeneralization) {
                classDiagram.parseUmlGeneralization(e);
            } else if (e instanceof UmlInterfaceRealization) {
                classDiagram.parseUmlInterfaceRealization(e);
            } else if (e instanceof UmlOperation) {
                classDiagram.parseUmlOperation(e);
            } else if (e instanceof UmlStateMachine) {
                stateDiagram.parseUmlStateMachine(e);
            } else if (e instanceof UmlInteraction) {
                sequenceDiagram.parseUmlInteraction(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlAssociation) {
                classDiagram.parseUmlAssociation(e);
            } else if (e instanceof UmlParameter) {
                classDiagram.parseUmlParameter(e);
            } else if (e instanceof UmlRegion) {
                stateDiagram.parseUmlRegion(e);
            } else if (e instanceof UmlLifeline) {
                sequenceDiagram.parseUmlLifeLine(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlState || e instanceof UmlFinalState
                    || e instanceof UmlPseudostate) {
                stateDiagram.parseState(e);
            } else if (e instanceof UmlMessage) {
                sequenceDiagram.parseUmlMessage(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlTransition) {
                stateDiagram.parseUmlTransition(e);
            }
        }
        for (UmlElement e : elements) {
            if (e instanceof UmlEvent) {
                stateDiagram.parseUmlEvent(e);
            }
        }
    }

    @Override
    public int getClassCount() {
        return classDiagram.getClassCount();
    }

    @Override
    public int getClassOperationCount(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationCount(s);
    }

    @Override
    public int getClassAttributeCount(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassAttributeCount(s);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationVisibility(s,s1);
    }

    @Override
    public Visibility getClassAttributeVisibility(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return classDiagram.getClassAttributeVisibility(s,s1);
    }

    @Override
    public String getClassAttributeType(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        return classDiagram.getClassAttributeType(s,s1);
    }

    @Override
    public List<OperationParamInformation> getClassOperationParamType(String s, String s1)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return classDiagram.getClassOperationParamType(s,s1);
    }

    @Override
    public List<String> getClassAssociatedClassList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassAssociatedClassList(s);
    }

    @Override
    public String getTopParentClass(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getTopParentClass(s);
    }

    @Override
    public List<String> getImplementInterfaceList(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getImplementInterfaceList(s);
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String s)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getInformationNotHidden(s);
    }

    @Override
    public int getParticipantCount(String s)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return sequenceDiagram.getParticipantCount(s);
    }

    @Override
    public int getIncomingMessageCount(String s, String s1)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return sequenceDiagram.getIncomingMessageCount(s,s1);
    }

    @Override
    public int getSentMessageCount(String s, String s1, MessageSort messageSort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return sequenceDiagram.getSentMessageCount(s,s1,messageSort);
    }

    @Override
    public int getStateCount(String s)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateDiagram.getStateCount(s);
    }

    @Override
    public int getSubsequentStateCount(String s, String s1)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return stateDiagram.getSubsequentStateCount(s,s1);
    }

    @Override
    public List<String> getTransitionTrigger(String s, String s1, String s2)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return stateDiagram.getTransitionTrigger(s,s1,s2);
    }

    @Override
    public void checkForAllRules() throws PreCheckRuleException {
        checkForUml001();
        checkForUml002();
        checkForUml003();
        checkForUml004();
        checkForUml005();
        checkForUml006();
        checkForUml007();
        checkForUml008();
    }

    @Override
    public void checkForUml001() throws UmlRule001Exception {
        classDiagram.checkForUml001();
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        classDiagram.checkForUml002();
    }

    @Override
    public void checkForUml003() throws UmlRule003Exception {
        classDiagram.checkForUml003();
    }

    @Override
    public void checkForUml004() throws UmlRule004Exception {
        classDiagram.checkForUml004();
    }

    @Override
    public void checkForUml005() throws UmlRule005Exception {
        classDiagram.checkForUml005();
    }

    @Override
    public void checkForUml006() throws UmlRule006Exception {
        classDiagram.checkForUml006();
    }

    @Override
    public void checkForUml007() throws UmlRule007Exception {
        sequenceDiagram.checkForUml007();
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        stateDiagram.checkForUml008();
    }
}
