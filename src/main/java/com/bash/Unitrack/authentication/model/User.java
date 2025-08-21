package com.bash.Unitrack.authentication.model;

import com.bash.Unitrack.Data.Models.Department;
import com.bash.Unitrack.Data.Models.Location;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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