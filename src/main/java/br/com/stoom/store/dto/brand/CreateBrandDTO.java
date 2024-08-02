package br.com.stoom.store.dto.brand;

import br.com.stoom.store.model.Brand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBrandDTO {

    @NotBlank(message = "name is mandatory")
    private String name;

    public Brand toBrand() {
        return Brand.builder()
                .name(this.name)
                .build();
    }

}
