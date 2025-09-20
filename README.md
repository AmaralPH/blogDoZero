# Study Tracker Monorepo

Monorepo contendo um backend Spring Boot 3 (Java 21) e um frontend Angular 17 para um micro-MVP de acompanhamento de estudos.

## Visão geral

- **Backend** (`backend/`): API com autenticação JWT, gerenciamento de disciplinas/tópicos, sessões, questões/tentativas, metas diárias e estatísticas (hoje e últimos 7 dias). Banco H2 em desenvolvimento e suporte opcional a PostgreSQL via Docker Compose.
- **Frontend** (`frontend/`): SPA em Angular 17 com componentes standalone, guardas de rota e interceptor JWT. Páginas "Hoje", "Questões" e "Histórico".
- **Infra**: Flyway para migrations, perfis `dev` e `prod`, console H2 habilitado em desenvolvimento, `docker-compose.yml` com PostgreSQL opcional.

## Pré-requisitos

- Java 21
- Maven 3.9+
- Node.js 18+ / npm 9+

## Executando o backend

```bash
cd backend
mvn spring-boot:run
```

O perfil padrão é `dev`, utilizando H2 em memória. O console do H2 fica disponível em `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:studytracker`). Para usar PostgreSQL, suba o serviço com Docker Compose e execute com `-Dspring.profiles.active=prod`.

## Executando o frontend

```bash
cd frontend
npm install
npm start
```

A aplicação ficará disponível em `http://localhost:4200`, comunicando-se com o backend em `http://localhost:8080`.

## Docker Compose (opcional)

```bash
docker compose up -d postgres
```

Configuração padrão: banco `studytracker`, usuário `postgres`, senha `postgres` na porta `5432`.

## Testes

```bash
cd backend
mvn test
```

O frontend possui configuração padrão para testes via Karma/Jasmine (`npm test`).
