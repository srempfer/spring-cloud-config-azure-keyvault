package org.srempfer.cloud.config.keyvault;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.springframework.cloud.config.server.environment.EnvironmentRepositoryFactory;
import org.srempfer.cloud.config.keyvault.autoconfigure.KeyVaultEnvironmentProperties;

/**
 * {@link EnvironmentRepositoryFactory} to build a {@link KeyVaultEnvironmentRepository}.
 *
 * @author Stefan Rempfer
 */
public class KeyVaultEnvironmentRepositoryFactory implements EnvironmentRepositoryFactory<KeyVaultEnvironmentRepository, KeyVaultEnvironmentProperties> {

    private final KeyVaultOperation keyVaultOperation;

    public KeyVaultEnvironmentRepositoryFactory ( KeyVaultOperation keyVaultOperation ) {
        this.keyVaultOperation = keyVaultOperation;
    }

    @Override
    public KeyVaultEnvironmentRepository build ( KeyVaultEnvironmentProperties environmentProperties ) {
        return new KeyVaultEnvironmentRepository ( keyVaultOperation, environmentProperties );
    }
}
