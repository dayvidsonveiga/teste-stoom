package br.com.stoom.store.business;

import br.com.stoom.store.business.interfaces.IProductService;
import br.com.stoom.store.dto.product.CreateProductDTO;
import br.com.stoom.store.dto.product.ProductResponseDTO;
import br.com.stoom.store.dto.product.UpdateProductStatusDTO;
import br.com.stoom.store.exception.ProductNotFoundException;
import br.com.stoom.store.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(CreateProductDTO createProductRequestDTO) {
        validateBrand(createProductRequestDTO.getBrandId());
        validateCategory(createProductRequestDTO.getCategoryId());

        var product = createProductRequestDTO.toProduct();

        return ProductResponseDTO.fromProduct(productRepository.save(product));
    }

    @Override
    public ProductResponseDTO listProductById(Long productId) {
        var product = productRepository.findProductByIdAndDeactivationDateIsNull(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return ProductResponseDTO.fromProduct(product);
    }

    @Override
    public Page<ProductResponseDTO> listAllProducts(PageRequest pageRequest) {
        return productRepository.findAllByDeactivationDateIsNull(pageRequest)
                .map(ProductResponseDTO::fromProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProductStatus(Long productId, UpdateProductStatusDTO updateProductStatusDTO) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        boolean shouldActivate = updateProductStatusDTO.isStatus();
        boolean isCurrentlyActive = product.isActive();

        if (shouldActivate != isCurrentlyActive) {
            product.setDeactivationDate(shouldActivate ? null : Instant.now());
            productRepository.save(product);
        }

        return ProductResponseDTO.fromProduct(product);
    }

    @Override
    public Page<ProductResponseDTO> listAllProductsByBrand(Long brandId, PageRequest pageRequest) {
        validateBrand(brandId);
        return productRepository.findAllByBrandIdAndDeactivationDateIsNull(brandId, pageRequest)
                .map(ProductResponseDTO::fromProduct);
    }

    @Override
    public Page<ProductResponseDTO> listAllProductsByCategory(Long categoryId, PageRequest pageRequest) {
        validateCategory(categoryId);
        return productRepository.findAllByCategoryIdAndDeactivationDateIsNull(categoryId, pageRequest)
                .map(ProductResponseDTO::fromProduct);
    }

    private void validateBrand(Long brandId) {
        brandService.listBrandById(brandId);
    }

    private void validateCategory(Long categoryId) {
        categoryService.listCategoryById(categoryId);
    }

}
