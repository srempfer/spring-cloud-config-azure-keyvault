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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyVaultEnvironmentEncryptorTest {

    private Map<String, String> data = new HashMap<> ();
    private KeyVaultEnvironmentEncryptor cut;

    @Before
    public void init () {
        KeyVaultOperation keyVaultOperation = mock ( KeyVaultOperation.class );
        when ( keyVaultOperation.list () ).thenAnswer (
            invocation -> data.keySet ().toArray ( new String[ data.size () ] ) );
        when ( keyVaultOperation.get ( anyString () ) ).thenAnswer (
            invocation -> data.get ( invocation.getArgument ( 0 ) ) );
        cut = new KeyVaultEnvironmentEncryptor ( keyVaultOperation );
    }

    @Test
    public void verifyDecrypt () {
        data.put ( "test-key-to-decrypt", "decrypted-value" );

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
