package uchet.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "units")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private int id;

    @Column(name = "unit", nullable = false, unique = true)
    private String unit;

    @Column(name = "description")
    private String description;

    public Unit() {
    }

    public Unit(String unit, String description) {
        this.unit = unit;
        this.description = description;
    }

    public Unit(String unit) {
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit1 = (Unit) o;
        return unit.equals(unit1.unit) &&
                description.equals(unit1.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit, description);
    }
}
