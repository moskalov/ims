package lv.notes.ims.mapper;

import lv.notes.ims.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;

import static java.time.Instant.ofEpochMilli;
import static org.assertj.core.api.Assertions.assertThat;


class ProductMapperTest {
    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    public void shouldRetainNonNullFieldsWhenMergingProductFields() {
        var original = new Product().setId(100L)
                .setPrice(BigDecimal.valueOf(10.5))
                .setDescription("description")
                .setName("chair")
                .setStock(75L);

        var fieldsToBeUpdated = new Product()
                .setDescription("updated_description")
                .setStock(83L);

        var updated = productMapper.mergeFields(original, fieldsToBeUpdated);
        assertThat(updated.getDescription()).isEqualTo("updated_description");
        assertThat(updated.getName()).isEqualTo("chair");
        assertThat(updated.getStock()).isEqualTo(83L);
        assertThat(updated.getId()).isEqualTo(100L);
    }
}