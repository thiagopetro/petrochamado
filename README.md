# 🎫 Chamado Petro - Sistema de Tickets de Suporte

Sistema completo de gerenciamento de tickets de suporte técnico desenvolvido com Spring Boot (backend) e Next.js (frontend).

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [API Endpoints](#-api-endpoints)
- [Autenticação](#-autenticação)
- [Banco de Dados](#-banco-de-dados)
- [Screenshots](#-screenshots)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

## 🎯 Visão Geral

O **Chamado Petro** é uma solução completa para gerenciamento de tickets de suporte técnico, oferecendo uma interface moderna e intuitiva para criação, acompanhamento e resolução de chamados.

### Principais Características

- ✅ **Interface Moderna**: UI responsiva desenvolvida com Next.js 15 e Tailwind CSS
- 🔐 **Autenticação Segura**: Sistema de login com JWT e Spring Security
- 📊 **Dashboard Analítico**: Métricas em tempo real e visualizações
- 🎫 **Gestão Completa de Tickets**: CRUD completo com filtros e busca
- 👥 **Gerenciamento de Usuários**: Sistema completo de usuários com diferentes perfis
- 🔍 **Busca Avançada**: Filtros por status, prioridade, data e responsável
- 📱 **Design Responsivo**: Funciona perfeitamente em desktop e mobile

## 🚀 Funcionalidades

### 🎫 Gestão de Tickets

- **Criação de Tickets**
  - Formulário completo com validações
  - Campos: título, descrição, prioridade, categoria
  - Upload de anexos (planejado)
  - Atribuição automática ou manual

- **Acompanhamento**
  - Status em tempo real (Aberto, Em Atendimento, Aguardando Usuário, Resolvido, Fechado)
  - Histórico de alterações
  - Comentários e atualizações
  - Notificações por email (planejado)

- **Filtros e Busca**
  - Busca por texto livre
  - Filtros por status, prioridade, responsável
  - Ordenação por data, prioridade, status
  - Paginação otimizada

### 👥 Gerenciamento de Usuários

- **CRUD Completo**
  - Criação, edição, visualização e exclusão
  - Campos: nome, login, senha, status (ativo/inativo)
  - Validações de entrada e segurança

- **Perfis de Acesso**
  - **ADMIN**: Acesso total ao sistema
  - **USER**: Criação e acompanhamento de tickets próprios
  - **TECHNICIAN**: Atendimento e resolução de tickets

- **Funcionalidades Avançadas**
  - Busca por nome ou login
  - Filtro por status (ativo/inativo)
  - Ativação/desativação de usuários
  - Estatísticas de usuários

### 📊 Dashboard e Relatórios

- **Métricas em Tempo Real**
  - Total de tickets por status
  - Tickets por prioridade
  - Performance da equipe
  - Tempo médio de resolução

- **Visualizações**
  - Gráficos de barras e pizza
  - Tabelas interativas
  - Cards informativos
  - Indicadores de tendência

### 🔐 Segurança e Autenticação

- **JWT Authentication**
  - Tokens seguros com expiração
  - Refresh tokens automáticos
  - Logout seguro

- **Controle de Acesso**
  - Rotas protegidas
  - Permissões baseadas em roles
  - Validação de sessão

## 🛠 Tecnologias

### Backend

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.4** - Framework principal
- **Spring Security 6** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados relacional
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **Maven** - Gerenciamento de dependências
- **Hibernate** - ORM

### Frontend

- **Next.js 15.2.4** - Framework React
- **React 18** - Biblioteca de interface
- **TypeScript** - Tipagem estática
- **Tailwind CSS** - Framework de estilos
- **Shadcn/ui** - Componentes de interface
- **Lucide React** - Ícones
- **React Hook Form** - Gerenciamento de formulários
- **Zod** - Validação de schemas

### Ferramentas de Desenvolvimento

- **ESLint** - Linting de código
- **Prettier** - Formatação de código
- **PostCSS** - Processamento de CSS
- **Maven** - Build e dependências (backend)
- **npm/pnpm** - Gerenciamento de pacotes (frontend)

## 📋 Pré-requisitos

### Sistema

- **Java 21** ou superior
- **Node.js 18** ou superior
- **PostgreSQL 12** ou superior
- **Maven 3.6** ou superior
- **Git** para controle de versão

### Banco de Dados

```sql
-- Criar banco de dados
CREATE DATABASE petrochamado;

-- Criar usuário (opcional)
CREATE USER chamado_user WITH PASSWORD 'sua_senha';
GRANT ALL PRIVILEGES ON DATABASE petrochamado TO chamado_user;
```

## 🚀 Instalação e Configuração

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/chamadopetro.git
cd chamadopetro
```

### 2. Configuração do Backend

```bash
cd backend

# Configurar banco de dados em src/main/resources/application.properties
# Ajustar as configurações conforme seu ambiente:

spring.datasource.url=jdbc:postgresql://localhost:5432/petrochamado
spring.datasource.username=postgres
spring.datasource.password=sua_senha

# Executar a aplicação
mvn spring-boot:run
```

O backend estará disponível em: `http://localhost:8081`

### 3. Configuração do Frontend

```bash
cd frontend

# Instalar dependências
npm install
# ou
pnpm install

# Executar em modo de desenvolvimento
npm run dev
# ou
pnpm dev
```

O frontend estará disponível em: `http://localhost:3000`

### 4. Dados Iniciais

O sistema criará automaticamente:
- Usuário administrador padrão
- Dados de exemplo para demonstração
- Estrutura completa do banco de dados

**Credenciais padrão:**
- **Login:** admin
- **Senha:** admin123

## 📁 Estrutura do Projeto

```
chamadopetro/
├── backend/                    # API Spring Boot
│   ├── src/main/java/
│   │   └── com/lovablepetro/chamadopetro/
│   │       ├── config/         # Configurações (Security, CORS, etc.)
│   │       ├── controller/     # Controllers REST
│   │       ├── dto/           # Data Transfer Objects
│   │       ├── entity/        # Entidades JPA
│   │       ├── repository/    # Repositórios
│   │       └── service/       # Lógica de negócio
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/                   # Interface Next.js
│   ├── app/                   # App Router (Next.js 13+)
│   │   ├── tickets/           # Páginas de tickets
│   │   ├── users/             # Páginas de usuários
│   │   ├── layout.tsx         # Layout principal
│   │   └── page.tsx           # Dashboard
│   ├── components/            # Componentes reutilizáveis
│   │   ├── ui/               # Componentes base (Shadcn)
│   │   └── app-sidebar.tsx   # Menu lateral
│   ├── lib/                  # Utilitários
│   └── styles/               # Estilos globais
└── README.md
```

## 🔌 API Endpoints

### Autenticação

```http
POST /api/auth/login          # Login de usuário
POST /api/auth/register       # Registro de usuário
POST /api/auth/refresh        # Renovar token
POST /api/auth/logout         # Logout
```

### Tickets

```http
GET    /api/tickets           # Listar tickets (com paginação)
GET    /api/tickets/{id}      # Buscar ticket por ID
POST   /api/tickets           # Criar novo ticket
PUT    /api/tickets/{id}      # Atualizar ticket
DELETE /api/tickets/{id}      # Excluir ticket
GET    /api/tickets/stats     # Estatísticas de tickets
```

### Usuários

```http
GET    /api/users             # Listar usuários (com paginação)
GET    /api/users/{id}        # Buscar usuário por ID
POST   /api/users             # Criar novo usuário
PUT    /api/users/{id}        # Atualizar usuário
DELETE /api/users/{id}        # Excluir usuário
PATCH  /api/users/{id}/toggle # Ativar/desativar usuário
GET    /api/users/stats       # Estatísticas de usuários
```

### Dashboard

```http
GET /api/dashboard/metrics    # Métricas do dashboard
GET /api/dashboard/recent     # Tickets recentes
```

## 🔐 Autenticação

### Fluxo de Autenticação

1. **Login**: Usuário envia credenciais
2. **Validação**: Sistema valida no banco de dados
3. **Token JWT**: Geração de token com expiração
4. **Autorização**: Token enviado em todas as requisições
5. **Refresh**: Renovação automática antes da expiração

### Headers de Requisição

```http
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

### Estrutura do Token JWT

```json
{
  "sub": "username",
  "role": "ADMIN",
  "iat": 1640995200,
  "exp": 1641081600
}
```

## 🗄 Banco de Dados

### Modelo de Dados

#### Tabela: users
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);
```

#### Tabela: tickets
```sql
CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    ticket_id VARCHAR(20) UNIQUE NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT NOT NULL,
    prioridade VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    aberto_por VARCHAR(100) NOT NULL,
    email_aberto_por VARCHAR(100) NOT NULL,
    atribuido_a VARCHAR(100),
    aberto_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolvido_em TIMESTAMP
);
```

### Relacionamentos

- **Users ↔ Tickets**: Um usuário pode ter múltiplos tickets
- **Tickets ↔ Comments**: Um ticket pode ter múltiplos comentários (futuro)
- **Users ↔ Roles**: Relacionamento de roles para controle de acesso

## 🎨 Screenshots

### Dashboard Principal
![Dashboard](docs/screenshots/dashboard.png)

### Lista de Tickets
![Tickets](docs/screenshots/tickets.png)

### Gerenciamento de Usuários
![Users](docs/screenshots/users.png)

### Formulário de Ticket
![New Ticket](docs/screenshots/new-ticket.png)

## 🧪 Testes

### Backend
```bash
cd backend
mvn test
```

### Frontend
```bash
cd frontend
npm test
# ou
pnpm test
```

## 🚀 Deploy

### Produção

#### Backend
```bash
cd backend
mvn clean package
java -jar target/chamadopetro-backend-0.0.1-SNAPSHOT.jar
```

#### Frontend
```bash
cd frontend
npm run build
npm start
```

### Docker (Futuro)
```bash
docker-compose up -d
```

## 🤝 Contribuição

1. **Fork** o projeto
2. **Crie** uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. **Push** para a branch (`git push origin feature/AmazingFeature`)
5. **Abra** um Pull Request

### Padrões de Código

- **Backend**: Seguir convenções Java e Spring Boot
- **Frontend**: Usar ESLint e Prettier
- **Commits**: Conventional Commits
- **Testes**: Cobertura mínima de 80%

## 📝 Roadmap

### Versão 2.0
- [ ] Sistema de comentários em tickets
- [ ] Upload de anexos
- [ ] Notificações por email
- [ ] Relatórios avançados
- [ ] API de integração
- [ ] App mobile

### Versão 2.1
- [ ] Chat em tempo real
- [ ] Automação de workflows
- [ ] Integração com Slack/Teams
- [ ] Dashboard customizável
- [ ] Backup automático

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Thiago de Amorim Moraes**
- GitHub: [@thiagoamorim](https://github.com/thiagoamorim)

## 🙏 Agradecimentos

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Next.js](https://nextjs.org/)
- [Tailwind CSS](https://tailwindcss.com/)
- [Shadcn/ui](https://ui.shadcn.com/)
- [PostgreSQL](https://www.postgresql.org/)

---

⭐ **Se este projeto foi útil para você, considere dar uma estrela!**
