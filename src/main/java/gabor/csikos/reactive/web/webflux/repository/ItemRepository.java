package gabor.csikos.reactive.web.webflux.repository;

import gabor.csikos.reactive.web.webflux.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {
}
