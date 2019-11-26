package br.com.wallace.softplanchallenge.service.services;


import br.com.wallace.softplanchallenge.service.data.base.entities.PersonEntity;
import br.com.wallace.softplanchallenge.service.data.base.entities.RoleEntity;
import br.com.wallace.softplanchallenge.service.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.service.data.base.repositories.PersonRepository;
import br.com.wallace.softplanchallenge.service.data.exceptions.*;
import br.com.wallace.softplanchallenge.service.data.model.ApiResponseModel;
import br.com.wallace.softplanchallenge.service.data.model.PersonModel;
import br.com.wallace.softplanchallenge.service.data.validations.DataValidation;
import br.com.wallace.softplanchallenge.service.resources.hateoas.mapper.PersonMapper;
import br.com.wallace.softplanchallenge.service.resources.hateoas.model.PersonHateoasModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Classe de serviço de controle de pessoas.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final SexService sexService;
    private final UserService userService;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper, SexService sexService, UserService userService) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.sexService = sexService;
        this.userService = userService;
    }

    /**
     * <p>
     * Método responsável por realizar a busca de todoas as pessoas. Caso o usuário seja um administrador, ele receberá
     * todos os registros da base, caso não, será retornado apenas os seus registros.
     * </p>
     *
     * @param userEmail Email do usuário que solicitou a busca.
     * @return Retorna uma Collection contendo todas as pessoas encontradas para aquele usuário.
     */
    public Collection<PersonHateoasModel> findAll(String userEmail) {
        UserEntity userEntity = userService.findUserByEmail(userEmail);
        List<PersonEntity> personsList = userEntity
                .getRolesList()
                .stream()
                .map(RoleEntity::getDescription)
                .filter("ROLE_ADMIN"::equalsIgnoreCase)
                .findFirst()
                .map(role -> personRepository.findAll())
                .orElseGet(() -> personRepository.findAllByUser(userEntity.getId()));
        return personMapper.collectionToHateoas(personsList);
    }

    /**
     * <p>
     * Método responsável por realizar a busca de pessoa por id. Primeiro, valida se o usuário tem permissão
     * a acessar aquela pessoa, caso não, causa uma PersonNotPermitedForUserException, caso sim, retorna a pessoa
     * encontrada.
     * </p>
     *
     * @param id        Id da pessoa a ser consultada.
     * @param userEmail Email do usuário que esta realizando a consulta.
     * @return Retorna o model já com o controle de Hypermedia.
     */
    public PersonHateoasModel findById(Long id, String userEmail) {
        if (this.validateUserForPerson(userEmail, id)) {
            return personRepository.findById(id)
                    .map(personMapper::getHateoas)
                    .orElseThrow(PersonNotFoundException::new);
        } else {
            throw new PersonNotPermitedForUserException();
        }

    }

    /**
     * <p>
     * Método responsável por criar uma nova pessoa. Primeiro, valida os dados, depois, transforma o model em
     * entity para que a pessoa seja salva, e por fim, realiza a adição dos links para o controle de Hypermedia.
     * </p>
     *
     * @param personModel Model com os dados da nova pessoa.
     * @param userEmail   Email do usuário que solicitou a criação.
     * @return Retorna o model já com o controle de Hypermedia.
     */
    public PersonHateoasModel create(PersonModel personModel, String userEmail) {
        return Optional.ofNullable(personModel)
                .map(person -> this.requestModelToEntity(person, userEmail))
                .map(this::savePerson)
                .map(personMapper::getHateoas)
                .orElseThrow(PersonCreationException::new);
    }

    private PersonEntity savePerson(PersonEntity personEntity) {
        try {
            return personRepository.save(personEntity);
        } catch (DataIntegrityViolationException e) {
            if (e.getRootCause().toString().contains("@")) {
                throw new DuplicatedEmailException();
            } else {
                throw new DuplicatedCpfException();
            }
        }
    }

    /**
     * <p>
     * Método responsável por deletar uma pessoa. Primeiro, valida se o usuário possui acesso a aquela pessoa, caso
     * sim, apaga o registro da pessoa.
     * </p>
     *
     * @param id        Id da pessoa que será apagada.
     * @param userEmail Email do usuário que solicitou a remoção.
     * @return Retorna a mensagem de que o usuário foi apagado em um ApiRespondeModel, ou extoura uma exception
     * caso não consiga deletar.
     */
    public ApiResponseModel delete(Long id, String userEmail) {
        return Optional.of(id)
                .filter(i -> this.validateUserForPerson(userEmail, i))
                .map(idd -> {
                    personRepository.deleteById(idd);
                    return ApiResponseModel
                            .builder()
                            .timestamp(new Date().getTime())
                            .status("OK")
                            .message("Pessoa apagada com sucesso")
                            .build();
                }).orElseThrow(PersonDeleteException::new);
    }

    /**
     * <p>
     * Método responsável por atualizar uma entidade pessoa. Primeiro, ele recebe em seu body um model contendo
     * os novos dados da pessoa, e também, como parâmetro de caminho, o id da pessoa, e seu email no header. Valida
     * se o usuário tem acesso a aquela pessoa, caso sim, segue com a transformação do model em uma entidade, depois
     * realiza o update e mapeia os links de retorno.
     * </p>
     *
     * @param id          Id da pessoa que será alterada.
     * @param personModel Model com os novos dados da pessoa.
     * @param userEmail   Email do usuário que solicitou a alteração.
     * @return Retorna o model já com o controle de Hypermedia.
     */
    public PersonHateoasModel update(Long id, PersonModel personModel, String userEmail) {
        return Optional.ofNullable(id)
                .map(this::findPersonById)
                .map(person -> this.requestModelToEntity(person, personModel, userEmail))
                .map(personRepository::save)
                .map(personMapper::getHateoas)
                .orElseThrow(PersonUpdateException::new);
    }

    /**
     * <p>
     * Método responsável por validar se um determinado usuário possui permissões sobre uma pessoa.
     * Caso sim, retorna true, caso não, causa uma PersonNotPermitedForUserException para ser tratada
     * pelo exceptionHandler.
     * </p>
     *
     * @param userEmail Email do usuário que solicitou a requisição.
     * @param id        Id da pessoa que será validada.
     * @return Caso possua permissão, retorna true.
     */
    private boolean validateUserForPerson(String userEmail, Long id) {
        return Optional.ofNullable(id)
                .map(this::findPersonById)
                .map(PersonEntity::getUser)
                .map(UserEntity::getEmail)
                .filter(email -> userEmail.equalsIgnoreCase(email) || this.isAdmin(userEmail))
                .map(Objects::nonNull)
                .orElseThrow(PersonNotPermitedForUserException::new);
    }

    /**
     * <p>
     * Método responsável por validar se o email do usuário que solicitou a requisição pertence ao grupo
     * de administradores.
     * </p>
     *
     * @param email Email que será validado.
     * @return Retorna true caso ele for uma administrador e false caso não.
     */
    private boolean isAdmin(final String email) {
        return Optional.ofNullable(email)
                .map(userService::findUserByEmail)
                .map(UserEntity::getRolesList)
                .map(roleEntities -> roleEntities
                        .stream()
                        .anyMatch(roleEntity -> roleEntity.getDescription().equalsIgnoreCase("ROLE_ADMIN")))
                .orElse(false);
    }

    /**
     * <p>
     * Método responsável por realizar a busca de pessoa por id, caso não encontre
     * causa uma PersonNotFoundException.
     * </p>
     *
     * @param id Id da pessoa que será buscada.
     * @return Retorna a entidade da pessoa encontrada.
     */
    private PersonEntity findPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(PersonNotFoundException::new);
    }

    /**
     * <p>
     * Método responsável por transformar um model de pessoa que veio no body da requisição em uma nova
     * entidade pessoa.
     * </p>
     *
     * @param personModel Model advindo do corpo da requisição.
     * @param userEmail   Email do usuário que solicitou a requisição.
     * @return Retorna a nova entidade montada com base no model pessoa.
     */
    private PersonEntity requestModelToEntity(PersonModel personModel, String userEmail) {
        this.personModelValidation(personModel);
        return PersonEntity
                .builder()
                .bornDate(personModel.getBornDate())
                .cpf(personModel.getCpf())
                .email(personModel.getEmail())
                .name(personModel.getName())
                .nacionality(personModel.getNacionality())
                .sex(sexService.findById(personModel.getSex()))
                .naturality(personModel.getNaturality())
                .user(userService.findUserByEmail(userEmail))
                .build();
    }

    /**
     * <p>
     * Método responsável por settar os valores de um model vindo do body da requisição em uma entidade do tipo Pessoa
     * previamente encontrada e recebida por parametro.
     * </p>
     *
     * @param personEntity Entidade da pessoa a qual serão settados os valores do model.
     * @param personModel  Model advindo do corpo da requisição.
     * @param userEmail    Email do usuário que solicitou a requisição.
     * @return Retorna a entidade Pessoa já com os novos dados settados.
     */
    private PersonEntity requestModelToEntity(PersonEntity personEntity, PersonModel personModel, String userEmail) {
        this.personModelValidation(personModel);

        personEntity.setBornDate(personModel.getBornDate());
        personEntity.setCpf(personModel.getCpf());
        personEntity.setEmail(personModel.getEmail());
        personEntity.setName(personModel.getName());
        personEntity.setNacionality(personModel.getNacionality());
        personEntity.setSex(sexService.findById(personModel.getSex()));
        personEntity.setNaturality(personModel.getNaturality());
        personEntity.setUser(userService.findUserByEmail(userEmail));
        return personEntity;
    }

    /**
     * <p>
     * Método responsável por validar os dados de uma pessoa, se seu cpf e email são válidos.
     * </p>
     *
     * @param personModel Model da pessoa que será validada.
     */
    private void personModelValidation(PersonModel personModel) {
        DataValidation.isValidCpf(personModel.getCpf());
        Optional.of(personModel)
                .filter(person -> DataValidation.isValidCpf(person.getCpf()))
                .map(PersonModel::getEmail)
                .filter(StringUtils::isNotBlank)
                .ifPresent(DataValidation::isValidEmail);
    }
}