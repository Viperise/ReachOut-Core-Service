package com.reachout.ReachoutSystem.users.controller;

import com.reachout.ReachoutSystem.users.dto.UserCreateRequestDTO;
import com.reachout.ReachoutSystem.users.dto.UserListResponseDTO;
import com.reachout.ReachoutSystem.users.entity.Role;
import com.reachout.ReachoutSystem.users.entity.User;
import com.reachout.ReachoutSystem.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    @Operation(summary = "Todos os Usuários", description = "Recupera uma lista de informações com todos os Usuários.")
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
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping("/{uid}")
    public ResponseEntity<User> getUserById(@PathVariable String uid) {
        System.out.println(uid);
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
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequestDTO userDTO, @RequestParam String roleUidPermission) {
        try {
            if (Objects.equals(roleUidPermission, Role.SYSADMIN.toString()) || Objects.equals(roleUidPermission, Role.ADMIN.toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Usuário não pode ser registrado como SYSADMIN, apenas como Cliente Parceiro.");
            } else {
                User user = userService.save(userDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(user);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserCreateRequestDTO userDTO) {
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}