package com.reachout.ReachoutSystem.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reachout.ReachoutSystem.user.entity.Role;
import jakarta.validation.constraints.NotBlank;


public class UserRequestDTO {
    @NotBlank(message = "The User's name cannot be blank!")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "The User's e-mail cannot be blank!")
    @JsonProperty("ema@Setter\\n\" +\n" +
            "            \"@Getter\\n\" +\n" +
            "            \"@NoArgsConstructor\\n\" +\n" +
            "            \"@AllArgsConstructor\\n\" +\n" +
            "            \"@JsonIgnoreProperties(ignoreUnknown = true)il")
    private String email;

    @NotBlank(message = "The User's password cannot be blank!")
    @JsonProperty("password")
    private String password;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("role")
    private Role role;
}