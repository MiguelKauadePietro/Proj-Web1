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
| 01/06/2026 | Francini Stefany Assandre | Integração: Ajustes no AlunoOfertaRepositorio e regras do Spring Security.        |
| 01/06/2026 | Francini Stefany Assandre | Frontend: Estilização dos badges de status e botões condicionais em ofertas.html. |
| 03/06/2026 | Miguel | Migração de persistência de H2 em memória para MySQL e integra fluxo do aluno ao PESCD com upload de PDF e logs de status.|

---

## Ajustes de estabilização para testes

Nesta rodada foram aplicadas apenas correções bloqueantes para permitir o teste da aplicação com segurança, sem implementar as estórias ainda pendentes da Pessoa 4.

### O que foi ajustado

- Correção das regras de segurança por perfil:
  - `/admin/**` exige `ADMINISTRADOR`
  - `/secretario/**` exige `SECRETARIO`
  - `/professor/**` exige `PROFESSOR`
  - `/aluno/**` exige `ALUNO`
  - `/`, `/ofertas`, `/login` e assets públicos continuam acessíveis para visitante
- Correção do redirecionamento pós-login:
  - `ADMINISTRADOR` redireciona para `/admin/usuarios`
  - `ALUNO` redireciona para `/aluno/ofertas`
  - `SECRETARIO` redireciona para `/secretario/ofertas`, com rota existente que reaproveita a listagem pública
  - `PROFESSOR` redireciona para `/professor/ofertas`, que redireciona para `/ofertas`
- Ajuste do seeder para massa de dados idempotente:
  - mantém/cria usuários de teste sem duplicação
  - corrige a senha da secretaria para `secretaria123`
  - garante a oferta principal `Estágio Docente – Computação 2025/1`
  - garante vínculos únicos para `carlospereira`, `mariaaluna` e `pedrorelatorio`
  - deixa `pedrorelatorio` em `PLANO_APROVADO` com `PlanoTrabalho` mínimo vinculado
  - cria ofertas auxiliares nos status `CONCLUIDA`, `AGUARDANDO_ENCERRAMENTO` e `EM_ATRASO`
- Correção do erro 500 em `/secretario/ofertas/{id}/alunos`:
  - ajuste de tratamento para oferta inexistente
  - ajuste defensivo nos templates para dados nulos de professor responsável

### Escopo preservado

- Não foi implementada a parte da Pessoa 4
- Não foram implementadas as estórias `PS.01`, `PS.02`, `PS.03`, `PR.01`, `PR.02` e `PR.04`
- Não houve alteração de arquitetura
- Não houve remoção de código existente
