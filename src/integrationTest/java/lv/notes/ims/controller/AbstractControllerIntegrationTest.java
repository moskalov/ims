package lv.notes.ims.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lv.notes.ims.dto.ProductUpdateDto;
import lv.notes.ims.mapper.ProductMapperImpl;
import lv.notes.ims.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest
@Import({ProductMapperImpl.class, ObjectMapper.class})
public class AbstractControllerIntegrationTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    protected MockMvc mvc;

    @MockBean
    ProductService productService;

    @SneakyThrows
    protected byte[] getAsBytes(ProductUpdateDto product) {
        return mapper.writeValueAsBytes(product);
    }

    @SneakyThrows
    protected void validateResponse(MockHttpServletRequestBuilder requestMock, String json, ResultMatcher matcher) {
        var errorResponse = mvc.perform(requestMock)
                .andExpect(matcher).andReturn()
                .getResponse().getContentAsString();

        var actual = mapper.readTree(errorResponse);
        var expected = mapper.readTree(json);
        assertThat(actual).isEqualTo(expected);
    }
}
