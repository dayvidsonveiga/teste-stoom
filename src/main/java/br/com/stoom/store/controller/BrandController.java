package br.com.stoom.store.controller;

import br.com.stoom.store.business.interfaces.IBrandService;
import br.com.stoom.store.business.interfaces.IProductService;
import br.com.stoom.store.dto.brand.BrandResponseDTO;
import br.com.stoom.store.dto.brand.CreateBrandDTO;
import br.com.stoom.store.dto.brand.UpdateBrandStatusDTO;
import br.com.stoom.store.dto.product.ProductResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final IBrandService brandService;
    private final IProductService productService;


    @PostMapping()
    public ResponseEntity<BrandResponseDTO> createBrand(@RequestBody @Valid CreateBrandDTO createBrandDTO) {
        var brandResponseDTO = brandService.createBrand(createBrandDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(brandResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> listBrandById(@PathVariable("id") Long brandId) {
        var brandResponseDTO = brandService.listBrandById(brandId);
        return ResponseEntity.status(HttpStatus.OK).body(brandResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<BrandResponseDTO>> listAllBrands(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var allBrands = brandService.listAllBrands(PageRequest.of(page, pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(allBrands);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> updateBrandStatus(@PathVariable("id") Long brandId,
                                                              @RequestBody @Valid UpdateBrandStatusDTO updateBrandStatusDTO) {
        var brandResponseDTO = brandService.updateBrandStatus(brandId, updateBrandStatusDTO);
        return ResponseEntity.status(HttpStatus.OK).body(brandResponseDTO);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Page<ProductResponseDTO>> listAllProductsByBrand(@PathVariable("id") Long brandId,
                                                                           @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        var allProductsByBrand = productService.listAllProductsByBrand(brandId, PageRequest.of(page, pageSize));
        return ResponseEntity.status(HttpStatus.OK).body(allProductsByBrand);
    }

}
