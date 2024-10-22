package com.reachout.ReachoutSystem.establishment.resources;

import com.reachout.ReachoutSystem.establishment.dto.ProductListResponseDTO;
import com.reachout.ReachoutSystem.establishment.entity.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductListConverter {

    public static ProductListResponseDTO productToProductListResponseConverter(Product product) {
        var response = new ProductListResponseDTO();

        response.setId(Long.valueOf(product.getId()));
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setPhotoPath(product.getPhotoPath());

        return response;
    }
}
