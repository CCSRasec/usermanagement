# User Management System

Este projeto é um sistema de gerenciamento de usuários com autenticação via JWT, desenvolvido com **Kotlin**, **Spring Boot**, e integrado a um banco de dados **PostgreSQL**. A aplicação permite realizar operações CRUD com usuários e protege rotas com autenticação JWT.

## Funcionalidades

- Registro de novos usuários.
- Login de usuários com autenticação JWT.
- Operações CRUD (Create, Read, Update, Delete) de usuários.
- Proteção de rotas utilizando JWT.
- Contêinerização do backend e banco de dados com Docker.

## Tecnologias Utilizadas

- **Kotlin**: Linguagem de programação para desenvolvimento backend.
- **Spring Boot**: Framework para criação do backend.
- **Spring Security**: Biblioteca de segurança para proteger as rotas.
- **JWT (JSON Web Token)**: Autenticação e autorização via tokens.
- **PostgreSQL**: Banco de dados relacional.
- **Docker**: Contêinerização da aplicação e do banco de dados.
- **Maven**: Gerenciador de dependências e build do projeto.

## Requisitos

- **JDK 17** ou superior
- **Maven**
- **Docker** e **Docker Compose**

## Como Executar o Projeto

1. Clonar o Repositório

2. Configurar o Banco de Dados no Docker
O projeto utiliza PostgreSQL como banco de dados. Para subir o banco de dados e a aplicação no Docker, siga os passos abaixo.

3. Executar o Docker Compose
Certifique-se de ter o Docker e o Docker Compose instalados. Em seguida, execute o comando:

docker-compose up --build
Isso irá iniciar tanto o banco de dados PostgreSQL quanto o backend Spring Boot em contêineres separados, com comunicação entre eles.

4. Gerar o JAR da Aplicação
Se desejar gerar o arquivo JAR da aplicação para rodar manualmente, use o Maven:

mvn clean install -DskipTests
Isso irá gerar o JAR do projeto na pasta target/ e pular os testes.

5. Endpoints Disponíveis
5.1. Registro de Usuário
Endpoint: /api/auth/register
Método: POST
Body:
json
{
  "username": "novoUsuario",
  "password": "senha123",
  "email": "email@exemplo.com"
}

5.2. Login de Usuário
Endpoint: /api/auth/login
Método: POST
Body:
json
{
  "username": "novoUsuario",
  "password": "senha123"
}
Esse endpoint retorna um JWT (JSON Web Token) que deve ser utilizado nas requisições seguintes para acessar rotas protegidas.

5.3. Acesso a Rotas Protegidas
Exemplo de Rota Protegida: /api/users
Método: GET
Headers:
text
Authorization: Bearer <token_JWT_recebido_ao_fazer_login>
6. Variáveis de Ambiente
No arquivo docker-compose.yml, as variáveis de ambiente relacionadas ao banco de dados são configuradas automaticamente:

yaml
environment:
  POSTGRES_USER: admin
  POSTGRES_PASSWORD: admin
  POSTGRES_DB: userdb
  
7. Estrutura do Projeto
bash
src
├── main
│   ├── kotlin
│   │   └── com.example.usermanagement
│   │       ├── config          # Configurações de segurança e JWT
│   │       ├── controller      # Controladores REST
│   │       ├── model           # Modelos de dados (entidades)
│   │       ├── repository      # Repositórios JPA
│   │       ├── service         # Serviços de regras de negócio e JWT
│   │       └── UsermanagementApplication.kt # Classe principal do Spring Boot
│   └── resources
│       └── application.yml     # Configurações do Spring Boot
└── test
    └── kotlin                  # Testes automatizados
