package lv.notes.ims.service;

import lv.notes.ims.domain.Product;

public interface ProductService {

    Product update(Product product);

    Product replace(Product product);

}
