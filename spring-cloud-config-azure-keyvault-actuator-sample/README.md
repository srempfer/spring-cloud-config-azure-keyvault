# Spring Cloud Config Azure KeyVault Starter Sample

This sample shows how to use [Spring Cloud Config Azure KeyVault Starter](../spring-cloud-config-azure-keyvault-starter/) in combination with Spring Boot Actuator. 

## Setup Azure KeyVault

* See the [Microsoft Getting Started Guide for Azure Key Vault Secrets](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-key-vault#create-a-new-azure-key-vault)
how to setup a KeyVault.
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
    }
  ]
}
```

Open `http://localhost:8888/actuator/health` in your favorite browser. You should see something like this:

```
{
  "status": "UP",
  "components": {
    "configServer": {
      "status": "UP",
      "details": {
        "repositories": [
          {
            "sources": [
              "keyvault-application-default"
            ],
            "name": "app",
            "profiles": [
              "default"
            ],
            "label": null
          }
        ]
      }
    },
    "discoveryComposite": {
      "description": "Discovery Client not initialized",
      "status": "UNKNOWN",
      "components": {
        "discoveryClient": {
          "description": "Discovery Client not initialized",
          "status": "UNKNOWN"
        }
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 1004624932864,
        "free": 676914393088,
        "threshold": 10485760,
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    },
    "refreshScope": {
      "status": "UP"
    }
  }
}
```