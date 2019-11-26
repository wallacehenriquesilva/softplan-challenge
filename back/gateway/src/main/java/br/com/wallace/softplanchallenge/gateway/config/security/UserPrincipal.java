package br.com.wallace.softplanchallenge.gateway.config.security;


import br.com.wallace.softplanchallenge.gateway.data.base.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Classe de manipulação de usuários advindos de autenticação do tipo OAuth2, como por GitHub, Facebook, Google, etc.
 * Foi criada uma nova implementação do UserPrincipal para facilitar o trabalho do callback do OAuth2.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
public class UserPrincipal implements OAuth2User, UserDetails {
    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * <p>
     * Método repomsável por criar um novo usuário com os dados encontrados na base deados, e lhe dar permissão de
     * 'ROLE_USER', usuário simples.
     * </p>
     *
     * @param userEntity Entidade do usuário que será transformado em UserPrincipal.
     * @return Retorna o UserPrincipal gerado.
     */
    private static UserPrincipal create(UserEntity userEntity) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                authorities
        );
    }

    /**
     * <p>
     * Método responsável por criar um novo usuário utilizando os dados de retorno da autenticação OAuth2.
     * </p>
     *
     * @param userEntity Entidade do usuário encontrado na base de dados para aquele da autenticação OAuth2.
     * @param attributes Lista de atributos resultante da autenticação OAuth2.
     * @return Retorna um novo UserPrincial.
     */
    public static UserPrincipal create(UserEntity userEntity, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(userEntity);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
