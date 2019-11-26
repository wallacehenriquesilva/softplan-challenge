package test.br.com.wallace.softplanchallenge.service;

import br.com.wallace.softplanchallenge.service.DataService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = DataService.class)
class DataServiceTest {
    @Test
    void contextLoads() {
        String x = "Teste";
        Assertions.assertEquals(Boolean.TRUE, StringUtils.isNotBlank(x));
    }

}
