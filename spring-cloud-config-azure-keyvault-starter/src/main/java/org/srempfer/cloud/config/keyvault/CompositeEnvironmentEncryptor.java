package org.srempfer.cloud.config.keyvault;

import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.server.encryption.EnvironmentEncryptor;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;

/**
 * A {@link EnvironmentEncryptor} that is composed of other environment encryptors and delegates calls to each of them in order.
 *
 * @author Stefan Rempfer
 */
public class CompositeEnvironmentEncryptor implements EnvironmentEncryptor {

    private final List<EnvironmentEncryptor> environmentEncryptors;

    public CompositeEnvironmentEncryptor ( List<EnvironmentEncryptor> environmentEncryptors ) {
        AnnotationAwareOrderComparator.sort ( environmentEncryptors );
        this.environmentEncryptors = environmentEncryptors;
    }

    @Override
    public Environment decrypt ( Environment environment ) {
        Environment environmentToDecrypt = environment;
        for ( EnvironmentEncryptor encryptor : environmentEncryptors ) {
            environmentToDecrypt = encryptor.decrypt ( environmentToDecrypt );
        }
        return environmentToDecrypt;
    }
}
