package br.com.brothertec.springboot.bdd.steps;

import br.com.brothertec.springboot.integration.AbstractContainerBaseTest;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CucumberSpringIntegration extends AbstractContainerBaseTest {
}
