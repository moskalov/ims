package lv.notes.ims.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lv.notes.ims.domain.Product;
import lv.notes.ims.dto.ProductUpdateDto;
import lv.notes.ims.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductApiE2eTest extends AbstractApiE2eTest {
    String PRODUCT_PATH = "/api/v1/products";
    Product persistedProduct;

    @Autowired
    protected ProductRepository productRepository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        persistedProduct = transaction.execute(status -> productRepository.save(getProduct()));
    }

    Product getProduct() {
        return new Product().setDescription("description")
                .setPrice(BigDecimal.ZERO)
                .setName("Chairs")
                .setStock(33L);
    }

    @Test
    void shouldReturnUpdatedProductWhenNoErrorsHappen() throws JsonProcessingException {
        var fieldsToUpdate = new ProductUpdateDto()
                .setVersion(persistedProduct.getVersion())
                .setPrice(BigDecimal.valueOf(32.44))
                .setName("Tables")
                .setStock(33L);

        var responseProduct = RestAssured.given()
                .baseUri(baseUri)
                .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
                .body(mapper.writeValueAsBytes(fieldsToUpdate))
                .when()
                .patch(PRODUCT_PATH + "/" + persistedProduct.getId())
                .then()
                .statusCode(200)
                .extract()
                .body().as(Product.class);

        assertThat(responseProduct.getDescription()).isEqualTo(persistedProduct.getDescription());
        assertThat(responseProduct.getId()).isEqualTo(persistedProduct.getId());
        assertThat(responseProduct.getName()).isEqualTo(fieldsToUpdate.getName());
        assertThat(responseProduct.getPrice()).isEqualTo(fieldsToUpdate.getPrice());
        assertThat(responseProduct.getStock()).isEqualTo(fieldsToUpdate.getStock());
    }

    @Test
    void shouldReturnErrorWhenProductNotFound() throws JsonProcessingException {
        var notExistedId = "232";
        var fieldsToUpdate = new ProductUpdateDto()
                .setVersion(1L)
                .setPrice(BigDecimal.valueOf(32.44))
                .setName("Tables")
                .setStock(33L);

        RestAssured.given()
                .baseUri(baseUri)
                .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
                .body(mapper.writeValueAsBytes(fieldsToUpdate))
                .when()
                .patch(PRODUCT_PATH + "/" + notExistedId)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturnErrorWhenProductWasUpdateBefore() throws JsonProcessingException {
        var fieldsToUpdateByPerson1 = new ProductUpdateDto()
                .setVersion(persistedProduct.getVersion())
                .setPrice(BigDecimal.valueOf(32.44))
                .setName("tables")
                .setStock(3322L);

        RestAssured.given()
                .baseUri(baseUri)
                .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
                .body(mapper.writeValueAsBytes(fieldsToUpdateByPerson1))
                .when()
                .patch(PRODUCT_PATH + "/" + persistedProduct.getId())
                .then()
                .statusCode(200);

        var fieldsToUpdateByPerson2 = new ProductUpdateDto()
                .setVersion(persistedProduct.getVersion())
                .setPrice(BigDecimal.valueOf(23.99))
                .setStock(3231L);

        RestAssured.given()
                .baseUri(baseUri)
                .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON)
                .body(mapper.writeValueAsBytes(fieldsToUpdateByPerson2))
                .when()
                .patch(PRODUCT_PATH + "/" + persistedProduct.getId())
                .then()
                .statusCode(409);
    }
}
