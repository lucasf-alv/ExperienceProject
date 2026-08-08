FROM eclipse-temurin:26-jdk

WORKDIR /app

# Copia os arquivos do Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Dá permissão ao Gradle Wrapper
RUN chmod +x gradlew

# Baixa as dependências
RUN ./gradlew dependencies --no-daemon || true

# Copia o restante do projeto
COPY . .

# Gera o JAR
RUN ./gradlew bootJar --no-daemon

EXPOSE 8080

ENTRYPOINT ["java","-jar","build/libs/ProjectExperience-0.0.1-SNAPSHOT.jar"]