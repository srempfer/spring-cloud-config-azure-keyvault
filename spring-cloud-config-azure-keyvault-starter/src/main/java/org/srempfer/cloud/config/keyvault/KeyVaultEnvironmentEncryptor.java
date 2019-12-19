package org.srempfer.cloud.config.keyvault;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.encryption.EnvironmentEncryptor;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;


/**
 * EnvironmentEncryptor that can decrypt property values prefixed with {keyvault} marker.
 *
 * @author Stefan Rempfer
 */
public class KeyVaultEnvironmentEncryptor implements EnvironmentEncryptor {

    private final KeyVaultOperation keyVaultOperation;

    public KeyVaultEnvironmentEncryptor( KeyVaultOperation keyVaultOperation ) {
        this.keyVaultOperation = keyVaultOperation;
    }

    @Override
    public Environment decrypt ( Environment environment ) {
        Environment result = new Environment ( environment );
        for ( PropertySource source : environment.getPropertySources () ) {
            Map<Object, Object> map = new LinkedHashMap<> ( source.getSource () );
            for ( Map.Entry<Object, Object> entry : new LinkedHashSet<> ( map.entrySet () ) ) {
                Object key = entry.getKey();
                Object value = entry.getValue ();
                String decryptedValue = decrypt ( value );
                if ( decryptedValue != null ) {
                    map.remove ( key );
                    map.put ( key.toString (), decryptedValue );
                }
            }
            result.add(new PropertySource(source.getName(), map));
        }
        return result;
    }

    private String decrypt ( Object value ) {
        if ( value != null && value.toString ().startsWith ( "{keyvault}" ) ) {
            String keyVaultPropertyKey = value.toString ().substring ( "{keyvault}".length () );
            return keyVaultOperation.get ( keyVaultPropertyKey );
        }
        return null;
    }

}
