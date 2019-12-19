package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.microsoft.azure.AzureResponseBuilder;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.spring.AzureKeyVaultCredential;
import com.microsoft.azure.keyvault.spring.Constants;
import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import com.microsoft.azure.serializer.AzureJacksonAdapter;
import com.microsoft.azure.spring.support.UserAgent;
import com.microsoft.rest.RestClient;
import com.microsoft.rest.credentials.ServiceClientCredentials;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-Configuration for Azure KeyVault access.
 *
 * @author Stefan Rempfer
 */
@ConditionalOnProperty ( prefix = "azure.keyvault", name = "enabled", matchIfMissing = true )
@EnableConfigurationProperties ( KeyVaultProperties.class )
@Configuration
public class KeyVaultAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public ServiceClientCredentials serviceClientCredentials ( KeyVaultProperties properties ) {
        return new AzureKeyVaultCredential ( properties.getClientId (), properties.getClientKey (), properties.getTokenAcquireTimeout () );
    }

    @ConditionalOnMissingBean
    @Bean
    public RestClient restClient ( KeyVaultProperties properties, ServiceClientCredentials credentials ) {
        return new RestClient.Builder ().withBaseUrl ( properties.getUri () )
            .withCredentials ( credentials )
            .withSerializerAdapter ( new AzureJacksonAdapter () )
            .withResponseBuilderFactory ( new AzureResponseBuilder.Factory () )
            .withUserAgent ( UserAgent.getUserAgent ( Constants.AZURE_KEYVAULT_USER_AGENT, properties.getAllowTelemetry () ) )
            .build ();
    }

    @ConditionalOnMissingBean
    @Bean
    public KeyVaultClient keyVaultClient ( RestClient restClient ) {
        return new KeyVaultClient ( restClient );
    }

    @ConditionalOnMissingBean
    @Bean
    public KeyVaultOperation keyVaultOperation ( KeyVaultProperties properties, KeyVaultClient client ) {
        return new KeyVaultOperation ( client, properties.getUri (), properties.getRefreshInterval () );
    }
}
