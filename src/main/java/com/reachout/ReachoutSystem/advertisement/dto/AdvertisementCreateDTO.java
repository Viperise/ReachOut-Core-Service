package com.reachout.ReachoutSystem.advertisement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementCreateDTO {
    private String name;
    private String description;
    private Boolean status;
    private Integer establishmentId;
    private Integer userId;
    private String fileBase64;
    private String fileName;
    private String fileType;
}