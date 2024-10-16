package com.reachout.ReachoutSystem.establishment.dto;

import com.reachout.ReachoutSystem.users.dto.DocumentEditRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstablishmentOwnerCreateRequestDTO {
    private String uid;
    private String name;
    private String email;
    private String phone;
    private DocumentEditRequestDTO document;
}
