package com.reachout.ReachoutSystem.establishment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstablishmentResponseDTO {
    private Long id;
    private String name;
    private String address;
    private List<ProductResponseDTO> products;
}
