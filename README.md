<img width="187" height="43" alt="logo" src="https://github.com/user-attachments/assets/67dec6da-7341-49e3-9d24-d8129331b9cc" />

# 🛒 Estela - Backend

O **Estela** é uma plataforma de e-commerce voltada para o mercado de jogos digitais, visando suprir as necessidades do público brasileiro com foco em segurança na transação e usabilidade.

Este repositório contém o código-fonte do **Backend** da aplicação, responsável por toda a lógica de negócio, persistência de dados e API de comunicação.

## ✨ Sobre o Projeto

O backend do Estela é o coração da aplicação, gerenciando o catálogo de jogos, usuários, transações e autenticação. Ele expõe uma API REST para comunicação com o frontend.

| Status Atual | Repositório do Frontend |
| :----------: | :----------------------: |
| 🟡 Em Desenvolvimento | [estela-frontend](https://github.com/DaviCalo/estela-frontend) |

## 💻 Tecnologias e Arquitetura

O backend foi construído com uma arquitetura Java tradicional, utilizando as seguintes tecnologias:

  * **Linguagem:** Java 11+
  * **Servidor Web:** **Servlets** (para lidar com as requisições HTTP e roteamento).
  * **Persistência:** **PostgreSQL** (como banco de dados relacional).
  * **Conexão DB:** **JDBC** (Java Database Connectivity).
  * **Serialização/Desserialização:** **Gson** (Biblioteca do Google para conversão de objetos Java em JSON e vice-versa).

## 🗄️ Estrutura do Banco de Dados

Utilizamos o PostgreSQL para persistir os dados. A criação do esquema e das tabelas é definida em um script SQL dedicado.

### Script de Criação

O script para inicializar o banco de dados (criação de tabelas, índices, etc.) está localizado na seguinte pasta:

  * **Localização:** `src/main/resources/scriptssql/`

> **Passo Essencial:** Antes de executar a aplicação, você deve executar o(s) script(s) SQL nesta pasta para garantir que o banco de dados esteja pronto.

## ⚙️ Pré-requisitos

Para configurar e executar este projeto de backend, você precisará:

1.  **JDK 11 ou superior** instalado.
2.  Um **Servidor de Aplicação Web** que suporte Servlets (como **Apache Tomcat**).
3.  Uma instância do **PostgreSQL** rodando e acessível.
4.  Um editor de código (e.g., VS Code, IntelliJ IDEA).

## 🚀 Como Executar

Siga os passos abaixo para clonar, configurar o banco de dados e implantar a aplicação.

### 1\. Clonando o Repositório

```bash
git clone https://github.com/DaviCalo/estela-backend.git
cd estela-backend
```

### 2\. Configuração do Banco de Dados

1.  **Crie o banco de dados** no seu servidor PostgreSQL (`CREATE DATABASE estela;`).
2.  **Execute o(s) script(s) SQL** da pasta `src/main/resources/scriptssql/` no banco de dados recém-criado.
3.  **Configure a conexão JDBC** em seu código Java com as credenciais (URL, usuário e senha) do seu PostgreSQL.

### 3\. Build e Deploy

1.  **Build do Projeto:** Se estiver usando Maven, compile e empacote a aplicação em um arquivo `.war` (Ex: `mvn clean install`).
2.  **Deploy:** Implante o arquivo `.war` gerado (ou a estrutura de projeto) no seu **Servidor Tomcat** ou equivalente.
3.  **Inicie o Servidor:** Inicie (ou reinicie) o servidor de aplicação.

A API estará acessível no endpoint definido pelo seu servidor (ex: `http://localhost:<PORTA_DO_SEU_SERVIDOR>/estela-backend/api/`).

## 🤝 Contribuições

Este projeto está em desenvolvimento e é parte de um esforço educacional/pessoal. Sugestões e *pull requests* são bem-vindos.

## 📄 Modelo Entidade-Relacional (MER)

Para referência do banco de dados e estrutura de dados, o Modelo Entidade-Relacional (MER) pode ser consultado na pasta compartilhada:

  * **MER:** [Pasta Google Drive](https://drive.google.com/drive/folders/12gjjoFcI_ZeQSz4jaWWYUB2FkQNZXMsA)
