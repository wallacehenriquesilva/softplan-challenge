package test.br.com.wallace.softplanchallenge.gateway.utils;

import br.com.wallace.softplanchallenge.gateway.Gateway;
import br.com.wallace.softplanchallenge.gateway.utils.Base64Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Gateway.class)
public class Base64UtilsTest {

    @Test
    public void serializeCorrect() {
        String result = Base64Utils.serialize("TESTE");
        Assertions.assertEquals(Boolean.TRUE, StringUtils.isNotBlank(result));
    }

}
