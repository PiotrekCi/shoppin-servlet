package org.shop.com.shoppin.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@CucumberContextConfiguration
@ActiveProfiles("test")
@ContextConfiguration
@RunWith(SpringRunner.class)
public class CucumberSpringContextConfiguration {
}
