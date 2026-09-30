# Portal de Solicitações Internas

Mini-projeto da Bit Soluções para registro, acompanhamento e consulta de
solicitações internas.

## Stacks

- Java 21;
- Spring Boot 3.4.10;
- Spring MVC/Web, Validation, Data JPA e Security;
- JWT para autenticação stateless;
- Springdoc OpenAPI 2.9.1 e Swagger UI;
- Flyway para migrations;
- PostgreSQL 16;
- Maven 3.9.9;
- Docker e Docker Compose;
- imagens Alpine no PostgreSQL, no build Maven e na execução Java.

O frontend React, Vite e TypeScript fica no repositório separado
`portal-solicitacoes-frontend`, no mesmo diretório de trabalho do backend.
Consulte o README do frontend para os comandos de execução da interface.

## Arquitetura

O backend usa MVC aplicado a uma API REST. Os módulos são organizados por
domínio, com as subpastas `controller`, `service`, `repository`, `dto` e
`model` quando aplicável:

```text
Controller -> Service -> Repository -> PostgreSQL
     |          |           |
    DTO       regras       JPA
```

Principais domínios:

- `users`: cadastro, login e JWT;
- `requestcategories`: categorias ativas;
- `requests`: criação, atualização, status, filtros e paginação;
- `dashboard`: indicadores por usuário autenticado;
- `common`: erros padronizados e tratamento global.

## Pré-requisitos

- Docker instalado e em execução;
- Docker Compose v2;
- portas `5432` e `8080` disponíveis;
- acesso à internet na primeira compilação.

Não é necessário instalar Java ou Maven no host para executar a aplicação.

## Configuração

Na raiz do projeto, copie o arquivo de exemplo:

```bash
cp .envexemple .env
```

Depois ajuste as credenciais e o `JWT_SECRET`. O arquivo `.env` é ignorado pelo
Git e não deve ser enviado ao repositório.

## Execução com scripts

Os scripts ficam em `scripts/` e podem ser executados a partir de qualquer
diretório:

```bash
./scripts/up.sh       # sobe banco e API, compilando a imagem
./scripts/start.sh    # para todos os serviços e sobe novamente
./scripts/status.sh   # mostra o status dos containers
./scripts/logs.sh     # acompanha os logs da API
./scripts/test.sh     # executa testes no Maven Alpine
./scripts/down.sh     # para os containers sem remover o volume do banco
```

O `start.sh` reinicia os serviços sem remover o volume persistente do PostgreSQL.
Se necessário, torne os scripts executáveis:

```bash
chmod +x scripts/*.sh
```

## Execução manual

```bash
docker compose up -d --build
docker compose ps
docker compose logs -f api
```

Para parar os serviços:

```bash
docker compose down
```

As migrations Flyway são executadas automaticamente quando a API inicia. O
Hibernate usa `ddl-auto=validate`, portanto o schema é controlado pelas
migrations em `src/main/resources/db/migration`.

## Endpoints e documentação

Base URL: `http://localhost:8080`

- Swagger UI: `http://localhost:8080/swagger-ui.html`;
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`;
- cadastro: `POST /api/v1/auth/register`;
- login: `POST /api/v1/auth/login`;
- categorias: `GET /api/v1/request-categories`;
- solicitações: `/api/v1/requests`;
- dashboard: `GET /api/v1/dashboard`.

Os endpoints protegidos exigem:

```text
Authorization: Bearer <token-jwt>
```

Exemplo de listagem paginada:

```text
GET /api/v1/requests?page=0&size=10&status=OPEN&title=financeiro
```

A listagem é restrita ao usuário autenticado e retorna o contrato estável
`PageResponse`, com `content`, `number`, `size`, `numberOfElements`,
`totalElements`, `totalPages`, `first` e `last`.

Métodos principais:

- `POST /api/v1/auth/register`: cadastra usuário;
- `POST /api/v1/auth/login`: autentica e retorna JWT;
- `GET /api/v1/request-categories`: lista categorias ativas;
- `POST /api/v1/requests`: cria solicitação;
- `GET /api/v1/requests`: lista com paginação e filtros;
- `GET /api/v1/requests/{id}`: consulta detalhes;
- `PUT /api/v1/requests/{id}`: atualiza solicitação aberta;
- `PUT /api/v1/requests/{id}/status`: altera status;
- `DELETE /api/v1/requests/{id}`: exclui solicitação aberta;
- `GET /api/v1/dashboard`: retorna indicadores do usuário.

Exemplo de cadastro e login:

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"joao.silva","password":"Senha@123","fullName":"João da Silva"}'

curl -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"joao.silva","password":"Senha@123"}'
```

Para executar em outro dispositivo da rede local, acesse
`http://IP_DA_MAQUINA:5173`. O frontend usa o proxy do Vite para encaminhar as
chamadas à API, sem exigir que o navegador remoto acesse diretamente a porta
8080.

## Testes

Os testes unitários e de integração HTTP podem ser executados sem banco externo:

```bash
./scripts/test.sh
```

A suíte cobre autenticação, validações, tratamento global de erros, paginação e
filtros dos endpoints.

## Estrutura resumida

```text
.
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
├── docs
│   ├── DICIONARIO_DE_DADOS.md
│   └── MEMORIAL_TECNICO.md
├── .envexemple
├── scripts
│   ├── down.sh
│   ├── logs.sh
│   ├── start.sh
│   ├── status.sh
│   ├── test.sh
│   └── up.sh
└── src
    ├── main/java/br/com/bitsolucoes/portalsolicitacoes
    └── main/resources/db/migration
```
