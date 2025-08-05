# Chamado Petro - Backend

Backend da aplicação de gerenciamento de tickets de suporte técnico desenvolvido em Spring Boot.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA**
- **Spring Web**
- **PostgreSQL** (banco de dados relacional)
- **Maven** (gerenciamento de dependências)

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/lovablepetro/chamadopetro/
│   │   ├── config/          # Configurações (CORS, inicialização de dados)
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── repository/     # Repositórios JPA
│   │   ├── service/        # Lógica de negócio
│   │   └── ChamadoPetroApplication.java
│   └── resources/
│       └── application.properties
└── test/
```

## Configuração e Execução

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6 ou superior

### Executando a aplicação

1. **Clone o repositório e navegue até a pasta backend:**
   ```bash
   cd backend
   ```

2. **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

3. **A aplicação estará disponível em:**
   - API: http://localhost:8081

### Configuração do Banco PostgreSQL
- **Host:** `localhost:5432`
- **Database:** `petrochamado`
- **Username:** `postgres`
- **Password:** `root`

### Pré-requisitos do Banco
Certifique-se de que o PostgreSQL esteja instalado e rodando com:
- Database `petrochamado` criada
- Usuário `postgres` com senha `root`
- Servidor rodando na porta padrão 5432

## API Endpoints

### Tickets

| Método | Endpoint | Descrição |
|--------|----------|----------|
| GET | `/api/tickets` | Lista todos os tickets |
| GET | `/api/tickets?search={term}&status={status}&prioridade={prioridade}&atribuidoA={tecnico}` | Lista tickets com filtros |
| GET | `/api/tickets/{id}` | Busca ticket por ID |
| GET | `/api/tickets/ticket/{ticketId}` | Busca ticket por ticket ID |
| POST | `/api/tickets` | Cria novo ticket |
| PUT | `/api/tickets/{id}` | Atualiza ticket |
| DELETE | `/api/tickets/{id}` | Remove ticket |

### Dashboard

| Método | Endpoint | Descrição |
|--------|----------|----------|
| GET | `/api/tickets/dashboard/metrics` | Métricas do dashboard |

### Dados de Referência

| Método | Endpoint | Descrição |
|--------|----------|----------|
| GET | `/api/tickets/technicians` | Lista técnicos |
| GET | `/api/tickets/priorities` | Lista prioridades |
| GET | `/api/tickets/statuses` | Lista status |

## Modelo de Dados

### Ticket
```json
{
  "id": 1,
  "ticketId": "INC3221310",
  "titulo": "Erro ao emitir PT na aplicação",
  "descricao": "Descrição detalhada do problema",
  "prioridade": "3 - Moderada",
  "abertoEm": "2025-01-29T14:42:33",
  "abertoPor": "Enoc Tobias Onofre",
  "atribuidoA": "Thiago de Amorim Moraes",
  "atualizado": "2025-01-31T13:16:06",
  "status": "Em atendimento"
}
```

### Prioridades
- `1 - Crítica`
- `2 - Alta`
- `3 - Moderada`
- `4 - Baixa`

### Status
- `Aguardando usuário`
- `Em atendimento`
- `Problema confirmado`
- `Resolvido`

### Técnicos
- `Thiago de Amorim Moraes`
- `Carlos Eduardo Lima`
- `Ana Carolina Ferreira`

## Exemplos de Uso

### Criar um novo ticket
```bash
curl -X POST http://localhost:8081/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Novo problema",
    "descricao": "Descrição do problema",
    "prioridade": "2 - Alta",
    "abertoPor": "João Silva",
    "atribuidoA": "Thiago de Amorim Moraes",
    "status": "Aguardando usuário"
  }'
```

### Buscar tickets com filtros
```bash
curl "http://localhost:8081/api/tickets?status=Em%20atendimento&prioridade=1%20-%20Crítica"
```

### Obter métricas do dashboard
```bash
curl http://localhost:8081/api/tickets/dashboard/metrics
```

## Configuração CORS

O backend está configurado para aceitar requisições do frontend em `http://localhost:3000`. Para alterar, modifique a classe `CorsConfig.java`.

## Dados de Exemplo

A aplicação inicializa automaticamente com 10 tickets de exemplo para facilitar o desenvolvimento e testes. Os dados são persistidos no PostgreSQL.

## Logs

Os logs estão configurados para mostrar:
- Queries SQL executadas
- Parâmetros das queries
- Logs de debug da aplicação

## Desenvolvimento

Para desenvolvimento, a aplicação utiliza:
- **Hot reload** com Spring Boot DevTools
- **PostgreSQL** para persistência de dados
- **Logs detalhados** para debugging
- **pgAdmin** ou cliente PostgreSQL para inspeção do banco

## Próximos Passos

Para produção, considere:
- Migrar para banco de dados persistente (PostgreSQL, MySQL)
- Implementar autenticação e autorização
- Adicionar testes unitários e de integração
- Configurar profiles para diferentes ambientes
- Implementar cache para melhor performance