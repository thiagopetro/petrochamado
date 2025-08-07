-- Script para corrigir problemas de UTF-8 no banco PostgreSQL
-- Execute este script para recriar o banco com configuração correta de UTF-8

-- ATENÇÃO: Este script irá APAGAR todos os dados existentes!
-- Faça backup se necessário antes de executar

-- Desconectar todas as conexões ativas do banco petrochamado
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'petrochamado' AND pid <> pg_backend_pid();

-- Aguardar um momento para garantir que as conexões foram fechadas
\! timeout /t 2 /nobreak > nul

-- Remover o banco existente
DROP DATABASE IF EXISTS petrochamado;

-- Criar o banco com configuração correta de UTF-8 usando template0
CREATE DATABASE petrochamado
    WITH 
    TEMPLATE = template0
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Comentário sobre o banco
COMMENT ON DATABASE petrochamado
    IS 'Banco de dados do sistema de chamados da Petro - Configurado para UTF-8';

-- Conectar ao banco petrochamado
\c petrochamado;

-- Criar extensões úteis
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Verificar se o banco foi criado corretamente
SELECT current_database(), current_user, version();

-- Verificar configuração de encoding
SELECT datname, datcollate, datctype, encoding 
FROM pg_database 
WHERE datname = 'petrochamado';

SELECT 'Banco recriado com sucesso! Configuração UTF-8 aplicada.' as status;