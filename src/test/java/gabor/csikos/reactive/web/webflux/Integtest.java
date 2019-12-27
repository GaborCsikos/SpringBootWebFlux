package gabor.csikos.reactive.web.webflux;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class Integtest {

    @Test
    public void loadContext() {

    }
}
