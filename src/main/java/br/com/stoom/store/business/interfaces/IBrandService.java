package br.com.stoom.store.business.interfaces;

import br.com.stoom.store.dto.brand.BrandResponseDTO;
import br.com.stoom.store.dto.brand.CreateBrandDTO;
import br.com.stoom.store.dto.brand.UpdateBrandStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IBrandService {

    BrandResponseDTO createBrand(CreateBrandDTO createBrandDTO);

    BrandResponseDTO listBrandById(Long brandId);

    Page<BrandResponseDTO> listAllBrands(PageRequest pageRequest);

    BrandResponseDTO updateBrandStatus(Long brandId, UpdateBrandStatusDTO updateBrandStatusDTO);

}
