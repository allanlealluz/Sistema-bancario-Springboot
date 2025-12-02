# 🏦 API de Conta Bancária & Pagamentos IoT

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

> Uma API REST robusta que simula um ecossistema bancário completo, integrando conceitos de **Domain-Driven Design (DDD)**, **Segurança Avançada (JWT + IoT)** e **Regras de Negócio Dinâmicas**.

---

## 📖 Sobre o Projeto

Este projeto foi desenvolvido como parte de uma Situação de Aprendizagem avançada. O objetivo foi transformar um sistema bancário simples em uma plataforma segura e escalável, capaz de processar pagamentos com **taxas configuráveis em tempo real** e autenticação de dois fatores simulando dispositivos **IoT via MQTT**.

### 🌟 Destaques da Arquitetura
* **Taxas Dinâmicas (Data-Driven):** Diferente de sistemas rígidos, as taxas (Juros, Multas, Tarifas) não estão "chumbadas" no código. O Gerente cadastra taxas no banco de dados vinculadas a tipos de pagamento (PIX, Boleto, etc.), e o sistema as aplica automaticamente no momento da transação.
* **Segurança IoT (Simulação):** Operações críticas (Saque, Transferência e Pagamento) exigem um token temporário. A API simula a comunicação assíncrona onde um dispositivo físico gera o token e o backend valida antes de debitar o saldo.
* **Transacionalidade Atômica:** O fluxo de pagamento garante que a validação do token, o cálculo de taxas e o débito em conta ocorram em uma única transação (`@Transactional`), garantindo integridade dos dados.

---

## 🛠️ Tecnologias

* **Linguagem:** [Java 21](https://www.oracle.com/java/technologies/downloads/#java21)
* **Framework Principal:** [Spring Boot 3.3.5](https://spring.io/projects/spring-boot)
* **Segurança:** Spring Security + JWT (JSON Web Token)
* **Documentação:** SpringDoc OpenAPI (Swagger UI)
* **Banco de Dados:** H2 Database (Em memória para desenvolvimento rápido)
* **Utilitários:** Lombok, Maven

---

## 🚀 Funcionalidades

### 👔 Módulo Gerente (Admin)
* **Gestão de Clientes:** Cadastro completo com criação automática de conta inicial.
* **Configuração de Taxas:** CRUD de taxas financeiras. O gerente define:
    * *Descrição* (ex: "Tarifa de Serviço Noturno")
    * *Percentual* (ex: 10%)
    * *Valor Fixo* (ex: R$ 2,00)
    * *Vinculação* (ex: Aplica-se apenas a pagamentos via **PIX**)
* **Relatórios:** Visualização de todas as contas e pagamentos processados.

### 👤 Módulo Cliente
* **Operações Bancárias:** Depósito, Saque e Transferência entre contas.
* **Pagamentos:** Pagamento de contas (Luz, Água, Boletos Externos). O cliente informa apenas o tipo (ex: "BOLETO") e o sistema calcula o total com base nas taxas ativas.
* **Autenticação 2FA:** Validação obrigatória de token IoT para saídas de dinheiro.

---

## 📦 Como Executar

### Pré-requisitos
* Java JDK 21 instalado.
* Git instalado.

### Passo a Passo

1.  **Clone o repositório**
    ```bash
    git clone [https://github.com/seu-usuario/conta-bancaria.git](https://github.com/seu-usuario/conta-bancaria.git)
    cd conta-bancaria
    ```

2.  **Execute a aplicação** (Windows)
    ```cmd
    .\mvnw.cmd spring-boot:run
    ```
    *(Para Linux/Mac use `./mvnw spring-boot:run`)*

3.  **Acesse a Documentação**
    Com a aplicação rodando, abra o navegador em:
    > [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧪 Guia de Testes (Postman / Insomnia)

Para testar o fluxo completo, siga este roteiro:

### 1️⃣ Autenticação Inicial (Admin)
O sistema inicia com um usuário administrador padrão.
* **POST** `/auth/login`
* **Body:** `{ "email": "admin@banco.com", "senha": "admin123" }`
* *Copie o Token Bearer retornado.*

### 2️⃣ Configuração do Ambiente (Como Gerente)
1.  **Cadastrar Cliente:** `POST /api/cliente` (Cria o cliente e a conta 10001).
2.  **Cadastrar Taxa:** `POST /api/taxas`
    * Crie uma regra de negócio, ex: Taxa de R$ 5,00 para pagamentos via **BOLETO**.
    ```json
    {
      "descricao": "Tarifa Fixa Boleto",
      "percentual": 0.00,
      "valorFixo": 5.00,
      "tipoPagamento": "BOLETO"
    }
    ```

### 3️⃣ Simulação de Pagamento (Como Cliente)
1.  **Login do Cliente:** `POST /auth/login` (Use os dados do cliente criado).
2.  **Simular Token IoT:** `POST /api/autenticacao/registrar`
    * Simula o dispositivo gerando o código `123456`.
3.  **Realizar Pagamento:** `POST /api/pagamento/cliente`
    * O cliente envia o boleto, o tipo **BOLETO** e o código **123456**.
    * **Resultado:** O sistema valida o token, busca a taxa de R$ 5,00 no banco, soma ao valor do boleto e debita da conta.

---

## 📂 Estrutura do Projeto

A arquitetura segue princípios de organização por responsabilidade:

```bash
src/main/java/com/conta_bancaria
├── application          # Camada de Aplicação (DTOs e Services de Orquestração)
│   ├── dto              # Objetos de Transferência de Dados (Request/Response)
│   └── service          # Lógica de fluxo e conexão entre controllers e domínio
├── domain               # Camada de Domínio (O coração do sistema)
│   ├── entity           # Entidades JPA (Cliente, Conta, Pagamento, Taxa...)
│   ├── repository       # Interfaces de acesso ao banco
│   └── PagamentoDomainService.java # Lógica pura de cálculo e regras financeiras
├── infrastructure       # Configurações (Security, Swagger, Beans)
└── ui_interface         # Camada de Entrada (Controllers REST e Handlers de Erro)
```
