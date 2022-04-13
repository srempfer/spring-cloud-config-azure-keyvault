package org.srempfer.cloud.config.keyvault;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.spring.cloud.autoconfigure.keyvault.environment.KeyVaultOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.srempfer.cloud.config.keyvault.autoconfigure.KeyVaultProperties;

import java.util.Collections;

/**
 * Factory to build {@link KeyVaultOperation}.
 */
public class KeyVaultOperationFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger ( KeyVaultOperationFactory.class );

    public KeyVaultOperation build ( KeyVaultProperties keyVaultProperties ) {
        final TokenCredential tokenCredential = buildTokenCredential ( keyVaultProperties );
        final SecretClient secretClient = buildKeyVaultClient ( keyVaultProperties, tokenCredential );
        return buildKeyVaultOperation ( keyVaultProperties, secretClient );
    }

    protected KeyVaultOperation buildKeyVaultOperation ( KeyVaultProperties properties, SecretClient client ) {
        return new KeyVaultOperation ( client, properties.getRefreshInterval (), Collections.emptyList (), properties.getCaseSensitive () );
    }

    protected SecretClient buildKeyVaultClient ( KeyVaultProperties properties, TokenCredential tokenCredential ) {
        return new SecretClientBuilder ()
            .vaultUrl ( properties.getUri () )
            .credential ( tokenCredential )
            .buildClient ();
    }

    protected TokenCredential buildTokenCredential ( KeyVaultProperties properties ) {
        String clientId = properties.getClientId ();
        String clientKey = properties.getClientKey ();

        if ( StringUtils.hasText ( clientId ) && StringUtils.hasText ( clientKey ) ) {
            LOGGER.debug ( "Client secret credentials will be used" );
            return new ClientSecretCredentialBuilder ()
                .clientId ( clientId )
                .clientSecret ( clientKey )
                .tenantId ( properties.getTenantId () )
                .build ();
        }

        if ( StringUtils.hasText ( clientId ) ) {
            LOGGER.debug ( "MSI credentials with specified clientId will be used" );
            return new ManagedIdentityCredentialBuilder ().clientId ( clientId ).build ();
        }

        LOGGER.debug ( "MSI credentials will be used" );
        return new ManagedIdentityCredentialBuilder ().build ();
    }
}
