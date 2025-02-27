package com.reachout.ReachoutSystem.advertisement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private Boolean status;
    private String establishmentName;
}
