package br.com.wallace.softplanchallenge.service.data.base.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = "person_cpf"
        ), @UniqueConstraint(
                columnNames = "person_email"
        )})
public class PersonEntity implements Identifiable<Long> {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_name", length = 50, nullable = false)
    private String name;

    @Column(name = "person_email", length = 50, unique = true)
    private String email;

    @Column(name = "person_born_date", nullable = false)
    private LocalDate bornDate;

    @Column(name = "person_naturality", length = 30)
    private String naturality;

    @Column(name = "person_nacionality", length = 30)
    private String nacionality;

    @Column(name = "person_cpf", length = 14, nullable = false)
    private String cpf;

    @CreationTimestamp
    @Column(name = "person_created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "person_updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sex_id")
    private SexEntity sex;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Override
    public Long getId() {
        return id;
    }

}
