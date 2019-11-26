package br.com.wallace.softplanchallenge.service.resources;


import br.com.wallace.softplanchallenge.service.data.base.entities.PersonEntity;
import br.com.wallace.softplanchallenge.service.data.model.ApiResponseModel;
import br.com.wallace.softplanchallenge.service.data.model.PersonModel;
import br.com.wallace.softplanchallenge.service.resources.hateoas.model.PersonHateoasModel;
import br.com.wallace.softplanchallenge.service.services.PersonService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Classe de endpoints de gerenciamento de pessoas.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@RestController
@ExposesResourceFor(PersonEntity.class)
@RequestMapping(value = "/api/v1/service/data/persons",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonResource {

    private final PersonService personService;

    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @ApiOperation("Endpoint de busca de todas as pessoas.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Busca por todos os usuários realizada com sucesso."),
            @ApiResponse(code = 404, message = "Usuário não encontrado para a operação.")
    })
    @GetMapping("/")
    public ResponseEntity<Collection<PersonHateoasModel>> findAll(@RequestHeader String userEmail) {
        return ResponseEntity.ok(personService.findAll(userEmail));
    }

    @ApiOperation("Endpoint de busca de pessoa por id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pessoa encontrada com successo."),
            @ApiResponse(code = 403, message = "Você não tem permissão para visualizar este usuário."),
            @ApiResponse(code = 404, message = "Pesosa não encontrado ou usuário não encontrado para a operação")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonHateoasModel> findById(@PathVariable Long id,
                                                       @RequestHeader String userEmail) {
        return ResponseEntity.ok(personService.findById(id, userEmail));
    }

    @ApiOperation("Endpoint de criação de pessoa.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pessoa criada com sucesso."),
            @ApiResponse(code = 404, message = "Erro ao criar pessoa."),
            @ApiResponse(code = 406, message = "Cpf e/ou email inválidos para a pessoa.")
    })
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonHateoasModel> create(@RequestBody PersonModel personModel,
                                                     @RequestHeader String userEmail) {
        return new ResponseEntity<>(personService.create(personModel, userEmail), HttpStatus.CREATED);
    }

    @ApiOperation("Endpoint de remoção de pessoa.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuário removido com sucesso."),
            @ApiResponse(code = 404, message = "Erro ao remover usuário."),
            @ApiResponse(code = 403, message = "Você não tem permissão para remover este usuário.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseModel> delete(@PathVariable Long id,
                                                   @RequestHeader String userEmail) {
        return ResponseEntity.ok(personService.delete(id, userEmail));
    }

    @ApiOperation("Endpoint de alteração de pessoa.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pessoa alterado com sucesso."),
            @ApiResponse(code = 403, message = "O usuário não tem permissão para alterar este registro."),
            @ApiResponse(code = 404, message = "Não foi possível alterar a pessoa."),
            @ApiResponse(code = 406, message = "Cpf e/ou email inválidos para a pessoa.")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonHateoasModel> update(@PathVariable Long id, @RequestBody PersonModel personModel,
                                                     @RequestHeader String userEmail) {
        return ResponseEntity.ok(personService.update(id, personModel, userEmail));
    }
}
