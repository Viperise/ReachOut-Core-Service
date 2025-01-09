# Estágio 1: Build do projeto
FROM maven:3.9.4-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copia apenas os arquivos necessários para resolver dependências
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copia o restante do código para o container e executa o build
COPY . ./
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final para execução
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia o JAR gerado no estágio de build
COPY --from=builder /app/target/ReachoutSystem-0.0.1-SNAPSHOT.jar /app/app.jar

# Copia o arquivo de configuração do Firebase
COPY src/main/resources/firebaseServiceAccount.json src/main/resources/firebaseServiceAccount.json

# Define as variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod

# Expõe a porta usada pela aplicação
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
