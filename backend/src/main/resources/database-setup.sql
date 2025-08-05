-- Script para configuração inicial do banco PostgreSQL
-- Execute este script como superusuário (postgres) antes de iniciar a aplicação

-- Criar o banco de dados se não existir
CREATE DATABASE petrochamado
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Comentário sobre o banco
COMMENT ON DATABASE petrochamado
    IS 'Banco de dados do sistema de chamados da Petro';

-- Conectar ao banco petrochamado
\c petrochamado;

-- Criar extensões úteis (opcional)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- As tabelas serão criadas automaticamente pelo Hibernate/JPA
-- com a configuração spring.jpa.hibernate.ddl-auto=update

-- Verificar se o banco foi criado corretamente
SELECT current_database(), current_user, version();