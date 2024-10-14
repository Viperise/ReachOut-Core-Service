package com.reachout.ReachoutSystem.establishment.controller;

import com.reachout.ReachoutSystem.establishment.dto.EstablishmentListResponseDTO;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.service.EstablishmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/establishments")
@RequiredArgsConstructor
@Tag(name = "Estabelecimentos", description = "Endpoints de controle de Estabelecimentos.")
public class EstablishmentController {

    private EstablishmentService establishmentService;

    @Autowired
    public EstablishmentController(EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
    }

    // ***
    // LISTAGEM DE ESTABELECIMENTOS
    // ***
    @Operation(summary = "Todos os Estabelecimentos", description = "Recupera uma lista de informações sobre os Estabelecimentos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Establishment.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping
    public ResponseEntity<Page<EstablishmentListResponseDTO>> getAllEstablishments(Pageable pageable) {
        return ResponseEntity.ok(this.establishmentService.findAll(pageable));
    }
}
