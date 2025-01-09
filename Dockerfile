# Estágio 1: Build do projeto
FROM maven:3.9.4-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copia apenas os arquivos necessários para resolver dependências
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copia o restante do código para o container
COPY . ./

# Estágio 2: Imagem final para execução
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia todo o projeto para o container
COPY --from=builder /app /app

# Define as variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod

# Expõe a porta usada pela aplicação
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["mvn", "spring-boot:run"]
