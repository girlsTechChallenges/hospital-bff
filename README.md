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
- **GraphQL** (para integração com serviços de consulta)
- **Spring GraphQL Client**
- **JaCoCo** (cobertura de testes)

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
| Kong Gateway | 8000 | 8000 | API Gateway (Proxy) |
| Kong Admin API | 8001 | 8001 | Kong Admin API |
| Kong Manager | 8002 | 8002 | Kong Interface Web |
| Kong SSL Proxy | 8443 | 8443 | Gateway HTTPS |
| Kong Admin SSL | 8444 | 8444 | Admin API HTTPS |

## 🌐 Kong API Gateway

O projeto utiliza o **Kong Gateway** como API Gateway para gerenciar e rotear requisições para a API hospitalar. Kong é um gateway de API open-source que oferece funcionalidades avançadas de proxy, segurança e observabilidade.

### 🔧 **Configuração Kong**

#### **Modo Database-less (DBless)**
- Kong roda em modo **declarativo sem banco de dados**
- Configuração via arquivo `kong-config/kong.yml`
- Melhor performance e simplicidade para APIs menores
- Configuração versionada junto com o código

#### **Arquitetura de Rede**
```
Cliente → Kong Gateway (8000) → Hospital API (8080)
```

### 📋 **Rotas Configuradas**

O Kong está configurado para rotear as seguintes rotas da API:

```yaml
# Autenticação
/api/v1/auth → Hospital API

# Gestão de Usuários  
/api/v1/users → Hospital API

# Consultas Médicas
/api/v1/consults → Hospital API
```

### ⚙️ **Funcionalidades Ativas**

#### **🔄 Resiliência**
- **Timeout**: 60 segundos para conexão/leitura/escrita
- **Retries**: 5 tentativas automáticas em caso de falha
- **Circuit Breaker**: Proteção contra sobrecarga

#### **📊 Observabilidade**
- **Logs de Acesso**: Requisições registradas em stdout
- **Logs de Erro**: Erros registrados em stderr
- **Métricas**: Disponíveis via Admin API

#### **🔒 Segurança**
- **Rate Limiting**: Controle de taxa de requisições
- **CORS**: Configuração de políticas cross-origin
- **SSL/TLS**: Suporte HTTPS na porta 8443

### 🚀 **Usando o Kong Gateway**

#### **Acessar API via Kong**
```bash
# Via Kong Gateway (recomendado)
curl http://localhost:8000/api/v1/auth/login

# Diretamente na API (desenvolvimento)
curl http://localhost:8080/api/v1/auth/login
```

#### **Monitoramento via Admin API**
```bash
# Status dos serviços
curl http://localhost:8001/services

# Status das rotas
curl http://localhost:8001/routes

# Métricas de saúde
curl http://localhost:8001/status
```

#### **Interface Web Kong Manager**
- **URL**: http://localhost:8002
- **Funcionalidades**:
  - Visualização de rotas e serviços
  - Monitoramento de tráfego
  - Configuração de plugins
  - Análise de logs e métricas

### 🛠️ **Configuração Avançada**

#### **Adicionando Plugins**
Para adicionar plugins Kong, edite o arquivo `kong-config/kong.yml`:

```yaml
services:
- name: hospital-bff
  url: http://app:8080
  plugins:
  - name: rate-limiting
    config:
      minute: 100
      hour: 1000
  - name: cors
    config:
      origins: ["*"]
      methods: ["GET", "POST", "PUT", "DELETE", "PATCH"]
```

#### **Rate Limiting para Hospital**
```yaml
# Exemplo: Limitação para consultas médicas
- name: rate-limiting
  service: hospital-bff
  route: easy-consult
  config:
    minute: 30    # 30 consultas por minuto
    hour: 500     # 500 consultas por hora
```

#### **Autenticação JWT**
```yaml
# Exemplo: Validação JWT no Kong
- name: jwt
  service: hospital-bff
  config:
    key_claim_name: iss
    secret_is_base64: false
```

### 📈 **Benefícios do Kong no Projeto**

#### **🏥 Para Ambiente Hospitalar**
- **Alta Disponibilidade**: Retry automático para APIs críticas
- **Monitoramento**: Tracking de APIs médicas sensíveis
- **Segurança**: Proteção contra ataques DDoS
- **Compliance**: Logs auditáveis para LGPD/HIPAA

#### **👩‍💻 Para Desenvolvimento**
- **Proxy Transparente**: Não afeta desenvolvimento local
- **Debugging**: Logs detalhados de todas as requisições
- **Testing**: Ambiente consistente entre dev/prod
- **Versionamento**: Rotas versionadas facilmente

### 🔍 **Troubleshooting Kong**

#### **Verificar Status do Kong**
```bash
# Status geral
docker logs kong-dbless-readonly

# Recarregar configuração
curl -X POST http://localhost:8001/config \
  -F config=@kong-config/kong.yml
```

#### **Problemas Comuns**

**Kong não responde na porta 8000:**
```bash
# Verificar se container está rodando
docker ps | grep kong

# Verificar logs
docker logs kong-dbless-readonly

# Reiniciar Kong
docker restart kong-dbless-readonly
```

