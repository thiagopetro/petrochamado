# Deploy Local com Acesso via Internet

Este guia explica como fazer o deploy do Chamado Petro direto da sua máquina com acesso via internet usando ngrok.

## Pré-requisitos

- [x] Java 21 instalado
- [x] Node.js 18+ instalado
- [x] PostgreSQL funcionando localmente
- [x] ngrok instalado (já feito via winget)
- [x] Banco `petrochamado` criado

## Passo a Passo

### 1. Preparação

```bash
# 1. Certifique-se que o banco PostgreSQL está rodando
# 2. Verifique se o banco 'petrochamado' existe
# 3. Instale dependências do frontend
cd frontend
npm install
```

### 2. Método Automático (Recomendado)

Execute o script que automatiza todo o processo:

```cmd
# Na raiz do projeto
start-ngrok.bat
```

O script irá:
- Iniciar o backend Spring Boot na porta 8081
- Iniciar o ngrok para o backend
- Iniciar o frontend Next.js na porta 3000
- Iniciar o ngrok para o frontend

### 3. Método Manual

#### 3.1. Iniciar Backend
```bash
cd backend
mvn spring-boot:run
```

#### 3.2. Iniciar ngrok para Backend
```bash
# Em novo terminal
ngrok http 8081
```

#### 3.3. Iniciar Frontend
```bash
cd frontend
npm run dev
```

#### 3.4. Iniciar ngrok para Frontend
```bash
# Em novo terminal
ngrok http 3000
```

### 4. Configuração das URLs

Após os ngrok iniciarem, você verá URLs similares a:
- Backend: `https://abc123.ngrok-free.app`
- Frontend: `https://xyz789.ngrok-free.app`

#### 4.1. Configurar Variáveis de Ambiente

Edite o arquivo `.env.local` com as URLs geradas:

```env
NEXT_PUBLIC_API_URL=https://abc123.ngrok-free.app
FRONTEND_URL=https://xyz789.ngrok-free.app
```

#### 4.2. Reiniciar Frontend

Para aplicar as mudanças nas variáveis de ambiente:

```bash
cd frontend
# Parar o processo (Ctrl+C) e executar novamente
npm run dev
```

### 5. Testar Acesso

1. **Local**: Acesse `http://localhost:3000`
2. **Internet**: Acesse a URL do ngrok do frontend
3. **Compartilhar**: Envie a URL do frontend para outros usuários

## URLs de Acesso

- **Frontend Local**: http://localhost:3000
- **Frontend Público**: https://xyz789.ngrok-free.app
- **Backend Local**: http://localhost:8081
- **Backend Público**: https://abc123.ngrok-free.app

## Importante

### Limitações do ngrok gratuito:
- URLs mudam a cada reinicialização
- Limite de conexões simultâneas
- Página de aviso do ngrok antes do acesso

### Para uso contínuo:
1. **Conta ngrok**: Crie uma conta em https://ngrok.com
2. **Token de autenticação**: Configure seu authtoken
3. **URLs fixas**: Use subdomínios reservados

## Comandos Úteis

```bash
# Ver status dos túneis ngrok
ngrok http --inspect=:4040

# Logs do ngrok
# Acesse: http://localhost:4040

# Parar todos os processos
# Use Ctrl+C em cada terminal ou feche as janelas
```

## Solução de Problemas

### CORS Error
- Verifique se as URLs do ngrok estão corretas no `.env.local`
- Reinicie o frontend após alterar variáveis de ambiente

### Backend não conecta
- Verifique se o PostgreSQL está rodando
- Confirme se o banco `petrochamado` existe
- Verifique logs do Spring Boot

### Frontend não carrega dados
- Confirme se `NEXT_PUBLIC_API_URL` está correto
- Teste a URL do backend diretamente no navegador
- Verifique Network tab nas ferramentas de desenvolvedor

## Segurança

⚠️ **Atenção**: Este setup expõe sua aplicação na internet. Para produção real, considere:

- Configurar autenticação adequada
- Usar HTTPS adequado
- Configurar firewall
- Monitorar acessos
- Considerar VPN para acesso restrito

## Alternativas

Se o ngrok não atender suas necessidades:

1. **Cloudflare Tunnel**: Gratuito, mais estável
2. **localtunnel**: Alternativa simples
3. **Port Forwarding**: Configuração no router
4. **VPS Proxy**: Servidor dedicado como proxy