package gabor.csikos.reactive.web.webflux.service;

import gabor.csikos.reactive.web.webflux.document.Item;
import gabor.csikos.reactive.web.webflux.dto.ItemDTO;
import gabor.csikos.reactive.web.webflux.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Flux<ItemDTO> getAllItems() {
        return itemRepository.findAll().map(item -> new ItemDTO(item));
    }


    public Mono<ResponseEntity<ItemDTO>> findById(String id) {
        return itemRepository.findById(id).map((item) ->
                new ResponseEntity<>(new ItemDTO(item), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    public Mono<ResponseEntity<ItemDTO>> save(Item item) {
        return itemRepository.save(item).map(x -> new ResponseEntity<>(new ItemDTO(x), HttpStatus.CREATED));
    }


    public Mono<ResponseEntity> deleteById(String id) {
        return itemRepository.deleteById(id).map(aVoid -> new ResponseEntity(HttpStatus.OK));
    }

    public Mono<ResponseEntity<ItemDTO>> updateItem(String id, Item item) {
        return itemRepository.findById(id)
                .flatMap(currentItem -> {
                    currentItem.setPrice(item.getPrice());
                    currentItem.setDescription(item.getDescription());
                    return itemRepository.save(currentItem);
                }).map((x) ->
                        new ResponseEntity<>(new ItemDTO(x), HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
