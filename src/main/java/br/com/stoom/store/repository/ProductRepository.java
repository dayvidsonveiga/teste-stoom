package br.com.stoom.store.repository;

import br.com.stoom.store.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByIdAndDeactivationDateIsNull(Long productId);

    Page<Product> findAllByBrandIdAndDeactivationDateIsNull(Long productId, PageRequest pageRequest);

    Page<Product> findAllByCategoryIdAndDeactivationDateIsNull(Long categoryId, PageRequest pageRequest);

    Page<Product> findAllByDeactivationDateIsNull(PageRequest pageRequest);

}