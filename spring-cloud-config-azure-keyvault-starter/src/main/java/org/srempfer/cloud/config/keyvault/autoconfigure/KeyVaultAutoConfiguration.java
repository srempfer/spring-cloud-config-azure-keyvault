package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Auto-Configuration for Azure KeyVault access.
 *
 * @author Stefan Rempfer
 */
@ConditionalOnProperty ( prefix = "spring.cloud.config.server.azure.keyvault", name = "uri" )
@EnableConfigurationProperties ( KeyVaultProperties.class )
@Configuration
public class KeyVaultAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public TokenCredential tokenCredential ( KeyVaultProperties properties ) {
        return new ClientSecretCredentialBuilder ()
            .clientId ( properties.getClientId () )
            .clientSecret ( properties.getClientKey () )
            .tenantId ( properties.getTenantId () )
            .build ();
    }

    @ConditionalOnMissingBean
    @Bean
    public SecretClient keyVaultClient ( KeyVaultProperties properties, TokenCredential tokenCredential ) {
        return new SecretClientBuilder ()
            .vaultUrl ( properties.getUri () )
            .credential ( tokenCredential )
            .buildClient ();
    }

    @ConditionalOnMissingBean
    @Bean
    public KeyVaultOperation keyVaultOperation ( KeyVaultProperties properties, SecretClient client ) {
        return new KeyVaultOperation ( client, properties.getUri (), properties.getRefreshInterval (), Collections.emptyList () );
    }
}
