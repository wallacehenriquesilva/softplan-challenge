package br.com.wallace.softplanchallenge.gateway.data.strategy;

import br.com.wallace.softplanchallenge.gateway.data.base.enums.LoginTypeEnum;

import java.util.Map;

/**
 * Classe de pattern Strategy para a escolha de fluxo e dados do OAuth2.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
public class LoginStrategy {
    private LoginTypeEnum userTypeEnum;
    private Map<String, Object> values;

    public LoginStrategy(LoginTypeEnum userTypeEnum, Map<String, Object> values) {
        this.userTypeEnum = userTypeEnum;
        this.values = values;
    }

    public LoginInfo getOAuthData() {
        return userTypeEnum.constructor(values);
    }
}
