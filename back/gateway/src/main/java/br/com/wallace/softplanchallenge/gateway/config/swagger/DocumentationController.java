package br.com.wallace.softplanchallenge.gateway.config.swagger;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de configuração de multiplos swaggers em um unico endpoint, que nesse caso é o do gateway.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@Primary
@Component
@EnableAutoConfiguration
public class DocumentationController implements SwaggerResourcesProvider {

    @Override
    public List get() {
        List resources = new ArrayList<>();
        resources.add(swaggerResource("Gateway", "/v2/api-docs", "2.0"));
        resources.add(swaggerResource("Data Service", "/api/v1/service/data/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}