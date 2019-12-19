package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyVaultAutoConfigurationTest {

    /**
     * Verifies the configuration by client id and key.
     *
     * <p>Hint: For security reasons the properties have to be provided by environment</p>
     */
    @Test
    public void verifyConfigurationByClientIdAndSecret () {
        new ApplicationContextRunner ()
            .withConfiguration ( AutoConfigurations.of ( KeyVaultAutoConfiguration.class ) )
            .run ( ( context ) -> {
                assertThat ( context ).hasSingleBean ( KeyVaultOperation.class );
                KeyVaultOperation vaultOperation = context.getBean ( KeyVaultOperation.class );
                assertThat ( vaultOperation.get ( "test-key-to-decrypt" ) ).isEqualTo ( "decrypted-value" );
            } );
    }

}
