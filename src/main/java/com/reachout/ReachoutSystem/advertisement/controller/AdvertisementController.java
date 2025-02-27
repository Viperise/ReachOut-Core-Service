package com.reachout.ReachoutSystem.advertisement.controller;

import com.reachout.ReachoutSystem.advertisement.dto.*;
import com.reachout.ReachoutSystem.advertisement.entity.Advertisement;
import com.reachout.ReachoutSystem.advertisement.service.AdvertisementService;
import com.reachout.ReachoutSystem.establishment.service.EstablishmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("advertisements")
@RequiredArgsConstructor
@Tag(name = "Anúncios", description = "Endpoints de controle de Anúncios.")
public class AdvertisementController {
    private EstablishmentService establishmentService;
    private AdvertisementService advertisementService;

    @Autowired
    public AdvertisementController(EstablishmentService establishmentService, AdvertisementService advertisementService) {
        this.establishmentService = establishmentService;
        this.advertisementService = advertisementService;
    }

    // ***
    // LISTA TODOS OS ANÚNCIOS DE FORMA PAGINADA
    // ***
    @Operation(summary = "Todos os Anúncios (Paginado)", description = "Recupera uma lista paginada de informações sobre os Anúncios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementListDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping
    public ResponseEntity<Page<AdvertisementListDTO>> getAllAdvertisements(Pageable pageable) {
        return ResponseEntity.ok(this.advertisementService.findAll(pageable));
    }

    // ***
    // LISTA TODOS OS ANÚNCIOS DE FORMA TOTAL
    // ***
    @Operation(summary = "Todos os Anúncios (Completa)", description = "Recupera uma lista completa de informações sobre os Anúncios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdvertisementListDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping("/all")
    public ResponseEntity<List<AdvertisementListDTO>> getAllAdvertisementsNonPaged() {
        return ResponseEntity.ok(this.advertisementService.findAllNonPaged());
    }

    // ***
    // CRIA UM ANÚNCIO
    // ***
    @Operation(summary = "Criar Anúncio com Mídia", description = "Cria um novo anúncio e faz upload de uma mídia associada ao Firebase Storage.")
    @ApiResponse(responseCode = "201", description = "Anúncio criado com sucesso!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @PostMapping
    public ResponseEntity<String> createAdvertisement(@RequestBody AdvertisementCreateDTO dto) {
        try {
            advertisementService.createAdvertisement(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body("Anúncio Criado com Sucesso!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ***
    // EDITAR UM ANÚNCIO
    // ***
    @Operation(summary = "Edita Anúncio sem Mídia", description = "Edita um anúncio.")
    @ApiResponse(responseCode = "201", description = "Anúncio criado com sucesso!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdvertisementListDTO.class)))
    @PutMapping
    public ResponseEntity<Advertisement> editAdvertisement(@RequestBody AdvertisementEditDTO dto) {
        try {
            return ResponseEntity.ok(advertisementService.editAdvertisement(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ***
    // DESATIVAR UM ANÚNCIO
    // ***
    @Operation(summary = "Desativa Anúncio", description = "Desativa um anúncio existente.")
    @ApiResponse(responseCode = "200", description = "Anúncio desativado com sucesso!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdvertisementResponseDTO.class)))
    @DeleteMapping("/deactivate")
    public ResponseEntity<AdvertisementResponseDTO> deactivateAdvertisement(@RequestBody AdvertisementDeactiveDTO dto) {
        try {
            AdvertisementResponseDTO responseDTO = advertisementService.deactivateAdvertisement(dto);

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ***
    // REATIVAR UM ANÚNCIO
    // ***
    @Operation(summary = "Reativa Anúncio", description = "Reativa um anúncio desativado.")
    @ApiResponse(responseCode = "200", description = "Anúncio reativado com sucesso!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AdvertisementResponseDTO.class)))
    @PatchMapping("/reactivate")
    public ResponseEntity<AdvertisementResponseDTO> reactivateAdvertisement(@RequestBody AdvertisementDeactiveDTO dto) {
        try {
            AdvertisementResponseDTO responseDTO = advertisementService.reactivateAdvertisement(dto);

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
