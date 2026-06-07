# PESCD — Programa de Estágio Supervisionado de Capacitação Docente

Sistema web para automação do workflow de créditos de estágio para alunos de pós-graduação da UFSCar.

**Stack:** Spring Boot 3 · Spring MVC · Spring Security · Spring Data JPA · Thymeleaf · MySQL

---

## Banco de dados

A aplicação está configurada para usar MySQL.

Valores padrão:

- `DB_HOST=localhost`
- `DB_PORT=3306`
- `DB_NAME=pescd`
- `DB_USERNAME=root`
- `DB_PASSWORD=root`

Exemplo de criação do banco:

```sql
CREATE DATABASE pescd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Exemplo para rodar a aplicação com variáveis de ambiente:

```bash
DB_HOST=localhost \
DB_PORT=3306 \
DB_NAME=pescd \
DB_USERNAME=root \
DB_PASSWORD=root
```

Configuração JPA atual:

- `spring.jpa.hibernate.ddl-auto=update`
- o schema é criado/atualizado automaticamente pelo Hibernate
- os dados deixam de ser apagados ao reiniciar a aplicação

---

## Changelog

| Data | Autor | Descrição                                                                         |
|------|-------|-----------------------------------------------------------------------------------|
| 24/05/2026 | Maria Luiza Fernandes Prestes Cesar | first commit                                                                      |
| 24/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona entidades e repositórios do domínio de estágio                           |
| 30/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona login, crud de usuarios e listagem de ofertas                            |
| 30/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona README e documento de requisitos                                         |
| 30/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona linter checkstyle                                                        |
| 30/05/2026 | Maria Luiza Fernandes Prestes Cesar | adiciona favicon                                                                  |
| 01/06/2026 | Francini Stefany Assandre | PR.03: Tela e controle de solicitação de encerramento da oferta.                  |
| 01/06/2026 | Francini Stefany Assandre | S.04: Lógica de homologação do encerramento e atualização do status.              |
| 01/06/2026 | Francini Stefany Assandre | Criação da página de ofertas tendo status e botões condicionais |
| 03/06/2026 | Miguel | Migração de persistência de H2 em memória para MySQL e integra fluxo do aluno ao PESCD com upload de PDF e logs de status.|
| 06/06/2026 | Miguel | Corrige segurança por perfil, redirecionamentos pós-login, massa idempotente de testes e erro 500 na tela do secretário. |
| 06/06/2026 | Nicole Brito Cardoso | Implementa funcionalidades dos perfis Professor Supervisor e Professor Responsável, incluindo ajustes e integração dos fluxos. |

