# 🎬 Screenmatch 

Status - Em Desenvolvimento

O **Screenmatch** é uma aplicação Java desenvolvida durante o curso **Alura + Oracle Next Education**, que simula uma plataforma de streaming de séries.  

O projeto utiliza **Spring Boot**, **Spring Data JPA**, consumo de API externa e conceitos de **orientação a objetos**, oferecendo uma interface via console para gerenciamento, busca e listagem de séries e episódios.

---

## 🚀 Tecnologias e Ferramentas

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- Banco de dados PostgreSQL
- Consumo de API externa (OMDb API)
- Maven (gerenciamento de dependências)
- IntelliJ IDEA (IDE recomendada)

---

## 📋 Arquitetura do Projeto

O projeto está estruturado seguindo boas práticas de desenvolvimento Java com Spring Boot, incluindo:

- **Entidades JPA**: `Serie` e `Episodios` com mapeamento ORM para o banco de dados.
- **Repository**: Interfaces que estendem Spring Data JPA para persistência e consultas customizadas.
- **Consumo de API externa**: Classes que consomem a OMDb API para obter dados reais das séries.
- **Classe principal (`ScreenmatchApplication`)**: inicia a aplicação Spring Boot e executa o menu interativo via `CommandLineRunner`.
- **Classe `Principal`**: lógica do menu via console para buscas, listagens e manipulações.

---

## 📌 Funcionalidades

- Buscar séries na OMDb API e salvar no banco local.
- Buscar episódios de séries, incluindo filtragem por título, ator, gênero, número de temporadas, avaliação, data de lançamento.
- Listar séries e episódios armazenados.
- Consultas complexas com critérios personalizados (top 5 séries, episódios a partir de determinada data, etc).
- Menu interativo no console para facilitar a navegação e uso.

---
🎯 Objetivos do projeto
Praticar o consumo de APIs externas em Java.

Desenvolver aplicações com Spring Boot e Spring Data JPA.
Aplicar conceitos sólidos de Orientação a Objetos e design de software.
Trabalhar com persistência de dados usando banco relacional.
Construir interfaces interativas via terminal.
---

📚 Curso
Projeto desenvolvido durante o curso Alura + Oracle Next Education.

👩‍💻 Autora
Renata Fombeli
