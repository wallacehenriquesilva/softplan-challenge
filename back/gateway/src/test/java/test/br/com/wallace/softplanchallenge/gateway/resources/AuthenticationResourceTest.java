package test.br.com.wallace.softplanchallenge.gateway.resources;


import br.com.wallace.softplanchallenge.gateway.Gateway;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.RoleEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.enums.LoginTypeEnum;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.RoleRepository;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.UserRepository;
import br.com.wallace.softplanchallenge.gateway.data.model.CredentialModel;
import br.com.wallace.softplanchallenge.gateway.data.model.TokenModel;
import br.com.wallace.softplanchallenge.gateway.data.model.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Gateway.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthenticationResourceTest {
    private String path;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @BeforeEach
    public void init() {
        this.path = new StringBuilder("http://127.0.0.1:")
                .append(port)
                .append("/api/v1/auth")
                .toString();


        UserEntity userEntity = UserEntity.builder()
                .email("wallace_25@hotmail.com")
                .loginType(LoginTypeEnum.DEFAULT)
                .name("wallace")
                .password("wallace123")
                .rolesList(new ArrayList<>())
                .build();

        RoleEntity roleAdminEntity = RoleEntity.builder()
                .description("ROLE_ADMIN")
                .usersList(new ArrayList<>())
                .build();

        RoleEntity roleUserEntity = RoleEntity.builder()
                .description("ROLE_USER")
                .usersList(new ArrayList<>())
                .build();

        userEntity.getRolesList().add(roleAdminEntity);
        roleAdminEntity.getUsersList().add(userEntity);

        roleRepository.saveAll(Arrays.asList(roleAdminEntity, roleUserEntity));
        userRepository.save(userEntity);
    }

    @AfterEach
    public void finalize() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void generateTokenCorrectTest() {
        CredentialModel credentialModel = CredentialModel.builder()
                .email("wallace_25@hotmail.com")
                .password("wallace123")
                .build();
        HttpEntity<CredentialModel> httpEntity = new HttpEntity<>(credentialModel);

        ResponseEntity<TokenModel> response = testRestTemplate.exchange(
                path + "/generate/token",
                HttpMethod.POST,
                httpEntity,
                TokenModel.class);

        Assertions.assertEquals(Boolean.TRUE, StringUtils.isNotBlank(response.getBody().getToken()));
    }

    @Test
    public void generateTokenInvalidCredentialsTest() {
        CredentialModel credentialModel = CredentialModel.builder()
                .email("wallace_25@hotmail.com")
                .password("wallace")
                .build();
        HttpEntity<CredentialModel> httpEntity = new HttpEntity<>(credentialModel);

        ResponseEntity<TokenModel> response = testRestTemplate.exchange(
                path + "/generate/token",
                HttpMethod.POST,
                httpEntity,
                TokenModel.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void generateTokenUserNotFoundTest() {
        CredentialModel credentialModel = CredentialModel.builder()
                .email("wallace_1685@hotmail.com")
                .password("wallace")
                .build();
        HttpEntity<CredentialModel> httpEntity = new HttpEntity<>(credentialModel);

        ResponseEntity<TokenModel> response = testRestTemplate.exchange(
                path + "/generate/token",
                HttpMethod.POST,
                httpEntity,
                TokenModel.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void signupInvalidEmailTest() {
        UserModel userModel = UserModel.builder()
                .email("wallace")
                .username("wallace")
                .password("senha")
                .build();
        HttpEntity<UserModel> httpEntity = new HttpEntity<>(userModel);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/signup",
                HttpMethod.POST,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }


    @Test
    public void signupCorrectTest() {
        UserModel userModel = UserModel.builder()
                .email("wallace_123@htomail.com")
                .username("wallace123")
                .password("senha")
                .build();
        HttpEntity<UserModel> httpEntity = new HttpEntity<>(userModel);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/signup",
                HttpMethod.POST,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void signupUserAlreadyExistsTest() {
        UserModel userModel = UserModel.builder()
                .email("wallace_25@hotmail.com")
                .username("wallace123")
                .password("senha")
                .build();
        HttpEntity<UserModel> httpEntity = new HttpEntity<>(userModel);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/signup",
                HttpMethod.POST,
                httpEntity,
                String.class);

        Assertions.assertEquals(Boolean.TRUE, response.getBody().contains("O usuário já esta cadastrado"));
    }


}
