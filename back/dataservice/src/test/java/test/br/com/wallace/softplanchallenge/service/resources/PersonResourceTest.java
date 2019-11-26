package test.br.com.wallace.softplanchallenge.service.resources;

import br.com.wallace.softplanchallenge.service.DataService;
import br.com.wallace.softplanchallenge.service.data.base.entities.PersonEntity;
import br.com.wallace.softplanchallenge.service.data.base.entities.RoleEntity;
import br.com.wallace.softplanchallenge.service.data.base.entities.SexEntity;
import br.com.wallace.softplanchallenge.service.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.service.data.base.enums.LoginTypeEnum;
import br.com.wallace.softplanchallenge.service.data.base.repositories.PersonRepository;
import br.com.wallace.softplanchallenge.service.data.base.repositories.RoleRepository;
import br.com.wallace.softplanchallenge.service.data.base.repositories.SexRepository;
import br.com.wallace.softplanchallenge.service.data.base.repositories.UserRepository;
import br.com.wallace.softplanchallenge.service.data.model.PersonModel;
import br.com.wallace.softplanchallenge.service.services.PersonService;
import br.com.wallace.softplanchallenge.service.services.SexService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
@FixMethodOrder(MethodSorters.JVM)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DataService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonResourceTest {
    private String path;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SexRepository sexRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private SexService sexService;


    @BeforeEach
    public void init() {
        this.path = new StringBuilder("http://127.0.0.1:")
                .append(port)
                .append("/api/v1/service/data/persons")
                .toString();


        UserEntity userAdminEntity = UserEntity.builder()
                .email("wallace_25@hotmail.com")
                .loginType(LoginTypeEnum.DEFAULT)
                .name("wallace")
                .password("wallace123")
                .rolesList(new ArrayList<>())
                .build();

        UserEntity userUserEntity = UserEntity.builder()
                .email("wallace_52@hotmail.com")
                .loginType(LoginTypeEnum.DEFAULT)
                .name("wallace2")
                .password("wallace1231")
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

        userAdminEntity.getRolesList().add(roleAdminEntity);
        roleAdminEntity.getUsersList().add(userAdminEntity);

        userUserEntity.getRolesList().add(roleUserEntity);
        roleUserEntity.getUsersList().add(userUserEntity);


        SexEntity sexEntity = SexEntity.builder()
                .name("Masculino")
                .reference("M")
                .personsSet(Collections.emptySet())
                .build();

        PersonEntity personEntity = PersonEntity
                .builder()
                .bornDate(LocalDate.now())
                .cpf("435.568.954-89")
                .email("wallace_25@hotmail.com")
                .nacionality("Brazil")
                .name("Wallace Silva")
                .naturality("Bauru")
                .sex(sexEntity)
                .user(userAdminEntity)
                .build();


        sexRepository.save(sexEntity);
        roleRepository.saveAll(Arrays.asList(roleAdminEntity, roleUserEntity));
        personRepository.save(personEntity);
        userRepository.saveAll(Arrays.asList(userAdminEntity, userAdminEntity));
    }

    @AfterEach
    public void finalize() {
        personRepository.deleteAll();
        sexRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void findAllCorrectTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/",
                HttpMethod.GET,
                httpEntity,
                String.class);

        Assertions.assertEquals(Boolean.TRUE, response.getBody().contains("wallace_25@hotmail.com"));
    }

    @Test
    public void findAllAnotherUserCorrectTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_1@hotmail.com");

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/",
                HttpMethod.GET,
                httpEntity,
                String.class);

        Assertions.assertEquals(Boolean.FALSE, response.getBody().contains("wallace_25@hotmail.com"));
    }

    @Test
    public void findByIdAnotherUserCorrectTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_1@hotmail.com");

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/" + personRepository.findAll().stream().findFirst().map(PersonEntity::getId).orElse(1L),
                HttpMethod.GET,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void findByIdCorrectTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/1",
                HttpMethod.GET,
                httpEntity,
                String.class);

        Assertions.assertEquals(Boolean.FALSE, response.getBody().contains("wallace_25@hotmail.com"));

    }

    @Test
    public void createCorrectTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("415.568.954-89")
                .email("wallace_225@hotmail.com")
                .nacionality("Brazil")
                .name("Wallace Ferraz Silva")
                .naturality("São Paulo")
                .sex(1L)
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/",
                HttpMethod.POST,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }


    @Test
    public void createIncorrectUpdateUserTest() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("415.568.954-89")
                .email("wallace_225@hotmail.com")
                .nacionality("Brazil")
                .name("Wallace Ferraz Silva")
                .naturality("São Paulo")
                .sex(1L)
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/0",
                HttpMethod.PUT,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }


    @Test
    public void createInvalidCpfTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("43556895489")
                .email("wallace_25@hotmail.com")
                .nacionality("Brazil")
                .name("Wallace Silva")
                .naturality("Bauru")
                .sex(1L)
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/",
                HttpMethod.POST,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    public void createDuplicatedCpfTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("435.568.954-89")
                .email("wallace_25@hotmail.com")
                .nacionality("Brazil")
                .name("Wallace Silva")
                .naturality("Bauru")
                .sex(sexRepository.findAll().stream().findFirst().map(SexEntity::getId).orElse(1L))
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/",
                HttpMethod.POST,
                httpEntity,
                String.class);
        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    public void createInvalidEmailTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("435.568.954-89")
                .email("wallace_25")
                .nacionality("Brazil")
                .name("Wallace Silva")
                .naturality("Bauru")
                .sex(1L)
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/",
                HttpMethod.POST,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    public void deleteCorrectTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/" + personRepository.findAll().stream().findFirst().map(PersonEntity::getId).orElse(1L),
                HttpMethod.DELETE,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteIncorrectTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_52@hotmail.com");

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/" + personRepository.findAll().stream().findFirst().map(PersonEntity::getId).orElse(1L),
                HttpMethod.DELETE,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


    @Test
    public void updateInvalidEmailTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("435.568.954-89")
                .email("wallace_25")
                .nacionality("Brazil")
                .name("Wallace Silva")
                .naturality("Bauru")
                .sex(1L)
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/" + personRepository.findAll().stream().findFirst().map(PersonEntity::getId).orElse(1L),
                HttpMethod.PUT,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }


    @Test
    public void updateInvalidCpfTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("435568.954-89")
                .email("wallace_25@hotmail.com")
                .nacionality("Brazil")
                .name("Wallace Silva")
                .naturality("Bauru")
                .sex(1L)
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/" + personRepository.findAll().stream().findFirst().map(PersonEntity::getId).orElse(1L),
                HttpMethod.PUT,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    public void updateUserNotPermitedTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_215@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("435.568.954-89")
                .email("wallace_25@hotmail.com")
                .nacionality("Brazil")
                .name("Wallace Silva")
                .naturality("Bauru")
                .sex(sexRepository.findAll().stream().findFirst().map(SexEntity::getId).orElse(1L))
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/" + personRepository.findAll().stream().findFirst().map(PersonEntity::getId).orElse(1L),
                HttpMethod.PUT,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateCorrectTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("435.568.954-89")
                .email("wallace_25@hotmail.com")
                .nacionality("Brazil")
                .name("Wallace Silva")
                .naturality("Bauru")
                .sex(sexRepository.findAll().stream().findFirst().map(SexEntity::getId).orElse(1L))
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/" + personRepository.findAll().stream().findFirst().map(PersonEntity::getId).orElse(1L),
                HttpMethod.PUT,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateSexInvalidTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("userEmail", "wallace_25@hotmail.com");

        PersonModel personModel = PersonModel.builder()
                .bornDate(LocalDate.now())
                .cpf("435.568.954-89")
                .email("wallace_25@hotmail.com")
                .nacionality("Brazil")
                .name("Wallace Silva")
                .naturality("Bauru")
                .sex(0L)
                .build();

        HttpEntity<PersonModel> httpEntity = new HttpEntity<>(personModel, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                path + "/" + personRepository.findAll().stream().findFirst().map(PersonEntity::getId).orElse(1L),
                HttpMethod.PUT,
                httpEntity,
                String.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}
