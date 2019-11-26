package test.br.com.wallace.softplanchallenge.gateway;

import br.com.wallace.softplanchallenge.gateway.Gateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = Gateway.class)
class GatewayTest {
    @Test
    void contextLoads() {
        String x = "Teste";
        Assertions.assertEquals(Boolean.TRUE, StringUtils.isNotBlank(x));
    }

}
