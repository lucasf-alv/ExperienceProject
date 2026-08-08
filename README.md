# Project Experience

Aplicação web para criação, descoberta e participação em atividades, com autenticação de usuários, preferências, participantes, check-in, conquistas e armazenamento de imagens.

O projeto é dividido em **backend REST em Spring Boot** e **frontend em React + TypeScript**.

---

## 🚀 Tecnologias utilizadas

### Backend

* Java 26
* Spring Boot 4
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* PostgreSQL
* Liquibase
* Gradle
* Swagger / OpenAPI
* AWS SDK for Java (S3)

### Frontend

* React
* TypeScript
* Vite
* Tailwind CSS
* React Router
* Axios
* React Leaflet
* Lucide React

### Infraestrutura

* Docker
* Docker Compose
* PostgreSQL
* LocalStack
* Amazon S3 (simulado pelo LocalStack)

---

# 📋 Pré-requisitos

Antes de executar o projeto, tenha instalado:

* Git
* Docker
* Docker Compose

Java e Gradle **não são necessários para executar a aplicação pelo Docker**, pois o backend é compilado dentro da imagem Docker.

Verifique:

```bash
git --version
docker --version
docker compose version
```

---

# 📥 Clonando o projeto

```bash
git clone https://github.com/SEU_USUARIO/ExperienceProject.git

cd ExperienceProject
```

---

# ▶️ Executando o projeto

O projeto utiliza Docker Compose para iniciar todos os serviços necessários.

Na raiz do projeto, onde está localizado o `docker-compose.yml`, execute:

```bash
docker compose up -d --build
```

Esse comando irá:

* construir o backend;
* construir o frontend;
* iniciar o PostgreSQL;
* iniciar o LocalStack;
* iniciar a aplicação backend;
* iniciar a aplicação frontend.

---

# 🐳 Verificando os containers

Execute:

```bash
docker ps
```

Os principais containers serão semelhantes a:

```text
backend
frontend
postgres
localstack
```

Para acompanhar os logs:

### Backend

```bash
docker logs -f backend
```

### Frontend

```bash
docker logs -f frontend
```

### PostgreSQL

```bash
docker logs postgres
```

### LocalStack

```bash
docker logs localstack
```

---

# 🗄️ Banco de dados

O projeto utiliza PostgreSQL.

O banco é configurado pelo `docker-compose.yml` e as tabelas são criadas/versionadas automaticamente através do **Liquibase**.

As migrations estão localizadas em:

```text
backend/src/main/resources/db/changelog/
```

Ao iniciar o backend, o Liquibase executa automaticamente as migrations pendentes.

---

# 🪣 Armazenamento de imagens

O projeto utiliza o **LocalStack** para simular o Amazon S3 localmente.

O bucket utilizado pela aplicação é:

```text
avatares
```

Após iniciar os containers, crie o bucket:

```bash
docker exec localstack awslocal s3 mb s3://avatares
```

Verifique:

```bash
docker exec localstack awslocal s3 ls
```

O resultado deverá conter:

```text
avatares
```

### Ver arquivos armazenados

```bash
docker exec localstack awslocal s3 ls s3://avatares/ --recursive
```

As imagens de usuários e atividades são armazenadas nesse bucket.

---

# 🔄 Executando tudo do zero

Caso seja necessário apagar os containers, volumes e banco de dados e começar novamente:

```bash
docker compose down -v
```

Depois:

```bash
docker compose up -d --build
```

E recrie o bucket:

```bash
docker exec localstack awslocal s3 mb s3://avatares
```

> ⚠️ O comando `docker compose down -v` remove os volumes do Docker. Isso significa que os dados do PostgreSQL serão apagados.

---

# 🌐 Acessando a aplicação

### Frontend

```text
http://localhost:5173
```

### Backend

```text
http://localhost:8080
```

---

# 📚 Documentação da API

Com o backend em execução, a documentação pode ser acessada pelo Swagger.

### Swagger UI

```text
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI

```text
http://localhost:8080/v3/api-docs
```

---

# 🔐 Autenticação

A API utiliza autenticação baseada em **JWT**.

### 1. Criar usuário

```http
POST /auth/register
```

### 2. Fazer login

```http
POST /auth/login
```

Exemplo:

```json
{
  "email": "usuario@email.com",
  "password": "123456"
}
```

A API retornará um token JWT.

### 3. Utilizar o token

No Swagger, clique em:

```text
Authorize
```

E informe:

```text
Bearer SEU_TOKEN
```

---

# 🏃 Funcionalidades

## Usuários

* Cadastro de usuários
* Login
* Autenticação JWT
* Atualização de perfil
* Upload de avatar
* Sistema de XP
* Sistema de conquistas

## Atividades

* Criação de atividades
* Edição de atividades
* Exclusão de atividades
* Listagem de atividades
* Filtragem por tipo
* Atividades recomendadas
* Atividades privadas
* Controle de participantes
* Aprovação de participantes
* Inscrição em atividades
* Check-in
* Conclusão de atividades
* Upload de imagens

## Preferências

* Seleção de tipos de atividades
* Personalização das atividades recomendadas

## Localização

* Seleção de localização das atividades
* Visualização através de mapa

---

# 📁 Estrutura do projeto

```text
ExperienceProject/
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   │           └── db/
│   │               └── changelog/
│   │
│   ├── build.gradle
│   ├── Dockerfile
│   └── gradlew
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── assets/
│   │   └── types/
│   │
│   ├── package.json
│   ├── Dockerfile
│   └── vite.config.ts
│
├── docker-compose.yml
└── README.md
```

---

# 🧪 Testando o login

Com a aplicação rodando:

```bash
curl -i -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"usuario@email.com","password":"123456"}'
```

Uma resposta bem-sucedida deverá retornar `HTTP 200` e um token JWT:

```json
{
  "token": "SEU_TOKEN",
  "id": 1,
  "name": "usuario",
  "email": "usuario@email.com"
}
```

---

# 🛑 Parando a aplicação

Para parar os containers:

```bash
docker compose down
```

Para parar e remover também os volumes:

```bash
docker compose down -v
```

---

# ⚠️ Configuração de ambiente

Não envie para o GitHub:

* senhas reais;
* tokens;
* chaves secretas;
* arquivos `.env` contendo credenciais;
* configurações de produção.

Para desenvolvimento local, utilize variáveis de ambiente ou arquivos de configuração específicos para o ambiente.

---

# 📌 Observações

O ambiente de desenvolvimento utiliza o **LocalStack** para simular o Amazon S3. Portanto, as imagens armazenadas localmente não são enviadas para a AWS real.

O PostgreSQL e o LocalStack são inicializados através do Docker Compose.

As migrations do banco são executadas automaticamente pelo Liquibase quando o backend é iniciado.

---

# 📄 Licença

Projeto desenvolvido para fins acadêmicos.



