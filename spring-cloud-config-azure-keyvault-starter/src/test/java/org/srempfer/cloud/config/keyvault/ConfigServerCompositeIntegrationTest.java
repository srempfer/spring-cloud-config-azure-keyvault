package org.srempfer.cloud.config.keyvault;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@ActiveProfiles ( { "composite" } )
@SpringBootTest (
    classes = { ConfigServerCompositeIntegrationTest.ConfigServerApplicationConfiguration.class },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.cloud.config.server.composite[0].type=native",
        "spring.cloud.config.server.composite[0].search-locations=classpath:/config-data/,classpath:/config-data/{application}/",
        "spring.cloud.config.server.composite[1].type=keyvault"
    } )
class ConfigServerCompositeIntegrationTest {

    @LocalServerPort
    private Integer serverPort;

    @Test
    void verifyKeyVaultEnvironmentEncryptorAndRepository () {

        String url = "http://localhost:" + serverPort + "/test-app/default";

        RestTemplate restTemplate = new RestTemplate ();
        ResponseEntity<Environment> response = restTemplate.exchange ( url, HttpMethod.GET, HttpEntity.EMPTY, Environment.class );
        assertThat ( response.getStatusCode () ).isEqualTo ( HttpStatus.OK );

        Environment environment = response.getBody ();
        assertThat ( environment ).isNotNull ();
        assertThat ( environment.getName () ).isEqualTo ( "test-app" );
        assertThat ( environment.getLabel () ).isBlank ();
        assertThat ( environment.getProfiles () ).contains ( "default" );

        List<PropertySource> propertySources = environment.getPropertySources ();
        assertThat ( propertySources ).hasSize ( 2 );

        PropertySource propertySource = propertySources.get ( 0 );
        assertThat ( propertySource.getName () ).isEqualTo ( "classpath:/config-data/application.properties" );
        Map<Object, Object> source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).containsOnly (
            entry ( "client.test.key", "test-value" ),
            entry ( "client.test.encrypted", "decrypted-value" ),
            entry ( "missing.client.test.encrypted-missing", "<n/a>" ) );

        propertySource = propertySources.get ( 1 );
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-application-default" );
        source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "simplekey", "dummy" ) );
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableConfigServer
    static class ConfigServerApplicationConfiguration {

    }
}
