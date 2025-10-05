# 🧪 Testes Unitários - Hospital BFF

Este documento descreve a estratégia e estrutura dos testes unitários implementados no projeto Hospital BFF, seguindo as melhores práticas de **TDD (Test-Driven Development)**.

## 📋 Visão Geral

### ✅ Abordagem TDD Implementada
- **Testes escritos antes da implementação** (quando aplicável)
- **Cobertura de cenários de sucesso e falha**
- **Testes isolados com mocks** para dependências externas
- **Nomenclatura clara e descritiva** seguindo padrões BDD
- **Organização em classes aninhadas** para melhor legibilidade

### 🎯 Princípios Seguidos
- **Isolamento**: Cada teste é independente e não afeta outros
- **Clareza**: Nomes descritivos explicam o comportamento esperado
- **Organização**: Testes agrupados por funcionalidade usando `@Nested`
- **Manutenibilidade**: Uso de builders e utilitários para reduzir duplicação

## 🗂️ Estrutura dos Testes

### 📁 Camadas Testadas

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

## 📊 Cobertura de Testes

### 🎯 **Use Cases (Casos de Uso)**
- ✅ **UserCommandUseCaseImpl**: Criação, atualização e exclusão de usuários
- ✅ **UserQueryUseCaseImpl**: Consultas de usuários
- ✅ **AuthenticationCommandUseCaseImpl**: Comandos de autenticação
- ✅ **AuthenticationQueryUseCaseImpl**: Validação de login e geração de tokens

### 🔄 **Mappers**
- ✅ **UserMapper**: Conversões entre DTOs, Domain Objects e Entities

### 🚪 **Gateways**
- ✅ **SaveGatewayImpl**: Persistência de usuários
- ✅ **FindByGatewayImpl**: Consultas de usuários

### 🌐 **Serviços Externos**
- ✅ **EasyConsultService**: Integração com serviços de consulta

## 🛠️ Ferramentas e Bibliotecas

### 📚 **Dependências de Teste**
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

### 🔧 **Frameworks Utilizados**
- **JUnit 5**: Framework de testes principal
- **Mockito**: Criação de mocks e verificação de interações
- **AssertJ**: Assertions fluentes e expressivas
- **Spring Boot Test**: Integração com contexto Spring

## 📝 Padrões de Nomenclatura

### 🎯 **Estrutura dos Nomes de Teste**
```java
@DisplayName("Deve [resultado esperado] quando [condição]")
void should[ExpectedResult]_When[Condition]() {
    // Arrange
    // Act  
    // Assert
}
```

### 📋 **Exemplos de Nomenclatura**
- `shouldCreateUserSuccessfully_WhenValidDataProvided()`
- `shouldThrowException_WhenEmailAlreadyExists()`
- `shouldReturnEmptyOptional_WhenUserDoesNotExist()`

## 🏗️ Padrão AAA (Arrange-Act-Assert)

Todos os testes seguem o padrão **AAA**:

```java
@Test
@DisplayName("Deve criar usuário com sucesso quando dados válidos são fornecidos")
void shouldCreateUserSuccessfully_WhenValidDataProvided() {
    // Arrange - Preparar dados de teste
    User inputUser = TestDataBuilder.createValidUserDomain();
    when(saveGateway.save(any(User.class))).thenReturn(expectedUser);

    // Act - Executar operação
    User actualUser = userCommandUseCase.createUser(inputUser);

    // Assert - Verificar resultados
    assertThat(actualUser).isNotNull();
    assertThat(actualUser.getNome()).isEqualTo(expectedUser.getNome());
    verify(saveGateway, times(1)).save(inputUser);
}
```

## 🧪 Test Data Builder

### 📦 **Utilitário para Dados de Teste**
A classe `TestDataBuilder` centraliza a criação de objetos de teste, evitando duplicação:

```java
public class TestDataBuilder {
    
    public static User createValidUserDomain() {
        return new User("João Silva", "joao@hospital.com", 
                       "joaosilva", "senha123456", "PACIENTE");
    }
    
    public static UserRequestDto createValidUserRequestDto() {
        return new UserRequestDto("João Silva", "joao@hospital.com",
                                 "joaosilva", "senha123456", TypeUsers.PACIENTE);
    }
}
```

## 🎯 Cenários de Teste Cobertos

### ✅ **Cenários de Sucesso**
- Criação, atualização, exclusão e consulta de usuários
- Autenticação com credenciais válidas
- Mapeamento correto entre diferentes representações de dados
- Integração com serviços externos

### ❌ **Cenários de Falha**
- Tentativa de criar usuário com email duplicado
- Autenticação com credenciais inválidas
- Busca por recursos não existentes
- Falhas de comunicação com serviços externos
- Erros de validação de dados

### 🔄 **Cenários de Borda**
- Dados nulos ou vazios
- Limites de tamanho de campos
- Timeouts de rede
- Graceful degradation

## 🚀 Executando os Testes

### 📋 **Comandos Maven**

```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=UserCommandUseCaseImplTest

# Executar testes com cobertura
mvn test jacoco:report

# Executar testes em modo silencioso
mvn test -q
```

### 📊 **Perfis de Teste**
Os testes utilizam o perfil `test` com configurações específicas:
- Banco H2 em memória
- Logs em nível DEBUG
- URLs de serviços externos mockadas

## 🎯 Métricas de Qualidade

### ✅ **Resultados Atuais**
- **67 testes implementados**
- **100% de sucesso** nos testes unitários  
- **Cobertura das principais regras de negócio**
- **Isolamento completo** com mocks

### 📈 **Benefícios Alcançados**
- **Detecção precoce de bugs**
- **Refatoração segura** com testes de regressão
- **Documentação viva** do comportamento esperado
- **Qualidade de código** melhorada

## 🔍 Principais Funcionalidades Testadas

### 👤 **Gestão de Usuários**
- ✅ Cadastro de usuários (PACIENTE, MEDICO, ENFERMEIRO)
- ✅ Validação de email único
- ✅ Criptografia de senhas
- ✅ Consulta e atualização de dados

### 🔐 **Autenticação**
- ✅ Login com email/senha
- ✅ Geração de tokens JWT
- ✅ Validação de credenciais
- ✅ Atualização de senhas

### 🏥 **Integração de Consultas**
- ✅ Criação e busca de consultas
- ✅ Agregação de dados de múltiplos serviços
- ✅ Tratamento de falhas de serviços externos
- ✅ Graceful degradation

## 📚 Próximos Passos

### 🎯 **Melhorias Futuras**
1. **Testes de Integração**: Implementar testes end-to-end
2. **Cobertura de Código**: Adicionar relatórios de cobertura
3. **Testes de Performance**: Implementar testes de carga
4. **Testes de Contrato**: Implementar contract testing para APIs externas

### 🔧 **Ferramentas Adicionais**
- **TestContainers**: Para testes de integração com banco real
- **WireMock**: Para simulação mais robusta de APIs externas
- **Cucumber**: Para testes de aceitação em BDD

---

## 📞 Suporte

Para dúvidas sobre os testes ou para contribuir:
- 📧 **Email**: contato@girlstechchallenges.com
- 📋 **Documentação**: Verifique os comentários nos testes
- 🐛 **Issues**: Reporte problemas no repositório

---

**✨ Testes bem escritos são a base de um software confiável! ✨**