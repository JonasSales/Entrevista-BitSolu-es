# Dicionário de Dados

## Tabela `users`

| Campo | Tipo | Obrigatório | Regra | Descrição |
|---|---|---:|---|---|
| `id` | `BIGINT` | Sim | PK, identity | Identificador do usuário |
| `username` | `VARCHAR(80)` | Sim | Único, não vazio | Nome usado na autenticação |
| `password_hash` | `VARCHAR(255)` | Sim | — | Hash BCrypt da senha |
| `full_name` | `VARCHAR(150)` | Sim | Não vazio | Nome completo do colaborador |
| `active` | `BOOLEAN` | Sim | Padrão `TRUE` | Indica se o usuário está ativo |
| `created_at` | `TIMESTAMPTZ` | Sim | Padrão atual | Data de criação |
| `updated_at` | `TIMESTAMPTZ` | Sim | Padrão atual | Data da última atualização |

## Tabela `request_categories`

| Campo | Tipo | Obrigatório | Regra | Descrição |
|---|---|---:|---|---|
| `id` | `BIGINT` | Sim | PK, identity | Identificador da categoria |
| `code` | `VARCHAR(30)` | Sim | Único, não vazio | Código interno da categoria |
| `name` | `VARCHAR(80)` | Sim | Único, não vazio | Nome exibido da categoria |
| `active` | `BOOLEAN` | Sim | Padrão `TRUE` | Indica se pode ser usada em novas solicitações |
| `created_at` | `TIMESTAMPTZ` | Sim | Padrão atual | Data de criação |

Categorias iniciais: `TI`, `RH`, `COMPRAS`, `FINANCEIRO` e `INFRAESTRUTURA`.

## Tabela `requests`

| Campo | Tipo | Obrigatório | Regra | Descrição |
|---|---|---:|---|---|
| `id` | `BIGINT` | Sim | PK, identity | Código da solicitação |
| `title` | `VARCHAR(150)` | Sim | Não vazio | Título curto da solicitação |
| `description` | `TEXT` | Sim | Não vazio | Detalhamento da necessidade |
| `category_id` | `BIGINT` | Sim | FK para `request_categories.id` | Categoria selecionada |
| `requester_id` | `BIGINT` | Sim | FK para `users.id` | Usuário solicitante |
| `status` | `VARCHAR(30)` | Sim | `OPEN`, `IN_PROGRESS` ou `COMPLETED` | Situação atual |
| `created_at` | `TIMESTAMPTZ` | Sim | Padrão atual | Data de abertura |
| `updated_at` | `TIMESTAMPTZ` | Sim | Padrão atual | Data da última atualização |

## Relacionamentos

```text
users (1) ---------------- (N) requests
request_categories (1) -- (N) requests
```

- Um usuário pode registrar várias solicitações.
- Uma categoria pode classificar várias solicitações.
- Cada solicitação possui exatamente um usuário e uma categoria.

## Índices

| Índice | Coluna | Finalidade |
|---|---|---|
| `idx_requests_requester_id` | `requests.requester_id` | Restringir e consultar dados do usuário |
| `idx_requests_category_id` | `requests.category_id` | Filtrar por categoria |
| `idx_requests_status` | `requests.status` | Filtrar por status |
| `idx_requests_created_at` | `requests.created_at` | Ordenar e filtrar por período |

## Integridade

- `users.username`, `request_categories.code` e `request_categories.name` são
  únicos;
- chaves estrangeiras impedem solicitações órfãs;
- constraints impedem textos vazios;
- a constraint de status limita os valores aceitos pelo banco;
- as migrations são executadas pelo Flyway e auditadas pelo histórico de
  versões.
