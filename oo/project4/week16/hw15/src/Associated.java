import com.oocourse.uml3.models.elements.UmlAssociationEnd;

public interface Associated {
    void addAssociatedEnd(Associated end);

    boolean equals(Object o);

    void addAssociated(UmlAssociationEnd end);
}
