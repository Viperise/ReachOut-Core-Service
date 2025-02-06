package com.reachout.ReachoutSystem.establishment.controller;

import com.reachout.ReachoutSystem.establishment.dto.*;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.service.EstablishmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
                            schema = @Schema(implementation = EstablishmentListResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping
    public ResponseEntity<Page<EstablishmentListResponseDTO>> getAllEstablishments(Pageable pageable) {
        return ResponseEntity.ok(this.establishmentService.findAll(pageable));
    }

    // ***
    // DETALHAR ESTABELECIMENTO
    // ***
    @Operation(summary = "Recupera os Dados de um Estabelecimento", description = "Recupera as informações do Estabelecimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados recuperados com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Establishment.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Establishment> getEstablishmentByID(@PathVariable Long id) {
        return establishmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ***
    // CRIAR ESTABELECIMENTO
    // ***
    @Operation(summary = "Cria um novo Estabelecimento", description = "Cria um Estabelecimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estabelecimento criado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstablishmentCreateRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @PostMapping
    public ResponseEntity<?> createEstablishment(
            @RequestBody EstablishmentCreateRequestDTO establishmentCreateRequestDTO,
            @RequestParam(name = "ownerUid") EstablishmentOwnerCreateRequestDTO ownerUid,
            @RequestParam String roleUidPermission
    ) {
        try {
            Establishment establishment = establishmentService.save(
                    establishmentCreateRequestDTO,
                    ownerUid,
                    roleUidPermission
            );
            EstablishmentCreateResponseDTO responseDTO = new EstablishmentCreateResponseDTO(
                    establishment.getName(),
                    establishment.getOwner().getName()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar Estabelecimento: \n" + e.getMessage());
        }
    }

    // ***
    // EDITAR ESTABELECIMENTO
    // ***
    @Operation(summary = "Edita um Estabelecimento", description = "Edita um Estabelecimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estabelecimento editado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstablishmentCreateRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @PutMapping
    public ResponseEntity<?> editEstablishment(@RequestBody EstablishmentEditRequestDTO establishmentCreateRequestDTO,
                                               @RequestParam Long establishmentId,
                                               @RequestParam String roleUidPermission) {
        try {
            Establishment updatedEstablishment = establishmentService.update(establishmentId, establishmentCreateRequestDTO, roleUidPermission);
            EstablishmentResponseDTO responseDTO = convertToResponseDTO(updatedEstablishment);

            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao editar Estabelecimento: \n" + e.getMessage());
        }
    }


     // ***
     // DESATIVAR UM ESTABELECIMENTO
     // ***
    @Operation(summary = "Desativa um Estabelecimento", description = "Desativa um Estabelecimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estabelecimento desativado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstablishmentCreateRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEstablishment(@PathVariable Long id, @RequestParam String RoleUidPermission) {
        try {
            establishmentService.delete(id, RoleUidPermission);
            return ResponseEntity.ok("Estabelecimento Desativado com Sucesso");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private EstablishmentResponseDTO convertToResponseDTO(Establishment establishment) {
        EstablishmentResponseDTO responseDTO = new EstablishmentResponseDTO();
        responseDTO.setId(Long.valueOf(establishment.getId()));
        responseDTO.setName(establishment.getName());
        responseDTO.setAddress(establishment.getAddress());

        List<ProductResponseDTO> productDTOs = establishment.getProducts().stream()
                .map(product -> {
                    ProductResponseDTO productDTO = new ProductResponseDTO();
                    productDTO.setId(Long.valueOf(product.getId()));
                    productDTO.setName(product.getName());
                    productDTO.setPrice(product.getPrice());
                    return productDTO;
                }).toList();

        responseDTO.setProducts(productDTOs);

        return responseDTO;
    }
}
