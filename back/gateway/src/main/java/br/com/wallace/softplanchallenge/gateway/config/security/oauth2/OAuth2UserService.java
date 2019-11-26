package br.com.wallace.softplanchallenge.gateway.config.security.oauth2;


import br.com.wallace.softplanchallenge.gateway.config.security.UserPrincipal;
import br.com.wallace.softplanchallenge.gateway.config.security.exceptions.OAuth2AuthenticationProcessingException;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.RoleEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.enums.LoginTypeEnum;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.RoleRepository;
import br.com.wallace.softplanchallenge.gateway.data.base.repositories.UserRepository;
import br.com.wallace.softplanchallenge.gateway.data.strategy.LoginInfo;
import br.com.wallace.softplanchallenge.gateway.data.strategy.LoginStrategy;
import br.com.wallace.softplanchallenge.gateway.utils.CryptoUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Classe de serviço de autentição OAuth2 nela, são validados os retornos e criados novos usuários caso necessário.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public OAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * <p>
     * Método responsável por carregar o usuário OAuth2 e passar para que o init possa cria-lo ou altera-lo caso
     * necessário
     * </p>
     *
     * @param oAuth2UserRequest Dados da solicitação de autenticação OAuth2.
     * @return Retorna o usuário OAuth2 normalizado.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return initUser(oAuth2UserRequest, oAuth2User);
    }

    /**
     * <p>
     * Método responsável por iniciar o fluxo com os dados do OAuth2, valida o usuário, cria novo, altera caso necessáio,
     *
     * </p>
     *
     * @param oAuth2UserRequest Dados da solicitação de autenticação OAuth2.
     * @param oAuth2User        Dados retornados da autenticação..
     * @return Retorna o usuário OAuth2 normalizado.
     */
    private OAuth2User initUser(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        final LoginInfo oAuthUserInfo = new LoginStrategy(
                LoginTypeEnum.getFromValue(oAuth2UserRequest.getClientRegistration().getRegistrationId()),
                oAuth2User.getAttributes()).getOAuthData();

        return Optional.of(oAuthUserInfo)
                .map(LoginInfo::getEmail)
                .filter(StringUtils::isNotBlank)
                .map(userRepository::findFirstByEmail)
                .map(userEntityOptional -> this.confereUser(userEntityOptional, oAuthUserInfo))
                .map(user -> UserPrincipal.create(user, oAuth2User.getAttributes()))
                .orElseThrow(OAuth2AuthenticationProcessingException::new);
    }

    /**
     * <p>
     * Método responsável por conferir se um determinado usuáro já existe. Caso existe, chama o método de update,
     * caso não, insere um novo usuário.
     * </p>
     *
     * @param userEntityOptional Optional resultante da busca do usuário na base de dados.
     * @param oAuthUserInfo      Dados retornados da autenticação OAuth2.
     * @return Retorna a entidade do usuário, seja ele atualizado ou novo.
     */
    private UserEntity confereUser(Optional<UserEntity> userEntityOptional, LoginInfo oAuthUserInfo) {
        return userEntityOptional
                .map(user -> updateUser(user, oAuthUserInfo))
                .orElseGet(() -> this.insertUser(oAuthUserInfo));

    }

    /**
     * <p>
     * Método reponsável por inserir novos usuários com as informações advindas da autenticação via OAuth2.
     * Os usuários criados por esse método terão sempre permissões do tipo 'ROLE_USER', permissões básicas de usuário.
     * </p>
     *
     * @param oAuthUserInfo Dados retornados da autenticação OAuth2.
     * @return Retorna a nova enidade de usuário gerada.
     */
    private UserEntity insertUser(LoginInfo oAuthUserInfo) {
        final RoleEntity roleEntity = roleRepository.findFirstByDescription(ROLE_USER).orElse(null);

        UserEntity userEntity =
                UserEntity.builder()
                        .name(oAuthUserInfo.getName())
                        .email(oAuthUserInfo.getEmail())
                        .picturePath(oAuthUserInfo.getImagePath())
                        .loginType(oAuthUserInfo.getType())
                        .password(CryptoUtils.generateHash(UUID.randomUUID().toString()))
                        .rolesList(Collections.singletonList(roleEntity))
                        .build();

        return userRepository.save(userEntity);
    }

    /**
     * <p>
     * Método responsável por atualizar um usuário já existente com os dados retornados do OAuth2, que são o link
     * da imagem de perfil e nome do usuário.
     * </p>
     *
     * @param userEntity    Entidade do usuário encontrado.
     * @param oAuthUserInfo Informações retornadas da requisição OAuth2.
     * @return Retorna o usuário atualizado.
     */
    private UserEntity updateUser(UserEntity userEntity, LoginInfo oAuthUserInfo) {
        userEntity.setPicturePath(oAuthUserInfo.getImagePath());
        userEntity.setName(oAuthUserInfo.getName());
        return userRepository.save(userEntity);
    }
}
