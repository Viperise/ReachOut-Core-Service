package com.reachout.ReachoutSystem.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reachout.ReachoutSystem.users.entity.Document;
import com.reachout.ReachoutSystem.users.entity.DocumentType;
import com.reachout.ReachoutSystem.users.entity.Role;
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
public class UserCreateRequestDTO {
    private String uid;
    private String name;
    private String email;
    private LocalDate birthday;

    private String address;
    private String profilePhotoPath;
    private DocumentType documentType;
    private String documentNumber;
}
