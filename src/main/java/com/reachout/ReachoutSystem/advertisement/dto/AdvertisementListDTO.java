package com.reachout.ReachoutSystem.advertisement.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvertisementListDTO {
    private Integer id;
    private String name;
    private String description;
    private Boolean status;
    private String establishmentName;
    private String userName;
    private String archivePath;
    private LocalDateTime createdAt;
}
