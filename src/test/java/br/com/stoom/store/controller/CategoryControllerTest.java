package br.com.stoom.store.controller;

import br.com.stoom.store.business.interfaces.ICategoryService;
import br.com.stoom.store.business.interfaces.IProductService;
import br.com.stoom.store.dto.category.CategoryResponseDTO;
import br.com.stoom.store.dto.category.CreateCategoryDTO;
import br.com.stoom.store.dto.category.UpdateCategoryStatusDTO;
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

class CategoryControllerTest {

    @Mock
    private ICategoryService categoryService;

    @Mock
    private IProductService productService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() {
        CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO();
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(1L);

        when(categoryService.createCategory(any(CreateCategoryDTO.class))).thenReturn(categoryResponseDTO);

        ResponseEntity<CategoryResponseDTO> response = categoryController.createCategory(createCategoryDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(categoryService, times(1)).createCategory(any(CreateCategoryDTO.class));
    }

    @Test
    void testListCategoryById() {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(1L);

        when(categoryService.listCategoryById(1L)).thenReturn(categoryResponseDTO);

        ResponseEntity<CategoryResponseDTO> response = categoryController.listCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(categoryService, times(1)).listCategoryById(1L);
    }

    @Test
    void testListAllCategory() {
        CategoryResponseDTO category1 = new CategoryResponseDTO();
        category1.setId(1L);
        CategoryResponseDTO category2 = new CategoryResponseDTO();
        category2.setId(2L);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CategoryResponseDTO> categoryPage = new PageImpl<>(List.of(category1, category2));

        when(categoryService.listAllCategories(pageRequest)).thenReturn(categoryPage);

        ResponseEntity<Page<CategoryResponseDTO>> response = categoryController.listAllCategory(0, 10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
        verify(categoryService, times(1)).listAllCategories(pageRequest);
    }

    @Test
    void testUpdateCategoryStatus() {
        UpdateCategoryStatusDTO updateCategoryStatusDTO = new UpdateCategoryStatusDTO();
        updateCategoryStatusDTO.setStatus(true);

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(1L);

        when(categoryService.updateCategoryStatus(anyLong(), any(UpdateCategoryStatusDTO.class))).thenReturn(categoryResponseDTO);

        ResponseEntity<CategoryResponseDTO> response = categoryController.updateCategoryStatus(1L, updateCategoryStatusDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(categoryService, times(1)).updateCategoryStatus(anyLong(), any(UpdateCategoryStatusDTO.class));
    }

    @Test
    void testListAllProductsByCategory() {
        ProductResponseDTO product1 = new ProductResponseDTO();
        product1.setId(1L);
        ProductResponseDTO product2 = new ProductResponseDTO();
        product2.setId(2L);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductResponseDTO> productPage = new PageImpl<>(List.of(product1, product2));

        when(productService.listAllProductsByCategory(1L, pageRequest)).thenReturn(productPage);

        ResponseEntity<Page<ProductResponseDTO>> response = categoryController.listAllProductsByCategory(1L, 0, 10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
        verify(productService, times(1)).listAllProductsByCategory(1L, pageRequest);
    }

}
