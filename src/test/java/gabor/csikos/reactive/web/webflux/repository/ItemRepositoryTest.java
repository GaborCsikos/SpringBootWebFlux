package gabor.csikos.reactive.web.webflux.repository;

import gabor.csikos.reactive.web.webflux.document.Item;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;



    @BeforeEach
    public void setUp(){
        List<Item> items = Arrays.asList(
                new Item(null, "TV", 500.0),
                new Item(null , "PC", 1500.0)
        );
        itemRepository.deleteAll().thenMany(Flux.fromIterable(items))
                .flatMap(itemRepository::save).doOnNext(item->System.out.println(item)).blockLast();
        //blocklast is here for the testing only

    }

    @Test
    public void readMonoItem(){
        StepVerifier.create(itemRepository.findByDescription("TV"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("TV")
                        && StringUtils.isNotEmpty(item.getId()))
                .verifyComplete();
    }
    @Test
    public void readFluxItem(){
        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }


    @Test
    public void saveItem(){
        Item item = new Item(null, "Book", 50.0);
        StepVerifier.create(itemRepository.save(item))
                .expectSubscription()
                .expectNextMatches(i -> i.getDescription().equals("Book")
                        && StringUtils.isNotEmpty(item.getId()))
                .verifyComplete();
    }

    @Test
    public void updateItem(){
        //create
        Item item = new Item(null, "Old book", 50.0);
        StepVerifier.create(itemRepository.save(item))
                .expectSubscription()
                .expectNextMatches(i -> i.getDescription().equals("Old book")
                        && StringUtils.isNotEmpty(item.getId()))
                .verifyComplete();

        //update the same
        String newTitle = "New book";
        Mono<Item> update = itemRepository.findByDescription("Old book")
                .map(i->{
                    i.setDescription(newTitle);
                    return i;
                }).flatMap(i-> itemRepository.save(i));
        StepVerifier.create(update)
                .expectSubscription()
                .expectNextMatches(i -> i.getDescription().equals(newTitle)
                        && StringUtils.isNotEmpty(item.getId()))
                .verifyComplete();
    }

    @Test
    public void deleteItem(){
        //create
        Item item = new Item(null, "Old book", 50.0);
        StepVerifier.create(itemRepository.save(item))
                .expectSubscription()
                .expectNextMatches(i -> i.getDescription().equals("Old book")
                        && StringUtils.isNotEmpty(item.getId()))
                .verifyComplete();

        //update the same
        Mono<Void> delete = itemRepository.findByDescription("Old book")
            .flatMap(i-> itemRepository.delete(i));
        StepVerifier.create(delete)
                .expectSubscription()
                .verifyComplete();
    }
}