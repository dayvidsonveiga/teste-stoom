package br.com.stoom.store.business;

import br.com.stoom.store.business.interfaces.IProductService;
import br.com.stoom.store.dto.product.CreateProductDTO;
import br.com.stoom.store.dto.product.ProductResponseDTO;
import br.com.stoom.store.dto.product.UpdateProductStatusDTO;
import br.com.stoom.store.exception.ProductNotFoundException;
import br.com.stoom.store.repository.ProductRepository;
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
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(CreateProductDTO createProductRequestDTO) {
        try {
            log.info("Creating product...");
            validateBrand(createProductRequestDTO.getBrandId());
            validateCategory(createProductRequestDTO.getCategoryId());

            var product = createProductRequestDTO.toProduct();
            var productResponseDto = ProductResponseDTO.fromProduct(productRepository.save(product));
            log.info("Product {} created", productResponseDto.getName());
            return productResponseDto;
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    public ProductResponseDTO listProductById(Long productId) {
        try {
            log.info("Listing product by ID: {}", productId);
            var product = productRepository.findProductByIdAndDeactivationDateIsNull(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
            return ProductResponseDTO.fromProduct(product);
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", productId);
            throw e;
        } catch (Exception e) {
            log.error("Error listing product by ID: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    public Page<ProductResponseDTO> listAllProducts(PageRequest pageRequest) {
        try {
            log.info("Listing all products with page request: {}", pageRequest);
            return productRepository.findAllByDeactivationDateIsNull(pageRequest)
                    .map(ProductResponseDTO::fromProduct);
        } catch (Exception e) {
            log.error("Error listing all products: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProductStatus(Long productId, UpdateProductStatusDTO updateProductStatusDTO) {
        try {
            log.info("Updating product status for ID: {}", productId);
            var product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            boolean shouldActivate = updateProductStatusDTO.isStatus();
            boolean isCurrentlyActive = product.isActive();

            if (shouldActivate != isCurrentlyActive) {
                product.setDeactivationDate(shouldActivate ? null : Instant.now());
                productRepository.save(product);
                log.info("Product status updated for ID: {}", productId);
            } else {
                log.info("No status change required for product ID: {}", productId);
            }

            return ProductResponseDTO.fromProduct(product);
        } catch (ProductNotFoundException e) {
            log.warn("Product not found: {}", productId);
            throw e;
        } catch (Exception e) {
            log.error("Error updating product status: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    public Page<ProductResponseDTO> listAllProductsByBrand(Long brandId, PageRequest pageRequest) {
        try {
            log.info("Listing all products by brand ID: {}", brandId);
            validateBrand(brandId);
            return productRepository.findAllByBrandIdAndDeactivationDateIsNull(brandId, pageRequest)
                    .map(ProductResponseDTO::fromProduct);
        } catch (Exception e) {
            log.error("Error listing all products by brand ID: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    @Override
    public Page<ProductResponseDTO> listAllProductsByCategory(Long categoryId, PageRequest pageRequest) {
        try {
            log.info("Listing all products by category ID: {}", categoryId);
            validateCategory(categoryId);
            return productRepository.findAllByCategoryIdAndDeactivationDateIsNull(categoryId, pageRequest)
                    .map(ProductResponseDTO::fromProduct);
        } catch (Exception e) {
            log.error("Error listing all products by category ID: {}", e.getMessage());
            // Add treatment such as logging into a monitoring table
            throw e;
        }
    }

    private void validateBrand(Long brandId) {
        try {
            log.info("Validating brand ID: {}", brandId);
            brandService.listBrandById(brandId);
        } catch (Exception e) {
            log.error("Error validating brand ID: {}", e.getMessage());
            throw e;
        }
    }

    private void validateCategory(Long categoryId) {
        try {
            log.info("Validating category ID: {}", categoryId);
            categoryService.listCategoryById(categoryId);
        } catch (Exception e) {
            log.error("Error validating category ID: {}", e.getMessage());
            throw e;
        }
    }

}
