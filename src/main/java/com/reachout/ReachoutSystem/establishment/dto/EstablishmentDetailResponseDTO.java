package com.reachout.ReachoutSystem.establishment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reachout.ReachoutSystem.user.dto.UserDetailResponseDTO;
import com.reachout.ReachoutSystem.user.entity.User;
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
public class EstablishmentDetailResponseDTO {
    private String name;
    private String type;
    private String description;
    private String photoPath;
    private String address;
    private String phone;
    private String status;
    private UserDetailResponseDTO owner;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
