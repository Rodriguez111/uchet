package uchet.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "in_doc_details")
public class InDocDetail implements Cloneable, CloneableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private long id;



    @ManyToOne
    @JoinColumn(name="in_doc_id", nullable=false)
    private InDoc inDoc;


    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    @Column(name = "serial")
    private String serial;

    @Column(name = "qty")
    private double qty;

    @Column(name = "price")
    private double price;


    @Column(name = "vat_price")
    private double vatPrice;

    @Column(name = "expire_date", length = 10)
    private String expireDate;

    @Transient
    private BigDecimal total;

    @Transient
    private BigDecimal vatTotal;


    @PostLoad
    private void onLoad() {
        this.total = new BigDecimal(qty * price).setScale(2, RoundingMode.HALF_UP);
        this.vatTotal = new BigDecimal(qty * vatPrice).setScale(2, RoundingMode.HALF_UP);
    }

    public InDocDetail(InDoc inDoc, Sku sku, double qty, double price) {
        this.inDoc = inDoc;
        this.sku = sku;
        this.qty = qty;
        this.price = price;
    }

    @Override
    public Object cloneEntity() {
        InDocDetail inDocDetail = null;
        try {
            inDocDetail = (InDocDetail) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return inDocDetail;
    }
}
