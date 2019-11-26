package br.com.wallace.softplanchallenge.gateway.config.security.entrypoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Classe de retorno de requisição caso ela não esteja autorizada após ser validada pelo Filter.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

@Slf4j
public class RequestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                e.getLocalizedMessage());
    }
}
