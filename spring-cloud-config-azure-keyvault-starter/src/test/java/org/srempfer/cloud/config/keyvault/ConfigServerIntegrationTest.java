package org.srempfer.cloud.config.keyvault;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@RunWith ( SpringRunner.class )
@ActiveProfiles ( { "native", "keyvault" } )
@SpringBootTest (
    classes = { ConfigServerIntegrationTest.ConfigServerApplicationConfiguration.class },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = { "spring.cloud.config.server.native.searchLocations=classpath:/config-data/,classpath:/config-data/{application}/" } )
public class ConfigServerIntegrationTest {

    @LocalServerPort
    private Integer serverPort;

    @Test
    public void verifyKeyVaultEnvironmentEncryptorAndRepository () {

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
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-application-default" );
        Map<Object, Object> source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "simplekey", "dummy" ) );

        propertySource = propertySources.get ( 1 );
        assertThat ( propertySource.getName () ).isEqualTo ( "classpath:/config-data/application.properties" );
        source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).containsOnly ( entry ( "client.test.key", "test-value" ), entry ( "client.test.encrypted", "decrypted-value" ) );
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableConfigServer
    public static class ConfigServerApplicationConfiguration {

    }
}
