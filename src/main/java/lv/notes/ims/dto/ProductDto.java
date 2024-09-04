package lv.notes.ims.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class ProductDto {
    private Long id;
    private Long version;
    private String description;
    private BigDecimal price;
    private String stock;
    private String name;
}
