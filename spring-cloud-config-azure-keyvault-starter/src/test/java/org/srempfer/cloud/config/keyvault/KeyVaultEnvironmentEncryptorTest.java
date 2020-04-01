package org.srempfer.cloud.config.keyvault;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyVaultEnvironmentEncryptorTest {

    private KeyVaultEnvironmentEncryptor cut;
    private KeyVaultOperation keyVaultOperation;

    @Before
    public void init () {
        keyVaultOperation = mock ( KeyVaultOperation.class );
        cut = new KeyVaultEnvironmentEncryptor ( keyVaultOperation );
    }

    @Test
    public void verifyDecrypt () {
        when ( keyVaultOperation.list () ).thenReturn ( new String[]{ "test-key-to-decrypt" } );
        when ( keyVaultOperation.get ( "test-key-to-decrypt" ) ).thenReturn ( "decrypted-value" );
        when ( keyVaultOperation.get ( "test-key-to-decrypt-missing" ) ).thenReturn ( null );
        when ( keyVaultOperation.get ( "test-key-to-decrypt-exception" ) ).thenThrow ( new RuntimeException ( "Unknown error" ) );

        Environment environmentToDecrypt = new Environment ( "testApp", "testProfile1", "testProfile2" );

        Map<String, String> properties = new HashMap<> ();
        properties.put ( "test.key-normal", "normal-value" );
        properties.put ( "test.keyvault-encypted", "{keyvault}test-key-to-decrypt" );
        properties.put ( "test.keyvault-encypted-missing", "{keyvault}test-key-to-decrypt-missing" );
        properties.put ( "test.keyvault-encypted-exception", "{keyvault}test-key-to-decrypt-exception" );
        properties.put ( "test.other-encypted", "{other}test-key-to-decrypt" );
        PropertySource ps = new PropertySource ( "my-property-source", properties );
        environmentToDecrypt.add ( ps );

        Environment decryptedEnvironment = cut.decrypt ( environmentToDecrypt );

        assertThat ( decryptedEnvironment.getPropertySources () ).hasSize ( 1 );
        assertThat ( decryptedEnvironment.getName () ).isEqualTo ( "testApp" );
        assertThat ( decryptedEnvironment.getProfiles () ).containsExactly ( "testProfile1", "testProfile2" );

        PropertySource decryptedPropertySource = decryptedEnvironment.getPropertySources ().get ( 0 );
        assertThat ( decryptedPropertySource.getName () ).isEqualTo ( "my-property-source" );
        Map<Object, Object> decryptedProperties = (Map<Object, Object>) decryptedPropertySource.getSource ();
        assertThat ( decryptedProperties ).containsOnly (
            entry ( "test.key-normal", "normal-value" ),
            entry ( "test.keyvault-encypted", "decrypted-value" ),
            entry ( "missing.test.keyvault-encypted-missing", "<n/a>" ),
            entry ( "invalid.test.keyvault-encypted-exception", "<n/a>" ),
            entry ( "test.other-encypted", "{other}test-key-to-decrypt" ) );
    }
}
