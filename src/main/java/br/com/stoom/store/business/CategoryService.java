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
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CreateCategoryDTO createCategoryDTO) {
        try {
            log.info("Creating category...");
            final Category category = createCategoryDTO.toCategory();
            var categoryResponseDto = CategoryResponseDTO.fromCategory(categoryRepository.save(category));
            log.info("Category {} created", categoryResponseDto.getName());
            return categoryResponseDto;
        } catch (Exception e) {
            log.error("Error creating category: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    public CategoryResponseDTO listCategoryById(Long categoryId) {
        try {
            log.info("Listing category by ID: {}", categoryId);
            var category = categoryRepository.findByIdAndDeactivationDateIsNull(categoryId)
                    .orElseThrow(() -> new BrandNotFoundException(categoryId));
            return CategoryResponseDTO.fromCategory(category);
        } catch (BrandNotFoundException e) {
            log.warn("Category not found: {}", categoryId);
            throw e;
        } catch (Exception e) {
            log.error("Error listing category by ID: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    public Page<CategoryResponseDTO> listAllCategories(PageRequest pageRequest) {
        try {
            log.info("Listing all categories with page request: {}", pageRequest);
            final Page<Category> categories = categoryRepository.findAllByDeactivationDateIsNull(pageRequest);
            return categories.map(CategoryResponseDTO::fromCategory);
        } catch (Exception e) {
            log.error("Error listing all categories: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategoryStatus(Long categoryId, UpdateCategoryStatusDTO updateCategoryStatusDTO) {
        try {
            log.info("Updating category status for ID: {}", categoryId);
            var category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new BrandNotFoundException(categoryId));

            boolean shouldActivate = updateCategoryStatusDTO.isStatus();
            boolean isCurrentlyActive = category.isActive();

            if (shouldActivate != isCurrentlyActive) {
                category.setDeactivationDate(shouldActivate ? null : Instant.now());
                categoryRepository.save(category);
                log.info("Category status updated for ID: {}", categoryId);
            } else {
                log.info("No status change required for category ID: {}", categoryId);
            }

            return CategoryResponseDTO.fromCategory(category);
        } catch (BrandNotFoundException e) {
            log.warn("Category not found: {}", categoryId);
            throw e;
        } catch (Exception e) {
            log.error("Error updating category status: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

}
