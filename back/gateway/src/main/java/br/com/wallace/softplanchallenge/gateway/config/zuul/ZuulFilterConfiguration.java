package br.com.wallace.softplanchallenge.gateway.config.zuul;


import br.com.wallace.softplanchallenge.gateway.config.security.jwt.Jwt;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * Classe de fitro do zuul, onde todas as requisições com token válidas que solicitaram informações de outros
 * micro serviços passam.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

@Configuration
public class ZuulFilterConfiguration extends ZuulFilter {

    @Value("${jwt.token.initial}")
    private String tokenInitial;

    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_EMAIL = "userEmail";

    private final Jwt jwt;

    public ZuulFilterConfiguration(Jwt jwt) {
        this.jwt = jwt;
    }

    public String filterType() {
        return "pre";
    }

    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * <p>
     * Método de execução, chamado para toda requisição que passa pelo gateway, neste caso, estamos utilizando-a
     * para retirar do token o email do solicitante de requisição e enviamos como header userEmail, para que o mesmo
     * possa ser utilizado pelos micro serviços como validação.
     *
     * Caso for o caminho do json do swagger, ele não coloca o header.
     * </p>
     *
     * @return Apenas encaminha a requisição como já estava, porém com um novo header chamado userEmail.
     */
    public Object run() {
        RequestContext context = getCurrentContext();
        if (!context.getRequest().getServletPath().equalsIgnoreCase("/api/v1/service/data/v2/api-docs")) {
            String token = context.getRequest().getHeader(AUTHORIZATION).replace(tokenInitial, "");
            context.getZuulRequestHeaders().put(USER_EMAIL, jwt.getEmailFromToken(token));
        }
        return null;
    }
}