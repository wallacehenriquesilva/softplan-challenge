package br.com.wallace.softplanchallenge.service.data.base.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class RoleEntity {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_description", length = 50, nullable = false)
    private String description;

    @JsonManagedReference
    @ManyToMany(mappedBy = "rolesList")
    private List<UserEntity> usersList = new ArrayList<>();

}
