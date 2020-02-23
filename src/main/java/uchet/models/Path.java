package uchet.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "paths")
public class Path {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private int id;

    @Column(name = "path", nullable = false, unique = true)
    private String path;

    @Column(name = "path_description", nullable = false, unique = true)
    private String pathDescription;

    @ManyToMany
    @JoinTable(name = "positions_paths",
            joinColumns = {@JoinColumn(name = "authority_id")},
            inverseJoinColumns = {@JoinColumn(name = "position_id")}
    )
    private Set<Position> positions = new HashSet<>();

    public Path() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public String getPathDescription() {
        return pathDescription;
    }

    public void setPathDescription(String pathDescription) {
        this.pathDescription = pathDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path authority1 = (Path) o;
        return path.equals(authority1.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
