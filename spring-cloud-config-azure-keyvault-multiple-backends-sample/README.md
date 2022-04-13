# Spring Cloud Config Azure KeyVault Starter Sample

This sample shows how to use [Spring Cloud Config Azure KeyVault Starter](../spring-cloud-config-azure-keyvault-starter/).

## Setup Azure KeyVault

* See the [Microsoft Getting Started Guide for Azure Key Vault Secrets](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-key-vault#create-a-new-azure-key-vault)
  how to setup a KeyVault.
* Add a secret with name `test-key-to-decrypt` and `decrypted-value`
* Add a secret with name `application---default---master---simplekey` and value `dummy`

## Add required properties

Open `application.properties` file and add below properties to specify your Azure KeyVault url, Azure service principal client id and client key.
```
spring.cloud.config.server.azure.keyvault.uri=put-your-azure-keyvault-url-here
spring.cloud.config.server.azure.keyvault.client-id=put-your-azure-client-id-here
spring.cloud.config.server.azure.keyvault.client-key=put-your-azure-client-key-here
spring.cloud.config.server.azure.keyvault.tenant-id=put-your-azure-tenant-id-here
```

## Testing

Open `http://localhost:8888/test-application/default/master` in your favorite browser. You should see something like this:

```
{
  "name": "test-application",
  "profiles": [
    "default"
  ],
  "label": "master",
  "version": null,
  "state": null,
  "propertySources": [
    {
      "name": "keyvault-application-default",
      "source": {
        "simplekey": "dummy"
      }
    },
    {
      "name": "classpath:/config-data/application.properties",
      "source": {
        "client.test.key": "test-value",
        "client.test.encrypted": "decrypted-value"
      }
    }
  ]
}
```
