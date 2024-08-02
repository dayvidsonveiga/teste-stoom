package br.com.stoom.store.dto.category;

import br.com.stoom.store.model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryDTO {

    @NotBlank(message = "name is mandatory")
    private String name;

    public Category toCategory() {
        return Category.builder()
                .name(name)
                .build();
    }

}
