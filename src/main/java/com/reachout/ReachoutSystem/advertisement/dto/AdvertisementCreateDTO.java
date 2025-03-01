package com.reachout.ReachoutSystem.advertisement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementCreateDTO {
    private String name;
    private String description;
    private Boolean status;
    private List<Long> establishmentIds = new ArrayList<>();
    private Integer userId;
    private String fileBase64;
    private String fileName;
    private String fileType;
}