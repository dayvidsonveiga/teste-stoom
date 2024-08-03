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
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class BrandService implements IBrandService {

    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public BrandResponseDTO createBrand(CreateBrandDTO createBrandDTO) {
        try {
            log.info("Creating brand...");
            final Brand brand = createBrandDTO.toBrand();
            var brandResponseDto = BrandResponseDTO.fromBrand(brandRepository.save(brand));
            log.info("Brand {} created", brandResponseDto.getName());
            return brandResponseDto;
        } catch (Exception e) {
            log.error("Error creating brand: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    public BrandResponseDTO listBrandById(Long brandId) {
        try {
            log.info("Listing brand by ID: {}", brandId);
            var brand = brandRepository.findByIdAndDeactivationDateIsNull(brandId)
                    .orElseThrow(() -> new BrandNotFoundException(brandId));
            return BrandResponseDTO.fromBrand(brand);
        } catch (BrandNotFoundException e) {
            log.warn("Brand not found: {}", brandId);
            throw e;
        } catch (Exception e) {
            log.error("Error listing brand by ID: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    public Page<BrandResponseDTO> listAllBrands(PageRequest pageRequest) {
        try {
            log.info("Listing all brands with page request: {}", pageRequest);
            final Page<Brand> brands = brandRepository.findAllByDeactivationDateIsNull(pageRequest);
            return brands.map(BrandResponseDTO::fromBrand);
        } catch (Exception e) {
            log.error("Error listing all brands: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    @Transactional
    public BrandResponseDTO updateBrandStatus(Long brandId, UpdateBrandStatusDTO updateBrandStatusDTO) {
        try {
            log.info("Updating brand status for ID: {}", brandId);
            var brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new BrandNotFoundException(brandId));

            boolean shouldActivate = updateBrandStatusDTO.isStatus();
            boolean isCurrentlyActive = brand.isActive();

            if (shouldActivate != isCurrentlyActive) {
                brand.setDeactivationDate(shouldActivate ? null : Instant.now());
                brandRepository.save(brand);
                log.info("Brand status updated for ID: {}", brandId);
            } else {
                log.info("No status change required for brand ID: {}", brandId);
            }

            return BrandResponseDTO.fromBrand(brand);
        } catch (BrandNotFoundException e) {
            log.warn("Brand not found: {}", brandId);
            throw e;
        } catch (Exception e) {
            log.error("Error updating brand status: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

}
