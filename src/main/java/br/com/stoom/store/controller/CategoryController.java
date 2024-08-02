package br.com.stoom.store.controller;

import br.com.stoom.store.business.interfaces.ICategoryService;
import br.com.stoom.store.business.interfaces.IProductService;
import br.com.stoom.store.dto.category.CategoryResponseDTO;
import br.com.stoom.store.dto.category.CreateCategoryDTO;
import br.com.stoom.store.dto.category.UpdateCategoryStatusDTO;
import br.com.stoom.store.dto.product.ProductResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final IProductService productService;

    @PostMapping()
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid CreateCategoryDTO createCategoryDTO) {
        var categoryResponseDTO = categoryService.createCategory(createCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> listCategoryById(@PathVariable("id") Long categoryId) {
        var readCategoryResponseDTO = categoryService.listCategoryById(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(readCategoryResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> listAllCategory(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var categoryResponseDTOS = categoryService.listAllCategories(PageRequest.of(page, pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategoryStatus(@PathVariable("id") Long categoryId,
                                                                    @RequestBody @Valid UpdateCategoryStatusDTO updateCategoryStatusDTO) {
        var categoryResponseDTO = categoryService.updateCategoryStatus(categoryId, updateCategoryStatusDTO);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTO);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Page<ProductResponseDTO>> listAllProductsByCategory(@PathVariable("id") Long categoryId,
                                                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var productResponseDTOS = productService.listAllProductsByCategory(categoryId, PageRequest.of(page, pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOS);
    }

}
