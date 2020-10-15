package org.srempfer.cloud.config.keyvault.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.server.support.EnvironmentRepositoryProperties;
import org.springframework.core.Ordered;
import org.srempfer.cloud.config.keyvault.KeyVaultEnvironmentRepository;

/**
 * Properties for the {@link KeyVaultEnvironmentRepository}.
 *
 * @author Stefan Rempfer
 */
@ConfigurationProperties ( "spring.cloud.config.server.azure.keyvault" )
public class KeyVaultEnvironmentProperties extends KeyVaultProperties implements EnvironmentRepositoryProperties {

    private int order = Ordered.LOWEST_PRECEDENCE - 10;

    public int getOrder () {
        return this.order;
    }

    @Override
    public void setOrder ( int order ) {
        this.order = order;
    }

}
