package com.reachout.ReachoutSystem.analytics.controller;

import com.reachout.ReachoutSystem.analytics.dto.UserRegisteredListDTO;
import com.reachout.ReachoutSystem.analytics.service.UserAnalyticsService;
import com.reachout.ReachoutSystem.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/analytic/users")
@Tag(name = "Analítico de Usuários", description = "Endpoints de controle de Análise de Dados de Usuários.")
public class UserAnalyticsController {

    private final UserAnalyticsService userAnalyticsService;

    public UserAnalyticsController(UserAnalyticsService userAnalyticsService) {
        this.userAnalyticsService = userAnalyticsService;
    }

    // ***
    // ANALISE DE DADOS DE QUANTOS CLIENTES PARCEIROS FORAM CADASTRADOS
    // ***
    @Operation(summary = "Listagem Analítica de Clientes Parceiros Cadastrados", description = "Recupera uma lista de informações com todos os Usuários Clientes Parceiros Cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "500", description = "Erro Interno do Servidor")
    })
    @GetMapping(value = "/key-partners-registereds")
    public ResponseEntity<?> getAllRegisteredKeyPartners(Pageable pageable) {
        try {
            return ResponseEntity.ok(this.userAnalyticsService.getAllUserRegistereds(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Falha ao resgatar os dados para análise de Usuários: " + e.getMessage());
        }
    }

    // **
    // ANALISE DE CONTAGEM TOTAL DE USUARIOS REGISTRADOS
    // **


    // Últimos 5 clientes registrados
    // Quantos Clientes Parceiros Cadastrados possuem Estabelecimentos?
}
