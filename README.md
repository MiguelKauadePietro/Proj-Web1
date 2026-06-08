# PESCD — Programa de Estágio Supervisionado de Capacitação Docente

Sistema web para automação do workflow de créditos de estágio para alunos de pós-graduação da UFSCar.

**Stack:** Spring Boot 3 · Spring MVC · Spring Security · Spring Data JPA · Thymeleaf · MySQL

---

## Como executar

### Pré-requisitos (Linux e Windows)

1. **JDK 21** instalado (`java -version` deve mostrar a versão 21).
2. **Docker** + **Docker Compose** (no Windows, instale o **Docker Desktop** e deixe-o aberto/rodando).
3. **Maven** instalado (necessário para os comandos `make` no Linux/macOS). No Windows use o Maven Wrapper (`mvnw.cmd`) já incluído no projeto.

> O banco MySQL roda em um container Docker. A aplicação cria o schema e popula os dados de teste automaticamente na primeira execução.

### Linux / macOS (com `make`)

O `Makefile` cuida de subir o banco e rodar a aplicação em um único comando:

```bash
make run
```

Esse comando sobe o banco (MySQL + phpMyAdmin), espera o MySQL ficar pronto e inicia a aplicação. Pronto — acesse http://localhost:8080.

Outros atalhos disponíveis:

| Comando | O que faz |
|---------|-----------|
| `make run` | Sobe o banco e inicia a aplicação |
| `make db-up` | Sobe só o banco (MySQL + phpMyAdmin) |
| `make db-down` | Para o banco |
| `make db-reset` | Apaga os dados e sobe o banco do zero |
| `make db-logs` | Acompanha os logs do banco |
| `make build` | Compila o projeto |
| `make test` | Roda os testes |
| `make lint` | Roda o Checkstyle |
| `make clean` | Limpa os artefatos de build |

### Windows (PowerShell ou CMD)

No Windows o `make` não está disponível por padrão, então rode os comandos diretamente:

```powershell
REM 1. Subir o banco (MySQL + phpMyAdmin) — Docker Desktop precisa estar rodando
docker compose -f docker/docker-compose.yml up -d

REM 2. Rodar a aplicação (use o wrapper .cmd)
mvnw.cmd spring-boot:run
```

Acesse http://localhost:8080.

### Acessando

| Recurso | URL | Credenciais |
|---------|-----|-------------|
| Aplicação | http://localhost:8080 | ver tabela de usuários abaixo |
| phpMyAdmin (gerenciar o banco) | http://localhost:8081 | servidor `mysql`, usuário `root`, senha `root` |

Para **parar** o banco: `make db-down` (ou `docker compose -f docker/docker-compose.yml down` no Windows)
Para **zerar** o banco (apaga os dados): `make db-reset` (ou `docker compose -f docker/docker-compose.yml down -v` no Windows)

### Usuários de teste

O login é feito pelo **usuário** (não pelo e-mail). Todos já vêm cadastrados:

| Perfil | Usuário | Senha |
|--------|---------|-------|
| Administrador | `admin` | `admin123` |
| Secretário | `secretaria` | `secretaria123` |
| Professor | `joaosilva` | `professor123` |
| Aluno | `carlospereira` | `aluno123` |

> Há mais alunos de teste em diferentes etapas do fluxo (todos com senha `aluno123`): `mariaaluna`, `pedrorelatorio`, `anaplano`, `diegodoc`, `brunorelatorio`, `clararelatorio`, `lucasconcluido`, `beatrizconcluida`, `rafaelaguardando`.

---

## Banco de dados

A aplicação está configurada para usar MySQL (provido via Docker).

Valores padrão (já configurados em `application.properties`):

- `DB_HOST=localhost`
- `DB_PORT=3307`
- `DB_NAME=pescd`
- `DB_USERNAME=root`
- `DB_PASSWORD=root`

> A porta **3307** no host é mapeada para a **3306** do container, evitando conflito com um MySQL já instalado localmente.
---

## Changelog

| Data | Autor                               | Descrição                                                                                                                      |
|------|-------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| 24/05/2026 | Maria Luiza Fernandes Prestes Cesar | first commit                                                                                                                   |
| 24/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona entidades e repositórios do domínio de estágio                                                                        |
| 30/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona login, crud de usuarios e listagem de ofertas                                                                         |
| 30/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona README e documento de requisitos                                                                                      |
| 30/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona linter checkstyle                                                                                                     |
| 30/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona favicon                                                                                                               |
| 01/06/2026 | Francini Stefany Assandre           | PR.03: Tela e controle de solicitação de encerramento da oferta.                                                               |
| 01/06/2026 | Francini Stefany Assandre           | S.04: Lógica de homologação do encerramento e atualização do status.                                                           |
| 01/06/2026 | Francini Stefany Assandre           | Criação da página de ofertas com botões estilizados e badges estilizados                                                       |
| 03/06/2026 | Miguel                              | Migração de persistência de H2 em memória para MySQL e integra fluxo do aluno ao PESCD com upload de PDF e logs de status.     |
| 06/06/2026 | Miguel                              | Corrige segurança por perfil, redirecionamentos pós-login, massa idempotente de testes e erro 500 na tela do secretário.       |
| 06/06/2026 | Nicole Brito Cardoso                | Implementa funcionalidades dos perfis Professor Supervisor e Professor Responsável, incluindo ajustes e integração dos fluxos. |
| 07/06/2026 | Miguel Kauã de Pietro               | Refinamento do fluxo do professor com validação de permissões e status, visualização segura de PDFs, resumo para encerramento de ofertas e exibição de frequência/nota nas telas. | 
| 07/06/2026 | Maria Luiza Fernandes Prestes Cesar | Instruções de execução para Linux e Windows no README e imagem multi-arch do phpMyAdmin no Docker.                              |

