package uchet.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "in_docs")
public class InDoc implements Cloneable, CloneableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private long id;

    @Column(name = "doc_number", length = 10, nullable = false, unique = true)
    private String docNumber;

    @Column(name = "doc_date", length = 19)
    private String docDate;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "doc_type_id", nullable = false)
    private InDocType type;


    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "contractor_id", nullable = false)
    private Contractor contractor;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    @OneToMany(mappedBy="inDoc")
    private List<InDocDetail> details = new ArrayList<>();

    public InDoc(String docNumber, Contractor contractor, User owner, InDocType type) {
        this.docNumber = docNumber;
        this.contractor = contractor;
        this.owner = owner;
        this.type = type;
    }

    public InDoc(InDocType type) {
        this.type = type;
    }

    public InDoc(Contractor contractor) {
        this.contractor = contractor;
    }

    @Override
    public Object cloneEntity() {
        InDoc inDoc = null;
        try {
            inDoc = (InDoc) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return inDoc;
    }
}
