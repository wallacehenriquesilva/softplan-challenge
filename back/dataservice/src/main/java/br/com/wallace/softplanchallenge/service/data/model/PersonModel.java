package br.com.wallace.softplanchallenge.service.data.model;

import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;


@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PersonModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String name;

    private String email;

    @NotNull
    private LocalDate bornDate;

    @CPF
    @NotEmpty
    private String cpf;

    private Long sex;
    private String naturality;
    private String nacionality;
}