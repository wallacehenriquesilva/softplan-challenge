package br.com.wallace.softplanchallenge.gateway.utils;

import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class Base64Utils {

    private Base64Utils() {

    }

    /**
     * <p>
     * Método repsonsável pore serializar um objeto para base64.
     * </p>
     *
     * @param object Objeto que será serializado.
     * @return Retorna uma string contendo o objeto serializado em Base64.
     */
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    /**
     * <p>
     * Método responsável por deserializar um objeto do tipo base64, vontando-o ao
     * seu tipo normal.
     * </p>
     *
     * @param value Base64 do arquivo que será deserializado
     * @param cls   Classe do objeto deserializado.
     * @param <T>   Tipo T genérico.
     * @return Retorna o Objeto deserializado com seu tipo padrão.
     */
    public static <T> T deserialize(String value, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(value)));
    }
}
