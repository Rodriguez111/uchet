package uchet.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sku")
public class Sku implements Cloneable, CloneableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "sku_code", length = 6, nullable = false, unique = true)
    private String code;

    @Column(name = "sku_name", nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "sku_unit_id", nullable = false)
    private Unit unit;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "contractor_id", nullable = false)
    private Contractor contractor;

    @Column(name = "qty_in_sec_pkg")
    private int quantityInSecondaryPackaging;

    @Column(name = "sku_is_active", nullable = false)
    private boolean isActive;

    @Transient
    private double vatValue;


    public Sku() {
    }

    public Sku(String code, String name, Unit unit, Contractor contractor, int quantityInSecondaryPackaging, boolean isActive) {
        this.code = code;
        this.name = name;
        this.unit = unit;
        this.contractor = contractor;
        this.quantityInSecondaryPackaging = quantityInSecondaryPackaging;
        this.isActive = isActive;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public int getQuantityInSecondaryPackaging() {
        return quantityInSecondaryPackaging;
    }

    public void setQuantityInSecondaryPackaging(int quantityInSecondaryPackaging) {
        this.quantityInSecondaryPackaging = quantityInSecondaryPackaging;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getVatValue() {
        return vatValue;
    }

    public void setVatValue(double vatValue) {
        this.vatValue = vatValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return quantityInSecondaryPackaging == sku.quantityInSecondaryPackaging &&
                isActive == sku.isActive &&
                Objects.equals(code, sku.code) &&
                Objects.equals(name, sku.name) &&
                Objects.equals(unit, sku.unit) &&
                Objects.equals(contractor, sku.contractor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, unit, contractor, quantityInSecondaryPackaging, isActive);
    }

    @Override
    public Sku cloneEntity() {
        Sku sku = null;
        try {
            sku = (Sku) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return sku;
    }

}
