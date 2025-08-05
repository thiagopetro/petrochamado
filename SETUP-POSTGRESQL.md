# Configuração do PostgreSQL para Chamado Petro

## Pré-requisitos

1. **PostgreSQL instalado** (versão 12 ou superior)
2. **pgAdmin** (opcional, para interface gráfica)

## Configuração do Banco de Dados

### 1. Instalação do PostgreSQL

Se ainda não tiver o PostgreSQL instalado:

- **Windows**: Baixe do site oficial https://www.postgresql.org/download/windows/
- **Durante a instalação**:
  - Defina a senha do usuário `postgres` como: `root`
  - Mantenha a porta padrão: `5432`

### 2. Criação do Banco de Dados

#### Opção A: Via linha de comando (psql)

```bash
# Conectar ao PostgreSQL como superusuário
psql -U postgres -h localhost

# Executar o script de configuração
\i backend/src/main/resources/database-setup.sql
```

#### Opção B: Via pgAdmin

1. Abra o pgAdmin
2. Conecte ao servidor PostgreSQL local
3. Clique com botão direito em "Databases" → "Create" → "Database"
4. Nome: `petrochamado`
5. Owner: `postgres`
6. Clique em "Save"

#### Opção C: Via comando direto

```bash
# Criar o banco diretamente
createdb -U postgres -h localhost petrochamado
```

### 3. Verificação da Configuração

As configurações da aplicação estão em:
```
backend/src/main/resources/application.properties
```

**Configurações atuais:**
- **Host**: localhost:5432
- **Database**: petrochamado
- **Username**: postgres
- **Password**: root

### 4. Inicialização da Aplicação

Quando você executar a aplicação Spring Boot pela primeira vez:

1. **As tabelas serão criadas automaticamente** pelo Hibernate (DDL auto)
2. **Dados de exemplo serão inseridos** automaticamente:
   - 1 usuário admin padrão
   - 10 tickets de exemplo

### 5. Executando o Backend

```bash
cd backend
mvn spring-boot:run
```

### 6. Verificação

Após iniciar a aplicação, você pode verificar:

- **API funcionando**: http://localhost:8081/api/tickets
- **Logs no console** mostrando as queries SQL
- **Banco no pgAdmin** para ver as tabelas criadas

## Estrutura das Tabelas

O Hibernate criará automaticamente as seguintes tabelas:

- `tickets` - Armazena os chamados
- `users` - Usuários do sistema
- Outras tabelas de apoio conforme necessário

## Troubleshooting

### Erro de Conexão

Se houver erro de conexão, verifique:

1. **PostgreSQL está rodando**:
   ```bash
   # Windows (Services)
   services.msc → PostgreSQL
   
   # Ou via comando
   pg_ctl status
   ```

2. **Credenciais corretas**:
   - Usuário: `postgres`
   - Senha: `root`
   - Porta: `5432`

3. **Banco existe**:
   ```sql
   \l  -- Lista todos os bancos
   ```

### Resetar Dados

Para resetar os dados de exemplo:

```sql
-- Conectar ao banco petrochamado
\c petrochamado;

-- Limpar tabelas (cuidado!)
TRUNCATE TABLE tickets RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Reiniciar a aplicação para recriar os dados
```

## Próximos Passos

Após configurar o PostgreSQL:

1. Execute o backend: `cd backend && mvn spring-boot:run`
2. Execute o frontend: `cd frontend && npm run dev`
3. Acesse: http://localhost:3000