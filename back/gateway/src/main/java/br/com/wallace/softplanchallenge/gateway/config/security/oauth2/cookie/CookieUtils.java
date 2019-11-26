package br.com.wallace.softplanchallenge.gateway.config.security.oauth2.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * Classe de manipulação de cookies.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
public class CookieUtils {

    private CookieUtils() {

    }

    /**
     * <p>
     * Método reponsável por buscar os cookies de uma requisição pelo nome.
     * </p>
     *
     * @param request Requisição a qual será buscada o cookie.
     * @param name    Nome do cookie solicitado.
     * @return Retorna um Optional contendo o cookie encontrado.
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (Objects.nonNull(cookies) && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * <p>
     * Método responsável por adicionar um cookie a uma resposta http.
     * Ele recebe o nome do cookie, seu valor e seu tempo  de expiração, e monta o cookie settando-o a resposta.
     * </p>
     *
     * @param response Resposta a ser formulada a qual será adicionado o cookie.
     * @param name     Nome do cookie que será adicionado.
     * @param value    Valor do cookie.
     * @param maxAge   Máximo de tempo do cookie.
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * <p>
     * Método reponsável por apagar o cookie de uma requisição, tirando seu path, seu valor, e settando seu tempo
     * de vida como 0.
     * </p>
     *
     * @param request  Requisição que esta sendo processada.
     * @param response Resposta a qual estão sendo manipulado os cookies.
     * @param name     Name do cookie que será apagado.
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies) && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
}
