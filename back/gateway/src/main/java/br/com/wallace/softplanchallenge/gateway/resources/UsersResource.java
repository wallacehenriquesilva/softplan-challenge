package br.com.wallace.softplanchallenge.gateway.resources;

import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersResource {
    private final UserService userService;

    public UsersResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> getCurrentUser(
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        return ResponseEntity.ok(userService.getUserFromToken(authorizationHeader));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UserEntity>> findAll(
    ) {
        return ResponseEntity.ok(userService.findAll());
    }
}
