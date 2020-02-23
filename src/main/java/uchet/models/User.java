package uchet.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id ")
    private int id;

    @Column(name = "user_login", length = 20, nullable = false, unique = true)
    private String login;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_name", length = 25, nullable = false)
    private String name;

    @Column(name = "user_surname", length = 25, nullable = false)
    private String surname;

    @ManyToMany
    @JoinTable(name = "positions_users",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "position_id")}
    )
    private Set<Position> positions = new HashSet<>();

    @Column(name = "user_is_active", nullable = false)
    private boolean isActive;


    @Column(name = "user_create_date", length = 19, nullable = false)
    private String createDate;

    @Column(name = "user_update_date", length = 19)
    private String lastUpdateDate;

    @Transient
    private String fullName = "";

    @PostLoad
    private void onLoad() {
        this.fullName = surname + " " + name;
    }

    public User(String login, String password, String name, String surname, boolean isActive, String createDate) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.isActive = isActive;
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(createDate, user.createDate) &&
                Objects.equals(lastUpdateDate, user.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, name, surname, isActive, createDate, lastUpdateDate);
    }
}
