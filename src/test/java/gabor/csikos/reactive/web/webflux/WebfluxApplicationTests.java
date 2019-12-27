package gabor.csikos.reactive.web.webflux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

class WebfluxApplicationTests extends Integtest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void fluxTest() {

        List<Integer> expectedIntegerList = Arrays.asList(1, 2, 3, 4);

        EntityExchangeResult<List<Integer>> entityExchangeResult = webTestClient
                .get().uri("/flux")
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectedIntegerList, entityExchangeResult.getResponseBody());
    }

    @Test
    public void fluxTestInline() {

        List<Integer> expectedIntegerList = Arrays.asList(1, 2, 3, 4);

        webTestClient
                .get().uri("/flux")
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(expectedIntegerList, response.getResponseBody());
                });
    }
}
