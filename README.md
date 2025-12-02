# 🏦 API Bancária com Pagamentos IoT & Taxas Dinâmicas

Uma plataforma bancária moderna, construída com DDD, segurança JWT, validação IoT simulada e regras financeiras totalmente dinâmicas.

---

## ⚡ Visão Geral

Esta API fornece um ecossistema bancário completo, incluindo:

- Domain-Driven Design (DDD)
- Segurança com JWT
- Tokens IoT simulados (2FA)
- Cálculo dinâmico de taxas por tipo de pagamento
- Operações financeiras transacionais e seguras

---

## 🧱 Principais Recursos

### 👤 Para Clientes
- Cadastro com criação automática de conta
- Depósito, saque e transferência
- Pagamento de contas (PIX, BOLETO, serviços)
- Autenticação 2FA via token IoT simulado

### 👔 Para Gerentes
- CRUD de clientes
- Cadastro e gestão de taxas financeiras
- Taxas percentuais e fixas por tipo de pagamento
- Relatórios de contas e pagamentos

### 🔐 Segurança
- Autenticação JWT
- Tokens IoT para operações sensíveis
- Transações atômicas garantindo integridade

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Finalidade |
|-----------|------------|
| **Java 21** | Linguagem principal |
| **Spring Boot 3.3.5** | Backend e IoC |
| **Spring Security + JWT** | Segurança |
| **H2 Database** | Banco em memória |
| **SpringDoc OpenAPI** | Documentação |
| **Maven** | Build e dependências |
| **Lombok** | Código mais limpo |

---

## 🚀 Como Executar

### ✔ Requisitos
- JDK 21  
- Git  

### ▶ Passos
```bash
git clone https://github.com/seu-usuario/conta-bancaria.git
cd conta-bancaria
