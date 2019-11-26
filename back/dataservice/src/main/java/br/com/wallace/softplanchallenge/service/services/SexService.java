package br.com.wallace.softplanchallenge.service.services;


import br.com.wallace.softplanchallenge.service.data.base.entities.SexEntity;
import br.com.wallace.softplanchallenge.service.data.base.repositories.SexRepository;
import br.com.wallace.softplanchallenge.service.data.exceptions.SexInvalidException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Classe de serviço de controle de sexo.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@Service
public class SexService {

    private final SexRepository sexRepository;

    public SexService(SexRepository sexRepository) {
        this.sexRepository = sexRepository;
    }

    /**
     * <p>
     * Método responsável por consultar um sexo pelo id, caso não encontre,
     * causa uma SexInvalidException, que será tratada pelo ExceptionHandler.
     * </p>
     *
     * @param id id do sexo que será consultado.
     * @return Retorna a entidade do sexo encontrado.
     */
    public SexEntity findById(final Long id) {
        return sexRepository.findById(id)
                .orElseThrow(SexInvalidException::new);
    }

    @PostConstruct
    private void init() {
        if (sexRepository.findAll().isEmpty()) {
            SexEntity sexEntityMasculino = SexEntity
                    .builder()
                    .name("Masculino")
                    .id(1L)
                    .reference("M")
                    .build();

            SexEntity sexEntityFeminino = SexEntity
                    .builder()
                    .name("Feminino")
                    .id(2L)
                    .reference("F")
                    .build();
            sexRepository.saveAll(Arrays.asList(sexEntityMasculino, sexEntityFeminino));
        }
    }
}
