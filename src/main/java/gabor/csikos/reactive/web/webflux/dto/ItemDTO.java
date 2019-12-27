package gabor.csikos.reactive.web.webflux.dto;

import gabor.csikos.reactive.web.webflux.document.Item;
import lombok.Data;

@Data
public class ItemDTO {

    private String id;
    private String description;
    private Double price;

    public ItemDTO(String id, String description, Double price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public ItemDTO(Item item) {
        this.id = item.getId();
        this.description = item.getDescription();
        this.price = item.getPrice();
    }
}
