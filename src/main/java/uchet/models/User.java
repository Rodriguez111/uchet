package uchet.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements Cloneable, CloneableEntity {

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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_position_id", nullable = false)
    private Position position;

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

    public User() {
    }

    public User(String login, String password, String name, String surname, Position position, boolean isActive) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.isActive = isActive;
    }

    public User(String login) {
        this.login = login;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
                login.equals(user.login) &&
                password.equals(user.password) &&
                name.equals(user.name) &&
                surname.equals(user.surname) &&
                position.equals(user.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, name, surname, position, isActive);
    }

    @Override
    public User cloneEntity() {
        User user = null;
        try {
            user = (User) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return user;
    }

}
