# Raízes do Nordeste — API Back-end

API REST desenvolvida como parte do Projeto Multidisciplinar da UNINTER (Trilha Back-end, 2026).  
O sistema é o back-end de uma rede de lanchonetes nordestinas, suportando múltiplos canais de atendimento (App, Totem, Balcão, Pick-up e Web), gestão de cardápio por unidade, pedidos, pagamento simulado (mock) e controle de estoque.

---

## Tecnologias e Versões

| Tecnologia | Versão                 |
|---|------------------------|
| Java | 17                     |
| Spring Boot | 3.5.15                 |
| Spring Security | 6.x (incluso no Boot)  |
| Spring Data JPA | 3.x (incluso no Boot)  |
| PostgreSQL | 15+                    |
| Flyway | 11.x (incluso no Boot) |
| Auth0 Java JWT | 4.5.2                  |
| springdoc-openapi | 2.8.3                  |
| Lombok | (incluso no Boot)      |
| Maven | 4.0.0                  |

---

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 4.0.0](https://maven.apache.org/download.cgi)
- [PostgreSQL 15+](https://www.postgresql.org/download/)
- [Git](https://github.com/kaueShi/raizes-do-nordeste)

---

## Clonando o repositório

```bash
git clone https://github.com/kaueShi/raizes-do-nordeste
cd raizes-do-nordeste
```

---

## Configurando variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto copiando o `.env.example`:

```bash
cp .env.example .env
```

Edite o `.env` com seus valores reais:

```properties
# Banco de dados
DB_URL=jdbc:postgresql://localhost:5432/raiz-do-nordeste
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_aqui

# JWT — use uma string longa e aleatória (mínimo 32 caracteres)
API_SECRET=sua_chave_secreta_aqui_minimo_32_caracteres
```

> **Atenção:** o arquivo `.env` real nunca deve ser commitado. Ele já está no `.gitignore`. O `.env.example` está versionado com valores fictícios para referência.

### Configurando as variáveis no IntelliJ IDEA

1. Abra **Run → Edit Configurations**
2. Selecione a configuração da aplicação (`DemoApplication`)
3. Em **Environment Variables**, adicione cada variável do `.env`
4. Clique em OK e reinicie a aplicação

### Referência no application.properties

O `application.properties` já usa as variáveis assim:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
api.security.token.secret=${JWT_TOKEN}
```

---

## Criando o banco de dados

Conecte no PostgreSQL e crie o banco:

```sql
CREATE DATABASE "raiz-do-nordeste";
```

As tabelas são criadas automaticamente pelo **Flyway** na primeira execução da aplicação. Não é necessário rodar nenhum script manualmente.

---

## Migrations e Seed

As migrations ficam em `src/main/resources/db/migration/` e são executadas em ordem pelo Flyway:

| Arquivo | Descrição |
|---|---|
| V1__create_tb_produtos.sql | Tabela de produtos (catálogo da rede) |
| V2__create_tb_usuario.sql | Tabela de usuários com roles |
| V3__create_tb_unidade.sql | Tabela de unidades da rede |
| V4__create_tb_produto_unidade.sql | Tabela de vínculo produto-unidade (cardápio) |
| V5__create_tb_pedido.sql | Tabelas de pedido e itens do pedido |
| V6__create_tb_pagamento.sql | Tabela de pagamentos |
| V7__insert_dados_iniciais.sql | Seed: usuário admin, uma unidade e 10 produtos |

O seed (V7) insere automaticamente:
## Criando o usuário Administrador

O sistema não possui um admin pré-cadastrado via migration por questão
de segurança — o hash de senha gerado pelo BCrypt varia a cada execução.

Para criar o admin inicial, com a API rodando, execute:

POST /auth/register/admin
Content-Type: application/json

{
"nome": "Administrador",
"email": "admin@admin.com",
"senha": "SuaSenha@123",
"role": "ROLE_ADMIN"
}

Após confirmar o cadastro (resposta 201), este endpoint deve ser
desativado comentando sua liberação no SecurityConfig.java:

// .requestMatchers(HttpMethod.POST, "/auth/register/admin").permitAll()

- **Unidade:** Matriz Hortolândia (SP)
- **10 produtos** nordestinos no catálogo

> **Atenção:** se o Flyway falhar em alguma migration, ele registra a falha e bloqueia inicializações futuras. Para resolver, delete o registro com falha: `DELETE FROM flyway_schema_history WHERE success = false;` e corrija o script antes de tentar novamente.

---

## Instalando dependências

```bash
mvn clean install -DskipTests
```

---

## Iniciando a API

```bash
mvn spring-boot:run
```

Ou, se preferir rodar o JAR compilado:

```bash
mvn clean package -DskipTests
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

A API estará disponível em `http://localhost:8080`.

---

## Documentação — Swagger / OpenAPI

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

O contrato OpenAPI em formato JSON está disponível em:

```
http://localhost:8080/v3/api-docs
```

> O Swagger é público — não requer autenticação para visualizar. Para testar endpoints protegidos diretamente pelo Swagger UI, clique em **Authorize** e cole o token JWT no campo `Bearer <token>`.

---

## Autenticação

A API usa **JWT (JSON Web Token)**. O fluxo é:

1. Faça login em `POST /auth/login` com `email` e `password`
2. Copie o `token` retornado
3. Em todas as requisições protegidas, envie o header:
   ```
   Authorization: Bearer <token_aqui>
   ```

Os tokens expiram em **2 horas**.

---

## Endpoints principais

### Autenticação — público

| Método | Rota | Descrição |
|---|---|---|
| POST | /auth/login | Login — retorna token JWT |
| POST | /auth/register/cliente | Cadastro de cliente |
| POST | /auth/register/funcionario | Cadastro de funcionário (requer ADMIN) |

### Unidades

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | /unidades | público | Lista todas as unidades |
| POST | /unidades | ADMIN | Cria nova unidade |

### Cardápio por unidade

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | /unidades/{id}/produtos | público | Lista cardápio disponível |
| POST | /unidades/{id}/produtos | ADMIN, FUNCIONARIO | Vincula produto à unidade |
| PUT | /unidades/{id}/produtos/{pid} | ADMIN, FUNCIONARIO | Atualiza preço/estoque |
| PATCH | /unidades/{id}/produtos/{pid}/desativar | ADMIN, FUNCIONARIO | Desativa produto |
| PATCH | /unidades/{id}/produtos/{pid}/reativar | ADMIN, FUNCIONARIO | Reativa produto |

### Produtos (catálogo)

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | /produtos | autenticado | Lista catálogo |
| POST | /produtos | ADMIN | Cadastra produto |
| PUT | /produtos/{id} | ADMIN | Edita produto |
| DELETE | /produtos/{id} | ADMIN | Remove produto |

### Pedidos

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | /pedidos | CLIENTE, FUNCIONARIO | Cria pedido |
| GET | /pedidos | ADMIN, FUNCIONARIO | Lista pedidos (filtrável por ?canalPedido=APP) |
| GET | /pedidos/{id} | autenticado | Busca pedido por ID |
| PATCH | /pedidos/{id}/status | ADMIN, FUNCIONARIO | Atualiza status |

### Pagamento

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | /pedidos/{id}/pagamento | CLIENTE, FUNCIONARIO | Processa pagamento mock |

---

## Regras de negócio principais

**Cardápio por unidade:** cada unidade tem seu próprio cardápio com preço e estoque independentes. Um produto pode existir em múltiplas unidades com preços diferentes.

**Multicanalidade:** todo pedido deve informar o `canalPedido` (APP, TOTEM, BALCAO, PICKUP ou WEB). A API permite filtrar pedidos por canal via query param.

**Fluxo de pedido e estoque:**
```
Criação → AGUARDANDO_PAGAMENTO (estoque não decrementado)
Pagamento aprovado → EM_PREPARO (estoque decrementado aqui)
Pagamento recusado → PAGAMENTO_RECUSADO
EM_PREPARO → PRONTO → ENTREGUE
Qualquer status cancelável → CANCELADO
```

**Pagamento mock:** envie `simularFalha: true` para simular recusa, `false` para aprovação. Um `transacaoId` UUID é gerado para simular o identificador de um gateway real.

**Controle de acesso:**
- `CLIENTE` cria pedidos e vê apenas os próprios
- `FUNCIONARIO` cria pedidos, vê todos e atualiza status
- `ADMIN` tem acesso total, incluindo catálogo e registro de funcionários

---

## Coleção Postman

A coleção de testes está em `postman/Raizes_do_Nordeste.postman_collection.json`.

### Como importar

1. Abra o Postman
2. Clique em **Import**
3. Selecione o arquivo da coleção
4. Importe também o environment: `postman/Raizes_do_Nordeste.postman_environment.json`

### Variáveis do environment

| Variável | Descrição |
|---|---|
| baseUrl | `http://localhost:8080` |
| token | Preenchido automaticamente pelo request de login admin |
| token_cliente | Preenchido automaticamente pelo login de cliente |
| pedidoId | Preenchido automaticamente pela criação de pedido |

### Ordem de execução sugerida

```
1. Auth/Login Admin          → preenche {{token}}
2. Auth/Login Cliente        → preenche {{token_cliente}}
3. Setup/Vincular Produto    → vincula produto à unidade de seed
4. Pedidos/Criar Pedido      → preenche {{pedidoId}}
5. Pagamento/Aprovado        → processa e avança status
6. ... demais cenários
```

### Cenários cobertos

| ID | Cenário | Tipo |
|---|---|---|
| T01 | Login com credenciais válidas | positivo |
| T02 | Acesso sem token | negativo |
| T03 | Perfil sem permissão (cliente tenta criar produto) | negativo |
| T04 | Campo obrigatório ausente (canalPedido faltando) | negativo |
| T05 | Enum inválido no canalPedido | negativo |
| T06 | Criar pedido com itens válidos | positivo |
| T07 | Criar pedido com produto inexistente | negativo |
| T08 | Criar pedido com estoque insuficiente | negativo |
| T09 | Consultar pedido próprio como cliente | positivo |
| T10 | Atualizar status do pedido | positivo |
| T11 | Pagamento mock aprovado | positivo |
| T12 | Pagamento mock recusado | negativo |
| T13 | Pagar pedido já processado | negativo |
| T14 | Cardápio público sem autenticação | positivo |

---

## Estrutura do projeto

```
src/
└── main/
    ├── java/com/example/demo/
    │   ├── config/security/        # JWT filter, SecurityConfig, TokenService
    │   ├── controller/             # Controllers REST
    │   ├── dtos/                   # DTOs de request e response
    │   ├── enums/                  # Roles, CanalPedido, StatusPedido, etc.
    │   ├── exceptions/             # GlobalExceptionHandler e exceções customizadas
    │   ├── model/                  # Entidades JPA
    │   ├── repository/             # Interfaces Spring Data JPA
    │   └── services/               # Regras de negócio
    └── resources/
        ├── db/migration/           # Scripts Flyway (V1 a V7)
        └── application.properties  # Configuração (usa variáveis de ambiente)
postman/
├── Raizes_do_Nordeste.postman_collection.json
└── Raizes_do_Nordeste.postman_environment.json
.env.example                        # Variáveis de ambiente (sem valores reais)
```

---

## Decisões técnicas documentadas

**Por que separar `tb_produto` de `tb_produto_unidade`?**  
Produtos do catálogo (nome, descrição) são definidos pela matriz e compartilhados entre unidades. Preço, estoque e disponibilidade variam por unidade — por isso ficam numa entidade separada. Isso permite desativar um produto sazonal em uma unidade sem afetar o catálogo global.

**Por que o estoque só é decrementado após o pagamento?**  
Decrementar na criação do pedido prenderia estoque de pedidos que nunca são pagos. O fluxo atual só desconta após confirmação do pagamento mock, garantindo que apenas pedidos efetivamente pagos afetam o estoque.

**Por que usar `simularFalha` no pagamento?**  
A abordagem determinística (cliente controla o resultado) garante que os testes sejam reproduzíveis — diferente de uma abordagem aleatória, onde o resultado do teste dependeria de sorte.

**Por que CLIENTE não aparece no `GET /pedidos`?**  
Por privacidade (LGPD): um cliente não deve ver pedidos de outros clientes. O `GET /pedidos/{id}` valida no service que o cliente só acessa o próprio pedido.

---

## LGPD — Cuidados implementados

- Senhas armazenadas com hash BCrypt (nunca em texto puro)
- Respostas de API nunca expõem o campo `senha`
- Consentimento representado no modelo de fidelização
- Pedidos de cliente visíveis apenas pelo próprio cliente
- Logs de ações sensíveis (criação/cancelamento de pedido, mudança de status)
- Dados pessoais mínimos coletados (nome, e-mail, role)

---

## Contato

Desenvolvido por: **[Kaue Cavalheiro Nogueira]**  
RU: **[4834256]**  
Curso: **[CST - Análise e Desenvolvimento de Sistemas]**   
Disciplina: **Projeto Multidisciplinar — Trilha Back-end**  
Professor: **Prof. Me. Luciane Yanase Kanashiro**
