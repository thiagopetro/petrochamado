# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Chamado Petro é um sistema completo de gerenciamento de tickets de suporte técnico desenvolvido com Spring Boot (backend) e Next.js (frontend). O sistema oferece autenticação JWT, CRUD completo de tickets e usuários, dashboard com métricas e interface responsiva.

## Development Commands

### Backend (Spring Boot)
- **Start development server**: `cd backend && mvn spring-boot:run`
- **Build project**: `cd backend && mvn clean package`
- **Run tests**: `cd backend && mvn test`
- **Clean build**: `cd backend && mvn clean`

### Frontend (Next.js)
- **Start development server**: `cd frontend && npm run dev` (ou `pnpm dev`)
- **Build for production**: `cd frontend && npm run build`
- **Start production server**: `cd frontend && npm start`
- **Lint code**: `cd frontend && npm run lint`
- **Install dependencies**: `cd frontend && npm install` (ou `pnpm install`)

### Database Setup
- PostgreSQL database: `petrochamado`
- Default credentials: `postgres/root` on `localhost:5432`
- Tables are auto-created via JPA/Hibernate DDL
- See `SETUP-POSTGRESQL.md` for detailed setup instructions

## Architecture Overview

### Backend Architecture (Spring Boot 3.5.4 + Java 21)

**Core Technologies:**
- Spring Security 6 with JWT authentication
- Spring Data JPA with PostgreSQL
- RESTful API design following `/api/*` pattern

**Package Structure:**
- `config/`: Security, CORS, data initialization, JWT configuration
- `controller/`: REST controllers (Auth, Ticket, User, Test)
- `dto/`: Data Transfer Objects for API requests/responses
- `entity/`: JPA entities (User, Ticket, enums for Status/Priority/Role)
- `repository/`: Spring Data JPA repositories
- `service/`: Business logic layer (Auth, JWT, Ticket, User services)

**Key Configuration:**
- Server runs on port 8081
- JWT tokens with configurable expiration (default 24h)
- CORS configured for frontend at `localhost:3000`
- Currently configured with `permitAll()` for development (no authentication required)
- Database auto-initialization with sample data via `DataInitializer`

### Frontend Architecture (Next.js 15.2.4 + TypeScript)

**Core Technologies:**
- App Router (Next.js 13+) with TypeScript
- Tailwind CSS + Shadcn/ui components
- React Hook Form + Zod validation
- Context-based authentication with JWT

**Key Structure:**
- `app/`: App Router pages (tickets, users, login, settings, reports)
- `components/`: Reusable UI components including Shadcn/ui components
- `contexts/`: AuthContext for authentication state management
- `hooks/`: Custom hooks including `useApi` for authenticated API calls
- `lib/`: Utilities and configurations

**Authentication Flow:**
- JWT tokens stored in localStorage
- `AuthContext` provides authentication state
- `useApi` hook handles authenticated requests with automatic logout on 401
- `ClientLayout` manages route protection and sidebar rendering
- `ProtectedRoute` component redirects unauthenticated users

**API Integration:**
- Base URL: `http://localhost:8081`
- All API calls through `useApi` hook with automatic token injection
- Error handling with automatic logout on authentication failures

## Key Development Patterns

### Backend Patterns
- Entity-Repository-Service-Controller layered architecture
- DTO pattern for API requests/responses
- Spring Security with JWT for stateless authentication
- JPA with Hibernate for ORM with PostgreSQL
- Centralized CORS configuration for development

### Frontend Patterns
- App Router with TypeScript for type safety
- Context API for global state (authentication)
- Custom hooks for API calls and reusable logic
- Shadcn/ui for consistent component library
- Form handling with React Hook Form + Zod validation

### API Endpoints
- **Auth**: `/api/auth/login`, `/api/auth/register`
- **Tickets**: `/api/tickets` (CRUD + dashboard metrics)
- **Users**: `/api/users` (CRUD + statistics)

## Database Schema

**Main Entities:**
- `users`: id, username, email, password, full_name, role, is_active, timestamps
- `tickets`: id, ticket_id, titulo, descricao, prioridade, status, aberto_por, atribuido_a, timestamps

**Enums:**
- `Role`: ADMIN, USER, TECHNICIAN
- `Status`: ABERTO, EM_ATENDIMENTO, AGUARDANDO_USUARIO, RESOLVIDO, FECHADO
- `Prioridade`: CRITICA, ALTA, MODERADA, BAIXA

## Common Development Tasks

### Adding New Features
1. Backend: Create entity → repository → service → controller → DTO
2. Frontend: Create pages in `app/` → components → API integration via `useApi`
3. Update navigation in `app-sidebar.tsx` if needed

### Authentication & Authorization
- Currently all endpoints are public (`.anyRequest().permitAll()`)
- JWT infrastructure is in place but not enforced
- To enable auth: modify `SecurityConfig.java` and add role-based access control

### Database Changes
- Modify entities in `backend/src/main/java/com/lovablepetro/chamadopetro/entity/`
- Use `spring.jpa.hibernate.ddl-auto=update` for automatic schema updates
- For production, consider migration scripts

## Environment Configuration

### Development Setup
- Backend: Port 8081, PostgreSQL on localhost:5432
- Frontend: Port 3000, proxying API calls to localhost:8081
- Database: Auto-created with sample data via DataInitializer

### Key Configuration Files
- `backend/src/main/resources/application.properties`: Database, JWT, logging
- `frontend/package.json`: Dependencies and scripts
- `backend/pom.xml`: Maven dependencies and build configuration