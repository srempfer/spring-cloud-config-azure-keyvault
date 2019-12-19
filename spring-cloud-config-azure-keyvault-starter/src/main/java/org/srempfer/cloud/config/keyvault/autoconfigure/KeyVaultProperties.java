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
 * <a href="https://github.com/microsoft/azure-spring-boot/blob/master/azure-spring-boot-starters/azure-keyvault-secrets-spring-boot-starter/README.md#custom-settings"/>
 */
@Validated
@ConfigurationProperties ( prefix = "azure.keyvault" )
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
     * <a href="https://github.com/microsoft/azure-spring-boot/tree/master/azure-spring-boot-samples/azure-keyvault-secrets-spring-boot-sample#setup-azure-key-vault"/>
     */
    @NotBlank
    private String clientId;


    /**
     * Client key to access the Azure KeyVault.
     *
     * @see
     * <a href="https://github.com/microsoft/azure-spring-boot/tree/master/azure-spring-boot-samples/azure-keyvault-secrets-spring-boot-sample#setup-azure-key-vault"/>
     */
    @NotBlank
    private String clientKey;

    /**
     * Interval in milli seconds how long the retrieved secrets are cached before lookup in Azure KeyVault again.
     */
    private Long refreshInterval = Constants.DEFAULT_REFRESH_INTERVAL_MS;

    /**
     * Flag if telemetry of usage is sent to Microsoft.
     *
     * @see
     * <a href="https://github.com/microsoft/azure-spring-boot/blob/master/azure-spring-boot-starters/azure-keyvault-secrets-spring-boot-starter/README.md#allow-telemetry"/>
     */
    private Boolean allowTelemetry = Boolean.TRUE;

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
}
