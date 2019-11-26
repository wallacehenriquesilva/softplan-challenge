package br.com.wallace.softplanchallenge.service.resources.hateoas.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
@EqualsAndHashCode
public class PersonHateoasModel extends ResourceSupport {
    private Long id;
    @Getter
    private String name;
    @Getter
    private String email;
    @Getter
    private LocalDate bornDate;
    @Getter
    private String naturality;
    @Getter
    private String nacionality;
    @Getter
    private String cpf;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private LocalDateTime updatedAt;
    @Getter
    private Long sex;
}