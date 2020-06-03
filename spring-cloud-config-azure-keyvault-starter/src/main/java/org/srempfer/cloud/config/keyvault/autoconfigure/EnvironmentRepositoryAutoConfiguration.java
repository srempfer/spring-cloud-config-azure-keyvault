package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.config.ConfigServerAutoConfiguration;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.srempfer.cloud.config.keyvault.KeyVaultEnvironmentRepository;
import org.srempfer.cloud.config.keyvault.KeyVaultEnvironmentRepositoryFactory;

/**
 * Auto-Configuration for {@link EnvironmentRepository} which use Azure KeyVault to decrypt values.
 *
 * @author Stefan Rempfer
 */
@AutoConfigureBefore ( ConfigServerAutoConfiguration.class )
@AutoConfigureAfter ( KeyVaultAutoConfiguration.class )
@Import ( KeyVaultRepositoryConfiguration.class )
@EnableConfigurationProperties ( KeyVaultEnvironmentProperties.class )
@Configuration ( proxyBeanMethods = false )
public class EnvironmentRepositoryAutoConfiguration {

    @Configuration ( proxyBeanMethods = false )
    static class KeyVaultFactoryConfig {

        @ConditionalOnBean ( KeyVaultOperation.class )
        // factory name have to start with the type name - see org.springframework.cloud.config.server.composite.CompositeUtils.getFactoryName()
        // to align it with the name of the profile the name is explicitly defined with a lower 'v'
        @Bean ( name = "keyvaultEnvironmentRepositoryFactory" )
        public KeyVaultEnvironmentRepositoryFactory keyVaultEnvironmentRepositoryFactory ( KeyVaultOperation keyVaultOperation ) {
            return new KeyVaultEnvironmentRepositoryFactory ( keyVaultOperation );
        }

    }
}

@Configuration ( proxyBeanMethods = false )
@Profile ( "keyvault" )
class KeyVaultRepositoryConfiguration {

    @ConditionalOnBean ( KeyVaultOperation.class )
    @Bean
    public KeyVaultEnvironmentRepository keyVaultEnvironmentRepository ( KeyVaultEnvironmentRepositoryFactory factory,
        KeyVaultEnvironmentProperties environmentProperties ) {
        return factory.build ( environmentProperties );
    }

}
