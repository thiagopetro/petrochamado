# Deploy no Vercel com PostgreSQL

Este guia mostra como fazer o deploy da aplicação Chamado Petro no Vercel (frontend) e configurar PostgreSQL gratuito.

## 📋 Pré-requisitos

- Conta no GitHub
- Conta no Vercel
- Conta no Supabase (para PostgreSQL gratuito)

## 🚀 Passo 1: Preparar o Frontend para Vercel

### 1.1 Criar arquivo vercel.json

Crie o arquivo `vercel.json` na raiz do projeto:

```json
{
  "version": 2,
  "builds": [
    {
      "src": "frontend/package.json",
      "use": "@vercel/next"
    }
  ],
  "routes": [
    {
      "src": "/(.*)",
      "dest": "/frontend/$1"
    }
  ],
  "env": {
    "NEXT_PUBLIC_API_URL": "@next_public_api_url"
  }
}
```

### 1.2 Ajustar next.config.js

O arquivo `frontend/next.config.js` já está configurado corretamente.

## 🗄️ Passo 2: Configurar PostgreSQL no Supabase

### 2.1 Criar projeto no Supabase

1. Acesse [supabase.com](https://supabase.com)
2. Clique em "Start your project"
3. Crie uma nova organização (se necessário)
4. Clique em "New Project"
5. Preencha:
   - **Name**: `chamadopetro-db`
   - **Database Password**: Gere uma senha forte
   - **Region**: Escolha a mais próxima (ex: South America)
6. Clique em "Create new project"

### 2.2 Obter credenciais do banco

Após criar o projeto:

1. Vá em **Settings** > **Database**
2. Anote as informações:
   - **Host**: `db.xxx.supabase.co`
   - **Database name**: `postgres`
   - **Port**: `5432`
   - **User**: `postgres`
   - **Password**: A senha que você definiu

3. Vá em **Settings** > **API**
4. Anote a **Database URL** (Connection string)

## 🌐 Passo 3: Deploy do Backend (Render)

### 3.1 Preparar variáveis de ambiente

Crie um arquivo `.env.prod` no backend com:

```env
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=postgresql://postgres:[PASSWORD]@db.[PROJECT-REF].supabase.co:5432/postgres
DB_USERNAME=postgres
DB_PASSWORD=[SUA-SENHA-SUPABASE]
JWT_SECRET=[GERE-UM-SECRET-FORTE]
JWT_EXPIRATION=86400000
FRONTEND_URL=https://[SEU-PROJETO].vercel.app
```

### 3.2 Deploy no Render

1. Acesse [render.com](https://render.com)
2. Conecte sua conta GitHub
3. Clique em "New" > "Web Service"
4. Selecione seu repositório
5. Configure:
   - **Name**: `chamadopetro-backend`
   - **Runtime**: `Docker`
   - **Dockerfile Path**: `./Dockerfile.backend`
   - **Plan**: `Free`

6. Adicione as variáveis de ambiente:
   - `SPRING_PROFILES_ACTIVE`: `prod`
   - `DATABASE_URL`: [URL do Supabase]
   - `DB_USERNAME`: `postgres`
   - `DB_PASSWORD`: [Senha do Supabase]
   - `JWT_SECRET`: [Secret forte]
   - `JWT_EXPIRATION`: `86400000`
   - `FRONTEND_URL`: `https://[SEU-PROJETO].vercel.app`

7. Clique em "Create Web Service"

## 🎨 Passo 4: Deploy do Frontend (Vercel)

### 4.1 Deploy via GitHub

1. Acesse [vercel.com](https://vercel.com)
2. Clique em "New Project"
3. Importe seu repositório do GitHub
4. Configure:
   - **Framework Preset**: `Next.js`
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `.next`

### 4.2 Configurar variáveis de ambiente

Na dashboard do Vercel:

1. Vá em **Settings** > **Environment Variables**
2. Adicione:
   - **Name**: `NEXT_PUBLIC_API_URL`
   - **Value**: `https://[SEU-BACKEND].onrender.com`
   - **Environments**: Production, Preview, Development

### 4.3 Deploy

1. Clique em "Deploy"
2. Aguarde o build completar
3. Sua aplicação estará disponível em `https://[SEU-PROJETO].vercel.app`

## 🔧 Passo 5: Configurações Finais

### 5.1 Atualizar CORS no Backend

No arquivo `application-prod.properties` do backend, adicione:

```properties
# CORS Configuration
cors.allowed-origins=https://[SEU-PROJETO].vercel.app
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.allow-credentials=true
```

### 5.2 Testar a aplicação

1. Acesse `https://[SEU-PROJETO].vercel.app`
2. Teste o login com as credenciais padrão
3. Verifique se todas as funcionalidades estão funcionando

## 💰 Custos

- **Vercel**: Gratuito (100GB bandwidth/mês)
- **Supabase**: Gratuito (500MB database, 2GB bandwidth)
- **Render**: Gratuito (com limitações de cold start)

## 🔍 Troubleshooting

### Erro de CORS
- Verifique se `FRONTEND_URL` no backend está correto
- Confirme se as origens permitidas incluem seu domínio Vercel

### Erro de conexão com banco
- Verifique se a `DATABASE_URL` está correta
- Confirme se o Supabase permite conexões externas

### Build falha no Vercel
- Verifique se todas as dependências estão no `package.json`
- Confirme se o `next.config.js` está correto

### Cold start no Render
- O tier gratuito do Render "dorme" após 15 minutos de inatividade
- Primeira requisição pode demorar 50+ segundos
- Considere upgrade para plano pago se necessário

## 📚 Recursos Úteis

- [Documentação Vercel](https://vercel.com/docs)
- [Documentação Supabase](https://supabase.com/docs)
- [Documentação Render](https://render.com/docs)
- [Next.js Deployment](https://nextjs.org/docs/deployment)

## 🎯 Próximos Passos

1. Configurar domínio customizado (opcional)
2. Configurar monitoramento e logs
3. Implementar CI/CD automático
4. Configurar backup do banco de dados
5. Otimizar performance e SEO