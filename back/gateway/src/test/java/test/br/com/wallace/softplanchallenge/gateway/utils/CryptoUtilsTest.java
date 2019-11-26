package test.br.com.wallace.softplanchallenge.gateway.utils;

import br.com.wallace.softplanchallenge.gateway.Gateway;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.InvalidHashException;
import br.com.wallace.softplanchallenge.gateway.utils.CryptoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Gateway.class)
public class CryptoUtilsTest {

    @Test
    public void hashPasswordTest() {
        String result = CryptoUtils.generateHash("PasSwORDT3sT3");
        Assertions.assertEquals(Boolean.TRUE, result.startsWith("$2a"));
    }

    @Test
    public void validateHashTest() {
        boolean result = CryptoUtils.validateHash("PasSwORDT3sT3", "$2a$12$zUsY7HxwtC1/lNJQ6zZwvu02Y7ByzbiQKISKY1UL4oFB/hexJtic6");
        Assertions.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void validateHashErrorTest() {
        Assertions.assertThrows(InvalidHashException.class,
                () -> CryptoUtils.validateHash("PasSwORDT3sT3", "1a$12$zUsY7HxwtC1/lNJQ6zZwvu02Y7ByzbiQKISKY1UL4oFB/hexJtic6"));
    }
}
