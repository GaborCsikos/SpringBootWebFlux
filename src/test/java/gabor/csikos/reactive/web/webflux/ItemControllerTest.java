package gabor.csikos.reactive.web.webflux;


import gabor.csikos.reactive.web.webflux.document.Item;
import gabor.csikos.reactive.web.webflux.dto.ItemDTO;
import gabor.csikos.reactive.web.webflux.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class ItemControllerTest extends Integtest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ItemRepository itemReactiveRepository;

    public List<Item> data() {

        return Arrays.asList(new Item(null, "TV", 400.00),
                new Item("PC", "Pc", 1000.00));
    }


    @Before
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .blockLast();
    }

    @Test
    public void getAllItems() {
        webTestClient.get().uri("/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
                .expectBodyList(ItemDTO.class)
                .hasSize(2)
                .consumeWith((response) -> {
                    List<ItemDTO> items = response.getResponseBody();
                    items.forEach((item) -> {
                        assertTrue(item.getId() != null);
                    });

                });
    }

    @Test
    public void getOneItem() {

        webTestClient.get().uri("/item/PC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 149.99);

    }

    @Test
    public void getOneItem_notFound() {

        webTestClient.get().uri("/item/DEF")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void createItem() {

        ItemDTO item = new ItemDTO(null, "Iphone X", 999.99);

        webTestClient.post().uri("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), ItemDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone X")
                .jsonPath("$.price").isEqualTo(999.99);


    }

    @Test
    public void deleteItem() {

        webTestClient.delete().uri("/item/delete/PC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

    }

    @Test
    public void updateItem() {
        double newPrice = 129.99;
        ItemDTO item = new ItemDTO(null, "Beats HeadPhones", newPrice);

        webTestClient.put().uri("/item/PC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), ItemDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", newPrice);

    }

    @Test
    public void updateItem_notFound() {
        double newPrice = 129.99;
        Item item = new Item(null, "Beats HeadPhones", newPrice);

        webTestClient.put().uri("item/DEF")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound();

    }

}