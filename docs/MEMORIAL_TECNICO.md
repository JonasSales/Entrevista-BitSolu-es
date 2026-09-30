# Memorial Técnico de Desenvolvimento

## 1. Objetivo

O Portal de Solicitações Internas permite que colaboradores autenticados
registrem, acompanhem, filtrem e atualizem solicitações internas até sua
conclusão.

A solução é composta por dois repositórios:

- `Entrevista-BitSolu-es`: API REST, regras de negócio e persistência;
- `portal-solicitacoes-frontend`: interface web da aplicação.

## 2. Arquitetura

O backend utiliza arquitetura MVC aplicada a uma API REST, com organização por
domínio. Cada domínio separa responsabilidades entre controllers, services,
repositories, DTOs e models.

```text
React/Vite/TypeScript
          |
          | HTTP + JSON + JWT
          v
Spring MVC Controller -> Service -> Repository -> PostgreSQL
          |
          +--> tratamento global de erros e validações
```

O frontend utiliza React com React Router. O token JWT é armazenado no
`localStorage` durante a sessão e enviado no cabeçalho `Authorization` das
requisições protegidas.

## 3. Fluxos principais

### Autenticação

1. O usuário cria uma conta ou informa suas credenciais;
2. a API valida os dados e retorna um JWT no login;
3. o frontend armazena o token durante a sessão;
4. as rotas protegidas verificam a existência do token;
5. o logout remove o token e retorna o usuário à tela de login.

### Solicitações

1. o frontend carrega as categorias ativas;
2. o usuário informa título, descrição e categoria;
3. a API associa a solicitação ao usuário autenticado e inicia com status
   `OPEN`;
4. a listagem consulta apenas registros do próprio usuário;
5. filtros e paginação são enviados para a API;
6. solicitações abertas podem ser editadas ou excluídas;
7. o status pode ser atualizado conforme o fluxo de atendimento;
8. a consulta detalhada apresenta os dados completos do registro.

## 4. Tecnologias e justificativas

### Backend

- Java 21: linguagem principal e suporte de longo prazo;
- Spring Boot 3.4.10: configuração e execução da aplicação;
- Spring Web/MVC: exposição dos endpoints REST;
- Spring Data JPA: persistência e consultas paginadas;
- Spring Security e JWT: autenticação stateless;
- Bean Validation: validação de payloads de entrada;
- Flyway: versionamento e execução controlada das migrations;
- PostgreSQL 16: banco relacional da aplicação;
- Springdoc OpenAPI: documentação e teste da API via Swagger;
- JUnit, Mockito e MockMvc: testes unitários e de integração HTTP.

### Frontend

- React 19: construção da interface;
- TypeScript: tipagem dos contratos e componentes;
- Vite: desenvolvimento e build;
- React Router: navegação entre as telas;
- Node.js 22 Alpine: execução dos scripts em ambiente leve.

### Operação

- Docker Compose: execução local dos serviços;
- imagens Alpine: redução do tamanho das imagens e do consumo de recursos;
- scripts shell: padronização dos comandos de inicialização, parada, logs e
  testes.

## 5. Modelo de dados

O banco é controlado pelo Flyway. O Hibernate utiliza `ddl-auto=validate`,
portanto não cria ou altera tabelas automaticamente.

As migrations são aplicadas na ordem:

1. `V1__create_users_table.sql`;
2. `V2__create_request_categories_table.sql`;
3. `V3__create_requests_table.sql`;
4. `V4__seed_request_categories.sql`.

As chaves estrangeiras garantem que toda solicitação tenha uma categoria e um
solicitante válidos. Índices foram criados para os campos usados nas consultas
por solicitante, categoria, status e data de criação.

## 6. Segurança

- Senhas são armazenadas usando hash BCrypt;
- autenticação baseada em JWT com expiração configurável;
- endpoints de solicitações, categorias e dashboard exigem autenticação;
- solicitações são filtradas pelo usuário autenticado;
- CORS permite a origem configurada em `CORS_ALLOWED_ORIGINS`;
- mensagens de erro não expõem o hash da senha ou o token;
- validações impedem campos obrigatórios vazios e tamanhos inválidos.

## 7. Regras de negócio

- uma solicitação nasce com status `OPEN`;
- somente o solicitante autenticado pode consultar ou alterar seus registros;
- edição e exclusão são permitidas apenas enquanto a solicitação estiver aberta;
- somente categorias ativas podem ser utilizadas em novas solicitações;
- os status disponíveis são `OPEN`, `IN_PROGRESS` e `COMPLETED`;
- o dashboard apresenta os indicadores somente das solicitações do usuário
  autenticado.

## 8. Tratamento de erros

O backend possui um handler global que converte exceções de validação,
autenticação, autorização, recurso inexistente e erros de negócio em uma
resposta padronizada contendo timestamp, status, erro, mensagem, URI e método
HTTP.

## 9. Execução

No backend:

```bash
cp .envexemple .env
./scripts/up.sh
```

No frontend:

```bash
cp .env.example .env
./scripts/start.sh
```

URLs padrão:

- frontend: `http://localhost:5173`;
- API: `http://localhost:8080`;
- Swagger: `http://localhost:8080/swagger-ui.html`.

## 10. Estratégia de testes

O backend possui testes unitários para autenticação e dashboard e testes de
integração HTTP para autenticação e solicitações. Também foram realizados
testes manuais dos fluxos de login, cadastro, criação, edição, exclusão,
alteração de status, filtros, paginação e consulta detalhada no frontend.

## 11. Decisões e limitações conhecidas

### Decisões técnicas

- MVC e organização por domínio separam transporte HTTP, regras de negócio e
  persistência;
- JWT mantém a API stateless e facilita o consumo pelo frontend separado;
- Flyway garante versionamento reprodutível do schema;
- PostgreSQL oferece integridade referencial, constraints e recursos de
  consulta adequados ao domínio;
- o DTO `PageResponse` evita expor a implementação interna do Spring Data e
  mantém um contrato JSON estável para o frontend;
- o proxy do Vite permite desenvolvimento em rede local sem hardcode de
  `localhost` no navegador.

### Trade-offs

- o armazenamento do JWT em `localStorage` simplifica a integração, mas exige
  atenção contra XSS em uma implantação pública;
- não foi criado um módulo administrativo, pois o escopo restringe os dados ao
  usuário solicitante;
- o frontend usa uma única página de solicitações para reduzir complexidade e
  manter o fluxo curto para o usuário.

- o escopo não possui perfis administrativos separados;
- o gerenciamento de solicitações é restrito ao próprio solicitante;
- a interface utiliza o endpoint paginado diretamente, sem cache local;
- o frontend possui validação de formulário e mensagens da API, mantendo as
  regras definitivas no backend.