**Erro 502 Bad Gateway:**
- Verificar se Hospital API está rodando na porta 8080
- Conferir conectividade entre containers
- Validar configuração de rede no docker-compose

### 🎯 **Exemplos Práticos de Uso**

#### **Cenários Hospitalares com Kong**

**1. Login de Médico via Kong:**
```bash
# Login direto via Kong Gateway
curl -X POST http://localhost:8000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "medico@hospital.com",
    "password": "senha123456"
  }'
```

**2. Criação de Consulta com Rate Limiting:**
```bash
# Consulta via Kong (com limitação de taxa)
curl -X POST http://localhost:8000/api/v1/consults \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{
    "patient": {
      "name": "João Silva",
      "email": "joao@email.com"
    },
    "consultDate": "2025-10-14",
    "consultTime": "10:30:00",
    "consultReason": "Consulta de rotina"
  }'
```

**3. Monitoramento de API Hospitalar:**
```bash
# Métricas de uso da API
curl http://localhost:8001/metrics

# Status de saúde dos serviços
curl http://localhost:8001/services/hospital-bff/health
```

#### **Configuração para Produção**

**Kong com SSL/TLS:**
```yaml
# Configuração HTTPS para produção
routes:
- name: auth-secure
  paths: ["/api/v1/auth"]
  protocols: ["https"]
  https_redirect_status_code: 301
```

**Logs Estruturados:**
```yaml
# Plugin de logging para auditoria hospitalar
plugins:
- name: file-log
  config:
    path: "/var/log/kong/hospital-api.log"
    format: "json"
```

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

## 🔗 Integração GraphQL

O serviço se integra com um backend GraphQL para operações de consulta médica:

### Configuração
```properties
# URL do serviço GraphQL (padrão)
app.graphql.easyconsult.url=http://localhost:8081/graphql
```

### Funcionalidades GraphQL
- **Queries**: Busca de consultas com filtros
- **Mutations**: Criação, atualização e exclusão de consultas
- **Autenticação**: Propagação automática do JWT token
- **Resiliência**: Tratamento de falhas de conectividade
- **Agregação**: Combinação de dados de múltiplos serviços

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

### Consultas (GraphQL Integration)
- `GET /api/v1/consults` - Listar todas as consultas via GraphQL
- `GET /api/v1/consults/filter` - Buscar consultas com filtros específicos
- `POST /api/v1/consults` - Criar nova consulta via GraphQL
- `PUT /api/v1/consults` - Atualizar consulta via GraphQL
- `DELETE /api/v1/consults/{id}` - Excluir consulta via GraphQL

#### GraphQL Endpoints
O serviço se integra com um backend GraphQL externo para operações de consulta:
- **GraphQL URL**: `http://localhost:8081/graphql` (configurável)
- **Autenticação**: JWT Bearer Token
- **Operações**: Query, Mutation para CRUD de consultas

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
- ✅ **EasyConsultService**: Integração com serviços GraphQL de consulta
  - Criação, consulta, atualização e exclusão de consultas
  - Seleção automática de enfermeiros disponíveis
  - Tratamento de falhas de conectividade
  - Validação de autenticação JWT

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
# Executar todos os testes (153 testes)
mvn test

# Executar apenas testes unitários
mvn test -Dtest="**/*Test"

# Executar apenas testes de integração
mvn test -Dtest="**/*IntegrationTest"

# Executar testes específicos
mvn test -Dtest=EasyConsultServiceTest

# Executar testes com cobertura
mvn test jacoco:report

# Executar testes em modo silencioso
mvn test -q

# Visualizar relatório de cobertura
# Após executar 'mvn test jacoco:report'
# Abrir: target/site/jacoco/index.html
```

#### 📊 **Perfis de Teste**
Os testes utilizam o perfil `test` com configurações específicas:
- Banco H2 em memória
- Logs em nível DEBUG
- URLs de serviços externos mockadas

### 🎯 **Métricas de Qualidade**

#### ✅ **Resultados Atuais**
- **153 testes implementados** (atualizado em outubro/2025)
- **100% de sucesso** nos testes unitários e de integração
- **0 falhas, 0 erros** na última execução
- **Cobertura das principais regras de negócio**
- **Isolamento completo** com mocks
- **Cenários hospitalares** específicos validados
- **Integração GraphQL** totalmente testada

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

## 💡 **Exemplos de Uso**

### 📋 **Resposta de Consulta (GraphQL)**

```json
{
  "id": "1",
  "patient": {
    "name": "João Silva",
    "email": "joao.silva@email.com"
  },
  "nameProfessional": "Enfermeira Maria",
  "localTime": "10:30:00",
  "date": "2025-10-14",
  "statusConsult": "AGENDADA",
  "reason": "Consulta de rotina"
}
```

### 🔍 **Exemplo de Mutation GraphQL**

```graphql
mutation CreateFullConsult($input: ConsultRequestDto!) {
  createFullConsult(input: $input) {
    id
    patient {
      name
      email
    }
    nameProfessional
    localTime
    date
    statusConsult
    reason
  }
}
```

### 📊 **Status dos Testes (Última Execução)**

```
Tests run: 153, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
Total time: 40.084 s
```
