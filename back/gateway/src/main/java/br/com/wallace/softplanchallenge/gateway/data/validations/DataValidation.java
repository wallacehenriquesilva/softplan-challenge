package br.com.wallace.softplanchallenge.gateway.data.validations;

import br.com.wallace.softplanchallenge.gateway.data.exceptions.InvalidEmailException;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe de validação de dados.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

public class DataValidation {
    private static final Pattern REGEX_EMAIL = Pattern.compile("^[a-zA-Z0-9_\\-\\.]*\\@[a-zA-Z0-9_\\-\\.]*\\.\\w{2,3}(\\.\\w{2})?$");


    private DataValidation() {

    }

    /**
     * <p>
     * Método responsável por validar se um email é valido de acordo com o REGEX criado em REGEX_EMAIL, caso seja válido,
     * retorna true, caso não, causa uma InvalidEmailException.
     * </p>
     *
     * @param email Email que será validado.
     * @return Retorna true caso o email seja válido.
     */
    public static boolean isValidEmail(String email) {
        return Optional.of(email)
                .map(String::trim)
                .map(REGEX_EMAIL::matcher)
                .filter(Matcher::find)
                .map(Objects::nonNull)
                .orElseThrow(InvalidEmailException::new);
    }
}
