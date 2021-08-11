import com.oocourse.uml3.models.elements.UmlAttribute;

public interface Operable {
    public void addAttribute(UmlAttribute umlAttribute);

    public void addOperation(MyOperation myOperation);

    public String getName();
}
