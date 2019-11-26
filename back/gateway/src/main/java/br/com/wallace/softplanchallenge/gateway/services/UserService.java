package br.com.wallace.softplanchallenge.gateway.services;


import br.com.wallace.softplanchallenge.gateway.config.security.jwt.Jwt;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.UserRepository;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Classe de serviço de usuários onde são implementadas as validações e interações com os objetos de usuário.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Jwt jwt;

    public UserService(UserRepository userRepository, Jwt jwt) {
        this.userRepository = userRepository;
        this.jwt = jwt;
    }

    /**
     * <p>
     * Método responsável por buscar um usuário por email  , caso não encontre o usuário na base de dados,
     * causa uma exception do tipo UserNotFoundException, que realizará a chamada do ExceptionHandler e retornará
     * ao cliente uma mensagem de erro.
     * </p>
     *
     * @param email Email do usuário que será consultado.
     * @return Retorna a entidade do usuário encontrado.
     */
    public UserEntity findUserByEmail(final String email) {
        return userRepository.findFirstByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }


    /**
     * <p>
     * Método responsável por buscar o usuário o qual determinado token pertence.
     * Primeiramente, tira o email presente no token, para depois, busca-lo na base de dados
     * e retornar o usuário caso encontrado.
     * </p>
     *
     * @param token Token o qual será buscado o usuário.
     * @return Retorna a entidade do usuário encontrado para o token.
     */
    public UserEntity getUserFromToken(String token) {
        return Optional.ofNullable(token)
                .map(jwt::getJwtFromRequest)
                .map(jwt::getEmailFromToken)
                .map(this::findUserByEmail)
                .orElseThrow(UserNotFoundException::new);
    }


    /**
     * <p>
     *     Método responsável por retornar todos os usuários registrados na base.
     * </p>
     * @return Retorna uma collection contendo os usuários encontrados.
     */
    public List<UserEntity> findAll(){
        return userRepository.findAll();
    }
}
