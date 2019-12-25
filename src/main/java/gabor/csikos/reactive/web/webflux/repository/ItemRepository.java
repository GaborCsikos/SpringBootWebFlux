package gabor.csikos.reactive.web.webflux.repository;

import gabor.csikos.reactive.web.webflux.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {
    Mono<Item> findByDescription(String description); //just for demo I used Mono, here flux would be better

}
