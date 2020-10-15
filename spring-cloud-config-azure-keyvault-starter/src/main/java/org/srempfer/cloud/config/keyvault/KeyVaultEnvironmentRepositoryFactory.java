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

    private final KeyVaultOperationFactory keyVaultOperationFactory;

    public KeyVaultEnvironmentRepositoryFactory ( KeyVaultOperationFactory keyVaultOperationFactory ) {
        this.keyVaultOperationFactory = keyVaultOperationFactory;
    }

    @Override
    public KeyVaultEnvironmentRepository build ( KeyVaultEnvironmentProperties environmentProperties ) {
        final KeyVaultOperation keyVaultOperation = keyVaultOperationFactory.build ( environmentProperties );
        return new KeyVaultEnvironmentRepository ( keyVaultOperation, environmentProperties );
    }

}
