# 📚 Sistema de Biblioteca — Spring Boot

Sistema web completo para gerenciamento de biblioteca, com CRUD de livros e autores, controle de empréstimos e interface responsiva.

---

## 🛠️ Tecnologias

| Camada         | Tecnologia                          |
|----------------|-------------------------------------|
| Backend        | Java 17 + Spring Boot 3.2           |
| Banco de Dados | PostgreSQL + Spring Data JPA        |
| Frontend       | Thymeleaf + Bootstrap 5             |
| Build          | Maven                               |
| Utilitários    | Lombok, Bean Validation, DevTools   |

---

## 📁 Estrutura do Projeto

```
src/main/java/com/biblioteca/
├── BibliotecaApplication.java      ← Classe principal
├── DataLoader.java                 ← Dados iniciais de teste
│
├── model/                          ← Entidades JPA (tabelas do banco)
│   ├── Autor.java
│   ├── Livro.java
│   └── Emprestimo.java
│
├── repository/                     ← Acesso ao banco (Spring Data JPA)
│   ├── AutorRepository.java
│   ├── LivroRepository.java
│   └── EmprestimoRepository.java
│
├── service/                        ← Regras de negócio
│   ├── AutorService.java
│   ├── LivroService.java
│   └── EmprestimoService.java
│
├── controller/                     ← Endpoints HTTP
│   ├── AutorController.java        ← API REST: /api/autores
│   ├── LivroController.java        ← API REST: /api/livros
│   ├── EmprestimoController.java   ← API REST: /api/emprestimos
│   ├── ViewController.java         ← Páginas web (Thymeleaf)
│   └── GlobalExceptionHandler.java ← Tratamento global de erros
│
└── dto/
    └── BibliotecaDTO.java          ← Objetos de transferência de dados

src/main/resources/
├── application.properties          ← Configurações da aplicação
├── templates/                      ← Páginas HTML (Thymeleaf)
│   ├── index.html                  ← Dashboard
│   ├── livros/lista.html
│   ├── autores/lista.html
│   └── emprestimos/lista.html
└── static/
    ├── css/style.css
    └── js/
        ├── app.js
        ├── livros.js
        ├── autores.js
        └── emprestimos.js
```

---

## ⚙️ Como Executar

### 1. Pré-requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL rodando localmente

### 2. Criar o banco de dados

```sql
CREATE DATABASE biblioteca_db;
```

### 3. Configurar credenciais

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca_db
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA_AQUI
```

### 4. Rodar a aplicação

```bash
# Com Maven Wrapper
./mvnw spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run
```

### 5. Acessar no navegador

```
http://localhost:8080
```

> O **DataLoader** vai inserir automaticamente 3 autores e 4 livros de exemplo na primeira execução.

---

## 🌐 Endpoints da API REST

### Autores — `/api/autores`
| Método | Endpoint              | Descrição             |
|--------|-----------------------|-----------------------|
| GET    | `/api/autores`        | Lista todos           |
| GET    | `/api/autores/{id}`   | Busca por ID          |
| GET    | `/api/autores/buscar?nome=x` | Busca por nome |
| POST   | `/api/autores`        | Cria novo             |
| PUT    | `/api/autores/{id}`   | Atualiza              |
| DELETE | `/api/autores/{id}`   | Remove                |

### Livros — `/api/livros`
| Método | Endpoint                    | Descrição              |
|--------|-----------------------------|------------------------|
| GET    | `/api/livros`               | Lista todos            |
| GET    | `/api/livros/{id}`          | Busca por ID           |
| GET    | `/api/livros/disponiveis`   | Somente disponíveis    |
| GET    | `/api/livros/buscar?titulo=x` | Busca por título     |
| POST   | `/api/livros`               | Cria novo              |
| PUT    | `/api/livros/{id}`          | Atualiza               |
| DELETE | `/api/livros/{id}`          | Remove                 |

### Empréstimos — `/api/emprestimos`
| Método | Endpoint                          | Descrição          |
|--------|-----------------------------------|--------------------|
| GET    | `/api/emprestimos`                | Lista todos        |
| GET    | `/api/emprestimos/{id}`           | Busca por ID       |
| GET    | `/api/emprestimos/ativos`         | Somente ativos     |
| GET    | `/api/emprestimos/atrasados`      | Somente atrasados  |
| POST   | `/api/emprestimos`                | Registra empréstimo|
| PATCH  | `/api/emprestimos/{id}/devolver`  | Registra devolução |

---

## 💡 Conceitos Importantes

### Por que DTOs?
Os **DTOs (Data Transfer Objects)** separam o que é exposto pela API do que existe no banco. Isso evita expor dados sensíveis e dá mais controle sobre o contrato da API.

### Por que `@Transactional`?
Garante que operações no banco sejam **atômicas** — se algo der errado no meio, tudo é revertido automaticamente.

### Por que `@RestController` vs `@Controller`?
- `@RestController` → Retorna **JSON** (para a API REST)
- `@Controller` → Retorna **nome de template HTML** (para o Thymeleaf)

---

## 🗄️ Modelo de Dados

```
Autor (1) ──────< Livro (N)
                      │
                      └──────< Emprestimo (N)
```

- Um **Autor** pode ter vários **Livros**
- Um **Livro** pode ter vários **Empréstimos** (histórico)
- Um **Empréstimo** tem status: `ATIVO`, `DEVOLVIDO` ou `ATRASADO`

---

## 👨‍💻 Autor

Desenvolvido por **Deivid Andrade**
