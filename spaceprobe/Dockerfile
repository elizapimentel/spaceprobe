# imagem Maven para compilar o projeto
FROM maven:3.9.8-eclipse-temurin-17 AS build

# diretório de trabalho dentro do contêiner
WORKDIR /app

# arquivo pom.xml e as dependências do projeto
COPY ./pom.xml ./pom.xml

# baixar dependências do Maven
#RUN mvn dependency:go-offline -B

# copiar código fonte do projeto
COPY ./src ./src

# Compilar o projeto e criar o arquivo JAR
RUN mvn clean package -DskipTests && cp ./target/spaceprobe-*.jar app.jar

# imagem JRE para executar o projeto
FROM eclipse-temurin:17-jdk

# diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR compilado da etapa anterior
COPY --from=build /app/app.jar ./app.jar

# comando padrão para iniciar a aplicação
CMD ["java", "-jar", "/app/app.jar"]
