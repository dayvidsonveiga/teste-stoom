package br.com.stoom.store.dto.product;

import br.com.stoom.store.model.Brand;
import br.com.stoom.store.model.Category;
import br.com.stoom.store.model.Product;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateProductDTO {

    @NotBlank(message = "sku is mandatory")
    private String sku;

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotNull(message = "price is mandatory")
    @Positive(message = "price must be positive")
    private BigDecimal price;

    @NotNull(message = "category_id is mandatory")
    private Long categoryId;

    @NotNull(message = "brand_id is mandatory")
    private Long brandId;

    public Product toProduct() {
        return Product.builder()
                .sku(this.sku)
                .name(this.name)
                .price(this.price)
                .brand(Brand.builder().id(this.brandId).build())
                .category(Category.builder().id(this.categoryId).build())
                .build();
    }

}
