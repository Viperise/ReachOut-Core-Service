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
public class EstablishmentEditRequestDTO {
    private String name;
    private String type;
    private String description;

    private String photoPath;
    private String address;
    private String phone;

    private EstablishmentOwnerCreateRequestDTO owner;
}
