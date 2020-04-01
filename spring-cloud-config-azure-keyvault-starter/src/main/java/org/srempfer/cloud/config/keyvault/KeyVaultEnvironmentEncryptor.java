package org.srempfer.cloud.config.keyvault;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger ( KeyVaultEnvironmentEncryptor.class );

    private final KeyVaultOperation keyVaultOperation;

    public KeyVaultEnvironmentEncryptor ( KeyVaultOperation keyVaultOperation ) {
        this.keyVaultOperation = keyVaultOperation;
    }

    @Override
    public Environment decrypt ( Environment environment ) {
        Environment result = new Environment ( environment );
        for ( PropertySource source : environment.getPropertySources () ) {
            Map<Object, Object> map = new LinkedHashMap<> ( source.getSource () );
            for ( Map.Entry<Object, Object> entry : new LinkedHashSet<> ( map.entrySet () ) ) {
                Object key = entry.getKey ();
                Object value = entry.getValue ();

                if ( isKeyVaultValue ( value ) ) {
                    processKeyVaultValue ( map, key, value );
                }
            }
            result.add ( new PropertySource ( source.getName (), map ) );
        }
        return result;
    }

    private void processKeyVaultValue ( Map<Object, Object> map, Object key, Object value ) {
        map.remove ( key );
        try {
            String decryptedValue = decrypt ( value );
            if ( decryptedValue != null ) {
                map.put ( key.toString (), decryptedValue );
            } else {
                map.put ( "missing." + key, "<n/a>" );
            }
        } catch ( Exception e ) {
            map.put ( "invalid." + key, "<n/a>" );

            String message = "Cannot decrypt key: " + key + " (" + e.getClass () + ": " + e.getMessage () + ")";
            if ( LOGGER.isDebugEnabled () ) {
                LOGGER.debug ( message, e );
            } else if ( LOGGER.isWarnEnabled () ) {
                LOGGER.warn ( message );
            }
        }
    }

    private boolean isKeyVaultValue ( Object value ) {
        return value != null && value.toString ().startsWith ( "{keyvault}" );
    }

    private String decrypt ( Object value ) {
        String keyVaultPropertyKey = value.toString ().substring ( "{keyvault}".length () );
        return keyVaultOperation.get ( keyVaultPropertyKey );
    }

}
