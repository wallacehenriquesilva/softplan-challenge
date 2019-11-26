package br.com.wallace.softplanchallenge.gateway.config.security.oauth2.cookie;


import br.com.wallace.softplanchallenge.gateway.utils.Base64Utils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Classe responsável por manipular os cookies de autenticação OAuth2.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    @Value("${cookie.oauth2-authorization}")
    private String oauth2Authorization;

    @Value("${cookie.redirect-name}")
    private String redirectUriName;

    @Value("${cookie.expired-time}")
    private int expiredTime;


    /**
     * <p>
     * Método responsável por carregar o authorization dos cookies, ele primeiramente faz a busca por nome, depois
     * deserializa, por os dados estão salvos como base64.
     * </p>
     *
     * @param request Requisição recebida, a qual será retirado o authorization.
     * @return Retorna o AuthorizationRequest recebido.
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, oauth2Authorization)
                .map(cookie -> Base64Utils.deserialize(cookie.getValue(), OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    /**
     * <p>
     * Método responsável por salvar o AuthorizarionRequest nos cookies, caso a AuthorizationRequest seja nula,
     * ele remove os cookies, caso contrário, adiciona o cookie do oauth2Authorization  e do link de redirect.
     * </p>
     *
     * @param authorizationRequest Requisição de autorização recebida pelo OAuth2
     * @param request              Requisição recebida.
     * @param response             Resposta que esta sendo formulada para devolver ao client.
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (Objects.isNull(authorizationRequest)) {
            CookieUtils.deleteCookie(request, response, oauth2Authorization);
            CookieUtils.deleteCookie(request, response, redirectUriName);
            return;
        }

        CookieUtils.addCookie(response, oauth2Authorization, Base64Utils.serialize(authorizationRequest), expiredTime);
        String redirectUriAfterLogin = request.getParameter(redirectUriName);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, redirectUriName, redirectUriAfterLogin, expiredTime);
        }
    }

    /**
     * <p>
     * Método responsável por remover o Authorization da requisição.
     * </p>
     *
     * @param request Requisição que será removida o authorization.
     * @return Retorna o OAuth2 que veio da requisição.
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    /**
     * <p>
     * Método responsável por remover os cookies de authorização do OAuth2 e redirect.
     * </p>
     *
     * @param request  Requisição recebida.
     * @param response Resposta que esta sendo alterada e será removido o cookie.
     */
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, oauth2Authorization);
        CookieUtils.deleteCookie(request, response, redirectUriName);
    }
}
