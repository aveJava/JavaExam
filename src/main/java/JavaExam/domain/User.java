package JavaExam.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "user")
@EqualsAndHashCode(of = "id")
@Getter @Setter
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private boolean enabled;

    @JoinColumn(name = "session_schema")
    @ManyToOne
    private ExamSessionSchema sessionSchema;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn (name = "user_id"),
            inverseJoinColumns = @JoinColumn (name = "authority"))
    private Set<Role> roles;

    public User() {
    }

    public User(String username, String password, ExamSessionSchema sessionSchema, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.enabled = true;
        this.sessionSchema = sessionSchema;
        this.roles = roles;
    }

    // поля html-формы

    @Transient
    private String passwordConfirm;

    @Transient
    private String newUserName;

    @Transient
    private String newPassword;

    @Transient
    private String formStage;   // стадия заполнения формы (предполагается, что сначала пользователь выбирает имя, потом пароль)
                                // new - ничего не заполнено, username - было введено имя, password - было введено имя и пароль


    public void addNewRole(Role role) {
        roles.add(role);
    }

    public void deleteRole(Role role) {
        roles.remove(role);
    }


    // UserDetails overridden methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
