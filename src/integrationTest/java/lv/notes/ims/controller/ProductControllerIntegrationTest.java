package lv.notes.ims.controller;

import lv.notes.ims.dto.ProductUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ProductControllerIntegrationTest extends AbstractControllerIntegrationTest {
    String PRODUCT_PATH = "/api/v1/products/763";

    @Test
    void shouldReturnBadRequestResponseWhenProductVersionNotPreset() {
        var expectedResponse = """ 
                {
                   "message":"Absent of mandatory request parameters or invalid parameter value",
                   "fields":[{"name":"version","value":"null","details":"must not be null"}]
                }""";

        var requestBody = new ProductUpdateDto()
                .setName("Test name")
                .setVersion(null);

        var request = MockMvcRequestBuilders.patch(PRODUCT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getAsBytes(requestBody));

        validateResponse(request, expectedResponse, status().isBadRequest());
    }

}
