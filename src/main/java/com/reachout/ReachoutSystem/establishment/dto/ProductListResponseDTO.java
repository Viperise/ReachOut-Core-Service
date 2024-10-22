package com.reachout.ReachoutSystem.establishment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductListResponseDTO {
    private Long id;
    private String name;
    private Long price;

    private String category;
    private String description;
    private String photoPath;
}
