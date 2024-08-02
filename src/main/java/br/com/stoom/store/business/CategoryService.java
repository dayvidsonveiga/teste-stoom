package br.com.stoom.store.business;

import br.com.stoom.store.business.interfaces.ICategoryService;
import br.com.stoom.store.dto.category.CategoryResponseDTO;
import br.com.stoom.store.dto.category.CreateCategoryDTO;
import br.com.stoom.store.dto.category.UpdateCategoryStatusDTO;
import br.com.stoom.store.exception.BrandNotFoundException;
import br.com.stoom.store.model.Category;
import br.com.stoom.store.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CreateCategoryDTO createCategoryDTO) {
        final Category category = createCategoryDTO.toCategory();
        return CategoryResponseDTO.fromCategory(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDTO listCategoryById(Long categoryId) {
        var category = categoryRepository.findByIdAndDeactivationDateIsNull(categoryId)
                .orElseThrow(() -> new BrandNotFoundException(categoryId));
        return CategoryResponseDTO.fromCategory(category);
    }

    @Override
    public Page<CategoryResponseDTO> listAllCategories(PageRequest pageRequest) {
        final Page<Category> categories = categoryRepository.findAllByDeactivationDateIsNull(pageRequest);
        return categories.map(CategoryResponseDTO::fromCategory);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategoryStatus(Long categoryId, UpdateCategoryStatusDTO updateCategoryStatusDTO) {
        var category = categoryRepository.findById(categoryId).orElseThrow(() -> new BrandNotFoundException(categoryId));

        boolean shouldActivate = updateCategoryStatusDTO.isStatus();
        boolean isCurrentlyActive = category.isActive();

        if (shouldActivate != isCurrentlyActive) {
            category.setDeactivationDate(shouldActivate ? null : Instant.now());
            categoryRepository.save(category);
        }

        return CategoryResponseDTO.fromCategory(category);
    }

}
