package com.bash.Unitrack.authentication.model;

import com.bash.Unitrack.Data.models.Department;
import com.bash.Unitrack.Data.models.Location;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "app_user", discriminatorType = DiscriminatorType.STRING)
@Table(name = "app_user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    @ManyToOne
    private Department department;
    @Embedded
    private Location location;
    private String  username;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private boolean isEnabled;

    public User() {

    }

    public User(String email, String password, Role role) {
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public User(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

}