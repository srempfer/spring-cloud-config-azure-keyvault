package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger ( KeyVaultAutoConfiguration.class );

    @ConditionalOnMissingBean
    @Bean
    public TokenCredential tokenCredential ( KeyVaultProperties properties ) {
        String clientId = properties.getClientId ();
        String clientKey = properties.getClientKey ();

        if ( StringUtils.isNoneBlank ( clientId, clientKey ) ) {
            LOGGER.debug ( "Client secret credentials will be used" );
            return new ClientSecretCredentialBuilder ()
                .clientId ( clientId )
                .clientSecret ( clientKey )
                .tenantId ( properties.getTenantId () )
                .build ();
        }

        if ( StringUtils.isNoneBlank ( clientId ) ) {
            LOGGER.debug ( "MSI credentials with specified clientId will be used" );
            return new ManagedIdentityCredentialBuilder ().clientId ( clientId ).build ();
        }

        LOGGER.debug ( "MSI credentials will be used" );
        return new ManagedIdentityCredentialBuilder ().build ();
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
