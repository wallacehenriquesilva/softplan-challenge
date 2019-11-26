package br.com.wallace.softplanchallenge.gateway.config.security.filters;

import br.com.wallace.softplanchallenge.gateway.config.security.jwt.Jwt;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.RoleEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de filtro, onde todas as requisições recebidas pelo gateway são validadas.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final Jwt jwt;
    private final UserService userService;

    public TokenAuthenticationFilter(ApplicationContext ctx) {
        this.jwt = ctx.getBean(Jwt.class);
        this.userService = ctx.getBean(UserService.class);
    }

    /**
     * <p>
     * Método de filtro que valida o token recebido da requisição, se o mesmo é válido e esta de acordo com
     * as especificações do token da api. Também valida as permissões do usuário para os endpois, se ele os mesmos permitem
     * todos ou não.
     * </p>
     *
     * @param request     Requisição recebida.
     * @param response    Resposta a ser formulada.
     * @param filterChain Filtro da gerado para a requisição.
     * @throws IOException      Exception de manipulação do input e output de dados.
     * @throws ServletException Exception de manipulação do Servlet.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token = jwt.getJwtFromRequest(request);

        if (jwt.validateToken(token)) {
            final String email = jwt.getEmailFromToken(token);
            UserEntity userEntity = userService.findUserByEmail(email);

            List<SimpleGrantedAuthority> claims = userEntity.getRolesList().stream()
                    .map(RoleEntity::getDescription)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, claims);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}