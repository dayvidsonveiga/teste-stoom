package br.com.stoom.store.dto.product;

import br.com.stoom.store.model.Product;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class ProductResponseDTO {

    private Long id;
    private String sku;
    private String name;
    private BigDecimal price;
    private boolean active;
    private Long categoryId;
    private Long brandId;

    public static ProductResponseDTO fromProduct(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .price(product.getPrice())
                .active(product.isActive())
                .brandId(product.getBrand().getId())
                .categoryId(product.getCategory().getId())
                .build();
    }

}
