package uchet.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "out_doc_types")
public class OutDocType implements Cloneable, CloneableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private long id;



    @Column(name = "doc_type", length = 60, nullable = false, unique = true)
    private String docType;




    @Override
    public Object cloneEntity() {
        OutDocType outDocType = null;
        try {
            outDocType = (OutDocType) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return outDocType;
    }
}
