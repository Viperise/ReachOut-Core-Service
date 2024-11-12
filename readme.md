# ReachOut - V2.0.2
## Sistema de Gerenciamento de Anúncios

Este projeto é uma API para gerenciar anúncios de estabelecimentos e seus respectivos clientes parceiros. Ele permite a criação, edição, listagem e exclusão de anúncios, bem como a gestão de campanhas publicitárias e dados de clientes.

## Tecnologias Utilizadas

- **Java 17**
- **Maven 3**
- **Spring Boot**
- **PostgreSQL**
- **Spring Data JPA**
- **Swagger (Springdoc OpenAPI)**
- **Lombok**

## Pré-requisitos

Antes de configurar o projeto localmente, certifique-se de ter os seguintes softwares instalados:

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou superior
- [Maven 3](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/) (banco de dados relacional)

## Configuração do Banco de Dados

1. **Instale o PostgreSQL** e crie um banco de dados para o projeto:
   ```sql
   CREATE DATABASE reachoutdb;

2. **Crie um usuário e conceda permissões ao banco de dados:**
    ```sql
    CREATE USER usuario_gerenciador WITH PASSWORD 'senha_secreta';
    GRANT ALL PRIVILEGES ON DATABASE gerenciador_anuncios TO usuario_gerenciador;

3. **Ajuste a configuração no arquivo application.properties ou application.yml:**

    ```
    # application.properties
   
    spring.datasource.url=jdbc:postgresql://localhost:5432/gerenciador_anuncios
    spring.datasource.username=usuario_gerenciador
    spring.datasource.password=senha_secreta
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

4. **Compile e instale as dependências com o Maven:**
    ```md
    mvn clean install

5. **Execute o Projeto:**
    ```
    mvn spring-boot:run


### Autor
Rafael Soares - @harunean - Viperise Backend Developer

### Licença
Este projeto está licenciado sob a Licença MIT.