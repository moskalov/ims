package lv.notes.ims.mapper;

import lv.notes.ims.domain.Product;
import lv.notes.ims.dto.ProductDto;
import lv.notes.ims.dto.ProductUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    public abstract Product map(ProductUpdateDto update);

    public abstract ProductDto map(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void map(@MappingTarget Product product, Product fieldsToUpdate);

    public Product mergeFields(Product product, Product fieldsToUpdate) {
        var copy = new Product(product);
        this.map(copy, fieldsToUpdate);
        return copy;
    }

}
