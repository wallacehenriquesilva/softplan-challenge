package br.com.wallace.softplanchallenge.gateway.config.security.oauth2;

import br.com.wallace.softplanchallenge.gateway.config.security.oauth2.cookie.CookieUtils;
import br.com.wallace.softplanchallenge.gateway.config.security.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Classe de handler de falha na autenticação via OAuth2.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${cookie.redirect-name}")
    private String redirectUriName;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


    public OAuth2AuthenticationFailureHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }


    /**
     * <p>
     * Método reponsável por manipular o redirect e limpar os cookies caso ocorra um erro durante a autenticação OAuth2.
     *
     * </p>
     *
     * @param request   Requisição recebida.
     * @param response  Resposta da requisição.
     * @param exception Exception da falha.
     * @throws IOException      Exception de manipulação do input e output de dados.
     * @throws ServletException Exception de manipulação do Servlet.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String url = CookieUtils.getCookie(request, redirectUriName)
                .map(Cookie::getValue)
                .orElse(("/"));

        url = UriComponentsBuilder.fromUriString(url)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
