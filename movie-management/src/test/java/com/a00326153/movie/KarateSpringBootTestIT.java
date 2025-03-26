package com.a00326153.movie;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("dev")
public class KarateSpringBootTestIT {

    @BeforeAll
    static void beforeAll() {
        System.setProperty("karate.env", "dev");
    }

    @Karate.Test
    Karate runTests() {
        return Karate.run("classpath:features").relativeTo(getClass());
    }
}

