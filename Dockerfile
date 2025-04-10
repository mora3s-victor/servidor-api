# Usando a imagem base do OpenJDK 17
FROM openjdk:17-jdk-slim

# Definindo o diretório de trabalho
WORKDIR /app

# Copiando o arquivo JAR da aplicação
COPY target/*.jar app.jar

# Expondo a porta que a aplicação usa
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"] 