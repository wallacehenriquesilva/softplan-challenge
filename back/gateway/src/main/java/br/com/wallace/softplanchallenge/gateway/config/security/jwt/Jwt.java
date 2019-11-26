package br.com.wallace.softplanchallenge.gateway.config.security.jwt;

import br.com.wallace.softplanchallenge.gateway.data.base.entities.RoleEntity;
import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe de manipulação de Token JWT.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

@Component
public class Jwt implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Value("${jwt.token.initial}")
    private String tokenInitial;

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private Long expirationTime;

    /**
     * <p>
     * Método responsável por recuperar as permissões do usuário, que são salvas no token, no nosso caso, como
     * claims.
     * </p>
     *
     * @param token Token o qual será recuperado as claims.
     * @return Retorna as claims recuperadas para aquele token.
     */
    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes())).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    /**
     * <p>
     * Método responsável por recuperar o email do usuário presente no token.
     * O email fica salvo no assunto, então, ele pega o assunto do token e devolve.
     * </p>
     *
     * @param token Token o qual será recuperado o email.
     * @return Retorna o email presente no token.
     */
    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * <p>
     * Método responsável por recuperar a data de expiração de um token.
     * </p>
     *
     * @param token Token o qual será recuperado a data de expiração
     * @return Retorna a data encontrada para o token.
     */
    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    /**
     * <p>
     * Método responsável por validar se um token foi expirado. Dato um token, ele primeiro retira a data de expiração
     * em seguida, valida se a data atual é maior que a data de expiração, caso sim, retorna como true, que o token
     * expirou, caso não, false.
     * </p>
     *
     * @param token Token que será validado.
     * @return Retorna true para token inválido e false para válido.
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * <p>
     * Método responsável por gerar um token a partir de uma entidade de usuário, onde transforma as roles do usuário
     * em claims para serem salvas no token no subject. Após isso, faz a chamada do gerador de token doGenerateToken.
     * </p>
     *
     * @param userEntity Entidade do usuário a qual será gerada o token.
     * @return Retorna o token gerado.
     */
    public String generateToken(UserEntity userEntity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userEntity.getRolesList().stream().map(RoleEntity::getDescription).collect(Collectors.toList()));
        return doGenerateToken(claims, userEntity.getEmail());
    }

    /**
     * <p>
     * Método responsável por gerar o token. Ele seta as datas de criação e expiração, bem como as claims, email,
     * e a chave secreta para que o token seja gerado.
     * </p>
     *
     * @param claims A lista de Roles a qual o usuário pertence que serão salvas no token.
     * @param email  Email do usuário o qual o token será gerado.
     * @return Retorna o token gerado.
     */
    private String doGenerateToken(Map<String, Object> claims, String email) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTime * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();
    }

    /**
     * <p>
     * Método responsável por validar se um token é valido, primeiro, verifica se o mesmo possui texto, depois
     * se sua data de expiração não passou.
     * </p>
     *
     * @param token Token a ser validado.
     * @return Retorna true para token valido e false para invalido.
     */
    public boolean validateToken(String token) {
        return StringUtils.hasText(token) && !isTokenExpired(token);
    }


    /**
     * <p>
     * Método responsável por pegar o token de uma String, validando se ele possui texto, se começa com a inicial de token
     * Bearer, caso sim, remove a inicial e retorna apenas o token.
     * </p>
     *
     * @param header String do header Authorization.
     * @return Retorna o token encontrado.
     */
    public String getJwtFromRequest(String header) {
        return Optional.of(header)
                .filter(StringUtils::hasText)
                .filter(str -> str.startsWith(tokenInitial))
                .map(str -> str.substring(7))
                .orElseThrow(InvalidTokenException::new);
    }

    /**
     * <p>
     * Método responsável por recuperar de uma requisição. primeiramente, pega o header AUTHORIZATION da request, depois
     * realiza a chamada do método que pega token de String que  valida se ele possui texto, se começa com a inicial de token
     * Bearer, caso sim, remove a inicial e retorna apenas o token.
     * </p>
     *
     * @param request Requisição a qual será tirada o token.
     * @return Retorna o token encontrado.
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        return Optional.of(request)
                .filter(Objects::nonNull)
                .map(req -> req.getHeader(HEADER_AUTHORIZATION))
                .map(this::getJwtFromRequest)
                .orElse(null);
    }
}
