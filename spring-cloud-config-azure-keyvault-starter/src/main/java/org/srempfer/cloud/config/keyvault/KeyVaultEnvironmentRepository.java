package org.srempfer.cloud.config.keyvault;

import com.azure.spring.cloud.autoconfigure.keyvault.environment.KeyVaultOperation;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.srempfer.cloud.config.keyvault.autoconfigure.KeyVaultEnvironmentProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * An {@link EnvironmentRepository} that picks up data from a Azure KeyVault.
 *
 * @author Stefan Rempfer
 */
public class KeyVaultEnvironmentRepository extends KeyVaultEnvironmentEncryptor implements EnvironmentRepository, Ordered {

    private final KeyVaultOperation keyVaultOperation;
    private int order;

    public KeyVaultEnvironmentRepository ( KeyVaultOperation keyVaultOperation, KeyVaultEnvironmentProperties properties ) {
        super ( keyVaultOperation );
        this.keyVaultOperation = keyVaultOperation;
        this.order = properties.getOrder ();
    }

    @Override
    public Environment findOne ( String application, String profile, String label ) {
        String config = application;
        if ( !StringUtils.hasLength ( label ) ) {
            label = "master";
        }
        if ( !StringUtils.hasLength ( profile ) ) {
            profile = "default";
        }
        if ( !profile.startsWith ( "default" ) ) {
            profile = "default," + profile;
        }
        String[] profiles = StringUtils.commaDelimitedListToStringArray ( profile );
        Environment environment = new Environment ( application, profiles, label, null, null );
        if ( !config.startsWith ( "application" ) ) {
            config = "application," + config;
        }

        List<String> applications = new ArrayList<> ( new LinkedHashSet<> (
            Arrays.asList ( StringUtils.commaDelimitedListToStringArray ( config ) ) ) );
        List<String> envs = new ArrayList<> (
            new LinkedHashSet<> ( Arrays.asList ( profiles ) ) );
        Collections.reverse ( applications );
        Collections.reverse ( envs );

        String[] keyVaultKeys = keyVaultOperation.getPropertyNames ();

        for ( String app : applications ) {
            for ( String env : envs ) {
                String prefix = buildPrefix ( app, env, label );
                Map<String, String> source = findProperties ( keyVaultKeys, prefix );
                if ( !source.isEmpty () ) {
                    environment.add ( new PropertySource ( "keyvault-" + app + "-" + env, source ) );
                }
            }
        }
        return environment;
    }

    private Map<String, String> findProperties ( String[] keyVaultKeys, String prefix ) {
        Map<String, String> source = new HashMap<> ();
        for ( String keyVaultKey : keyVaultKeys ) {
            if ( keyVaultKey.startsWith ( prefix ) ) {
                String propertyKey = keyVaultKey.substring ( prefix.length () );
                if ( propertyKey.length () == 0 ) {
                    continue;
                }
                propertyKey = StringUtils.replace ( propertyKey, "--", "." );
                String propertyValue = keyVaultOperation.getProperty ( keyVaultKey );
                source.put ( propertyKey, propertyValue );
            }
        }
        return source;
    }

    private String buildPrefix ( String application, String profile, String label ) {
        String sanitizedApplication = StringUtils.replace ( application, "/", "----" );
        String sanitizedLabel = StringUtils.replace ( label, "/", "----" );
        return sanitizedApplication + "---" + profile + "---" + sanitizedLabel + "---";
    }

    @Override
    public int getOrder () {
        return this.order;
    }

    public void setOrder ( int order ) {
        this.order = order;
    }
}
