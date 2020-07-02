[![Build Status](https://dev.azure.com/srempfer-github/spring-cloud-config-azure-keyvault/_apis/build/status/ci?branchName=master)](https://dev.azure.com/srempfer-github/spring-cloud-config-azure-keyvault/_build/latest?definitionId=4&branchName=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.srempfer%3Aspring-cloud-config-azure-keyvault&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.github.srempfer%3Aspring-cloud-config-azure-keyvault)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.srempfer/spring-cloud-config-azure-keyvault-starter)](https://search.maven.org/search?q=g:io.github.srempfer%20AND%20a:spring-cloud-config-azure-keyvault-starter)
# Spring Cloud Config Azure KeyVault Starter

The Starter brings you the ability to use the *encrypted values feature* in your remote property sources. The values securely stored as secrets in Azure KeyVault.  
In addition to that you could use your Azure KeyVault as backend to store your properties.

## Sample Code
Please refer to [sample project here](./spring-cloud-config-azure-keyvault-sample)

## Quick Start

### Add the dependency

The starter is published on Maven Central. If you're using Maven add the following dependency: 

```xml
<dependency>
    <groupId>io.github.srempfer</groupId>
    <artifactId>spring-cloud-config-azure-keyvault-starter</artifactId>
    <version>0.4.0-SNAPSHOT</version>
</dependency>
```

### Settings
To get it working you have to specify your Azure KeyVault url, Azure service principal client id and client key.
```
spring.cloud.config.server.azure.keyvault.uri=put-your-azure-keyvault-url-here
spring.cloud.config.server.azure.keyvault.client-id=put-your-azure-client-id-here
spring.cloud.config.server.azure.keyvault.client-key=put-your-azure-client-key-here
spring.cloud.config.server.azure.keyvault.tenant-id=put-your-azure-tenant-id-here
```

For the interaction with Azure KeyVault the [Azure Key Vault Secrets Spring Boot Starter](https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/spring/azure-spring-boot-starter-keyvault-secrets) 
is used.

### Usage

#### Decryption
The starter provides an Azure KeyVault based EnvironmentEncryptor and works similar to the [cipher](https://cloud.spring.io/spring-cloud-config/reference/html/#_encryption_and_decryption)
based mechanism.

```
spring.datasource.username=dbuser
spring.datasource.password={keyvault}secret-name-in-key-vault
```

#### KeyVault Backend

The starter provides an Azure KeyVault based [EnvironmentRepository](https://cloud.spring.io/spring-cloud-config/reference/html/#_environment_repository)
and works similar to the [JDBC Backend](https://cloud.spring.io/spring-cloud-config/reference/html/#_jdbc_backend).

The format of the secret names have to be `{application}---{profile}---{label}---keyname`

Secret Name  | Key
------------ | -------------
application---default---master---simplekey | simplekey
application---default---master---test-key | test-key
application---default---master---spring--datasource--password | spring.datasource.password
