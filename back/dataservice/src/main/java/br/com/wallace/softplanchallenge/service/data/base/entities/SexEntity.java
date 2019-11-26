package br.com.wallace.softplanchallenge.service.data.base.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Setter
@Getter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sex",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = "sex_reference"
        )})
public class SexEntity {
    @Id
    @Column(name = "sex_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sex_name", length = 10, nullable = false)
    private String name;

    @Column(name = "sex_reference", length = 1, nullable = false)
    private String reference;

    @JsonIgnore
    @OneToMany(mappedBy = "sex")
    private Set<PersonEntity> personsSet;
}
