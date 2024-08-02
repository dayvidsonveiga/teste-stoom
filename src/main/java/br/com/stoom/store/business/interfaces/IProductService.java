package br.com.stoom.store.business.interfaces;

import br.com.stoom.store.dto.product.CreateProductDTO;
import br.com.stoom.store.dto.product.ProductResponseDTO;
import br.com.stoom.store.dto.product.UpdateProductStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {

    Page<ProductResponseDTO> listAllProducts(PageRequest pageRequest);

    ProductResponseDTO createProduct(CreateProductDTO createProductRequestDTO);

    ProductResponseDTO listProductById(Long productId);

    ProductResponseDTO updateProductStatus(Long productId, UpdateProductStatusDTO updateProductStatusDTO);

    Page<ProductResponseDTO> listAllProductsByBrand(Long brandId, PageRequest pageRequest);

    Page<ProductResponseDTO> listAllProductsByCategory(Long categoryId, PageRequest pageRequest);

}
