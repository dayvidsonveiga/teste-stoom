package br.com.stoom.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_sequence")
    @SequenceGenerator(name = "brand_sequence", allocationSize = 500)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "deactivation_date")
    private Instant deactivationDate;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.DETACH)
    private List<Product> products;

    public boolean isActive() {
        return this.deactivationDate == null;
    }

}
