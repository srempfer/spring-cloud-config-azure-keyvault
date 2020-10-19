package org.srempfer.config.keyvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerSampleApplication {

    public static void main ( String[] args ) {
        SpringApplication.run ( ConfigServerSampleApplication.class );
    }

}
