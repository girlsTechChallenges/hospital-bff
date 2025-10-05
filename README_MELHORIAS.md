# Hospital API

## Melhorias Arquiteturais Implementadas

### 🏗️ **Arquitetura e Separação de Camadas**

#### ✅ **Correções Implementadas:**

1. **Nomenclatura Corrigida:**
   - `ConsultCommandUseCase` → `AuthenticationCommandUseCase`
   - `ConsultQueryUseCase` → `AuthenticationQueryUseCase`
   - Separação clara entre domínios de autenticação e consultas médicas

2. **Separação de Responsabilidades Corrigida:**
   - Lógica de JWT removida dos gateways e centralizada nos use cases
   - Gateways agora fazem apenas acesso a dados
   - Use cases processam regras de negócio

3. **Padrão de Agregação de Dados:**
   - Implementação de agregação de dados de múltiplos serviços
   - Chamadas assíncronas para melhoria de performance
   - Graceful degradation para resiliência

### 🔧 **Padrões RESTful Implementados**

#### ✅ **Melhorias RESTful:**

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

#### ✅ **Recursos Adicionados:**

1. **Health Checks:**
   - Monitoramento de serviços externos
   - Endpoints Actuator configurados
   - Verificação de conectividade

2. **Métricas e Logs:**
   - Logging estruturado com níveis apropriados
   - Métricas Prometheus
   - Rastreamento de chamadas externas

### 🔒 **Segurança e Validação**

#### ✅ **Melhorias de Segurança:**

1. **Validação Robusta:**
   - Bean Validation em todos os endpoints
   - Sanitização de inputs
   - Respostas de erro detalhadas para validação

2. **Criptografia de Senhas:**
   - BCrypt para hash de senhas
   - Implementação segura no use case de comando

## Estrutura da Aplicação

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

## Endpoints da API

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

## Exemplo de Resposta Agregada

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

## Configuração

As configurações principais estão em `application.properties`:

```properties
# URLs dos serviços externos
external.services.consult.url=http://kong:8000
external.services.patient.url=http://kong:8000
external.services.doctor.url=http://kong:8000

# Timeouts HTTP
spring.http.client.connect-timeout=5000
spring.http.client.read-timeout=10000

# Monitoramento
management.endpoints.web.exposure.include=health,info,metrics,prometheus
```

## Execução

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## Melhorias Implementadas - Resumo

1. ✅ **Arquitetura Hexagonal corrigida** - Separação clara de responsabilidades
2. ✅ **Nomenclatura consistente** - Interfaces refletem o domínio real
3. ✅ **Padrão de agregação adequado** - Agregação de dados e resiliência
4. ✅ **Versionamento da API** - `/api/v1/`
5. ✅ **Tratamento de erros global** - Respostas padronizadas
6. ✅ **Health checks** - Monitoramento de serviços externos
7. ✅ **Logging estruturado** - Rastreamento adequado
8. ✅ **Validação robusta** - Bean Validation em todos os endpoints
9. ✅ **Segurança melhorada** - Criptografia e sanitização
10. ✅ **APIs RESTful limpas** - Respostas diretas sem complexidade desnecessária
