package uchet.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "positions")
public class Position implements Cloneable, CloneableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private int id;

    @Column(name = "position", nullable = false, unique = true)
    private String position;

    @Column(name = "position_description")
    private String positionDescription;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "positions_permissions",
            joinColumns = {@JoinColumn(name = "position_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")}
    )
    private List<Permission> permissions = new ArrayList<>();

    public Position() {
    }

//    public Position(String position, String positionDescription, List<Permission> permissions) {
//        this.position = position;
//        this.positionDescription = positionDescription;
//        this.permissions = permissions;
//    }

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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = true;
        if (this == o) {
            result = true;
        } else if (o == null || getClass() != o.getClass()) {
            result = false;
        } else {
            Position position1 = (Position) o;
            if (!Objects.equals(position, position1.position)) {
                result = false;
            } else if (!Objects.equals(positionDescription, position1.positionDescription)) {
                result = false;
            } else {
                if (permissions.size() != position1.permissions.size()) {
                    result = false;
                } else {
                    permissions.sort(Comparator.comparing(Permission::getPermission));
                    position1.permissions.sort(Comparator.comparing(Permission::getPermission));

                    for (int i = 0; i < permissions.size(); i++) {
                        if (!permissions.get(i).equals(position1.permissions.get(i))) {
                            result = false;
                            break;
                        }
                    }

                }
            }
        }
        return  result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, positionDescription, permissions);
    }

    @Override
    public Object cloneEntity() {
        Position position = null;
        try {
            position = (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return position;
    }
}
