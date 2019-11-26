package test.br.com.wallace.softplanchallenge.gateway.resources;

import br.com.wallace.softplanchallenge.gateway.Gateway;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.RoleEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.enums.LoginTypeEnum;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.RoleRepository;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.UserRepository;
import br.com.wallace.softplanchallenge.gateway.data.model.CredentialModel;
import br.com.wallace.softplanchallenge.gateway.services.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Gateway.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsersResourceTest {
    private String path;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RoleRepository roleRepository;


    @BeforeEach
    public void init() {
        this.path = new StringBuilder("http://127.0.0.1:")
                .append(port)
                .append("/api/v1/users")
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
    public void getMeDataTest() {
        final String token = authenticationService.generateToken(CredentialModel
                .builder()
                .email("wallace_25@hotmail.com")
                .password("wallace123")
                .build()).getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<UserEntity> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/me",
                HttpMethod.GET,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getMeDataIncorrectTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        HttpEntity<UserEntity> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/me",
                HttpMethod.GET,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void getFildAllTest() {
        final String token = authenticationService.generateToken(CredentialModel
                .builder()
                .email("wallace_25@hotmail.com")
                .password("wallace123")
                .build()).getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<UserEntity> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/findAll",
                HttpMethod.GET,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
