package com.reachout.ReachoutSystem.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reachout.ReachoutSystem.user.entity.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailResponseDTO {
    private String uid;
    private String name;
    private String role;
    private String email;
    private LocalDate birthday;
    private DocumentType documentType;
    private String documentNumber;
    private String address;
    private String phone;
    private String photoPath;
}
