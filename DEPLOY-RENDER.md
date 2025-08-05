# 🚀 Deploy no Render.com - Chamado Petro

Este guia explica como fazer o deploy completo da aplicação Chamado Petro no Render.com.

## 📋 Pré-requisitos

- Conta no [Render.com](https://render.com)
- Repositório Git com o código (GitHub/GitLab)
- Código já configurado para produção

## 🏗️ Arquitetura do Deploy

A aplicação será deployada com 3 serviços no Render:

1. **PostgreSQL Database** - Banco de dados gerenciado
2. **Backend API** - Spring Boot (Java 21)  
3. **Frontend Web** - Next.js (Node.js 18)

## 📦 Opção 1: Deploy Automático com render.yaml

### 1. Usar o arquivo render.yaml

O arquivo `render.yaml` na raiz do projeto configura automaticamente todos os serviços.

### 2. Conectar repositório no Render

1. Acesse [Render Dashboard](https://dashboard.render.com)
2. Clique em **"New +"** → **"Blueprint"**
3. Conecte seu repositório Git
4. O Render detectará automaticamente o `render.yaml`
5. Clique em **"Apply"**

### 3. Aguardar o deploy

O Render criará automaticamente:
- Database PostgreSQL
- Backend Web Service
- Frontend Web Service

## 📦 Opção 2: Deploy Manual (Recomendado para controle total)

### Passo 1: Criar PostgreSQL Database

1. No Render Dashboard → **"New +"** → **"PostgreSQL"**
2. Configurações:
   - **Name**: `chamadopetro-db`
   - **Database**: `petrochamado` 
   - **User**: `chamadopetro_user`
   - **Region**: Oregon (Free tier)
   - **Plan**: Free
3. Clique em **"Create Database"**
4. **Anote as credenciais** que serão exibidas

### Passo 2: Deploy do Backend (Spring Boot API)

1. No Render Dashboard → **"New +"** → **"Web Service"**
2. Conecte seu repositório Git
3. Configurações:
   - **Name**: `chamadopetro-backend`
   - **Environment**: `Java`
   - **Region**: Oregon
   - **Branch**: `main`
   - **Root Directory**: `backend`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -Dspring.profiles.active=prod -jar target/chamadopetro-backend-0.0.1-SNAPSHOT.jar`

4. **Variáveis de Ambiente** (Environment Variables):
   ```
   DATABASE_URL=postgresql://user:password@host:port/database
   DB_USERNAME=chamadopetro_user
   DB_PASSWORD=[senha do banco]
   JWT_SECRET=[gerar uma chave segura de 64+ caracteres]
   JWT_EXPIRATION=86400000
   FRONTEND_URL=https://[nome-do-frontend].onrender.com
   ```

5. Clique em **"Create Web Service"**

### Passo 3: Deploy do Frontend (Next.js)

1. No Render Dashboard → **"New +"** → **"Web Service"**
2. Conecte o mesmo repositório
3. Configurações:
   - **Name**: `chamadopetro-frontend`
   - **Environment**: `Node`
   - **Region**: Oregon  
   - **Branch**: `main`
   - **Root Directory**: `frontend`
   - **Build Command**: `npm ci && npm run build`
   - **Start Command**: `npm start`

4. **Variáveis de Ambiente**:
   ```
   NEXT_PUBLIC_API_URL=https://[nome-do-backend].onrender.com
   ```

5. Clique em **"Create Web Service"**

### Passo 4: Atualizar CORS no Backend

Após o frontend estar no ar, atualize a variável `FRONTEND_URL` no backend com a URL real do frontend.

## 🔧 Configurações Importantes

### URLs dos Serviços

Após o deploy, você terá URLs como:
- **Backend**: `https://chamadopetro-backend.onrender.com`
- **Frontend**: `https://chamadopetro-frontend.onrender.com`
- **Database**: Interno (conexão via DATABASE_URL)

### Credenciais Padrão

O sistema criará automaticamente o usuário admin:
- **Username**: `admin`
- **Password**: `123456`

⚠️ **IMPORTANTE**: Altere a senha padrão após o primeiro login em produção!

## 🔍 Verificação do Deploy

### 1. Teste do Backend
```bash
curl https://sua-backend-url.onrender.com/api/auth/users
```

### 2. Teste do Frontend
Acesse a URL do frontend e tente fazer login.

### 3. Teste da Integração
1. Acesse o frontend
2. Faça login com admin/123456
3. Crie um ticket de teste
4. Verifique se os dados são salvos

## 🛠️ Troubleshooting

### Backend não inicia
- Verifique as variáveis de ambiente do banco
- Confira os logs no Render Dashboard
- Teste a conexão com o banco

### Frontend não conecta com Backend
- Verifique a variável `NEXT_PUBLIC_API_URL`
- Confira as configurações de CORS no backend
- Teste os endpoints da API diretamente

### Erro de CORS
- Verifique se `FRONTEND_URL` no backend está correto
- Confirme que o protocolo (https://) está correto

### Build falha
- Verifique se Java 21 está sendo usado
- Confirme se as dependências estão corretas
- Verifique se o Maven está funcionando

## 📊 Monitoramento

### Logs
- Acesse o Render Dashboard
- Clique no serviço
- Vá em **"Logs"** para ver logs em tempo real

### Métricas  
- CPU e memória disponíveis no dashboard
- Tempo de resposta das requisições
- Status de saúde dos serviços

## 💰 Custos

### Free Tier (Gratuito)
- PostgreSQL: 1GB storage, 100 conexões
- Web Services: 512MB RAM, dormem após inatividade
- Bandwidth: 100GB/mês

### Limitações do Free Tier
- Serviços dormem após 15min de inatividade
- Tempo de cold start pode ser maior
- SSL/TLS incluído

## 🔄 Atualizações

### Deploy Automático
O Render faz deploy automático quando há push para a branch `main`.

### Deploy Manual
No dashboard do serviço → **"Manual Deploy"** → **"Deploy Latest Commit"**

## 🔐 Segurança em Produção

1. **Alterar credenciais padrão** do usuário admin
2. **Usar JWT secret seguro** (64+ caracteres aleatórios)
3. **Habilitar HTTPS** (automático no Render)
4. **Monitorar logs** para tentativas de acesso suspeitas
5. **Fazer backup regular** do banco de dados

## 📞 Suporte

- **Render Docs**: https://render.com/docs
- **Community**: https://community.render.com
- **Status**: https://status.render.com

---

✅ **Deploy concluído!** Sua aplicação Chamado Petro estará rodando em produção no Render.com.