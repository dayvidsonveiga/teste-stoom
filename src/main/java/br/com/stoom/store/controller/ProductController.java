package br.com.stoom.store.controller;

import br.com.stoom.store.business.interfaces.IProductService;
import br.com.stoom.store.dto.product.CreateProductDTO;
import br.com.stoom.store.dto.product.ProductResponseDTO;
import br.com.stoom.store.dto.product.UpdateProductStatusDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid CreateProductDTO createProductDTO) {
        var productResponseDTO = productService.createProduct(createProductDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> listProductById(@PathVariable("id") Long productId) {
        var productResponseDTO = this.productService.listProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> listAllProducts(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var productResponseDTOS = productService.listAllProducts(PageRequest.of(page, pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProductStatus(@PathVariable("id") Long productId,
                                                                  @RequestBody @Valid UpdateProductStatusDTO updateProductStatusDTO) {
        var productResponseDTO = productService.updateProductStatus(productId, updateProductStatusDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

}
