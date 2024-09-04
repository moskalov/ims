package lv.notes.ims.service;

import lv.notes.ims.domain.Product;
import lv.notes.ims.excpetion.EntityNotFoundException;
import lv.notes.ims.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.stream.StreamSupport;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductModificationIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    TransactionTemplate transactionTemplate;
    Product existProduct;

    @BeforeEach
    void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        existProduct = transactionTemplate.execute(status -> saveProduct());
    }

    private Product saveProduct() {
        return productRepository.save(new Product()
                .setPrice(valueOf(22.32))
                .setName("tables")
                .setStock(22L));
    }

    @Test
    void shouldThrowExceptionWhenConcurrentModificationDetected() {
        var fieldsToUpdate = new Product().setId(existProduct.getId()).setName("updated");
        transactionTemplate.execute(status -> productService.update(fieldsToUpdate));
        assertThrows(OptimisticLockingFailureException.class, () -> transactionTemplate.execute(status -> productService.replace(existProduct)));
        assertThat(getProductsAmount()).isEqualTo(1);
    }

    @Test
    void shouldThrowExceptionWhenEntityWasNotFound() {
        var notExistProduct = new Product().setId(23L);
        var throwable = assertThrows(EntityNotFoundException.class, () -> productService.update(notExistProduct));
        assertThat(throwable.getMessage()).isEqualTo("Product with ID 23 not found");
    }

    @Test
    void shouldReturnProductWithUnchangedIdAfterReplacement() {
        var modified = new Product(existProduct).setDescription(null).setName("Chairs");
        var replaced = transactionTemplate.execute(status -> productService.replace(modified));

        assertThat(replaced.getId()).isEqualTo(existProduct.getId());
        assertThat(replaced.getName()).isEqualTo("Chairs");
        assertThat(replaced.getPrice()).isEqualTo(valueOf(22.32));
        assertThat(replaced.getStock()).isEqualTo(22L);
        assertThat(getProductsAmount()).isEqualTo(1);
        assertThat(replaced.getDescription()).isNull();
    }

    private Integer getProductsAmount() {
        var products = transactionTemplate.execute(status -> productRepository.findAll());
        return StreamSupport.stream(products.spliterator(), false)
                .toList()
                .size();
    }

    @Test
    void shouldReturnProductWithModifiedFields() {
        var fieldsToUpdate = new Product()
                .setId(existProduct.getId())
                .setName("chairs")
                .setStock(182L);

        var updatedID = transactionTemplate.execute(status -> productService.update(fieldsToUpdate).getId());
        var updated = transactionTemplate.execute(status -> productRepository.findById(updatedID).get());

        assertThat(updated.getId()).isEqualTo(existProduct.getId());
        assertThat(updated.getName()).isEqualTo("chairs");
        assertThat(updated.getPrice()).isEqualTo(valueOf(22.32));
        assertThat(updated.getStock()).isEqualTo(182L);
    }

}
