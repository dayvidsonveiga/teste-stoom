package br.com.stoom.store.controller;

import br.com.stoom.store.business.interfaces.IBrandService;
import br.com.stoom.store.business.interfaces.IProductService;
import br.com.stoom.store.dto.brand.BrandResponseDTO;
import br.com.stoom.store.dto.brand.CreateBrandDTO;
import br.com.stoom.store.dto.brand.UpdateBrandStatusDTO;
import br.com.stoom.store.dto.product.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BrandControllerTest {

    @Mock
    private IBrandService brandService;

    @Mock
    private IProductService productService;

    @InjectMocks
    private BrandController brandController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBrand() {
        CreateBrandDTO createBrandDTO = new CreateBrandDTO();
        BrandResponseDTO brandResponseDTO = new BrandResponseDTO();
        brandResponseDTO.setId(1L);

        when(brandService.createBrand(any(CreateBrandDTO.class))).thenReturn(brandResponseDTO);

        ResponseEntity<BrandResponseDTO> response = brandController.createBrand(createBrandDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(brandService, times(1)).createBrand(any(CreateBrandDTO.class));
    }

    @Test
    void testListBrandById() {
        BrandResponseDTO brandResponseDTO = new BrandResponseDTO();
        brandResponseDTO.setId(1L);

        when(brandService.listBrandById(1L)).thenReturn(brandResponseDTO);

        ResponseEntity<BrandResponseDTO> response = brandController.listBrandById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(brandService, times(1)).listBrandById(1L);
    }

    @Test
    void testListAllBrands() {
        BrandResponseDTO brand1 = new BrandResponseDTO();
        brand1.setId(1L);
        BrandResponseDTO brand2 = new BrandResponseDTO();
        brand2.setId(2L);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BrandResponseDTO> brandPage = new PageImpl<>(List.of(brand1, brand2));

        when(brandService.listAllBrands(pageRequest)).thenReturn(brandPage);

        ResponseEntity<Page<BrandResponseDTO>> response = brandController.listAllBrands(0, 10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
        verify(brandService, times(1)).listAllBrands(pageRequest);
    }

    @Test
    void testUpdateBrandStatus() {
        UpdateBrandStatusDTO updateBrandStatusDTO = new UpdateBrandStatusDTO();
        updateBrandStatusDTO.setStatus(true);

        BrandResponseDTO brandResponseDTO = new BrandResponseDTO();
        brandResponseDTO.setId(1L);

        when(brandService.updateBrandStatus(anyLong(), any(UpdateBrandStatusDTO.class))).thenReturn(brandResponseDTO);

        ResponseEntity<BrandResponseDTO> response = brandController.updateBrandStatus(1L, updateBrandStatusDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(brandService, times(1)).updateBrandStatus(anyLong(), any(UpdateBrandStatusDTO.class));
    }

    @Test
    void testListAllProductsByBrand() {
        ProductResponseDTO product1 = new ProductResponseDTO();
        product1.setId(1L);
        ProductResponseDTO product2 = new ProductResponseDTO();
        product2.setId(2L);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductResponseDTO> productPage = new PageImpl<>(List.of(product1, product2));

        when(productService.listAllProductsByBrand(1L, pageRequest)).thenReturn(productPage);

        ResponseEntity<Page<ProductResponseDTO>> response = brandController.listAllProductsByBrand(1L, 0, 10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
        verify(productService, times(1)).listAllProductsByBrand(1L, pageRequest);
    }

}
