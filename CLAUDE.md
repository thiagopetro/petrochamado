# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Chamado Petro é um sistema completo de gerenciamento de tickets de suporte técnico desenvolvido com Spring Boot (backend) e Next.js (frontend). O sistema oferece autenticação JWT, CRUD completo de tickets e usuários, dashboard com métricas e interface responsiva.

## Development Commands

### Backend (Spring Boot)
- **Start development server**: `cd backend && mvn spring-boot:run`
- **Start with production profile**: `cd backend && mvn spring-boot:run -Dspring.profiles.active=prod`
- **Build project**: `cd backend && mvn clean package`
- **Run tests**: `cd backend && mvn test`
- **Clean build**: `cd backend && mvn clean`

### Frontend (Next.js)
- **Start development server**: `cd frontend && npm run dev` (ou `pnpm dev`)
- **Build for production**: `cd frontend && npm run build`
- **Start production server**: `cd frontend && npm start`
- **Lint code**: `cd frontend && npm run lint`
- **Install dependencies**: `cd frontend && npm install` (ou `pnpm install`)

### Full Stack Development
- **Start both services**: Use separate terminals to run backend and frontend simultaneously
- **Backend logs**: Check console output for SQL queries and debug information in development mode

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
- Base URL: `http://localhost:8081` (development) / `NEXT_PUBLIC_API_URL` (production)
- All API calls through `useApi` hook with automatic token injection
- Error handling with automatic logout on authentication failures
- Environment-aware URL configuration via `process.env.NEXT_PUBLIC_API_URL`

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
- Full SQL logging enabled for debugging

### Production Setup (Render.com)
- Backend: Dynamic port via `${PORT:8080}`, managed PostgreSQL database
- Frontend: Standalone Next.js build with environment-specific API URL
- Database: Auto-configured via `DATABASE_URL` environment variable
- JWT secrets and other sensitive data via environment variables
- Optimized logging (INFO level, SQL logging disabled)

### Environment Variables
**Required for Production:**
- `DATABASE_URL`: PostgreSQL connection string (auto-provided by Render)
- `JWT_SECRET`: Secret key for JWT token signing
- `JWT_EXPIRATION`: Token expiration time in milliseconds (default: 86400000)
- `FRONTEND_URL`: Frontend URL for CORS configuration
- `NEXT_PUBLIC_API_URL`: Backend API URL accessible from browser

**Optional:**
- `SPRING_PROFILES_ACTIVE`: Set to `prod` for production configuration

### Key Configuration Files
- `backend/src/main/resources/application.properties`: Database, JWT, logging (development)
- `backend/src/main/resources/application-prod.properties`: Production-specific configurations
- `frontend/package.json`: Dependencies and scripts
- `backend/pom.xml`: Maven dependencies and build configuration
- `frontend/next.config.mjs`: Next.js production configuration with standalone output
- `render.yaml`: Render.com deployment configuration
- `.env.example`: Template for environment variables
- `backend/src/main/java/com/lovablepetro/chamadopetro/config/DatabaseConfig.java`: Dynamic database URL parsing for production

## Important Development Notes

### Security Configuration
- **Current State**: All endpoints are public (`.anyRequest().permitAll()` in SecurityConfig.java)
- **JWT Infrastructure**: Fully implemented but not enforced - ready for activation
- **CORS**: Dynamically configured for development (localhost:3000) and production (via FRONTEND_URL)

### Database Management
- **Development**: Auto-DDL with sample data initialization via `DataInitializer`
- **Production**: Manual schema management recommended for production environments
- **Logging**: Full SQL query logging in development, optimized for production

### API Architecture
- **RESTful Design**: All endpoints follow `/api/*` pattern
- **DTO Pattern**: Consistent use of DTOs for request/response mapping
- **Error Handling**: Centralized error responses with proper HTTP status codes

### Frontend State Management
- **Authentication**: Context-based with localStorage persistence
- **API Calls**: Centralized through `useApi` hook with automatic token injection
- **Route Protection**: `ProtectedRoute` component and `ClientLayout` for authenticated routes