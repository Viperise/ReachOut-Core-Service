package com.reachout.ReachoutSystem.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reachout.ReachoutSystem.users.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestDTO {
    @NotBlank(message = "The User's name cannot be blank!")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "The User's e-mail cannot be blank!")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "The User's password cannot be blank!")
    @JsonProperty("password")
    private String password;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("role")
    private Role role;

    @JsonProperty("clientPartner")
    private Long clientPartnerId;
}