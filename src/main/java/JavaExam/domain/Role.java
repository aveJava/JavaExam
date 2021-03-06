package JavaExam.domain;

import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
@EqualsAndHashCode(of = "name")
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "role")
    private String name;

    // GrantedAuthority overridden method
    @Override
    public String getAuthority() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = "ROLE_" + name;
    }

}
