package lv.notes.ims.dao;

import lv.notes.ims.domain.Product;
import lv.notes.ims.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.within;

public class ProductDaoIntegrationTest extends AbstractDaoRepositoryIntegrationTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void shouldSetTimestampsWhenNewProductIsSaved() {
        var product = new Product()
                .setPrice(BigDecimal.valueOf(130.44))
                .setName("Chair")
                .setStock(34L);

        var savedProductId = transaction.execute((status) -> productRepository.save(product)).getId();
        var savedProduct = transaction.execute(status -> productRepository.findById(savedProductId)).orElseThrow();

        assertThat(savedProduct.getLastUpdatedOn()).isCloseTo(Instant.now(), within(5, SECONDS));
        assertThat(savedProduct.getCreatedOn()).isCloseTo(Instant.now(), within(5, SECONDS));
    }

    @Test
    void should() {
        var product = new Product()
                .setPrice(BigDecimal.valueOf(130.44))
                .setDescription("description")
                .setName("Chair")
                .setStock(34L);

        var savedProductId = transaction.execute((status) -> productRepository.save(product)).getId();
        var savedProduct = transaction.execute(status -> productRepository.findById(savedProductId)).orElseThrow();

        assertThat(savedProduct.getDescription()).isEqualTo("description");
        assertThat(savedProduct.getPrice().toString()).isEqualTo("130.44");
        assertThat(savedProduct.getName()).isEqualTo("Chair");
        assertThat(savedProduct.getStock()).isEqualTo(34L);
    }

}
