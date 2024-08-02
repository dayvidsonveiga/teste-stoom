package br.com.stoom.store.business;

import br.com.stoom.store.business.interfaces.IBrandService;
import br.com.stoom.store.dto.brand.BrandResponseDTO;
import br.com.stoom.store.dto.brand.CreateBrandDTO;
import br.com.stoom.store.dto.brand.UpdateBrandStatusDTO;
import br.com.stoom.store.exception.BrandNotFoundException;
import br.com.stoom.store.model.Brand;
import br.com.stoom.store.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BrandService implements IBrandService {

    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public BrandResponseDTO createBrand(CreateBrandDTO createBrandDTO) {
        final Brand brand = createBrandDTO.toBrand();
        return BrandResponseDTO.fromBrand(brandRepository.save(brand));
    }

    @Override
    public BrandResponseDTO listBrandById(Long brandId) {
        var brand = brandRepository.findByIdAndDeactivationDateIsNull(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
        return BrandResponseDTO.fromBrand(brand);
    }

    @Override
    public Page<BrandResponseDTO> listAllBrands(PageRequest pageRequest) {
        final Page<Brand> brands = brandRepository.findAllByDeactivationDateIsNull(pageRequest);
        return brands.map(BrandResponseDTO::fromBrand);
    }

    @Override
    @Transactional
    public BrandResponseDTO updateBrandStatus(Long brandId, UpdateBrandStatusDTO updateBrandStatusDTO) {
        var brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        boolean shouldActivate = updateBrandStatusDTO.isStatus();
        boolean isCurrentlyActive = brand.isActive();

        if (shouldActivate != isCurrentlyActive) {
            brand.setDeactivationDate(shouldActivate ? null : Instant.now());
            brandRepository.save(brand);
        }

        return BrandResponseDTO.fromBrand(brand);
    }

}
