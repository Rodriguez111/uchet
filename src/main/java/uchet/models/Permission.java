package uchet.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "permissions")
public class Permission implements Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private int id;

    @Column(name = "permission", nullable = false, unique = true)
    private String permission;

    @Column(name = "permission_description", nullable = false, unique = true)
    private String permissionDescription;

//    @ManyToMany
//    @JoinTable(name = "positions_permissions",
//            joinColumns = {@JoinColumn(name = "permission_id")},
//            inverseJoinColumns = {@JoinColumn(name = "position_id")}
//    )
//    private List<Position> positions = new ArrayList<>();


    public Permission() {
    }

    public Permission(String permissionDescription) {
        this.permissionDescription = permissionDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    public void setPermissionDescription(String permissionDescription) {
        this.permissionDescription = permissionDescription;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission authority1 = (Permission) o;
        return permission.equals(authority1.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permission);
    }

    @Override
    public int compareTo(Object o) {
        Permission permission = (Permission) o;
        return this.permission.compareTo(permission.permission);
    }
}
