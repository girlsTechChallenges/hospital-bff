# Hospital API

Sistema de API para gerenciamento de consultas hospitalares com autenticação JWT e Clean Architecture.

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
java -jar target/hospital-bff-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
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

**Observações:**
- O primeiro build pode demorar alguns minutos
- A aplicação irá aguardar o banco de dados estar pronto
- Todos os serviços serão inicializados automaticamente

#### 2. Verificar se está funcionando

- **Aplicação**: `http://localhost:8080`
- **Kong Gateway**: `http://localhost:8000`
- **Kong Admin**: `http://localhost:8001`
- **Kong Manager**: `http://localhost:8002`

#### 3. Executar em background (detached mode)

```bash
docker-compose up --build -d
```

#### 4. Ver logs da aplicação

```bash
# Ver todos os logs
docker-compose logs

# Ver apenas logs da aplicação
docker-compose logs app

# Seguir logs em tempo real
docker-compose logs -f app
```

#### 5. Para parar

```bash
# Parar containers (preserva dados)
docker-compose down

# Parar e remover volumes (remove dados do banco)
docker-compose down -v
```

---

## 📊 Serviços Disponíveis

| Serviço | Porta Local | Porta Docker | Descrição |
|---------|-------------|--------------|-----------|
| Hospital API | 8080 | 8080 | API principal |
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
spring.datasource.username=postgres
spring.datasource.password=postgres
```

**Nota**: A aplicação conecta ao serviço `app-db` definido no `docker-compose.yml`.

## 🔑 Autenticação

A aplicação utiliza JWT para autenticação. As chaves públicas e privadas estão em:
- `src/main/resources/app.key` (chave privada)
- `src/main/resources/app.pub` (chave pública)

## 📖 API Documentation

Após iniciar a aplicação, a documentação da API estará disponível em:
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## 🐛 Troubleshooting

### Erro de conexão com banco (Execução Local)
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

## 🏗️ Arquitetura e Melhorias Implementadas

### ✅ **Melhorias Arquiteturais**

1. **Nomenclatura Corrigida:**
   - `ConsultCommandUseCase` → `AuthenticationCommandUseCase`
   - `ConsultQueryUseCase` → `AuthenticationQueryUseCase`
   - Separação clara entre domínios de autenticação e consultas médicas

2. **Separação de Responsabilidades:**
   - Lógica de JWT removida dos gateways e centralizada nos use cases
   - Gateways agora fazem apenas acesso a dados
   - Use cases processam regras de negócio

3. **Padrão de Agregação de Dados:**
   - Implementação de agregação de dados de múltiplos serviços
   - Chamadas assíncronas para melhoria de performance
   - Graceful degradation para resiliência

### 🔧 **Padrões RESTful Implementados**

1. **Versionamento da API:**
   ```
   /api/v1/auth/*
   /api/v1/consults/*
   ```

2. **Códigos de Status HTTP Corretos:**
   - 201 para criação de recursos
   - 200 para consultas bem-sucedidas
   - 204 para atualizações sem retorno
   - 400 para erros de validação
   - 401 para falhas de autenticação
   - 404 para recursos não encontrados
   - 503 para serviços indisponíveis

3. **Tratamento de Erros Padronizado:**
   - Global Exception Handler
   - Respostas consistentes para diferentes tipos de erro
   - Logs estruturados

### 📊 **Monitoramento e Observabilidade**

1. **Health Checks:**
   - Monitoramento de serviços externos
   - Endpoints Actuator configurados
   - Verificação de conectividade

2. **Métricas e Logs:**
   - Logging estruturado com níveis apropriados
   - Métricas Prometheus
   - Rastreamento de chamadas externas

### 🔒 **Segurança e Validação**

1. **Validação Robusta:**
   - Bean Validation em todos os endpoints
   - Sanitização de inputs
   - Respostas de erro detalhadas para validação

2. **Criptografia de Senhas:**
   - BCrypt para hash de senhas
   - Implementação segura no use case de comando

## 🏗️ Estrutura da Aplicação

```
src/main/java/com/fiap/hospital/bff/
├── core/
│   ├── domain/model/          # Modelos de domínio
│   ├── inputport/             # Interfaces de casos de uso
│   ├── outputport/            # Interfaces de gateways
│   └── usecase/               # Implementações dos casos de uso
├── infra/
│   ├── adapter/gateway/       # Implementações dos gateways
│   ├── config/                # Configurações
│   ├── entrypoint/controller/ # Controllers REST
│   ├── exception/             # Tratamento de exceções
│   └── service/               # Serviços de agregação
```

## 🔗 Endpoints da API

### Autenticação
- `POST /api/v1/auth/login` - Login de usuário
- `PATCH /api/v1/auth/password` - Atualização de senha

### Consultas
- `GET /api/v1/consults` - Listar todas as consultas
- `GET /api/v1/consults/{id}` - Buscar consulta por ID
- `GET /api/v1/consults/{id}/details` - Buscar consulta com dados agregados
- `GET /api/v1/consults/patient/{patientId}` - Consultas por paciente
- `POST /api/v1/consults` - Criar nova consulta
- `PUT /api/v1/consults/{id}` - Atualizar consulta

### Monitoramento
- `GET /actuator/health` - Status da aplicação
- `GET /actuator/metrics` - Métricas da aplicação

---

## 🧪 Estratégia de Testes

### 📋 **Visão Geral dos Testes**

Este projeto implementa uma estratégia completa de testes seguindo as melhores práticas de **TDD (Test-Driven Development)** com foco em cenários hospitalares específicos.

### ✅ **Abordagem TDD Implementada**
- **Testes escritos antes da implementação** (quando aplicável)
- **Cobertura de cenários de sucesso e falha**
- **Testes isolados com mocks** para dependências externas
- **Nomenclatura clara e descritiva** seguindo padrões BDD
- **Organização em classes aninhadas** para melhor legibilidade

### 🎯 **Princípios Seguidos**
- **Isolamento**: Cada teste é independente e não afeta outros
- **Clareza**: Nomes descritivos explicam o comportamento esperado
- **Organização**: Testes agrupados por funcionalidade usando `@Nested`
- **Manutenibilidade**: Uso de builders e utilitários para reduzir duplicação

### 🗂️ **Estrutura dos Testes**

#### 📁 **Testes Unitários**

```
src/test/java/com/fiap/hospital/bff/
├── core/usecase/                    # Testes de casos de uso (regras de negócio)
│   ├── UserCommandUseCaseImplTest
│   ├── UserQueryUseCaseImplTest
│   ├── AuthenticationCommandUseCaseImplTest
│   └── AuthenticationQueryUseCaseImplTest
├── infra/entrypoint/mapper/         # Testes de mapeamento de dados
│   └── UserMapperTest
├── infra/adapter/gateway/           # Testes de gateways (acesso a dados)
│   ├── SaveGatewayImplTest
│   └── FindByGatewayImplTest
├── infra/adapter/easyconsult/       # Testes de serviços externos
│   └── EasyConsultServiceTest
└── util/                           # Utilitários para testes
    └── TestDataBuilder
```

#### 📁 **Testes de Integração**

Os testes de integração foram criados com foco em cenários específicos do ambiente hospitalar:

**AuthControllerEnhancedIntegrationTest:**
- ✅ Login por tipos de usuário (PACIENTE, MEDICO, ENFERMEIRO)
- ✅ Segurança e validação robusta
- ✅ Rotação de senhas com validação completa
- ✅ Testes de performance para emergências
- ✅ Validação LGPD (dados sensíveis protegidos)

**UserControllerAdvancedIntegrationTest:**
- ✅ Listagem para interconsulta (médico busca colegas)
- ✅ Atualização de dados corporativos
- ✅ Exclusão controlada (compliance LGPD)
- ✅ Testes de carga simulando múltiplos cadastros

### 📊 **Cobertura de Testes**

#### 🎯 **Use Cases (Casos de Uso)**
- ✅ **UserCommandUseCaseImpl**: Criação, atualização e exclusão de usuários
- ✅ **UserQueryUseCaseImpl**: Consultas de usuários
- ✅ **AuthenticationCommandUseCaseImpl**: Comandos de autenticação
- ✅ **AuthenticationQueryUseCaseImpl**: Validação de login e geração de tokens

#### 🔄 **Mappers**
- ✅ **UserMapper**: Conversões entre DTOs, Domain Objects e Entities

#### 🚪 **Gateways**
- ✅ **SaveGatewayImpl**: Persistência de usuários
- ✅ **FindByGatewayImpl**: Consultas de usuários

#### 🌐 **Serviços Externos**
- ✅ **EasyConsultService**: Integração com serviços de consulta

### 🛠️ **Ferramentas e Bibliotecas**

```xml
<!-- JUnit 5 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- H2 Database para testes -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>

<!-- Spring Security Test -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 🏥 **Cenários Hospitalares Testados**

#### **🔐 Autenticação:**
- **Médico** fazendo login para plantão noturno
- **Enfermeiro** acessando sistema durante troca de turno
- **Paciente** consultando histórico médico
- **Rotação de senhas** por política de segurança

#### **👥 Gestão de Usuários:**
- **Recepcionista** cadastrando paciente rapidamente
- **RH** cadastrando médico recém-contratado
- **Coordenação** transferindo enfermeiro de setor
- **Médico** buscando especialista para interconsulta

#### **⚡ Performance e Carga:**
- **Múltiplos cadastros** simultâneos (dia de vacinação)
- **Login rápido** para emergências médicas
- **Listagem eficiente** mesmo com muitos usuários

### 🚀 **Executando os Testes**

#### 📋 **Comandos Maven**

```bash
# Executar todos os testes
mvn test

# Executar apenas testes unitários
mvn test -Dtest="**/*Test"

