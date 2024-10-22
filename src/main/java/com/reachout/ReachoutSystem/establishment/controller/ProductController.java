package com.reachout.ReachoutSystem.establishment.controller;

import com.reachout.ReachoutSystem.establishment.dto.EstablishmentProductAddRequestDTO;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.service.EstablishmentService;
import com.reachout.ReachoutSystem.users.dto.UserCreateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/establishments")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints de controle de Produtos.")
public class ProductController {

    private EstablishmentService establishmentService;

    @Autowired
    public ProductController(EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
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
    @PostMapping("/{id}/products")
    public ResponseEntity<Establishment> addProducts(@PathVariable Long id, @RequestBody List<EstablishmentProductAddRequestDTO> products, @RequestParam String roleUidPermission) {
        Establishment updatedEstablishment = establishmentService.addProductsToEstablishment(id, products, roleUidPermission);
        return ResponseEntity.ok(updatedEstablishment);
    }
}
