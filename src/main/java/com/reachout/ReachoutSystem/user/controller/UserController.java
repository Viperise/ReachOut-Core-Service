package com.reachout.ReachoutSystem.user.controller;

import com.reachout.ReachoutSystem.user.dto.UserCreateRequestDTO;
import com.reachout.ReachoutSystem.user.dto.UserDetailResponseDTO;
import com.reachout.ReachoutSystem.user.dto.UserListResponseDTO;
import com.reachout.ReachoutSystem.user.entity.Role;
import com.reachout.ReachoutSystem.user.entity.User;
import com.reachout.ReachoutSystem.user.service.UserService;
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

import java.util.Objects;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints de controle de Usuários.")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ***
    // LISTAGEM DE USUÁRIOS
    // ***
    @Operation(summary = "Todos os Usuários - Clientes Parceiros", description = "Recupera uma lista de informações com todos os Usuários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping
    public ResponseEntity<Page<UserListResponseDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(this.userService.findAll(pageable));
    }

    // ***
    // DETALHAR DO USUÁRIO
    // ***
    @Operation(summary = "Recupera os Dados de um Usuário - Cliente Parceiro", description = "Recupera as informações do Usuário - Cliente Parceiro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados recuperados com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping("/{uid}")
    public ResponseEntity<UserDetailResponseDTO> getUserById(@PathVariable String uid) {
        return userService.findByUid(uid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // ***
    // CRIAR UM NOVO USUÁRIO
    // ***
    @Operation(summary = "Cria um novo usuário - Cliente parceiro", description = "Cria um Cliente Parceiro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente Parceiro criado com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequestDTO userDTO, @RequestParam String roleUidPermission, @RequestParam Role role) {
        try {
            if (Objects.equals(role, Role.SYSADMIN.toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário não pode ser registrado como SYSADMIN.");
            } else if (role.equals(Role.PARTNER_CLIENT) || role.equals(Role.PARTNER_EMPLOYEE) || role.equals(Role.CLIENT)) {
                User user = userService.save(userDTO, roleUidPermission, role);
                return ResponseEntity.status(HttpStatus.CREATED).body(user);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Role não permitida. Utilize PARTNER_CLIENT ou PARTNER_EMPLOYEE.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    // ***
    // EDITAR UM USUÁRIO
    // ***
    @Operation(summary = "Atualiza um Usuário Existente - Cliente Parceiro", description = "Atualiza os dados de um Usuário Cliente Parceiro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com Sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateRequestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @PutMapping("/{uid}")
    public ResponseEntity<?> updateUser(@RequestParam String roleUidPermission, @RequestBody UserCreateRequestDTO userDTO) {
        try {
            if (Objects.equals(roleUidPermission, Role.SYSADMIN.toString()) || Objects.equals(roleUidPermission, Role.CLIENT.toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Usuário não pode ser registrado como SYSADMIN, apenas como Cliente Parceiro.");
            } else {
                User updatedUser = userService.update(userDTO);
                return ResponseEntity.ok(updatedUser);
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    // ***
    // DESATIVAR UM USUÁRIO
    // ***
    @Operation(summary = "Desativa um Usuário Existente - Cliente Parceiro", description = "Desativa um Usuário Existe - Cliente Parceiro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário Desativado com Sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateRequestDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados"),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @DeleteMapping("/{uid}")
    public ResponseEntity<String> deleteUser(@PathVariable String uid) {
        try {
            userService.disable(uid);
            return ResponseEntity.ok("Usuário Desativado com Sucesso");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Não é permitido desativar SYSADMIN ou ADMIN.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}