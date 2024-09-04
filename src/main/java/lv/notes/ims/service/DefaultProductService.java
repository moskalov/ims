package lv.notes.ims.service;

import lombok.RequiredArgsConstructor;
import lv.notes.ims.domain.Product;
import lv.notes.ims.excpetion.EntityNotFoundException;
import lv.notes.ims.mapper.ProductMapper;
import lv.notes.ims.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Product update(Product fieldsToMerge) {
        var productToUpdate = findProductOrThrow(fieldsToMerge);
        var updated = productMapper.mergeFields(productToUpdate, fieldsToMerge);
        return productRepository.save(updated);
    }

    private Product findProductOrThrow(Product product) {
        var productID = product.getId();
        return productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product", productID));
    }

    @Override
    public Product replace(Product product) {
        if (!productRepository.existsById(product.getId())) {
            throw new EntityNotFoundException("Product", product.getId());
        }
        return productRepository.save(product);
    }

}
