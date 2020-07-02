package org.srempfer.cloud.config.keyvault.autoconfigure;

import com.microsoft.azure.keyvault.spring.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Properties for Azure KeyVault access.
 *
 * @author Stefan Rempfer
 * @see
 * <a href="https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/spring/azure-spring-boot-starter-keyvault-secrets/README.md#custom-settings">Documentation of Azure Keyvault Secrets Spring Boot Starter</a>
 */
@Validated
@ConfigurationProperties ( "spring.cloud.config.server.azure.keyvault" )
public class KeyVaultProperties {

    /**
     * Flag if Azure KeyVault access is enabled.
     */
    private Boolean enabled = Boolean.TRUE;

    /**
     * Timeout to acquire a access token in seconds.
     */
    private Long tokenAcquireTimeout = Constants.TOKEN_ACQUIRE_TIMEOUT_SECS;

    /**
     * URI of the Azure KeyVault (e.g. https://my.vault.azure.net/).
     */
    @NotBlank
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
    @NotBlank
    private String tenantId;

    /**
     * Interval in milli seconds how long the retrieved secrets are cached before lookup in Azure KeyVault again.
     */
    private Long refreshInterval = Constants.DEFAULT_REFRESH_INTERVAL_MS;

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

    public Long getTokenAcquireTimeout () {
        return tokenAcquireTimeout;
    }

    public void setTokenAcquireTimeout ( Long tokenAcquireTimeout ) {
        this.tokenAcquireTimeout = tokenAcquireTimeout;
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

    public Long getRefreshInterval () {
        return refreshInterval;
    }

    public void setRefreshInterval ( Long refreshInterval ) {
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
