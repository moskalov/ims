package lv.notes.ims.controller;

import lombok.RequiredArgsConstructor;
import lv.notes.ims.dto.ProductDto;
import lv.notes.ims.dto.ProductUpdateDto;
import lv.notes.ims.mapper.ProductMapper;
import lv.notes.ims.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper mapper;

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ProductDto update(@PathVariable Long id, @RequestBody @Validated ProductUpdateDto product) {
        var fieldsToUpdate = mapper.map(product).setId(id);
        var updated = productService.update(fieldsToUpdate);
        return mapper.map(updated);
    }
}
