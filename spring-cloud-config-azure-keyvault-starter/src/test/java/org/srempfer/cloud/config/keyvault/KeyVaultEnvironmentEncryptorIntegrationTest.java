package org.srempfer.cloud.config.keyvault;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.srempfer.cloud.config.keyvault.autoconfigure.KeyVaultAutoConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@RunWith ( SpringRunner.class )
@SpringBootTest ( classes = KeyVaultAutoConfiguration.class )
public class KeyVaultEnvironmentEncryptorIntegrationTest {

    @Autowired
    private KeyVaultOperation keyVaultOperation;

    @Test
    public void verifyDecrypt () {
        KeyVaultEnvironmentEncryptor cut = new KeyVaultEnvironmentEncryptor ( keyVaultOperation );

        Environment environmentToDecrypt = new Environment ( "testApp", "testProfile1", "testProfile2" );

        Map<String, String> properties1 = new HashMap<> ();
        properties1.put ( "test.key-normal", "normal-value" );
        properties1.put ( "test.keyvault-encypted", "{keyvault}test-key-to-decrypt" );
        properties1.put ( "test.other-encypted", "{other}test-key-to-decrypt" );
        PropertySource ps1 = new PropertySource ( "ps1", properties1 );
        environmentToDecrypt.add ( ps1 );

        Environment decryptedEnvironment = cut.decrypt ( environmentToDecrypt );

        assertThat ( decryptedEnvironment.getPropertySources () ).hasSize ( 1 );
        assertThat ( decryptedEnvironment.getName () ).isEqualTo ( "testApp" );
        assertThat ( decryptedEnvironment.getProfiles () ).containsExactly ( "testProfile1", "testProfile2" );

        PropertySource decryptedPropertySource = decryptedEnvironment.getPropertySources ().get ( 0 );
        assertThat ( decryptedPropertySource.getName () ).isEqualTo ( "ps1" );
        Map<Object, Object> decryptedProperties1 = (Map<Object, Object>) decryptedPropertySource.getSource ();
        assertThat ( decryptedProperties1 ).containsOnly (
            entry ( "test.key-normal", "normal-value" ),
            entry ( "test.keyvault-encypted", "decrypted-value" ),
            entry ( "test.other-encypted", "{other}test-key-to-decrypt" ) );
    }
}
