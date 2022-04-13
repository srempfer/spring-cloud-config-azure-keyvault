package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.azure.spring.cloud.autoconfigure.keyvault.environment.KeyVaultOperation;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.config.ConfigServerAutoConfiguration;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.srempfer.cloud.config.keyvault.KeyVaultEnvironmentRepository;
import org.srempfer.cloud.config.keyvault.KeyVaultEnvironmentRepositoryFactory;
import org.srempfer.cloud.config.keyvault.KeyVaultOperationFactory;

/**
 * Auto-Configuration for {@link EnvironmentRepository} which use Azure KeyVault to decrypt values.
 *
 * @author Stefan Rempfer
 */
@ConditionalOnClass ( KeyVaultOperation.class )
@AutoConfigureBefore ( ConfigServerAutoConfiguration.class )
@Import ( KeyVaultRepositoryConfiguration.class )
@EnableConfigurationProperties ( KeyVaultEnvironmentProperties.class )
@Configuration ( proxyBeanMethods = false )
public class EnvironmentRepositoryAutoConfiguration {

    @Configuration ( proxyBeanMethods = false )
    static class KeyVaultFactoryConfig {

        @Bean
        @ConditionalOnMissingBean
        public KeyVaultOperationFactory keyVaultOperationFactory () {
            return new KeyVaultOperationFactory ();
        }

        // factory name have to start with the type name - see org.springframework.cloud.config.server.composite.CompositeUtils.getFactoryName()
        // to align it with the name of the profile the name is explicitly defined with a lower 'v'
        @Bean ( name = "keyvaultEnvironmentRepositoryFactory" )
        @ConditionalOnMissingBean
        public KeyVaultEnvironmentRepositoryFactory keyVaultEnvironmentRepositoryFactory ( KeyVaultOperationFactory keyVaultOperationFactory ) {
            return new KeyVaultEnvironmentRepositoryFactory ( keyVaultOperationFactory );
        }

    }
}

@Configuration ( proxyBeanMethods = false )
@Profile ( "keyvault" )
class KeyVaultRepositoryConfiguration {

    @Bean
    public KeyVaultEnvironmentRepository keyVaultEnvironmentRepository ( KeyVaultEnvironmentRepositoryFactory factory,
        KeyVaultEnvironmentProperties environmentProperties ) {
        return factory.build ( environmentProperties );
    }

}
