# ✅ Checklist de Deploy - Vercel + Supabase

## 📋 Pré-Deploy

### ✅ Preparação do Código
- [ ] Código commitado e pushed para GitHub
- [ ] Arquivo `vercel.json` criado na raiz
- [ ] Configurações de produção atualizadas
- [ ] Build local testado e funcionando

### ✅ Contas Necessárias
- [ ] Conta GitHub ativa
- [ ] Conta Vercel criada e conectada ao GitHub
- [ ] Conta Supabase criada
- [ ] Conta Render criada (para backend)

## 🗄️ Configuração do Banco (Supabase)

### ✅ Criar Projeto
- [ ] Acessar [supabase.com](https://supabase.com)
- [ ] Criar novo projeto: `chamadopetro-db`
- [ ] Escolher região: South America (ou mais próxima)
- [ ] Definir senha forte para o banco
- [ ] Aguardar criação do projeto (2-3 minutos)

### ✅ Obter Credenciais
- [ ] Ir em Settings > Database
- [ ] Anotar informações:
  - [ ] Host: `db.xxx.supabase.co`
  - [ ] Database: `postgres`
  - [ ] Port: `5432`
  - [ ] User: `postgres`
  - [ ] Password: [sua senha]
- [ ] Ir em Settings > API
- [ ] Copiar Connection String (Database URL)

### ✅ Testar Conexão (Opcional)
```bash
psql "postgresql://postgres:[PASSWORD]@db.[PROJECT-REF].supabase.co:5432/postgres"
```

## 🚀 Deploy do Backend (Render)

### ✅ Preparar Variáveis
- [ ] Gerar JWT Secret forte:
```bash
openssl rand -base64 32
```
- [ ] Preparar lista de variáveis:
  - [ ] `SPRING_PROFILES_ACTIVE`: `prod`
  - [ ] `DATABASE_URL`: [URL do Supabase]
  - [ ] `DB_USERNAME`: `postgres`
  - [ ] `DB_PASSWORD`: [senha do Supabase]
  - [ ] `JWT_SECRET`: [secret gerado]
  - [ ] `JWT_EXPIRATION`: `86400000`
  - [ ] `FRONTEND_URL`: `https://[SEU-PROJETO].vercel.app`

### ✅ Deploy no Render
- [ ] Acessar [render.com](https://render.com)
- [ ] Conectar conta GitHub
- [ ] Criar "New Web Service"
- [ ] Selecionar repositório
- [ ] Configurar:
  - [ ] Name: `chamadopetro-backend`
  - [ ] Runtime: `Docker`
  - [ ] Dockerfile Path: `./Dockerfile.backend`
  - [ ] Plan: `Free`
- [ ] Adicionar todas as variáveis de ambiente
- [ ] Iniciar deploy
- [ ] Aguardar build completar (5-10 minutos)
- [ ] Anotar URL do backend: `https://[BACKEND].onrender.com`

### ✅ Testar Backend
- [ ] Acessar `https://[BACKEND].onrender.com/api/health`
- [ ] Verificar se retorna status OK
- [ ] Testar endpoint de login

## 🎨 Deploy do Frontend (Vercel)

### ✅ Deploy via Vercel
- [ ] Acessar [vercel.com](https://vercel.com)
- [ ] Clicar "New Project"
- [ ] Importar repositório GitHub
- [ ] Configurar:
  - [ ] Framework Preset: `Next.js`
  - [ ] Root Directory: `frontend`
  - [ ] Build Command: `npm run build`
  - [ ] Output Directory: `.next`

### ✅ Configurar Variáveis
- [ ] Ir em Settings > Environment Variables
- [ ] Adicionar:
  - [ ] Name: `NEXT_PUBLIC_API_URL`
  - [ ] Value: `https://[SEU-BACKEND].onrender.com`
  - [ ] Environments: Production, Preview, Development

### ✅ Deploy
- [ ] Clicar "Deploy"
- [ ] Aguardar build (3-5 minutos)
- [ ] Anotar URL: `https://[SEU-PROJETO].vercel.app`

## 🔧 Configurações Finais

### ✅ Atualizar CORS
- [ ] Voltar ao Render (backend)
- [ ] Ir em Environment
- [ ] Atualizar `FRONTEND_URL` com URL real do Vercel
- [ ] Fazer redeploy do backend

### ✅ Teste Completo
- [ ] Acessar aplicação no Vercel
- [ ] Testar login com credenciais padrão:
  - [ ] Usuário: `admin`
  - [ ] Senha: `admin123`
- [ ] Verificar se todas as páginas carregam
- [ ] Testar criação de chamado
- [ ] Verificar se dados são salvos no banco

## 🔍 Troubleshooting

### ❌ Erro de CORS
- [ ] Verificar se `FRONTEND_URL` no backend está correto
- [ ] Confirmar se não há `/` no final da URL
- [ ] Redeploy do backend após mudança

### ❌ Erro de Conexão com Banco
- [ ] Verificar se `DATABASE_URL` está correta
- [ ] Confirmar se Supabase permite conexões externas
- [ ] Testar conexão manual com psql

### ❌ Build Falha no Vercel
- [ ] Verificar logs de build no Vercel
- [ ] Confirmar se todas as dependências estão no package.json
- [ ] Testar build local: `npm run build`

### ❌ Cold Start no Render
- [ ] Primeira requisição pode demorar 50+ segundos
- [ ] Isso é normal no tier gratuito
- [ ] Considerar upgrade se necessário

## 📊 Monitoramento

### ✅ URLs Importantes
- [ ] Frontend: `https://[SEU-PROJETO].vercel.app`
- [ ] Backend: `https://[SEU-BACKEND].onrender.com`
- [ ] API Health: `https://[SEU-BACKEND].onrender.com/api/health`
- [ ] Supabase Dashboard: `https://app.supabase.com/project/[PROJECT-ID]`

### ✅ Dashboards
- [ ] Vercel: Monitorar builds e performance
- [ ] Render: Monitorar logs e uptime
- [ ] Supabase: Monitorar uso do banco

## 💰 Custos (Tier Gratuito)

- **Vercel**: Gratuito (100GB bandwidth/mês)
- **Supabase**: Gratuito (500MB database, 2GB bandwidth)
- **Render**: Gratuito (com cold start)

**Total: R$ 0,00/mês** 🎉

## 🎯 Próximos Passos

- [ ] Configurar domínio customizado (opcional)
- [ ] Implementar monitoramento de erros
- [ ] Configurar backup automático do banco
- [ ] Otimizar performance
- [ ] Implementar CI/CD automático

---

**✅ Deploy Concluído com Sucesso!**

Sua aplicação está agora disponível publicamente e pode ser acessada por qualquer pessoa com o link do Vercel.