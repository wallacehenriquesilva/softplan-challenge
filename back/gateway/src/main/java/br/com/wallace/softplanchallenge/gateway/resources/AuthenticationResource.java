package br.com.wallace.softplanchallenge.gateway.resources;

import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.data.model.CredentialModel;
import br.com.wallace.softplanchallenge.gateway.data.model.TokenModel;
import br.com.wallace.softplanchallenge.gateway.data.model.UserModel;
import br.com.wallace.softplanchallenge.gateway.services.AuthenticationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Classe de endpoints de autenticacao
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@RestController
@RequestMapping(value = "/api/v1/auth",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class AuthenticationResource {
    @Autowired
    private AuthenticationManager authenticationManager;

    private final AuthenticationService authenticationService;

    public AuthenticationResource(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation("Endpoint de geração do tokens.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Token gerado com sucesso. Retorna o token gerado."),
            @ApiResponse(code = 400, message = "As informações de autenticação estão inválidas."),
            @ApiResponse(code = 403, message = "Usuário e/ou senha inválidos."),
            @ApiResponse(code = 404, message = "Usuário não encontrado na base de dados"),
            @ApiResponse(code = 406, message = "O email informado não é válido")
    })
    @PostMapping("/generate/token")
    public ResponseEntity<TokenModel> generateToken(@Valid @RequestBody CredentialModel credentialModel) {
        return ResponseEntity.ok(authenticationService.generateToken(credentialModel));
    }

    @ApiOperation("Endpoint de cadastro de usuários.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuário cadastrado com sucesso."),
            @ApiResponse(code = 400, message = "As informações de criação de usuário estão inválidas."),
            @ApiResponse(code = 403, message = "Usuário já cadastrado, não é possível cadastrar novamente."),
            @ApiResponse(code = 406, message = "O email informado para cadastro não é válido")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signup(@Valid @RequestBody UserModel userModel) {
        return ResponseEntity.status(201).body(authenticationService.signup(userModel));
    }
}
