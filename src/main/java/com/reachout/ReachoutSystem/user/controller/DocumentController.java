package com.reachout.ReachoutSystem.user.controller;

import com.reachout.ReachoutSystem.user.dto.DocumentEditRequestDTO;
import com.reachout.ReachoutSystem.user.dto.UserCreateRequestDTO;
import com.reachout.ReachoutSystem.user.entity.Document;
import com.reachout.ReachoutSystem.user.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
@Tag(name = "Documentos", description = "Endpoints de controle de Documentos.")
public class DocumentController {
    private final DocumentService documentService;

    // ***
    // EDITAR UM DOCUMENTO EXISTENTE DIRETAMENTE
    // ***
    @Operation(summary = "Editar um documento de um usuário - Cliente Parceiro", description = "Edita um Documento de um Cliente Parceiro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateRequestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @PutMapping
    public ResponseEntity<?> editDocument(@RequestBody DocumentEditRequestDTO documentDTO, @RequestParam Long documentID) {
        try {
            Document updateDocument = documentService.updateDocument(documentDTO, documentID);
            return ResponseEntity.ok(updateDocument);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }
}
