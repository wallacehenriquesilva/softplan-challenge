package br.com.wallace.softplanchallenge.gateway.utils;


import br.com.wallace.softplanchallenge.gateway.data.exceptions.InvalidHashException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;
import java.util.function.Predicate;

public class CryptoUtils {
    private static final String HASH_INITIAL = "$2a$";

    private CryptoUtils() {

    }

    /**
     * <p>
     * Método responsável por gerar uma hash com base em um texto.
     * Neste caso, sendo utilizado para criptografar as senhas.
     * </p>
     *
     * @param text Texto o qual será gerado a hash.
     * @return Hash gerada.
     */
    public static String generateHash(String text) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(text, salt);
    }

    /**
     * <p>
     * Método responsável por validar se um texto é semelhante ao texto de uma hash.
     * Neste caso, estamos utilizando para comparar a senha com a presente no banco.
     * </p>
     *
     * @param text Texto puro que será comparado.
     * @param hash Hash a qual o texto será comparado.
     * @return Retorna um boolean com true caso o texto e hash sejam iguais e false caso não.
     */
    public static boolean validateHash(String text, String hash) {
        Predicate<String> hashPredicate = hs -> hs.startsWith(HASH_INITIAL);
        return Optional.ofNullable(hash)
                .filter(hashPredicate)
                .map(hs -> BCrypt.checkpw(text, hs))
                .orElseThrow(InvalidHashException::new);
    }
}
