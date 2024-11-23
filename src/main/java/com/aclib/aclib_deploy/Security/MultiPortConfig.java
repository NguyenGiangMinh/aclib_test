package com.aclib.aclib_deploy.Security;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiPortConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
        return factory -> {
            Connector additionalConnector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            additionalConnector.setPort(5173);
            factory.addAdditionalTomcatConnectors(additionalConnector);
        };
    }
}
