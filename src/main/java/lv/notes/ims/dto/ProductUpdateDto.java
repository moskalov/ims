package lv.notes.ims.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class ProductUpdateDto {
    @NotNull
    private Long version;
    private String description;
    private BigDecimal price;
    private String name;
    private Long stock;
}
