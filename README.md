# Hospital BFF

Serviço Backend for Frontend (BFF) para o sistema de agendamento de consultas hospitalares.

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot 3.5.5**
- **PostgreSQL**
- **Docker & Docker Compose**
- **Maven**
- **JWT Authentication**
- **Spring Security**

## 🚀 Como Executar

### Pré-requisitos

- Java 21
- Maven
- Docker e Docker Compose

### 📋 Opção 1: Execução Local (Desenvolvimento)

Esta opção roda apenas o banco de dados no Docker e a aplicação localmente.

#### 1. Iniciar o banco PostgreSQL

```bash
docker-compose up app-db -d
```

#### 2. Compilar o projeto

```bash
./mvnw clean package -DskipTests
```

#### 3. Executar a aplicação

```bash
java -jar target/easyconsult-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

#### 4. Verificar se está funcionando

A aplicação estará disponível em: `http://localhost:8080`

#### 5. Para parar

```bash
# Parar a aplicação: Ctrl+C no terminal
# Parar o banco:
docker-compose down app-db
```

---

### 🐳 Opção 2: Execução Completa com Docker

Esta opção roda toda a aplicação (app + banco + Kong Gateway) no Docker.

#### 1. Executar tudo

```bash
docker-compose up --build
```

#### 2. Verificar se está funcionando

- **Aplicação**: `http://localhost:8080`
- **Kong Gateway**: `http://localhost:8000`
- **Kong Admin**: `http://localhost:8001`
- **Kong Manager**: `http://localhost:8002`

#### 3. Para parar

```bash
docker-compose down
```

---

## 📊 Serviços Disponíveis

| Serviço | Porta Local | Porta Docker | Descrição |
|---------|-------------|--------------|-----------|
| Hospital BFF | 8080 | 8080 | API principal |
| PostgreSQL | 5432 | 5432 | Banco de dados |
| Kong Gateway | 8000 | 8000 | API Gateway |
| Kong Admin | 8001 | 8001 | Kong Admin API |
| Kong Manager | 8002 | 8002 | Kong Interface Web |

## 🔧 Configuração

### Desenvolvimento Local

O perfil `dev` usa as configurações em `src/main/resources/application-dev.properties`:

```properties
# Banco local
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Docker

O perfil padrão usa as configurações em `src/main/resources/application.properties`:

```properties
# Banco via Docker
spring.datasource.url=jdbc:postgresql://app-db:5432/postgres
spring.datasource.password=${POSTGRES_PASSWORD}
```

## 🔑 Autenticação

A aplicação utiliza JWT para autenticação. As chaves públicas e privadas estão em:
- `src/main/resources/app.key` (chave privada)
- `src/main/resources/app.pub` (chave pública)

## 📖 API Documentation

Após iniciar a aplicação, a documentação da API estará disponível em:
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## 🐛 Troubleshooting

### Erro de conexão com banco
```
Connection to localhost:5432 refused
```
**Solução**: Certifique-se de que o PostgreSQL está rodando:
```bash
docker-compose up app-db -d
```

### Porta já está em uso
```
Port 8080 was already in use
```
**Solução**: 
- Pare outros processos na porta 8080
- Ou altere a porta em `application.properties`:
```properties
server.port=8081
```

---


