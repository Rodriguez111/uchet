package uchet.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contractors")
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "contractor_code", length = 6, nullable = false, unique = true)
    private String contractorCode;

    @Column(name = "contractor_name", nullable = false)
    private String name;

    @Column(name = "is_supplier")
    private boolean isSupplier;

    @Column(name = "is_customer")
    private boolean isCustomer;

    @Column(name = "create_date", length = 19, nullable = false)
    private String createDate;

    @Column(name = "update_date", length = 19)
    private String updateDate;


    public Contractor(String contractorCode) {
        this.contractorCode = contractorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contractor that = (Contractor) o;
        return Objects.equals(contractorCode, that.contractorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractorCode);
    }
}