# Executar apenas testes de integração
mvn test -Dtest="**/*IntegrationTest"

# Executar testes específicos
mvn test -Dtest=UserCommandUseCaseImplTest

# Executar testes com cobertura
mvn test jacoco:report

# Executar testes em modo silencioso
mvn test -q
```

#### 📊 **Perfis de Teste**
Os testes utilizam o perfil `test` com configurações específicas:
- Banco H2 em memória
- Logs em nível DEBUG
- URLs de serviços externos mockadas

### 🎯 **Métricas de Qualidade**

#### ✅ **Resultados Atuais**
- **67+ testes implementados**
- **100% de sucesso** nos testes unitários e de integração
- **Cobertura das principais regras de negócio**
- **Isolamento completo** com mocks
- **Cenários hospitalares** específicos validados

#### 📈 **Benefícios Alcançados**
- **Detecção precoce de bugs**
- **Refatoração segura** com testes de regressão
- **Documentação viva** do comportamento esperado
- **Qualidade de código** melhorada
- **Compliance LGPD/HIPAA** verificado
- **Performance de emergência** testada

### 🔍 **Principais Funcionalidades Testadas**

#### 👤 **Gestão de Usuários**
- ✅ Cadastro de usuários (PACIENTE, MEDICO, ENFERMEIRO)
- ✅ Validação de email único
- ✅ Criptografia de senhas
- ✅ Consulta e atualização de dados

#### 🔐 **Autenticação**
- ✅ Login com email/senha
- ✅ Geração de tokens JWT
- ✅ Validação de credenciais
- ✅ Atualização de senhas

#### 🏥 **Integração de Consultas**
- ✅ Criação e busca de consultas
- ✅ Agregação de dados de múltiplos serviços
- ✅ Tratamento de falhas de serviços externos
- ✅ Graceful degradation

---

## 💡 **Exemplo de Resposta Agregada**

```json
{
  "consult": {
    "id": 1,
    "patientId": 123,
    "doctorId": 456,
    "patientName": "João Silva",
    "consultDateTime": "2024-01-15",
    "consultStatus": "SCHEDULED"
  },
  "patientDetails": {
    "id": 123,
    "name": "João Silva",
    "email": "joao@email.com"
  },
  "doctorDetails": {
    "id": 456,
    "name": "Dr. Maria Santos",
    "specialty": "Cardiologia"
  }
}
```
