package org.srempfer.config.keyvault;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest ( classes = ConfigServerSampleApplication.class )
@AutoConfigureMockMvc
class ConfigServerSampleApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void verifyPropertySources () throws Exception {
        mockMvc.perform ( get ( "/test-application/default/master" ) )
            .andExpect ( status ().isOk () )
            .andExpect ( content ().contentType ( MediaType.APPLICATION_JSON ) )
            .andExpect ( jsonPath ( "$.propertySources" ).isArray () )
            .andExpect ( jsonPath ( "$.propertySources", hasSize ( 1 ) ) )
            .andExpect ( jsonPath ( "$.propertySources[0].name", is ( "keyvault-application-default" ) ) )
            .andExpect ( jsonPath ( "$.propertySources[0].source", hasEntry ( "simplekey", "dummy" ) ) );
    }

    @Test
    void verifyThatHealthIndicatorEndpointIsPresent () throws Exception {
        mockMvc.perform ( get ( "/actuator/health/" ) )
            .andExpect ( status ().isOk () );
    }

    @Test
    void verifyThatOriginalHealthIndicatorIsNotConfigured () throws Exception {
        mockMvc.perform ( get ( "/actuator/health/keyVault/" ) )
            .andExpect ( status ().isNotFound () );
    }

}
