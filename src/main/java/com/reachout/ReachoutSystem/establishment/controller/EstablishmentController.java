package com.reachout.ReachoutSystem.establishment.controller;

import com.reachout.ReachoutSystem.establishment.dto.EstablishmentCreateRequestDTO;
import com.reachout.ReachoutSystem.establishment.dto.EstablishmentListResponseDTO;
import com.reachout.ReachoutSystem.establishment.dto.EstablishmentProductAddRequestDTO;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.entity.Product;
import com.reachout.ReachoutSystem.establishment.service.EstablishmentService;
import com.reachout.ReachoutSystem.users.dto.UserCreateRequestDTO;
import com.reachout.ReachoutSystem.users.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
                            schema = @Schema(implementation = UserCreateRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @PostMapping
    public ResponseEntity<?> createEstablishment(@RequestBody EstablishmentCreateRequestDTO establishmentCreateRequestDTO, @RequestParam String roleUidPermission) {
        try {
            Establishment establishment = establishmentService.save(establishmentCreateRequestDTO, roleUidPermission);
            return ResponseEntity.status(HttpStatus.CREATED).body(establishment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar Estabelecimento: \n" + e.getMessage());
        }
    }

    // ***
    // ADICIONA PRODUTOS PARA O DETERMINADO ESTABELECIMENTO
    // ***
    @Operation(summary = "Registra novos produtos para um determinado estabelecimento", description = "Adiciona Produtos para um estabelecimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produtos adicionados para este estabelecimento com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @PatchMapping("/{id}/products")
    public ResponseEntity<Establishment> addProducts(@PathVariable Long id, @RequestBody List<EstablishmentProductAddRequestDTO> products, @RequestParam String roleUidPermission) {
        Establishment updatedEstablishment = establishmentService.addProductsToEstablishment(id, products, roleUidPermission);
        return ResponseEntity.ok(updatedEstablishment);
    }


}
