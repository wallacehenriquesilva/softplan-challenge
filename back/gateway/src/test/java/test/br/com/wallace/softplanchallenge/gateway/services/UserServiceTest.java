package test.br.com.wallace.softplanchallenge.gateway.services;

import br.com.wallace.softplanchallenge.gateway.Gateway;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.RoleEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.enums.LoginTypeEnum;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.RoleRepository;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.UserRepository;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.UserNotFoundException;
import br.com.wallace.softplanchallenge.gateway.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest(classes = Gateway.class)
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void init() {
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
    public void findUserByEmailCorrectTest() {
        UserEntity userEntity = userService.findUserByEmail("wallace_25@hotmail.com");
        Assert.assertEquals(Boolean.TRUE,
                userEntity.getEmail().equalsIgnoreCase("wallace_25@hotmail.com"));
    }

    @Test
    public void findUserByEmailIncorrectTest() {
        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.findUserByEmail("wallace@hotmail.com"));
    }
}
