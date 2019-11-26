package br.com.wallace.softplanchallenge.gateway.config.hystrix;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe de fallback de todas as requisições do zuul. Caso algum serviço não possa ser chamado, o hystrix intercepta
 * e encaminha para o método fallbackResponse, que tratará a exception e retornará uma mensagem ao solicitante.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */

@Component
class HystrixFallbackConfiguration implements FallbackProvider {

    private static final String DEFAULT_MESSAGE = "{'message':'Serviço %s temporariamente fora. Mas fique tranquilo, " +
            "estamos trabalhando para resolve-lo o mais rápido possível.'}";

    /**
     * <p>
     * Método reponsável por mapear as rotas que serão cobertas pelo fallbackResponse.
     * Neste caso, o uso do '*', faz com que todas as rotas de serviços mapeadas sejam monitoradas pelo Hystrix.
     * </p>
     *
     * @return
     */
    @Override
    public String getRoute() {
        return "*";
    }

    /**
     * <p>
     * Método rensável por capturar todas as exceptions de fallback do Hystrix para as rotas mapeadas no método getRoute
     * Nele é avalida qual o tipo de exception para que seja retornado ao client a respota adequada.
     * </p>
     *
     * @param route Nome do serviço que ocasionou a exception.
     * @param cause Exception que ocasionou a chamada do fallbackResponse.
     * @return Retorna um ClientHttpResponse contendo o status e mensagem de resposta ao client.
     */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (cause instanceof HystrixTimeoutException) {
            return this.response(HttpStatus.GATEWAY_TIMEOUT, String.format(DEFAULT_MESSAGE, route));
        } else {
            return this.response(HttpStatus.INTERNAL_SERVER_ERROR, String.format(DEFAULT_MESSAGE, route));
        }
    }

    /**
     * <p>
     * Método responsável pela implementação do ClientHttpResponse, que será enviado para o client caso o fallbackResponse
     * seja acionado.
     * </p>
     *
     * @param status  Status http da resposta.
     * @param message Mensagem que será enviada no corpo da resposta.
     * @return Retorna o ClientHttpResponse para que o fallbackResponse retorne-o ao client.
     */
    private ClientHttpResponse response(final HttpStatus status, final String message) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
                //Implementation
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(message.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}