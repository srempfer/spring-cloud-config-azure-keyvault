[![Build Status](https://dev.azure.com/srempfer-github/spring-cloud-config-azure-keyvault/_apis/build/status/ci?branchName=master)](https://dev.azure.com/srempfer-github/spring-cloud-config-azure-keyvault/_build/latest?definitionId=4&branchName=master)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.srempfer%3Aspring-cloud-config-azure-keyvault&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.github.srempfer%3Aspring-cloud-config-azure-keyvault)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.srempfer/spring-cloud-config-azure-keyvault-starter)](https://search.maven.org/search?q=g:io.github.srempfer%20AND%20a:spring-cloud-config-azure-keyvault-starter)
# Spring Cloud Config Azure KeyVault Starter

The Starter brings you the ability to use the *encrypted values feature* in your remote property sources. The values securely stored as secrets in Azure KeyVault.  
In addition to that you could use your Azure KeyVault as backend to store your properties.

## Sample Code
Please refer to the different sample projects
 - [Multiple Backends via Profile](./spring-cloud-config-azure-keyvault-multiple-backends-sample)
 - [Single Backend via Profile](./spring-cloud-config-azure-keyvault-single-backend-sample)
 - [Single Backend via Profile in combination with Spring Boot Actuator](./spring-cloud-config-azure-keyvault-actuator-sample)
 - [Multiple Backends via Composite Profile](./spring-cloud-config-azure-keyvault-composite-sample)
 - [Multiple KeyVaults via Composite Profile](./spring-cloud-config-azure-keyvault-multiple-keyvaults-sample)

## Quick Start

### Add the dependency

The starter is published on Maven Central. If you're using Maven add the following dependency: 

```xml
<dependency>
    <groupId>io.github.srempfer</groupId>
    <artifactId>spring-cloud-config-azure-keyvault-starter</artifactId>
    <version>0.9.0-SNAPSHOT</version>
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

For the interaction with Azure KeyVault the [Spring Cloud Azure Starter Keyvault Secrets](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring/spring-cloud-azure-starter-keyvault-secrets)
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

| Secret Name                                                   | Application    | Profile       | Label       | Key                        |
|---------------------------------------------------------------|----------------|---------------|-------------|----------------------------|
| application---default---master---simplekey                    | application    |               |             | simplekey                  |
| application---default---master---test-key                     | application    |               |             | test-key                   |
| application---default---master---spring--datasource--password | application    |               |             | spring.datasource.password |
| application---simpleprofile---master---simplekey              | application    | simpleprofile |             | simplekey                  |
| application---profile-one---master---simplekey                | application    | profile-one   |             | simplekey                  |
| application---default---simplelabel---simplekey               | application    |               | simplelabel | simplekey                  |
| application---default---label-one---simplekey                 | application    |               | label-one   | simplekey                  |
| application---default---v1----prod---simplekey                | application    |               | v1/prod     | simplekey                  |
| my-application---default---master---simplekey                 | my-application |               |             | simplekey                  |
| Org1----MyApp---default---master---simplekey                  | Org1/MyApp     |               |             | simplekey                  |
