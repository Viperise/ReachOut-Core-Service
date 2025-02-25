package com.reachout.ReachoutSystem.establishment.controller;

import com.reachout.ReachoutSystem.establishment.dto.*;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.entity.Product;
import com.reachout.ReachoutSystem.establishment.service.EstablishmentService;
import com.reachout.ReachoutSystem.establishment.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints de controle de Produtos.")
public class ProductController {

    private EstablishmentService establishmentService;
    private ProductService productService;

    @Autowired
    public ProductController(EstablishmentService establishmentService, ProductService productService) {
        this.establishmentService = establishmentService;
        this.productService = productService;
    }

    // ***
    // LISTAGEM DE PRODUTOS POR ESTABELECIMENTO
    // ***
    @Operation(summary = "Lista todos os produtos por um determinado estabelecimento", description = "Lista Produtos de um Estabelecimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping()
    public ResponseEntity<Page<ProductListResponseDTO>> getAllProductsByEstablishment(Pageable pageable, @RequestParam String establishmentId) {
        return ResponseEntity.ok(this.productService.findAll(pageable, establishmentId));
    }

    // ***
    // DETALHAR UM PRODUTO DE UM DETERMINADO ESTABELECIMENTO
    // ***
    @Operation(summary = "Recupera os Dados de um Produto", description = "Recupera as informações do Produto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados recuperados com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Establishment> getProductByID(@PathVariable Long id) {
        return establishmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ***
    // ADICIONA PRODUTOS PARA O DETERMINADO ESTABELECIMENTO
    // ***
    @Operation(summary = "Registra novos produtos para um determinado estabelecimento", description = "Adiciona Produtos para um estabelecimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produtos adicionados para este estabelecimento com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @PostMapping()
    public ResponseEntity<String> addProducts(
            @RequestParam Long establishmentId,
            @RequestBody List<EstablishmentProductAddRequestDTO> products,
            @RequestParam String roleUidPermission
    ) {
        Establishment updatedEstablishment = productService.addProductsToEstablishment(
                establishmentId,
                products,
                roleUidPermission
        );

        return ResponseEntity.status(HttpStatus.CREATED).body("Produto/s criado/s com sucesso");
    }

    // ***
    // EDITAR UM PRODUTO
    // ***
    @Operation(summary = "Edita um Produto", description = "Edita um Produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto editado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @PutMapping
    public ResponseEntity<?> editProduct(@RequestBody ProductEditResponseDTO productEditRequestDTO,
                                               @RequestParam String roleUidPermission) {
        try {
            Product updatedProduct = productService.update(productEditRequestDTO, roleUidPermission);
            ProductEditResponseDTO responseDTO = convertToResponseDTO(updatedProduct);

            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao editar Produto: \n" + e.getMessage());
        }
    }

    // ***
    // DESATIVAR UM PRODUTO
    // ***
    @Operation(summary = "Desativa um Produto", description = "Desativa um Produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto desativado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id, @RequestParam String RoleUidPermission) {
        try {
            productService.delete(id, RoleUidPermission);
            return ResponseEntity.ok("Produto Desativado com Sucesso");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private ProductEditResponseDTO convertToResponseDTO(Product product) {
        ProductEditResponseDTO responseDTO = new ProductEditResponseDTO();
        responseDTO.setName(product.getName());
        responseDTO.setDescription(product.getDescription());
        responseDTO.setPhotoPath(product.getPhotoPath());
        responseDTO.setCategory(product.getCategory());
        responseDTO.setPrice(product.getPrice());

        return responseDTO;
    }
}
