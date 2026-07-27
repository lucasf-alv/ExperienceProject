# Project Experience API

API desenvolvida em Spring Boot para gerenciamento de atividades, usuários, participantes, autenticação JWT, conquistas e progresso de usuários.

## Tecnologias utilizadas

- Java 26
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Liquibase
- Docker
- LocalStack S3
- Swagger OpenAPI
- Gradle

---

# Pré-requisitos

Antes de executar o projeto, tenha instalado:

- Java 26+
- Docker
- Docker Compose
- Git

Verifique as versões:

java -version
docker --version
git --version

# Configuração do Ambiente com Docker

O projeto utiliza:

- PostgreSQL para persistência dos dados.
- LocalStack S3 para armazenamento de imagens (avatar e imagens de atividades).

---

# Subindo o LocalStack (S3)

Execute o comando abaixo para iniciar o LocalStack com o serviço S3 habilitado:

```bash
docker run -d \
  --name localstack \
  -p 4566:4566 \
  -e SERVICES=s3 \
  -e DEBUG=1 \
  localstack/localstack


