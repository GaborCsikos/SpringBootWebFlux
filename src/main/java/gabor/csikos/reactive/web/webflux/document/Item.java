package gabor.csikos.reactive.web.webflux.document;

import gabor.csikos.reactive.web.webflux.dto.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    private String id;
    private String description;
    private Double price;

    public Item(ItemDTO item) {
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.id = item.getId();
    }
}
