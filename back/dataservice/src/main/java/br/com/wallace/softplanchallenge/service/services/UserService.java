package br.com.wallace.softplanchallenge.service.services;


import br.com.wallace.softplanchallenge.service.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.service.data.base.repositories.UserRepository;
import br.com.wallace.softplanchallenge.service.data.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Classe de serviço de controle de usuários.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * <p>
     * Método responsável por consultar um usuário pelo seu email, caso não encontre,
     * causa uma UserNotFoundException, que será tratada pelo ExceptionHandler.
     * </p>
     *
     * @param email Email do usuário que será buscado.
     * @return Retorna a entidade do usuário encontrado.
     */
    public UserEntity findUserByEmail(final String email) {
        return userRepository.findFirstByEmail(email).orElseThrow(UserNotFoundException::new);
    }
}
