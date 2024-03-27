package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    List<Product> findByPriceLessThanOrderByPriceAsc(Integer price);
    List<Product> findByPriceGreaterThanOrderByPriceAsc(Integer price);
    List<Product> findByPriceBetweenOrderByPriceAsc(Integer priceMin, Integer priceMax);
    // END
}
