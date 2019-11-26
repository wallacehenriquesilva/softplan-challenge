package br.com.wallace.softplanchallenge.gateway.config.security.oauth2;


import br.com.wallace.softplanchallenge.gateway.config.security.UserPrincipal;
import br.com.wallace.softplanchallenge.gateway.config.security.jwt.Jwt;
import br.com.wallace.softplanchallenge.gateway.config.security.oauth2.cookie.CookieUtils;
import br.com.wallace.softplanchallenge.gateway.config.security.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Classe de handler de manipulação de autenticação via OAuth2 caso a autenticação seja realizada com sucesso.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${cookie.redirect-name}")
    private String redirectUriName;

    private final Jwt jwt;
    private final UserService userService;


    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    OAuth2AuthenticationSuccessHandler(Jwt jwt,
                                       UserService userService,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.userService = userService;
        this.jwt = jwt;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    /**
     * <p>
     * Método responsável por validar se o link gerado para redirect e se a reposta já foi fechada. Caso não, limpa
     * os atributos de autenticação e remove gera o link de redirect completo.
     * </p>
     *
     * @param request        Requisição recebida.
     * @param response       Response montada que será enviada ao client.
     * @param authentication Dados da autenticação.
     * @throws IOException      Exception de manipulação do input e output de dados.
     * @throws ServletException Exception de manipulação do Servlet.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final String url = determineTargetUrl(request, authentication);

        if (response.isCommitted()) {
            log.debug("Resposta finalizada. Não foi possível redirecionar para {}", url);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    /**
     * <p>
     * Método responsável por criar o link de redirect pós autenticação realizada com suscesso. Fazendo com que,
     * após a autenticação OAuth2, seja gerado um token para o usuário.
     * </p>
     *
     * @param request        Requisição recebida.
     * @param authentication Dados da autenticação.
     * @return Retorna o link de redirect.
     */
    private String determineTargetUrl(HttpServletRequest request, Authentication authentication) {
        Optional<String> redirectUri =
                CookieUtils.getCookie(request, redirectUriName)
                        .map(Cookie::getValue);

        final String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        final String email = ((UserPrincipal) authentication.getPrincipal()).getEmail();
        UserEntity userEntity = userService.findUserByEmail(email);
        final String token = jwt.generateToken(userEntity);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    /**
     * <p>
     * Método responsávle por limpar os atributos de autenticação da requisição e remover os cookies.
     * </p>
     *
     * @param request  Requisição que será limpa.
     * @param response Resposta do Servlet.
     */
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}