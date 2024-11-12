package com.reachout.ReachoutSystem.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ReachOut - Core Service")
                        .description("API para gerenciar anúncios de estabelecimentos e seus respectivos clientes parceiros. Permite a criação, edição, listagem e exclusão de anúncios, bem como a gestão de campanhas publicitárias e dados de clientes.")
                        .version("v2.0")
                        .contact(new Contact()
                                .name("Rafael Leandro Diniz Soares")
                                .email("sawarafael@gmail.com")
                        )
                        .license(new License()
                                .name("Licença MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
