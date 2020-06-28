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
@Table(name = "in_doc_types")
public class InDocType implements Cloneable, CloneableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private long id;

    @Column(name = "doc_type", length = 60, nullable = false, unique = true)
    private String docType;


    public InDocType(String docType) {
        this.docType = docType;
    }

    @Override
    public Object cloneEntity() {
        InDocType inDocType = null;
        try {
            inDocType = (InDocType) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return inDocType;
    }
}
