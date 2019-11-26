package br.com.wallace.softplanchallenge.gateway.services;


import br.com.wallace.softplanchallenge.gateway.config.security.jwt.Jwt;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.RoleEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.enums.LoginTypeEnum;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.RoleRepository;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.UserRepository;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.InvalidCredentialsException;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.UserAlreadyExistsException;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.UserNotFoundException;
import br.com.wallace.softplanchallenge.gateway.data.model.CredentialModel;
import br.com.wallace.softplanchallenge.gateway.data.model.TokenModel;
import br.com.wallace.softplanchallenge.gateway.data.model.UserModel;
import br.com.wallace.softplanchallenge.gateway.data.validations.DataValidation;
import br.com.wallace.softplanchallenge.gateway.utils.CryptoUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Classe de serviço de controle de autenticação e criação de usuários.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@Service
public class AuthenticationService {

    private static final String DEFAULT_PICTURE_PATH = "https://upload.wikimedia.org/wikipedia/commons/2/2b/WelshCorgi.jpeg";
    private final Jwt jwt;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;


    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, Jwt jwt, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwt = jwt;
        this.userService = userService;
    }

    /**
     * <p>
     * Método responsável por gerar o token para um usuário.
     * Primeiramente, valida se o email é valido, depois, se o usuário é  válido, logo após, se o mesmo esta com a
     * senha correta, caso ambas validações obtenham sucesso, então é gerado o token para o usuário solicitado.
     * </p>
     *
     * @param credentialModel Credenciais do usuário que será validado para a geração do token.
     * @return Retorna o model de token contendo o token gerado.
     */
    public TokenModel generateToken(CredentialModel credentialModel) {
        Predicate<UserEntity> userPasswordPredicate = user ->
                CryptoUtils.validateHash(credentialModel.getPassword(), user.getPassword());

        return Optional.of(credentialModel)
                .map(credential -> userService.findUserByEmail(credential.getEmail()))
                .filter(credential -> DataValidation.isValidEmail(credential.getEmail()))
                .filter(userPasswordPredicate)
                .map(jwt::generateToken)
                .map(TokenModel::new)
                .orElseThrow(InvalidCredentialsException::new);
    }

    /**
     * <p>
     * Método responsável por criar novos usuários.
     * Primeiramente é validado se o email é válido e não esta em uso por nenhum outro usuário cadastrado, caso esteja,
     * retorna uma UserAlreadyExistsException, caso o email não esteja sendo utilizado por ninguém, então, é criado um
     * novo usuário.
     * Todos os usuários criados por esse método possuel por default a permissão de 'ROLE_USER', que lhe da a permissão
     * de manipular e visualizar apenas os seus próprios registros.
     * </p>
     *
     * @param userModel Model com os dados do usuário que será criado.
     * @return Retorna e entidade do novo usuário criado.
     */
    public UserEntity signup(UserModel userModel) {
        DataValidation.isValidEmail(userModel.getEmail());
        if (userRepository.findFirstByEmail(userModel.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        UserEntity userEntity = UserEntity.builder()
                .email(userModel.getEmail())
                .loginType(LoginTypeEnum.DEFAULT)
                .name(userModel.getUsername())
                .password(CryptoUtils.generateHash(userModel.getPassword()))
                .picturePath(DEFAULT_PICTURE_PATH)
                .rolesList(Collections.singletonList(roleRepository.findFirstByDescription("ROLE_USER").orElseThrow(UserNotFoundException::new)))
                .build();
        return userRepository.save(userEntity);
    }

    @PostConstruct
    private void init() {
        if (roleRepository.findAll().isEmpty()) {
            RoleEntity roleEntityAdmin = RoleEntity
                    .builder()
                    .description("ROLE_ADMIN")
                    .build();


            RoleEntity roleEntityUser = RoleEntity
                    .builder()
                    .description("ROLE_USER")
                    .build();


            roleRepository.saveAll(Arrays.asList(roleEntityAdmin, roleEntityUser));
        }
    }
}