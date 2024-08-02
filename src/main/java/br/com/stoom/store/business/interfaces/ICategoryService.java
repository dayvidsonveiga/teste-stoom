package br.com.stoom.store.business.interfaces;

import br.com.stoom.store.dto.category.CategoryResponseDTO;
import br.com.stoom.store.dto.category.CreateCategoryDTO;
import br.com.stoom.store.dto.category.UpdateCategoryStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ICategoryService {

    CategoryResponseDTO createCategory(CreateCategoryDTO createCategoryDTO);

    CategoryResponseDTO listCategoryById(Long categoryId);

    Page<CategoryResponseDTO> listAllCategories(PageRequest pageRequest);

    CategoryResponseDTO updateCategoryStatus(Long categoryId, UpdateCategoryStatusDTO updateCategoryStatusDTO);

}
