package test.br.com.wallace.softplanchallenge.gateway.config.security.oauth2;

import br.com.wallace.softplanchallenge.gateway.Gateway;
import br.com.wallace.softplanchallenge.gateway.config.security.oauth2.OAuth2UserService;
import br.com.wallace.softplanchallenge.gateway.utils.Base64Utils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@SpringBootTest(classes = Gateway.class)
public class OAuth2UserServiceTest {

    @Autowired
    private OAuth2UserService oAuth2UserService;

    @Test
    public void loadUserInvalidArgumentTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> oAuth2UserService.loadUser(null));
    }

    @Test
    public void loadUserUnauthorizedTest() {

        File fileToken = FileUtils.getFile("src", "test", "resources", "files", "AccessToken.txt");
        String textToken = this.readTxtFile(fileToken);
        OAuth2AccessToken accessToken = Base64Utils.deserialize(textToken, OAuth2AccessToken.class);

        File fileClientRegistration = FileUtils.getFile("src", "test", "resources", "files", "ClientResgistration.txt");
        String textClientRegistration = this.readTxtFile(fileClientRegistration);
        ClientRegistration clientRegistration = Base64Utils.deserialize(textClientRegistration, ClientRegistration.class);

        File fileMap = FileUtils.getFile("src", "test", "resources", "files", "AditionalParameters.txt");
        String textMap = this.readTxtFile(fileMap);
        Map<String, Object> objectsMap = Base64Utils.deserialize(textMap, Map.class);

        OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, accessToken, objectsMap);

        Assertions.assertThrows(OAuth2AuthenticationException.class,
                () -> oAuth2UserService.loadUser(oAuth2UserRequest));
    }

    private String readTxtFile(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String st;
            while (Objects.nonNull(st = bufferedReader.readLine())) {
                stringBuilder.append(st);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            return null;
        }
    }
}

