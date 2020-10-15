package org.srempfer.cloud.config.keyvault.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.config.server.config.ConfigServerAutoConfiguration;
import org.springframework.cloud.config.server.encryption.EnvironmentEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.srempfer.cloud.config.keyvault.CompositeEnvironmentEncryptor;

import java.util.List;

/**
 * Auto-Configuration for {@link CompositeEnvironmentEncryptor} which use the available {@link EnvironmentEncryptor}.
 *
 * @author Stefan Rempfer
 */
@AutoConfigureBefore ( { ConfigServerAutoConfiguration.class } )
@AutoConfigureAfter ( { EnvironmentRepositoryAutoConfiguration.class } )
@Configuration ( proxyBeanMethods = false )
public class EnvironmentEncryptorAutoConfiguration {

    @Primary
    @Bean
    public CompositeEnvironmentEncryptor compositeEnvironmentEncryptor ( List<EnvironmentEncryptor> environmentEncryptors ) {
        return new CompositeEnvironmentEncryptor ( environmentEncryptors );
    }

}
