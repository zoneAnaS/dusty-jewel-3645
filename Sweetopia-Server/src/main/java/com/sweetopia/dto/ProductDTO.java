package com.sweetopia.dto;

import com.sweetopia.entity.Category;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@Embeddable
public class ProductDTO {
    private Long productId;
    private String name;
    private String photoPath;
    private Double price;
    private String description;
    private Integer quantity;

    private String categoryName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDTO that)) return false;
        return productId.equals(that.productId) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name);
    }
}
