package com.reachout.ReachoutSystem.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reachout.ReachoutSystem.users.entity.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class DocumentEditRequestDTO {
    @JsonProperty(value = "document_type")
    private DocumentType documentType;

    @JsonProperty(value = "document_number")
    private String documentNumber;
}
