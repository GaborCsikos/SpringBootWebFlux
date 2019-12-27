package gabor.csikos.reactive.web.webflux.controller;

import gabor.csikos.reactive.web.webflux.document.Item;
import gabor.csikos.reactive.web.webflux.dto.ItemDTO;
import gabor.csikos.reactive.web.webflux.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;


    @GetMapping(value = "/items", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemDTO> returnFluxStream() {
        return itemService.getAllItems();
    }

    @GetMapping(value = "/item/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ResponseEntity<ItemDTO>> getOneItem(@PathVariable String id) {
        return itemService.findById(id);
    }

    @PostMapping(value = "/item")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ItemDTO>> createItem(@RequestBody ItemDTO item) {
        return itemService.save(new Item(item));
    }

    @DeleteMapping(value = "/item/delete/{id}")
    public Mono<ResponseEntity> deleteItem(@PathVariable String id) {
        return itemService.deleteById(id);
    }

    @PutMapping(value = "/item/{id}")
    public Mono<ResponseEntity<ItemDTO>> updateItem(@PathVariable String id,
                                                    @RequestBody ItemDTO item) {
        return itemService.updateItem(id, new Item(item));
    }
}
