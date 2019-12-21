package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.srempfer.cloud.config.keyvault.KeyVaultEnvironmentRepository;
import org.srempfer.cloud.config.keyvault.KeyVaultEnvironmentRepositoryFactory;

/**
 * Auto-Configuration for {@link EnvironmentRepository} which use Azure KeyVault to decrypt values.
 *
 * @author Stefan Rempfer
 */
@AutoConfigureAfter ( KeyVaultAutoConfiguration.class )
@EnableConfigurationProperties ( KeyVaultEnvironmentProperties.class )
@Configuration
public class EnvironmentRepositoryAutoConfiguration {

    @ConditionalOnBean ( KeyVaultOperation.class )
    @Bean
    public KeyVaultEnvironmentRepositoryFactory keyVaultEnvironmentRepositoryFactory ( KeyVaultOperation keyVaultOperation ) {
        return new KeyVaultEnvironmentRepositoryFactory ( keyVaultOperation );
    }

    @Profile ( "keyvault" )
    @ConditionalOnBean ( KeyVaultOperation.class )
    @Bean
    public KeyVaultEnvironmentRepository keyVaultEnvironmentRepository ( KeyVaultEnvironmentRepositoryFactory factory,
        KeyVaultEnvironmentProperties environmentProperties ) {
        return factory.build ( environmentProperties );
    }

}
