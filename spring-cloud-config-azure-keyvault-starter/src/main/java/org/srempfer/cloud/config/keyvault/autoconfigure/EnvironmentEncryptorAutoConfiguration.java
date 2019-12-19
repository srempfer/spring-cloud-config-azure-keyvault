package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.server.config.ConfigServerMvcConfiguration;
import org.springframework.cloud.config.server.config.EncryptionAutoConfiguration;
import org.springframework.cloud.config.server.config.EnvironmentRepositoryConfiguration;
import org.springframework.cloud.config.server.encryption.EnvironmentEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.srempfer.cloud.config.keyvault.CompositeEnvironmentEncryptor;
import org.srempfer.cloud.config.keyvault.KeyVaultEnvironmentEncryptor;

import java.util.List;

/**
 * Auto-Configuration for {@link KeyVaultEnvironmentEncryptor} which use Azure KeyVault to decrypt values.
 *
 * @author Stefan Rempfer
 */
@AutoConfigureBefore ( { EncryptionAutoConfiguration.class, ConfigServerMvcConfiguration.class, EnvironmentRepositoryConfiguration.class } )
@AutoConfigureAfter ( KeyVaultAutoConfiguration.class )
@Configuration
public class EnvironmentEncryptorAutoConfiguration {

    @Primary
    @Bean
    public CompositeEnvironmentEncryptor compositeEnvironmentEncryptor ( List<EnvironmentEncryptor> environmentEncryptors ) {
        return new CompositeEnvironmentEncryptor ( environmentEncryptors );
    }

    @ConditionalOnBean ( KeyVaultOperation.class )
    @Bean
    public KeyVaultEnvironmentEncryptor keyVaultEnvironmentEncryptor ( KeyVaultOperation keyVaultOperation ) {
        return new KeyVaultEnvironmentEncryptor ( keyVaultOperation );
    }
}
