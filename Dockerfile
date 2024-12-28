# Usa a imagem base do OpenJDK
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo jar da aplicação para o container
COPY target/ReachoutSystem-0.0.1-SNAPSHOT.jar /app/app.jar

# Copia o arquivo de configuração do Firebase para o diretório correto dentro do container
COPY src/main/resources/firebaseServiceAccount.json src/main/resources/firebaseServiceAccount.json

# Configurar variáveis de ambiente (se necessário)
ENV SPRING_PROFILES_ACTIVE=prod

# Expondo a porta usada pela aplicação
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
