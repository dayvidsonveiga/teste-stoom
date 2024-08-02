package br.com.stoom.store.business;

import br.com.stoom.store.dto.product.CreateProductDTO;
import br.com.stoom.store.dto.product.ProductResponseDTO;
import br.com.stoom.store.dto.product.UpdateProductStatusDTO;
import br.com.stoom.store.exception.ProductNotFoundException;
import br.com.stoom.store.model.Brand;
import br.com.stoom.store.model.Category;
import br.com.stoom.store.model.Product;
import br.com.stoom.store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandService brandService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setBrandId(1L);
        createProductDTO.setCategoryId(1L);
        var product = Product.builder()
                .id(1L)
                .sku("UIH")
                .name("Celular")
                .price(BigDecimal.valueOf(1000l))
                .deactivationDate(null)
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO responseDTO = productService.createProduct(createProductDTO);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        verify(brandService, times(1)).listBrandById(1L);
        verify(categoryService, times(1)).listCategoryById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testListProductById() {
        var product = Product.builder()
                .id(1L)
                .sku("UIH")
                .name("Celular")
                .price(BigDecimal.valueOf(1000l))
                .deactivationDate(Instant.now())
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .build();

        when(productRepository.findProductByIdAndDeactivationDateIsNull(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO responseDTO = productService.listProductById(1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        verify(productRepository, times(1)).findProductByIdAndDeactivationDateIsNull(1L);
    }

    @Test
    void testListProductByIdNotFound() {
        when(productRepository.findProductByIdAndDeactivationDateIsNull(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.listProductById(1L));
        verify(productRepository, times(1)).findProductByIdAndDeactivationDateIsNull(1L);
    }

    @Test
    void testListAllProducts() {
        var product1 = Product.builder()
                .id(1L)
                .sku("UIH")
                .name("Celular")
                .price(BigDecimal.valueOf(1000l))
                .deactivationDate(Instant.now())
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .build();

        var product2 = Product.builder()
                .id(2L)
                .sku("UIH")
                .name("Celular2")
                .price(BigDecimal.valueOf(2000l))
                .deactivationDate(Instant.now())
                .brand(Brand.builder().id(2L).build())
                .category(Category.builder().id(1L).build())
                .build();

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product1, product2));

        when(productRepository.findAllByDeactivationDateIsNull(pageRequest)).thenReturn(productPage);

        Page<ProductResponseDTO> responsePage = productService.listAllProducts(pageRequest);

        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());
        verify(productRepository, times(1)).findAllByDeactivationDateIsNull(pageRequest);
    }

    @Test
    void testUpdateProductStatusActivate() {
        var product = Product.builder()
                .id(1L)
                .sku("UIH")
                .name("Celular")
                .price(BigDecimal.valueOf(1000l))
                .deactivationDate(Instant.now())
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .build();

        UpdateProductStatusDTO updateProductStatusDTO = new UpdateProductStatusDTO();
        updateProductStatusDTO.setStatus(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO responseDTO = productService.updateProductStatus(1L, updateProductStatusDTO);

        assertNotNull(responseDTO);
        assertNull(product.getDeactivationDate());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProductStatusDeactivate() {
        var product = Product.builder()
                .id(1L)
                .sku("UIH")
                .name("Celular")
                .price(BigDecimal.valueOf(1000l))
                .deactivationDate(null)
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .build();

        UpdateProductStatusDTO updateProductStatusDTO = new UpdateProductStatusDTO();
        updateProductStatusDTO.setStatus(false);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO responseDTO = productService.updateProductStatus(1L, updateProductStatusDTO);

        assertNotNull(responseDTO);
        assertNotNull(product.getDeactivationDate());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProductStatusNotFound() {
        UpdateProductStatusDTO updateProductStatusDTO = new UpdateProductStatusDTO();
        updateProductStatusDTO.setStatus(true);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProductStatus(1L, updateProductStatusDTO));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testListAllProductsByBrand() {
        var product1 = Product.builder()
                .id(1L)
                .sku("UIH")
                .name("Celular")
                .price(BigDecimal.valueOf(1000l))
                .deactivationDate(Instant.now())
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .build();

        var product2 = Product.builder()
                .id(2L)
                .sku("UIH")
                .name("Celular2")
                .price(BigDecimal.valueOf(2000l))
                .deactivationDate(Instant.now())
                .brand(Brand.builder().id(2L).build())
                .category(Category.builder().id(1L).build())
                .build();

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product1, product2));

        when(productRepository.findAllByBrandIdAndDeactivationDateIsNull(1L, pageRequest)).thenReturn(productPage);

        Page<ProductResponseDTO> responsePage = productService.listAllProductsByBrand(1L, pageRequest);

        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());
        verify(brandService, times(1)).listBrandById(1L);
        verify(productRepository, times(1)).findAllByBrandIdAndDeactivationDateIsNull(1L, pageRequest);
    }

    @Test
    void testListAllProductsByCategory() {
        var product1 = Product.builder()
                .id(1L)
                .sku("UIH")
                .name("Celular")
                .price(BigDecimal.valueOf(1000l))
                .deactivationDate(Instant.now())
                .brand(Brand.builder().id(1L).build())
                .category(Category.builder().id(1L).build())
                .build();

        var product2 = Product.builder()
                .id(2L)
                .sku("UIH")
                .name("Celular2")
                .price(BigDecimal.valueOf(2000l))
                .deactivationDate(Instant.now())
                .brand(Brand.builder().id(2L).build())
                .category(Category.builder().id(1L).build())
                .build();

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product1, product2));

        when(productRepository.findAllByCategoryIdAndDeactivationDateIsNull(1L, pageRequest)).thenReturn(productPage);

        Page<ProductResponseDTO> responsePage = productService.listAllProductsByCategory(1L, pageRequest);

        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());
        verify(categoryService, times(1)).listCategoryById(1L);
        verify(productRepository, times(1)).findAllByCategoryIdAndDeactivationDateIsNull(1L, pageRequest);
    }

}
