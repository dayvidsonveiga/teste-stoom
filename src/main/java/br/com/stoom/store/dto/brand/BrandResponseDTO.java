package br.com.stoom.store.dto.brand;

import br.com.stoom.store.model.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandResponseDTO {

    private Long id;
    private String name;
    private boolean active;

    public static BrandResponseDTO fromBrand(Brand brand) {
        return BrandResponseDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .active(brand.isActive())
                .build();
    }

}
