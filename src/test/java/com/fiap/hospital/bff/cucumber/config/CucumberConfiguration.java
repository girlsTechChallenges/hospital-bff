package com.fiap.hospital.bff.cucumber.config;

import com.fiap.hospital.bff.HospitalMain;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(
    classes = HospitalMain.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("e2e")
public class CucumberConfiguration {
    
    @LocalServerPort
    private int port;
    
    public int getPort() {
        return port;
    }
}
