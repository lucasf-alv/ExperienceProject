# Project Experience API

API REST desenvolvida em Spring Boot para gerenciamento de atividades, usuários, participantes, autenticação JWT, conquistas e progresso de usuários.

---

# Tecnologias utilizadas

- Java 26
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Liquibase
- Docker
- LocalStack (Amazon S3)
- Swagger OpenAPI
- Gradle

---

# Pré-requisitos

Antes de executar o projeto, instale:

- Java 26+
- Docker
- Docker Compose
- Git

Verifique as versões:

```bash
java -version
docker --version
git --version
```

---

# Clonando o projeto

```bash
git clone https://github.com/SEU_USUARIO/ExperienceProject.git

cd ExperienceProject
```

---

# Configuração do ambiente

O projeto utiliza:

- PostgreSQL
- LocalStack para simular o Amazon S3

---

# 1. Iniciar o LocalStack

```bash
docker run -d \
  --name localstack \
  -p 4566:4566 \
  -e SERVICES=s3 \
  -e DEBUG=1 \
  localstack/localstack
```

---

# 2. Iniciar o PostgreSQL

Caso o container já exista:

```bash
docker start project-experience-db
```

Caso ainda não exista, crie o container utilizando sua configuração do PostgreSQL.

---

# 3. Criar o bucket S3

Após iniciar o LocalStack execute:

```bash
docker exec localstack awslocal s3 mb s3://avatares
```

---

# 4. Acessar o banco de dados

Para abrir o terminal do PostgreSQL:

```bash
docker exec -it project-experience-db psql -U postgres -d project_experience
```

---

# 5. Verificar os containers

```bash
docker ps
```

Você deverá visualizar containers semelhantes a:

- project-experience-db
- localstack

---

# 6. Iniciar containers parados

Caso eles estejam criados mas desligados:

```bash
docker start localstack

docker start project-experience-db
```

---

# Executando a aplicação

Na raiz do projeto:

```bash
./gradlew bootRun
```

ou

```bash
gradle bootRun
```

---

# Documentação da API

Após iniciar a aplicação, a documentação estará disponível em:

### Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON

```
http://localhost:8080/v3/api-docs
```

---

# Banco de Dados

O projeto utiliza:

- PostgreSQL
- Liquibase para versionamento do banco

As migrations são executadas automaticamente ao iniciar a aplicação.

---

# Armazenamento de arquivos

As imagens de usuários e atividades são armazenadas em um bucket S3 simulado pelo LocalStack.

Bucket utilizado:

```
avatares
```

---

# Autenticação

A API utiliza autenticação JWT.

Fluxo:

1. Criar usuário

```
POST /auth/register
```

2. Fazer login

```
POST /auth/login
```

3. Copiar o token JWT retornado.

4. Autorizar no Swagger clicando em **Authorize** e informando:

```
Bearer seu_token
```

---

# Funcionalidades

- Cadastro de usuários
- Login JWT
- Atualização de perfil
- Upload de avatar
- Criação de atividades
- Inscrição em atividades
- Aprovação de participantes
- Check-in
- Conclusão de atividades
- Sistema de XP
- Sistema de conquistas
- Upload de imagens para o S3 (LocalStack)

---

# Licença

Projeto desenvolvido para fins acadêmicos.


