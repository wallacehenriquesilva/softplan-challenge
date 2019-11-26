package br.com.wallace.softplanchallenge.gateway.data.base.entities;


import br.com.wallace.softplanchallenge.gateway.data.base.enums.LoginTypeEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = "user_email"
        )})
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 50, nullable = false)
    private String name;

    @JsonIgnore
    @Column(name = "user_password", length = 255, nullable = false)
    private String password;

    @Column(name = "user_email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "user_picture_path", length = 200, nullable = true)
    private String picturePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_login_type", nullable = false)
    private LoginTypeEnum loginType;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<LogEntity> logsSet;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<PersonEntity> personsSet;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> rolesList = new ArrayList<>();
}
