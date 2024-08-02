package br.com.stoom.store.business;

import br.com.stoom.store.dto.brand.BrandResponseDTO;
import br.com.stoom.store.dto.brand.CreateBrandDTO;
import br.com.stoom.store.dto.brand.UpdateBrandStatusDTO;
import br.com.stoom.store.exception.BrandNotFoundException;
import br.com.stoom.store.model.Brand;
import br.com.stoom.store.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;

    @Mock
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBrand() {
        CreateBrandDTO createBrandDTO = new CreateBrandDTO();
        Brand brand = new Brand();
        Brand savedBrand = new Brand();
        savedBrand.setId(1L);

        when(brandRepository.save(any(Brand.class))).thenReturn(savedBrand);

        BrandResponseDTO responseDTO = brandService.createBrand(createBrandDTO);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void testListBrandById() {
        Brand brand = new Brand();
        brand.setId(1L);

        when(brandRepository.findByIdAndDeactivationDateIsNull(1L)).thenReturn(Optional.of(brand));

        BrandResponseDTO responseDTO = brandService.listBrandById(1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        verify(brandRepository, times(1)).findByIdAndDeactivationDateIsNull(1L);
    }

    @Test
    void testListBrandByIdNotFound() {
        when(brandRepository.findByIdAndDeactivationDateIsNull(1L)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandService.listBrandById(1L));
        verify(brandRepository, times(1)).findByIdAndDeactivationDateIsNull(1L);
    }

    @Test
    void testListAllBrands() {
        Brand brand1 = new Brand();
        brand1.setId(1L);
        Brand brand2 = new Brand();
        brand2.setId(2L);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Brand> brandPage = new PageImpl<>(List.of(brand1, brand2));

        when(brandRepository.findAllByDeactivationDateIsNull(pageRequest)).thenReturn(brandPage);

        Page<BrandResponseDTO> responsePage = brandService.listAllBrands(pageRequest);

        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());
        verify(brandRepository, times(1)).findAllByDeactivationDateIsNull(pageRequest);
    }

    @Test
    void testUpdateBrandStatusActivate() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setDeactivationDate(Instant.now());

        UpdateBrandStatusDTO updateBrandStatusDTO = new UpdateBrandStatusDTO();
        updateBrandStatusDTO.setStatus(true);

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        BrandResponseDTO responseDTO = brandService.updateBrandStatus(1L, updateBrandStatusDTO);

        assertNotNull(responseDTO);
        assertNull(brand.getDeactivationDate());
        verify(brandRepository, times(1)).findById(1L);
        verify(brandRepository, times(1)).save(brand);
    }

    @Test
    void testUpdateBrandStatusDeactivate() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setDeactivationDate(null);

        UpdateBrandStatusDTO updateBrandStatusDTO = new UpdateBrandStatusDTO();
        updateBrandStatusDTO.setStatus(false);

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        BrandResponseDTO responseDTO = brandService.updateBrandStatus(1L, updateBrandStatusDTO);

        assertNotNull(responseDTO);
        assertNotNull(brand.getDeactivationDate());
        verify(brandRepository, times(1)).findById(1L);
        verify(brandRepository, times(1)).save(brand);
    }

    @Test
    void testUpdateBrandStatusNotFound() {
        UpdateBrandStatusDTO updateBrandStatusDTO = new UpdateBrandStatusDTO(true);
        updateBrandStatusDTO.setStatus(true);

        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandService.updateBrandStatus(1L, updateBrandStatusDTO));
        verify(brandRepository, times(1)).findById(1L);
    }

}
