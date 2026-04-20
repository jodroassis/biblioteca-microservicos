# Biblioteca Microservicos

Sistema de gerenciamento de biblioteca construído com arquitetura de microsserviços, utilizando comunicação assíncrona via RabbitMQ.

---

## Visão Geral

O projeto é composto por dois microsserviços independentes que se comunicam por meio de mensageria:

- **Catalogo Service** — gerencia o acervo de livros da biblioteca
- **Emprestimo Service** — registra e controla os empréstimos realizados

Quando um empréstimo é registrado, o Emprestimo Service publica um evento no RabbitMQ, e o Catalogo Service consome essa mensagem para atualizar automaticamente o status do livro para `EMPRESTADO`.

---

## Arquitetura

```
Cliente
   │
   ├──► POST /emprestimos  ──► Emprestimo Service (8082)
   │                                    │
   │                           Publica no RabbitMQ
   │                           (exchange.emprestimo)
   │                                    │
   │                           Catalogo Service (8081)
   │                           consome a fila e atualiza
   │                           o status do livro
   │
   └──► GET/POST /livros   ──► Catalogo Service (8081)
```

### Fluxo de mensageria

| Componente       | Tipo           | Identificador            |
|------------------|----------------|--------------------------|
| Exchange         | DirectExchange | `exchange.emprestimo`    |
| Queue            | Durable Queue  | `fila.emprestimo`        |
| Routing Key      | —              | `emprestimo.solicitado`  |

---

## Tecnologias

| Tecnologia     | Versão |
|----------------|--------|
| Java           | 21     |
| Spring Boot    | 4.0.5  |
| Spring AMQP    | —      |
| Spring Data JPA| —      |
| MySQL          | 8      |
| RabbitMQ       | 3      |
| Docker Compose | —      |
| Maven          | —      |

---

## Pré-requisitos

- [Java 21+](https://adoptium.net/)
- [Docker](https://www.docker.com/) e Docker Compose
- [Maven](https://maven.apache.org/) (ou usar o wrapper `./mvnw` incluso)

---

## Como executar

### 1. Subir a infraestrutura com Docker

Na raiz do projeto, execute:

```bash
docker compose up -d
```

Isso inicializa:

| Container         | Serviço           | Porta(s)          |
|-------------------|-------------------|-------------------|
| `rabbitmq`        | RabbitMQ          | 5672 / 15672      |
| `mysql-catalogo`  | MySQL (catálogo)  | 3307              |
| `mysql-emprestimo`| MySQL (empréstimo)| 3308              |

### 2. Iniciar o Catalogo Service

```bash
cd catalogo-service
./mvnw spring-boot:run
```

Ou com Maven instalado globalmente:

```bash
mvn spring-boot:run
```

### 3. Iniciar o Emprestimo Service

```bash
cd emprestimo-service
./mvnw spring-boot:run
```

---

## Microsserviços

### Catalogo Service

Gerencia o acervo de livros da biblioteca.

- **Porta:** `8081`
- **Banco de dados:** `catalogo` (MySQL na porta `3307`)
- **Papel no RabbitMQ:** Consumidor — escuta a fila `fila.emprestimo` e atualiza o status do livro

#### Endpoints

| Método   | Endpoint                         | Descrição                                      |
|----------|----------------------------------|------------------------------------------------|
| `POST`   | `/livros`                        | Cadastra um novo livro                         |
| `GET`    | `/livros`                        | Lista todos os livros                          |
| `GET`    | `/livros?titulo={titulo}`        | Busca livros por título (case-insensitive)      |
| `GET`    | `/livros?autor={autor}`          | Busca livros por autor (case-insensitive)       |
| `GET`    | `/livros/{isbn}`                 | Busca um livro pelo ISBN                       |
| `PATCH`  | `/livros/{isbn}/emprestar`       | Marca o livro como emprestado                  |
| `PATCH`  | `/livros/{isbn}/devolver`        | Marca o livro como disponível                  |
| `DELETE` | `/livros/{isbn}`                 | Remove um livro (somente se disponível)        |

#### Payload — Cadastro de livro

```json
{
  "isbn": "978-3-16-148410-0",
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "ano": 2008
}
```

#### Status do livro

| Status       | Descrição          |
|--------------|--------------------|
| `DISPONIVEL` | Disponível para empréstimo |
| `EMPRESTADO` | Atualmente emprestado      |

---

### Emprestimo Service

Registra os empréstimos de livros e notifica o serviço de catálogo via RabbitMQ.

- **Porta:** `8082`
- **Banco de dados:** `emprestimo` (MySQL na porta `3308`)
- **Papel no RabbitMQ:** Produtor — publica o ISBN do livro na fila ao registrar um empréstimo

#### Endpoints

| Método | Endpoint                                               | Descrição                  |
|--------|--------------------------------------------------------|----------------------------|
| `POST` | `/emprestimos?isbn={isbn}&emailUsuario={email}`        | Registra um novo empréstimo |

#### Exemplo de requisição

```
POST http://localhost:8082/emprestimos?isbn=978-3-16-148410-0&emailUsuario=joao@email.com
```

---

## Portas e URLs

| Serviço                | URL                                  | Credenciais         |
|------------------------|--------------------------------------|---------------------|
| Catalogo Service       | http://localhost:8081                | —                   |
| Emprestimo Service     | http://localhost:8082                | —                   |
| MySQL (catálogo)       | `jdbc:mysql://localhost:3307/catalogo` | `root` / `root`   |
| MySQL (empréstimo)     | `jdbc:mysql://localhost:3308/emprestimo` | `root` / `root` |
| RabbitMQ AMQP          | `amqp://localhost:5672`              | `admin` / `admin`   |
| RabbitMQ Management UI | http://localhost:15672               | `admin` / `admin`   |

---

## Estrutura do Projeto

```
biblioteca-microservicos/
├── docker-compose.yml
├── catalogo-service/
│   └── src/main/java/school/sptech/catalogo_service/
│       ├── config/         # Configuração do RabbitMQ
│       ├── controller/     # Endpoints REST
│       ├── dto/            # Request e Response DTOs
│       ├── enums/          # StatusLivro
│       ├── exception/      # Tratamento global de erros
│       ├── messaging/      # Consumer RabbitMQ
│       ├── model/          # Entidade Livro
│       ├── repository/     # Spring Data JPA
│       └── service/        # Regras de negócio
└── emprestimo-service/
    └── src/main/java/school/sptech/emprestimo_service/
        ├── config/         # Configuração do RabbitMQ
        ├── controller/     # Endpoints REST
        ├── messaging/      # Producer RabbitMQ
        ├── model/          # Entidade Emprestimo
        ├── repository/     # Spring Data JPA
        └── service/        # Regras de negócio
```

---

## Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
