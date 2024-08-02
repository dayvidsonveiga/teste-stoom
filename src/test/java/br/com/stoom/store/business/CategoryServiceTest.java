package br.com.stoom.store.business;

import br.com.stoom.store.dto.category.CategoryResponseDTO;
import br.com.stoom.store.dto.category.CreateCategoryDTO;
import br.com.stoom.store.dto.category.UpdateCategoryStatusDTO;
import br.com.stoom.store.exception.BrandNotFoundException;
import br.com.stoom.store.model.Category;
import br.com.stoom.store.repository.CategoryRepository;
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

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() {
        CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO();
        Category category = new Category();
        Category savedCategory = new Category();
        savedCategory.setId(1L);

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponseDTO responseDTO = categoryService.createCategory(createCategoryDTO);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testListCategoryById() {
        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findByIdAndDeactivationDateIsNull(1L)).thenReturn(Optional.of(category));

        CategoryResponseDTO responseDTO = categoryService.listCategoryById(1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        verify(categoryRepository, times(1)).findByIdAndDeactivationDateIsNull(1L);
    }

    @Test
    void testListCategoryByIdNotFound() {
        when(categoryRepository.findByIdAndDeactivationDateIsNull(1L)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> categoryService.listCategoryById(1L));
        verify(categoryRepository, times(1)).findByIdAndDeactivationDateIsNull(1L);
    }

    @Test
    void testListAllCategories() {
        Category category1 = new Category();
        category1.setId(1L);
        Category category2 = new Category();
        category2.setId(2L);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(category1, category2));

        when(categoryRepository.findAllByDeactivationDateIsNull(pageRequest)).thenReturn(categoryPage);

        Page<CategoryResponseDTO> responsePage = categoryService.listAllCategories(pageRequest);

        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());
        verify(categoryRepository, times(1)).findAllByDeactivationDateIsNull(pageRequest);
    }

    @Test
    void testUpdateCategoryStatusActivate() {
        Category category = new Category();
        category.setId(1L);
        category.setDeactivationDate(Instant.now());

        UpdateCategoryStatusDTO updateCategoryStatusDTO = new UpdateCategoryStatusDTO();
        updateCategoryStatusDTO.setStatus(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO responseDTO = categoryService.updateCategoryStatus(1L, updateCategoryStatusDTO);

        assertNotNull(responseDTO);
        assertNull(category.getDeactivationDate());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testUpdateCategoryStatusDeactivate() {
        Category category = new Category();
        category.setId(1L);
        category.setDeactivationDate(null);

        UpdateCategoryStatusDTO updateCategoryStatusDTO = new UpdateCategoryStatusDTO();
        updateCategoryStatusDTO.setStatus(false);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO responseDTO = categoryService.updateCategoryStatus(1L, updateCategoryStatusDTO);

        assertNotNull(responseDTO);
        assertNotNull(category.getDeactivationDate());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testUpdateCategoryStatusNotFound() {
        UpdateCategoryStatusDTO updateCategoryStatusDTO = new UpdateCategoryStatusDTO();
        updateCategoryStatusDTO.setStatus(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> categoryService.updateCategoryStatus(1L, updateCategoryStatusDTO));
        verify(categoryRepository, times(1)).findById(1L);
    }

}
