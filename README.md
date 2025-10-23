# API de Controle Financeiro

API REST simples para gerenciamento de transações financeiras, desenvolvida com Spring Boot.

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.5.6
- Spring Data JPA
- SQL Server
- H2 Database (testes)
- Lombok
- JUnit 5

## 📦 Estrutura do Projeto

```
src/
├── main/
│   ├── java/br/com/fiap/CP6Devops/
│   │   ├── controller/     # Controllers REST
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositórios
│   │   └── service/        # Lógica de negócio
│   └── resources/
│       └── application.properties
└── test/
    └── java/br/com/fiap/CP6Devops/
        └── service/        # Testes unitários
```

## 🗄️ Modelo de Dados

### Categoria
- `id` (Long) - PK
- `nome` (String) - obrigatório
- `descricao` (String)
- `ativa` (Boolean)

### Transacao
- `id` (Long) - PK
- `descricao` (String) - obrigatório
- `valor` (BigDecimal) - obrigatório
- `data` (LocalDate) - obrigatório
- `tipo` (ENUM: RECEITA/DESPESA) - obrigatório
- `categoria_id` (Long) - FK

## ⚙️ Configuração

### 1. SQL Server Local

Edite o arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=financeiro_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 2. Criar o banco de dados

Execute no SQL Server:

```sql
CREATE DATABASE financeiro_db;
```

### 3. Executar a aplicação

```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

## 📚 Endpoints da API

### Categorias

#### Listar todas as categorias
```http
GET /api/categorias
```

#### Listar categorias ativas
```http
GET /api/categorias/ativas
```

#### Buscar categoria por ID
```http
GET /api/categorias/{id}
```

#### Criar nova categoria
```http
POST /api/categorias
Content-Type: application/json

{
  "nome": "Alimentação",
  "descricao": "Despesas com alimentação",
  "ativa": true
}
```

#### Atualizar categoria
```http
PUT /api/categorias/{id}
Content-Type: application/json

{
  "nome": "Alimentação",
  "descricao": "Nova descrição",
  "ativa": true
}
```

#### Deletar categoria
```http
DELETE /api/categorias/{id}
```

### Transações

#### Listar todas as transações
```http
GET /api/transacoes
```

#### Buscar transação por ID
```http
GET /api/transacoes/{id}
```

#### Buscar por tipo
```http
GET /api/transacoes/tipo/RECEITA
GET /api/transacoes/tipo/DESPESA
```

#### Buscar por período
```http
GET /api/transacoes/periodo?inicio=2024-01-01&fim=2024-12-31
```

#### Criar nova transação
```http
POST /api/transacoes
Content-Type: application/json

{
  "descricao": "Salário",
  "valor": 5000.00,
  "data": "2024-10-22",
  "tipo": "RECEITA",
  "categoria": {
    "id": 1
  }
}
```

#### Atualizar transação
```http
PUT /api/transacoes/{id}
Content-Type: application/json

{
  "descricao": "Salário atualizado",
  "valor": 5500.00,
  "data": "2024-10-22",
  "tipo": "RECEITA",
  "categoria": {
    "id": 1
  }
}
```

#### Deletar transação
```http
DELETE /api/transacoes/{id}
```

## 🧪 Executar Testes

```bash
mvn test
```

Os testes utilizam banco H2 em memória automaticamente.

## ☁️ Deploy no Azure

### 1. Criar SQL Database no Azure

```bash
# Criar grupo de recursos
az group create --name rg-financeiro --location brazilsouth

# Criar SQL Server
az sql server create \
  --name srv-financeiro \
  --resource-group rg-financeiro \
  --location brazilsouth \
  --admin-user adminuser \
  --admin-password SuaSenha123!

# Criar banco de dados
az sql db create \
  --name financeiro-db \
  --server srv-financeiro \
  --resource-group rg-financeiro \
  --service-objective S0
```

### 2. Atualizar application.properties para Azure

```properties
spring.datasource.url=jdbc:sqlserver://srv-financeiro.database.windows.net:1433;database=financeiro-db;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
spring.datasource.username=adminuser@srv-financeiro
spring.datasource.password=SuaSenha123!
```

### 3. Deploy no App Service

```bash
# Criar App Service
az webapp create \
  --resource-group rg-financeiro \
  --plan asp-financeiro \
  --name app-financeiro-api \
  --runtime "JAVA:21-java21"

# Deploy do JAR
mvn clean package
az webapp deploy \
  --resource-group rg-financeiro \
  --name app-financeiro-api \
  --src-path target/CP6-Devops-0.0.1-SNAPSHOT.jar
```

## 📝 Exemplos de Uso com cURL

### Criar uma categoria
```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Alimentação",
    "descricao": "Despesas com alimentação",
    "ativa": true
  }'
```

### Criar uma transação
```bash
curl -X POST http://localhost:8080/api/transacoes \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Almoço",
    "valor": 50.00,
    "data": "2024-10-22",
    "tipo": "DESPESA",
    "categoria": {"id": 1}
  }'
```

## 🐛 Troubleshooting

### Erro de conexão com SQL Server

1. Verifique se o SQL Server está rodando
2. Confirme usuário e senha no `application.properties`
3. Verifique se o banco `financeiro_db` foi criado
4. No Azure, adicione o IP do cliente nas regras de firewall

### Tabelas não são criadas

Verifique a propriedade:
```properties
spring.jpa.hibernate.ddl-auto=update
```

## 📄 Licença

Projeto educacional - FIAP