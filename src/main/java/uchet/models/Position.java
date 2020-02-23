package uchet.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private int id;

    @Column(name = "position", nullable = false, unique = true)
    private String position;

    @Column(name = "position_description")
    private String positionDescription;

    @ManyToMany
    @JoinTable(name = "positions_paths",
            joinColumns = {@JoinColumn(name = "position_id")},
            inverseJoinColumns = {@JoinColumn(name = "paths_id")}
    )
    private Set<Path> paths = new HashSet<>();

    public Position() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    public Set<Path> getPaths() {
        return paths;
    }

    public void setPaths(Set<Path> paths) {
        this.paths = paths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position1 = (Position) o;
        return Objects.equals(position, position1.position) &&
                Objects.equals(positionDescription, position1.positionDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, positionDescription);
    }
}
