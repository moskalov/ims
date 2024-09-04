package lv.notes.ims.repository;

import lv.notes.ims.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
