package test.br.com.wallace.softplanchallenge.service.service;

import br.com.wallace.softplanchallenge.service.DataService;
import br.com.wallace.softplanchallenge.service.data.exceptions.PersonCreationException;
import br.com.wallace.softplanchallenge.service.services.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DataService.class)
public class PersonServiceTest {
    @Autowired
    PersonService personService;


    @Test
    public void createExceptionTest() {
        Assertions.assertThrows(PersonCreationException.class,
                () -> personService.create(null, null));
    }

}
