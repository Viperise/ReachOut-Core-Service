package com.reachout.ReachoutSystem.advertisement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponseDTO {
    private String fileName;
    private String downloadUri;
    private long size;
}