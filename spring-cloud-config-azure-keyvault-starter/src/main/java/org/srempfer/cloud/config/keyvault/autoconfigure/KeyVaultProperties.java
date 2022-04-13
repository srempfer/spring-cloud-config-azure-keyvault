package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.azure.spring.cloud.autoconfigure.implementation.keyvault.secrets.properties.AzureKeyVaultPropertySourceProperties;

import java.time.Duration;

/**
 * Properties for Azure KeyVault access.
 *
 * @author Stefan Rempfer
 * @see
 * <a href="https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/spring/azure-spring-boot-starter-keyvault-secrets/README.md#custom-settings">Documentation of Azure Keyvault Secrets Spring Boot Starter</a>
 */
public class KeyVaultProperties {

    /**
     * Flag if Azure KeyVault access is enabled.
     */
    private Boolean enabled = Boolean.TRUE;

    /**
     * URI of the Azure KeyVault (e.g. https://my.vault.azure.net/).
     */
    private String uri;

    /**
     * Client id to access the Azure KeyVault.
     *
     * @see
     * <a href="https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/spring/azure-spring-boot-samples/azure-spring-boot-sample-keyvault-secrets#getting-started">Documentation of Azure Keyvault Secrets Spring Boot Sample</a>
     */
    private String clientId;


    /**
     * Client key to access the Azure KeyVault.
     *
     * @see
     * <a href="https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/spring/azure-spring-boot-samples/azure-spring-boot-sample-keyvault-secrets#getting-started">Documentation of Azure Keyvault Secrets Spring Boot Sample</a>
     */
    private String clientKey;

    /**
     * Tenant Id to access the Azure KeyVault.
     *
     * @see
     * <a href="https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/spring/azure-spring-boot-samples/azure-spring-boot-sample-keyvault-secrets#getting-started">Documentation of Azure Keyvault Secrets Spring Boot Sample</a>
     */
    private String tenantId;

    /**
     * Interval how long the retrieved secrets are cached before lookup in Azure KeyVault again.
     */
    private Duration refreshInterval = AzureKeyVaultPropertySourceProperties.DEFAULT_REFRESH_INTERVAL;

    /**
     * Flag if telemetry of usage is sent to Microsoft.
     *
     * @see
     * <a href="https://github.com/Azure/azure-sdk-for-java/blob/master/sdk/spring/azure-spring-boot-starter-keyvault-secrets/README.md#allow-telemetry">Documentation of Azure Keyvault Secrets Spring Boot Starter</a>
     */
    private Boolean allowTelemetry = Boolean.TRUE;

    /**
     * Flag if keys should be treated case sensitive.
     *
     * @see
     * <a href="https://github.com/Azure/azure-sdk-for-java/blob/master/sdk/spring/azure-spring-boot-starter-keyvault-secrets/README.md#case-sensitive-key-mode">Documentation of Azure Keyvault Secrets Spring Boot Starter</a>
     */
    private Boolean caseSensitive = Boolean.FALSE;

    public Boolean getEnabled () {
        return enabled;
    }

    public void setEnabled ( Boolean enabled ) {
        this.enabled = enabled;
    }

    public String getUri () {
        return uri;
    }

    public void setUri ( String uri ) {
        this.uri = uri;
    }

    public String getClientId () {
        return clientId;
    }

    public void setClientId ( String clientId ) {
        this.clientId = clientId;
    }

    public String getClientKey () {
        return clientKey;
    }

    public void setClientKey ( String clientKey ) {
        this.clientKey = clientKey;
    }

    public String getTenantId () {
        return tenantId;
    }

    public void setTenantId ( String tenantId ) {
        this.tenantId = tenantId;
    }

    public Duration getRefreshInterval () {
        return refreshInterval;
    }

    public void setRefreshInterval ( Duration refreshInterval ) {
        this.refreshInterval = refreshInterval;
    }

    public Boolean getAllowTelemetry () {
        return allowTelemetry;
    }

    public void setAllowTelemetry ( Boolean allowTelemetry ) {
        this.allowTelemetry = allowTelemetry;
    }

    public Boolean getCaseSensitive () {
        return caseSensitive;
    }

    public void setCaseSensitive ( Boolean caseSensitive ) {
        this.caseSensitive = caseSensitive;
    }
}
