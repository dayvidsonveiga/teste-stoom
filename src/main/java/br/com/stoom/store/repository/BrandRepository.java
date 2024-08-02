package br.com.stoom.store.repository;

import br.com.stoom.store.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByIdAndDeactivationDateIsNull(Long id);

    Page<Brand> findAllByDeactivationDateIsNull(PageRequest pageRequest);

}
