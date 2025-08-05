# 🔐 Credenciais de Acesso - Sistema Chamado Petro

## Usuário Administrador Padrão

O sistema cria automaticamente um usuário administrador na primeira execução:

### 📋 Dados de Acesso

- **Usuário:** `admin`
- **Senha:** `admin123`
- **Email:** `admin@petro.com`
- **Nome Completo:** `Administrador do Sistema`
- **Perfil:** `ADMIN`

### 🌐 URLs de Acesso

- **Frontend (Login):** http://localhost:3000/login
- **Frontend (Dashboard):** http://localhost:3000/
- **Backend API:** http://localhost:8081/api/

### 🔑 Como Fazer Login

1. Acesse: http://localhost:3000/login
2. Digite o usuário: `admin`
3. Digite a senha: `admin123`
4. Clique em "Entrar"
5. Você será redirecionado para o dashboard principal

### 📝 Endpoints da API de Autenticação

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Resposta de Sucesso:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@petro.com",
  "fullName": "Administrador do Sistema",
  "role": "ADMIN"
}
```

### 🛡️ Segurança

- **IMPORTANTE:** Altere a senha padrão após o primeiro acesso
- O token JWT tem validade de 24 horas
- Todas as rotas (exceto login) requerem autenticação
- O sistema faz logout automático em caso de token expirado

### 👥 Criando Novos Usuários

Após fazer login como administrador, você pode:

1. Acessar a seção "Usuários" no menu lateral
2. Clicar em "Novo Usuário"
3. Preencher os dados do novo usuário
4. Definir o perfil (ADMIN, USER, TECHNICIAN)
5. Salvar o usuário

### 🔧 Troubleshooting

**Erro de Login:**
- Verifique se o backend está rodando na porta 8081
- Confirme se o banco PostgreSQL está ativo
- Verifique se as credenciais estão corretas

**Token Expirado:**
- Faça logout e login novamente
- O sistema redirecionará automaticamente para a tela de login

**Problemas de Conexão:**
- Verifique se o frontend está rodando na porta 3000
- Confirme se não há firewall bloqueando as portas
- Verifique os logs do backend para erros de conexão com o banco

---

**📞 Suporte:** Para dúvidas ou problemas, verifique os logs do sistema ou consulte a documentação completa no README.md