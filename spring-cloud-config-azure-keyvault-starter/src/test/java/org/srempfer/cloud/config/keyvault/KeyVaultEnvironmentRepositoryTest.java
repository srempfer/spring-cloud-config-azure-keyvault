package org.srempfer.cloud.config.keyvault;

import com.microsoft.azure.keyvault.spring.KeyVaultOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.srempfer.cloud.config.keyvault.autoconfigure.KeyVaultEnvironmentProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KeyVaultEnvironmentRepositoryTest {

    private Map<String, String> data = new HashMap<> ();
    private KeyVaultEnvironmentRepository cut;

    @BeforeEach
    void init () {
        KeyVaultEnvironmentProperties properties = new KeyVaultEnvironmentProperties ();
        KeyVaultOperation keyVaultOperation = mock ( KeyVaultOperation.class );
        when ( keyVaultOperation.getPropertyNames () ).thenAnswer (
            invocation -> data.keySet ().toArray ( new String[ data.size () ] ) );
        when ( keyVaultOperation.getProperty ( anyString () ) ).thenAnswer (
            invocation -> data.get ( invocation.getArgument ( 0 ) ) );
        cut = new KeyVaultEnvironmentRepository ( keyVaultOperation, properties );
    }

    @Test
    void verifyIgnoreKeysWithWrongFormat () {
        data.put ( "some-key", "dummy" );
        data.put ( "application", "dummy" );
        data.put ( "application---default", "dummy" );
        data.put ( "application---default---master", "dummy" );
        data.put ( "application---default---master---", "dummy" );

        Environment environment = cut.findOne ( "application", "default", "master" );
        assertThat ( environment ).isNotNull ();
        assertThat ( environment.getName () ).isEqualTo ( "application" );
        assertThat ( environment.getProfiles () ).containsExactly ( "default" );
        assertThat ( environment.getLabel () ).isEqualTo ( "master" );
        assertThat ( environment.getPropertySources () ).isEmpty ();
    }

    @Test
    void verifySimpleKey () {
        data.put ( "application---default---master---simplekey", "dummy" );

        Environment environment = cut.findOne ( "application", "default", "master" );
        assertThat ( environment ).isNotNull ();
        assertThat ( environment.getName () ).isEqualTo ( "application" );
        assertThat ( environment.getProfiles () ).containsExactly ( "default" );
        assertThat ( environment.getLabel () ).isEqualTo ( "master" );

        List<PropertySource> propertySources = environment.getPropertySources ();
        assertThat ( propertySources ).hasSize ( 1 );

        PropertySource propertySource = propertySources.get ( 0 );
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-application-default" );
        Map<Object, Object> source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "simplekey", "dummy" ) );
    }

    @Test
    void verifyDotContainingKey () {
        data.put ( "application---default---master---test--key", "dummy" );

        Environment environment = cut.findOne ( "application", "default", "master" );
        assertThat ( environment ).isNotNull ();
        assertThat ( environment.getName () ).isEqualTo ( "application" );
        assertThat ( environment.getProfiles () ).containsExactly ( "default" );
        assertThat ( environment.getLabel () ).isEqualTo ( "master" );

        List<PropertySource> propertySources = environment.getPropertySources ();
        assertThat ( propertySources ).hasSize ( 1 );

        PropertySource propertySource = propertySources.get ( 0 );
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-application-default" );
        Map<Object, Object> source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "test.key", "dummy" ) );
    }

    @Test
    void verifyDotAndDashContainingKey () {
        data.put ( "application---default---master---test--key-one", "dummy" );

        Environment environment = cut.findOne ( "application", "default", "master" );
        assertThat ( environment ).isNotNull ();
        assertThat ( environment.getName () ).isEqualTo ( "application" );
        assertThat ( environment.getProfiles () ).containsExactly ( "default" );
        assertThat ( environment.getLabel () ).isEqualTo ( "master" );

        List<PropertySource> propertySources = environment.getPropertySources ();
        assertThat ( propertySources ).hasSize ( 1 );

        PropertySource propertySource = propertySources.get ( 0 );
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-application-default" );
        Map<Object, Object> source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "test.key-one", "dummy" ) );
    }

    @Test
    void verifyWithEmptyProfileAndLabel () {
        data.put ( "application---default---master---simplekey", "dummy" );

        Environment environment = cut.findOne ( "application", "", "" );
        assertThat ( environment ).isNotNull ();
        assertThat ( environment.getName () ).isEqualTo ( "application" );
        assertThat ( environment.getProfiles () ).containsExactly ( "default" );
        assertThat ( environment.getLabel () ).isEqualTo ( "master" );

        List<PropertySource> propertySources = environment.getPropertySources ();
        assertThat ( propertySources ).hasSize ( 1 );

        PropertySource propertySource = propertySources.get ( 0 );
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-application-default" );
        Map<Object, Object> source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "simplekey", "dummy" ) );
    }

    @Test
    void verifyWithDifferentAppAndNoDefault () {
        data.put ( "other-app---default---master---simplekey", "dummy" );

        Environment environment = cut.findOne ( "test-app", "default", "master" );
        assertThat ( environment ).isNotNull ();
        assertThat ( environment.getName () ).isEqualTo ( "test-app" );
        assertThat ( environment.getProfiles () ).containsExactly ( "default" );
        assertThat ( environment.getLabel () ).isEqualTo ( "master" );
        assertThat ( environment.getPropertySources () ).isEmpty ();
    }

    @Test
    void verifyWithDifferentProfileAndNoDefault () {
        data.put ( "test-app---other-profile---master---simplekey", "dummy" );

        Environment environment = cut.findOne ( "test-app", "default", "master" );
        assertThat ( environment ).isNotNull ();
        assertThat ( environment.getName () ).isEqualTo ( "test-app" );
        assertThat ( environment.getProfiles () ).containsExactly ( "default" );
        assertThat ( environment.getLabel () ).isEqualTo ( "master" );
        assertThat ( environment.getPropertySources () ).isEmpty ();
    }

    @Test
    void verifyPropertySourceOrder () {
        data.put ( "application---default---master---simplekey", "default-app-default-value" );
        data.put ( "application---test-profile---master---simplekey", "default-app-profile-value" );
        data.put ( "test-app---default---master---simplekey", "test-app-default-value" );
        data.put ( "test-app---test-profile---master---simplekey", "test-app-profile-value" );

        Environment environment = cut.findOne ( "test-app", "test-profile", "master" );
        assertThat ( environment ).isNotNull ();
        assertThat ( environment.getName () ).isEqualTo ( "test-app" );
        assertThat ( environment.getProfiles () ).containsExactly ( "default", "test-profile" );
        assertThat ( environment.getLabel () ).isEqualTo ( "master" );

        List<PropertySource> propertySources = environment.getPropertySources ();
        assertThat ( propertySources ).hasSize ( 4 );

        PropertySource propertySource = propertySources.get ( 0 );
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-test-app-test-profile" );
        Map<Object, Object> source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "simplekey", "test-app-profile-value" ) );

        propertySource = propertySources.get ( 1 );
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-test-app-default" );
        source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "simplekey", "test-app-default-value" ) );

        propertySource = propertySources.get ( 2 );
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-application-test-profile" );
        source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "simplekey", "default-app-profile-value" ) );

        propertySource = propertySources.get ( 3 );
        assertThat ( propertySource.getName () ).isEqualTo ( "keyvault-application-default" );
        source = (Map<Object, Object>) propertySource.getSource ();
        assertThat ( source ).contains ( entry ( "simplekey", "default-app-default-value" ) );
    }
}
