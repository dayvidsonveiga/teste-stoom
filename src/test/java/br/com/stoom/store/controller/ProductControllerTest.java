package br.com.stoom.store.controller;

import br.com.stoom.store.business.interfaces.IProductService;
import br.com.stoom.store.dto.product.CreateProductDTO;
import br.com.stoom.store.dto.product.ProductResponseDTO;
import br.com.stoom.store.dto.product.UpdateProductStatusDTO;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        CreateProductDTO createProductDTO = new CreateProductDTO();
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);

        when(productService.createProduct(any(CreateProductDTO.class))).thenReturn(productResponseDTO);

        ResponseEntity<ProductResponseDTO> response = productController.createProduct(createProductDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(productService, times(1)).createProduct(any(CreateProductDTO.class));
    }

    @Test
    void testListProductById() {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);

        when(productService.listProductById(1L)).thenReturn(productResponseDTO);

        ResponseEntity<ProductResponseDTO> response = productController.listProductById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(productService, times(1)).listProductById(1L);
    }

    @Test
    void testListAllProducts() {
        ProductResponseDTO product1 = new ProductResponseDTO();
        product1.setId(1L);
        ProductResponseDTO product2 = new ProductResponseDTO();
        product2.setId(2L);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductResponseDTO> productPage = new PageImpl<>(List.of(product1, product2));

        when(productService.listAllProducts(pageRequest)).thenReturn(productPage);

        ResponseEntity<Page<ProductResponseDTO>> response = productController.listAllProducts(0, 10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
        verify(productService, times(1)).listAllProducts(pageRequest);
    }

    @Test
    void testUpdateProductStatus() {
        UpdateProductStatusDTO updateProductStatusDTO = new UpdateProductStatusDTO();
        updateProductStatusDTO.setStatus(true);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);

        when(productService.updateProductStatus(anyLong(), any(UpdateProductStatusDTO.class))).thenReturn(productResponseDTO);

        ResponseEntity<ProductResponseDTO> response = productController.updateProductStatus(1L, updateProductStatusDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(productService, times(1)).updateProductStatus(anyLong(), any(UpdateProductStatusDTO.class));
    }

}
