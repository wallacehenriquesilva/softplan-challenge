package br.com.wallace.softplanchallenge.service.resources.hateoas.mapper;


import br.com.wallace.softplanchallenge.service.data.base.entities.PersonEntity;
import br.com.wallace.softplanchallenge.service.resources.hateoas.model.PersonHateoasModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Classe de controle de hateoas, onde são mapeados os links de referência da pessoa para controle de hypermedia.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@Component
public class PersonMapper {

    @Value("${server.port}")
    private String defaultPort;

    private final EntityLinks entityLinks;
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";

    public PersonMapper(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    /**
     * <p>
     * Método responsável por configurar os links de hypermedia da entidade de uma pessoa.
     * Ele transforma a epssoa no tipo de retorno com Hateoas, e adiciona os links.
     * </p>
     *
     * @param personEntity Entidade da pessoa que será transformada.
     * @return Retorna o model de pessoa pronto, já com os links.
     */
    public PersonHateoasModel getHateoas(PersonEntity personEntity) {
        PersonHateoasModel personModel = PersonHateoasModel.builder()
                .id(personEntity.getId())
                .bornDate(personEntity.getBornDate())
                .cpf(personEntity.getCpf())
                .createdAt(personEntity.getCreatedAt())
                .email(personEntity.getEmail())
                .nacionality(personEntity.getNacionality())
                .name(personEntity.getName())
                .naturality(personEntity.getNaturality())
                .sex(personEntity.getSex().getId())
                .updatedAt(personEntity.getUpdatedAt())
                .build();
        final Link selfLink = entityLinks.linkToSingleResource(personEntity);
        personModel.add(selfLink.withSelfRel());
        personModel.add(selfLink.withRel(UPDATE));
        personModel.add(selfLink.withRel(DELETE));
        return personModel;
    }

    /**
     * <p>
     * Método responsável por transformar uma collection de entidades de pessoa em uma collection de models
     * com hateos, contendo seus devidos links.
     * Para cada entidade, é chamado o método getHateoas, que gera o model com os links.
     * </p>
     *
     * @param personsList Colleciton das entidades de pessoas que serão transformadas.
     * @return Retorna uma collection contendo os models pessoa pronto, já com os links.
     */
    public Collection<PersonHateoasModel> collectionToHateoas(Collection<PersonEntity> personsList) {
        return personsList.stream()
                .map(this::getHateoas)
                .collect(Collectors.toList());
    }
}
