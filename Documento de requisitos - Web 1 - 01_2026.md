**Desenvolvimento de software para a Web 1**  
**Professor: André Takeshi Endo (UFSCar)**  
**Estagiário: Yohan Duarte**

**Atividade AA: Sistema de PESCD**

# **Contexto**

“O Programa de Estágio Supervisionado de Capacitação Docente \- PESCD \- é destinado a aprimorar a formação de discentes de Pós-Graduação, oferecendo-lhes adequada preparação pedagógica, por meio de estágio supervisionado em atividades didáticas de graduação. Podem participar do PESCD os alunos regularmente matriculados nos cursos de pós-graduação, Mestrado ou Doutorado, da Universidade Federal de São Carlos.

No programa, o estágio PESCD é obrigatório para todos os alunos de doutorado. O objetivo é fazer com que esses alunos se aproximem das atividades de docência realizadas no âmbito do DC/UFSCar. O aluno de doutorado deve realizar o estágio PESCD por no mínimo 02 (dois) e no máximo 03 (três) semestres letivos.” 

Fonte e documentação complementar:   
[https://www.ppgcc.ufscar.br/pt-br/area-discente/exigencias-academicas/programa-de-estagio-supervisionado-de-capacitacao-docente-pescd](https://www.ppgcc.ufscar.br/pt-br/area-discente/exigencias-academicas/programa-de-estagio-supervisionado-de-capacitacao-docente-pescd)  

# **Sistema e Regras de Negócio Globais**

O Sistema tem como objetivo automatizar o workflow para a contabilização de créditos para uma oferta do PESCD de um dado programa. Uma oferta do PESCD é caracterizada como uma disciplina de um dado semestre, com alunos inscritos e a conclusão na forma de uma nota e uma frequência.  

| Regras de Negócio Globais |  |
| ----- | :---- |
| **ID** | **Descrição** |
| **RNG-1** | O sistema deve possuir diferentes tipos de perfis de usuário, com telas específicas para cada perfil e controle de acesso. Os perfis são:  Visitante (V): não precisa de login, consegue visualizar as ofertas e a quantidade de alunos inscritos na oferta em questão.  Administrador (AD): precisa de login (nome de usuário e senha), gerencia o cadastro dos usuários para perfil Secretário, e Professor. O perfil Professor Responsável é definido no momento do cadastro da oferta, e o perfil Professor Supervisor é definido no momento do envio do plano pelo aluno.  Secretário (S): precisa de login (nome de usuário e senha), consegue criar uma oferta, acompanhar o andamento da oferta e encerrar a oferta.  Aluno (AL): precisa de login (nome de usuário e senha), deve enviar a documentação necessária para obter os créditos, acompanha o status dos seus envios dentro da oferta.  Professor Supervisor (PS): precisa de login (nome de usuário e senha), é o supervisor do aluno que está fazendo o estágio. Ele deve aprovar o plano de trabalho e o relatório, e acompanhar o status dos alunos que supervisiona em uma dada oferta.  Professor Responsável (PR): precisa de login (nome de usuário e senha), é o responsável pelo andamento de uma oferta do PESCD, acompanhar o andamento e dá a aprovação final no relatório.	 |
| **RNG-2** | O sistema deve ser internacionalizado em, pelo menos, dois idiomas: português e inglês. |
| **RNG-3** | O sistema deve tratar todos os erros possíveis (cadastro duplicado, problemas técnicos, tentativa de acesso sem permissão, evitar a remoção de elementos em uso, etc) exibindo uma página de erro amigável ao usuário e registrando o erro no log. |
| **RNG-4** | O sistema deve iniciar com dados de exemplo no Banco de Dados (BD), para cada estória implementada. |
| **RNG-5** | Para uma oferta concluída, o sistema deve, em todos os perfis, permitir apenas o acesso de leitura das informações, sem opções para inserir, atualizar ou excluir dados.  |

# 

# **Visitante (V)**

## **V.01 \- COMO Visitante, EU QUERO visualizar a lista de ofertas do programa, PARA ter acesso transparente a esses dados do programa.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** |  |
|  |  |
| **Regras de Negócio** |  |
| **RN-1** | Cada oferta deve conter as seguintes informações: Nome da oferta Semestre Data de início Data de fim Professor responsável Número de alunos matriculados |
| **RN-2** | A lista deve ser ordenada pelo semestre de maneira decrescente.  |
| **RN-3** | Todo usuário não logado é considerado um visitante.  |

## 

# **Usuários com Login (U)**

## **U.01 \- COMO Administrador ou Secretário ou Aluno ou Professor Supervisor ou Professor Responsável, EU QUERO realizar o login, PARA ter acesso às funcionalidades únicas do meu perfil no sistema.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** |  |
|  |  |
| **Regras de Negócio** |  |
| **RN-1** | O login acontece por meio do nome do usuário e senha. |
| **RN-2** | Mensagens de erro típicas devem ser fornecidas para tentativas inválidas. |
| **RN-3** | O login com sucesso deve redirecionar o usuário para uma tela adequada a seu perfil. |
| **RN-4** | O sistema deve tratar adequadamente casos de acúmulo de responsabilidade, por exemplo, professor responsável e também o supervisor.  |

## 

# **Administrador (AD)** 

## **AD.01 \- COMO Administrador, EU QUERO gerenciar o cadastro de usuários, PARA manter usuários e definir acesso segundo perfis definidos.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
|  |  |
| **Regras de Negócio** |  |
| **RN-1** | O usuário possui os seguintes dados: nome completo\* e-mail\* nome de usuário\* senha\* perfil\* No caso, o perfil pode ser Administrador, Secretário, ou Professor.  |
| **RN-2** | O gerenciamento envolve operações CRUD.  |
| **RN-3** | Não é possível remover seu próprio usuário.  |
| **RN-4** | O e-mail deve ser único por usuário.  |

## 

# **Secretário (S)**

## 

## **S.01 \- COMO Secretário, EU QUERO criar uma oferta, PARA que o workflow do PESCD iniciar no semestre.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
|  |  |
| **Regras de Negócio** |  |
| **RN-1** | A oferta deve conter as seguintes informações: Nome da oferta Semestre\* Data de início\* Data de fim\* Professor responsável\* Se o nome da oferta não for preenchido, monte uma string com base no semestre.  |
| **RN-2** | A data de fim deve ser depois da data de início. |
| **RN-3** | O professor responsável deve ser selecionado da lista de professores no BD.  |
| **RN-4** | O sistema deve registrar a data e hora de criação (timestamp) e o usuário que criou a oferta. |

## 

## **S.02 \- COMO Secretário, EU QUERO adicionar alunos a uma oferta, PARA que alunos inscritos tenham acesso ao sistema.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | Ter uma oferta selecionada.  |
| **Regras de Negócio** |  |
| **RN-1** | O aluno possui os seguintes dados: nome completo\* e-mail\* nome de usuário\* senha\* |
| **RN-2** | O gerenciamento dos alunos envolve operações CRUD. Caso um aluno já exista no BD, ele pode ser adicionado à oferta. Um aluno pode estar em múltiplas ofertas.    |
| **RN-3** | É possível adicionar os alunos fazendo o upload de um arquivo CSV com a seguinte estrutura: Cabeçalho \- RA,NOME\_COMPLETO,EMAIL Cada linha seguinte representa um aluno. Usando o e-mail, verificar se o aluno existe no BD e caso sim, apenas adicioná-lo à oferta. Caso o e-mail do aluno não exista, efetue o cadastro usando o e-mail também como nome do usuário e o RA com a senha. |
|  |  |

## 

## **S.03 \- COMO Secretário, EU QUERO acompanhar as ofertas, PARA ter uma visão geral do andamento do PESCD.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | Ter pelo menos uma oferta cadastrada. |
| **Regras de Negócio** |  |
| **RN-1** | As ofertas devem ser apresentadas em uma lista com suas principais informações. As ofertas podem estar nos seguintes status: “Em andamento” \- data de início \< data atual \< data de fim “Concluída” \- Secretário encerrou a oferta “Aguardando encerramento do secretário” \- Professor responsável concluiu a oferta e secretário ainda não encerrou. “Em atraso” \- data atual \> data de fim e o oferta ainda não foi concluída.  |
| **RN-2** | Ao clicar em Detalhes de uma dada oferta, o sistema deve apresentar os alunos inscritos e o status de cada um. Um aluno pode estar nos seguintes status: “não enviado” \- quando o aluno não fez nenhum envio no sistema. “plano enviado” “plano aprovado” “documentação enviada” “relatório enviado” “relatório aprovado pelo supervisor” “concluído pelo responsável” Caso não tenha alunos inscritos, mostrar uma mensagem informando esta situação.  |
| **RN-3** | Cada aluno tem um botão “Ver Detalhes” que deve mostrar todas as informações armazenadas sobre o aluno naquela oferta, inclusive logs de mudanças de status. |

## 

## **S.04 \- COMO Secretário, EU QUERO encerrar uma oferta, PARA que os créditos sejam atribuídos aos alunos e a oferta concluída.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | A oferta em questão está no status “Aguardando encerramento do secretário”.  |
| **Regras de Negócio** |  |
| **RN-1** | Depois que o professor responsável concluiu uma oferta, um botão de ação “Encerrar” fica disponível na lista de ofertas. Tal ação só é permitida quando a oferta está no status “Aguardando encerramento do secretário”.   |
| **RN-2** | Ao clicar em encerrar, o sistema deve pedir uma confirmação, listar um conjunto de instruções, e em caso positivo, deve: Mudar o status para “Concluída” Registrar o timestamp do encerramento e o usuário |
| **RN-3** | O conjunto de instruções é um campo textual que pode ser configurado pelo administrador.  |
|  |  |

## 

# **Aluno (AL)**

## **AL.01 \- COMO Aluno, EU QUERO visualizar as ofertas, PARA que eu realize ações para obter créditos nas ofertas que estou matriculado e obtenha um status no sistema sobre o estágio.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | Estar inscrito em pelo menos uma oferta. |
| **Regras de Negócio** |  |
| **RN-1** | Cada oferta associada ao aluno deve conter as seguintes informações: Nome da oferta Semestre Data de início Data de fim Professor responsável Status da oferta |
|  |  |
|  |  |

## 

## **AL.02 \- COMO Aluno, EU QUERO enviar o plano de trabalho, PARA que o professor supervisor aprove o plano e inicie o estágio.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | Estar inscrito em uma oferta. |
| **PC-3** | Selecionar uma oferta com status “em andamento”. |
| **PC-4** | O status do aluno nesta oferta deve ser “não enviado”.  |
| **Regras de Negócio** |  |
| **RN-1** | O formulário deve conter os seguintes dados: Código da disciplina\* Nome da disciplina\* Curso da disciplina\* Professor supervisor\*  Arquivo com o plano\* |
| **RN-2** | O professor supervisor deve ser selecionado da lista de professores no BD. |
| **RN-3** | O arquivo com o plano deve ser um pdf com no máximo 5mb.  |
| **RN-4** | O envio com sucesso deve mudar o status do aluno para “plano enviado”.  |

## 

## **AL.03 \- COMO Aluno, EU QUERO enviar a documentação que ministrei aulas no ensino superior, PARA receber os créditos sem realizar o estágio.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | Estar inscrito em uma oferta. |
| **PC-3** | Selecionar uma oferta com status “em andamento”. |
| **PC-4** | O status do aluno nesta oferta deve ser “não enviado”.  |
| **Regras de Negócio** |  |
| **RN-1** | O formulário deve conter os seguintes dados: Nome da instituição onde ministrou a disciplina\* Nome da disciplina\* Curso da disciplina\* Carga horária (em horas) da disciplina\* Arquivo com a documentação comprobatória\* |
| **RN-3** | O arquivo com a documentação comprobatória deve ser um pdf com no máximo 5mb.  |
| **RN-4** | O envio com sucesso deve mudar o status do aluno para “documentação enviada”.  |

## 

## **AL.04 \- COMO Aluno, EU QUERO enviar o relatório final do estágio, PARA o professor supervisor aprovar e iniciar a finalização do estágio.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | Estar inscrito em uma oferta. |
| **PC-3** | Selecionar uma oferta com status “em andamento”. |
| **PC-4** | O status do aluno nesta oferta deve ser “plano aprovado”.  |
| **Regras de Negócio** |  |
| **RN-1** | A tela apresenta dados (leitura) sobre a oferta, o plano enviado, e mudanças de status.    O formulário deve conter os seguintes dados: Indicador de frequência\* (0 a 100%) Arquivo com o relatório\* |
| **RN-2** | O arquivo com o relatório deve ser um pdf com no máximo 5mb.  |
| **RN-4** | O envio com sucesso deve mudar o status do aluno para “relatório enviado”.  |

## 

# **Professor Supervisor (PS)**

## 

## **PS.01 \- COMO Professor Supervisor, EU QUERO visualizar as ofertas e os alunos inscritos sob minha supervisão, PARA entender o andamento dos estágios.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
|  |  |
| **Regras de Negócio** |  |
| **RN-1** | O sistema deve listar as ofertas e suas informações principais.  |
| **RN-2** | Quando existir algum aluno sob minha supervisão, o sistema deve listar também as informações deste aluno visualmente próximas a oferta.  |
| **RN-3** | Indicar junto ao aluno seu status e botões de ação, se for o caso.  |
|  |  |

## 

## **PS.02 \- COMO Professor Supervisor, EU QUERO aprovar o plano de trabalho, PARA o aluno iniciar o estágio.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | O status do aluno deve ser “plano enviado”. |
| **Regras de Negócio** |  |
| **RN-1** | O sistema deve apresentar um botão de ação “Aprovar Plano”.  |
| **RN-2** | Ao clicar na ação “Aprovar Plano”, o sistema deve apresentar além do nome do aluno, os dados do plano inseridos na estória **\[AL.02\].** Estes dados são somente para leitura.  |
| **RN-3** | O formulário tem os seguintes campos: Parecer\* O formulário também contém os botões Aprovar e Cancelar. O cancelar não faz nada e volta à tela anterior. |
| **RN-4** | Ao aprovar, o status do aluno muda para “plano aprovado”, os dados preenchidos salvos no BD, e o sistema registra o timestamp da operação.  |

## 

## **PS.03 \- COMO Professor Supervisor, EU QUERO aprovar o relatório do estágio, PARA dar andamento a finalização do estágio.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | O status do aluno deve ser “relatório enviado”. |
| **Regras de Negócio** |  |
| **RN-1** | O sistema deve apresentar um botão de ação “Aprovar Relatório”.  |
| **RN-2** | Ao clicar na ação “Aprovar Relatório”, o sistema deve apresentar além do nome do aluno, os dados do plano inseridos na estória **\[AL.02\]**, o relatório inserido na estória **\[AL.04\]**, informações sobre as mudanças de status**.** Estes dados são somente para leitura.  |
| **RN-3** | O formulário tem os seguintes campos:  Parecer\*  Indicador de frequência\* \- vem o valor preenchido do aluno, mas pode ser alterado.   Sugestão de Nota\*, das opções A, B, C, D, e E. O formulário também contém os botões Aprovar e Cancelar. O cancelar não faz nada e volta à tela anterior. |
| **RN-4** | Ao aprovar, o status do aluno muda para “relatório aprovado pelo supervisor”, os dados preenchidos salvos no BD, e o sistema registra o timestamp da operação.  |

## 

# **Professor Responsável (PR)**

## 

## **PR.01 \- COMO Professor Responsável, EU QUERO concluir o relatório do estágio de um aluno PARA dar andamento a finalização do estágio.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | O status do aluno deve ser “relatório aprovado pelo supervisor”. |
| **Regras de Negócio** |  |
| **RN-1** | O sistema deve apresentar um botão de ação “Aprovar Relatório”.  |
| **RN-2** | Ao clicar na ação “Aprovar Relatório”, o sistema deve apresentar além do nome do aluno, os dados do plano inseridos na estória **\[AL.02\]**, o relatório inserido na estória **\[AL.04\]**, os dados inseridos pelo professor supervisor na estória **\[PS.03\]**, informações sobre as mudanças de status**.** Estes dados são somente para leitura.  |
| **RN-3** | O formulário tem os seguintes campos:  Parecer\*  Frequência\* \- vem o valor preenchido do professor supervisor, mas pode ser alterado.   Nota\*, das opções A, B, C, D, e E \- vem o valor preenchido do professor supervisor, mas pode ser alterado. O formulário também contém os botões Aprovar e Cancelar. O cancelar não faz nada e volta à tela anterior. |
| **RN-4** | Ao aprovar, o status do aluno muda para “concluído pelo responsável”, os dados preenchidos salvos no BD, e o sistema registra o timestamp da operação.  |

## 

## **PR.02 \- COMO Professor Responsável, EU QUERO analisar a documentação de aulas enviada por um aluno PARA dar andamento a finalização do estágio.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | O status do aluno deve ser “documentação enviada”. |
| **Regras de Negócio** |  |
| **RN-1** | O sistema deve apresentar um botão de ação “Analisar Documentação”.  |
| **RN-2** | Ao clicar na ação “Analisar Documentação”, o sistema deve apresentar além do nome do aluno, os dados da documentação inseridos na estória **\[AL.03\]** e informações sobre as mudanças de status**.** Estes dados são somente para leitura.  |
| **RN-3** | O formulário tem os seguintes campos:  Parecer\*  Indicador de frequência\*  (0 a 100%).   Nota\*, das opções A, B, C, D, e E. O formulário também contém os botões Finalizar e Cancelar. O cancelar não faz nada e volta à tela anterior. |
| **RN-4** | Ao finalizar, o status do aluno muda para “concluído pelo responsável”, os dados preenchidos salvos no BD, e o sistema registra o timestamp da operação.  |

## 

## **PR.03 \- COMO Professor Responsável, EU QUERO encerrar uma oferta PARA que a secretaria dê andamento ao encerramento da oferta.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | A oferta selecionada deve ter todos seus alunos inscritos no status “concluído pelo responsável”. |
| **Regras de Negócio** |  |
| **RN-1** | O sistema deve apresentar, para a oferta, um botão de ação “Encerrar Oferta”.  |
| **RN-2** | Ao clicar na ação “Encerrar Oferta”, o sistema deve apresentar os dados dos alunos, se obtiveram os créditos por estágio ou por documentação, a frequência, e a nota.  Deve apresentar também algumas estatísticas como média da frequência, quantidade por créditos e por documentação e quantidade de cada nota.  |
| **RN-3** | O formulário tem os seguintes campos:  Descrição de lições aprendidas na oferta\*  O formulário também contém os botões Encerrar e Cancelar. O cancelar não faz nada e volta à tela anterior. |
| **RN-4** | Ao encerrar, o status da oferta muda para “Aguardando encerramento do secretário”, os dados preenchidos salvos no BD, e o sistema registra o timestamp da operação.  |

## 

## **PR.04 \- COMO Professor Responsável, EU QUERO acompanhar as ofertas, PARA ter uma visão geral do andamento do PESCD.**

| Pré-Condições |  |
| :---: | :---- |
| **PC-1** | Estar logado no sistema.  |
| **PC-2** | Ter pelo menos uma oferta cadastrada como professor responsável. |
| **Regras de Negócio** |  |
| **RN-1** | As informações disponíveis são as mesmas descritas na estória **\[S.03\]**. |

## 

# **Estória Surpresa**

## **U.? \- (Estória de usuário surpresa \- Obrigatório) Cada grupo deve incluir e implementar uma nova estória que seja relevante e faça sentido no contexto do sistema.**

# 

# **Priorização dos Requisitos**

Aqui apresentamos uma sugestão de como priorizar e ordenar o desenvolvimento. As estórias de prioridade ALTA são obrigatórias para consideração de nota do projeto. 

| Prior. | Cód. | Estória | Observação |
| :---: | :---: | :---- | :---- |
| ALTA | U.01 | COMO Administrador ou Secretário ou Aluno ou Professor Supervisor ou Professor Responsável, EU QUERO realizar o login, PARA ter acesso às funcionalidades únicas do meu perfil no sistema. | Define a base do sistema, controle de acesso e telas específicas de cada perfil. |
| ALTA | S.01 | COMO Secretário, EU QUERO criar uma oferta, PARA que o workflow do PESCD iniciar no semestre. | Importante porque habilita o perfil professor responsável.  |
| ALTA | S.02 | COMO Secretário, EU QUERO adicionar alunos a uma oferta, PARA que alunos inscritos tenham acesso ao sistema. | Importante porque habilita o perfil aluno.  |
| ALTA | AL.01 | COMO Aluno, EU QUERO visualizar as ofertas, PARA que eu realize ações para obter créditos nas ofertas que estou matriculado e obtenha um status no sistema sobre o estágio. |  |
| ALTA | AL.02 | COMO Aluno, EU QUERO enviar o plano de trabalho, PARA que o professor supervisor aprove o plano e inicie o estágio. | Conecta com PS.02 \-\> AL.04 \-\> PS.03 \-\> PR.01 |
| ALTA | AL.03 | COMO Aluno, EU QUERO enviar a documentação que ministrei aulas no ensino superior, PARA receber os créditos sem realizar o estágio. | Conecta com PR.02 |
| ALTA | AL.04 | COMO Aluno, EU QUERO enviar o relatório final do estágio, PARA o professor supervisor aprovar e iniciar a finalização do estágio. |  |
| ALTA | PS.02 | COMO Professor Supervisor, EU QUERO aprovar o plano de trabalho, PARA o aluno iniciar o estágio. |  |
| ALTA | PS.03 | COMO Professor Supervisor, EU QUERO aprovar o relatório do estágio, PARA dar andamento a finalização do estágio. |  |
| ALTA | PR.01 | COMO Professor Responsável, EU QUERO concluir o relatório do estágio de um aluno PARA dar andamento a finalização do estágio. | Pode habilitar PR.03 |
| ALTA | PR.02 | COMO Professor Responsável, EU QUERO analisar a documentação de aulas enviada por um aluno PARA dar andamento a finalização do estágio. | Pode habilitar PR.03 |
| MED | U.? | Estória Surpresa |  |
| MED | S.03 | COMO Secretário, EU QUERO acompanhar as ofertas, PARA ter uma visão geral do andamento do PESCD. | Basicamente a mesma que a PR.04. Pode ser tratada pelo mesmo Dev. |
| MED | S.04 | COMO Secretário, EU QUERO encerrar uma oferta, PARA que os créditos sejam atribuídos aos alunos e a oferta concluída. |  |
| MED | PS.01 | COMO Professor Supervisor, EU QUERO visualizar as ofertas e os alunos inscritos sob minha supervisão, PARA entender o andamento dos estágios. |  |
| MED | PR.03 | COMO Professor Responsável, EU QUERO encerrar uma oferta PARA que a secretaria dê andamento ao encerramento da oferta. | Conecta com S.04. |
| MED | PR.04 | COMO Professor Responsável, EU QUERO acompanhar as ofertas, PARA ter uma visão geral do andamento do PESCD. | Basicamente a mesma que a S.03. Pode ser tratada pelo mesmo Dev. |
| BAIXA | AD.01 | COMO Administrador, EU QUERO gerenciar o cadastro de usuários, PARA manter usuários e definir acesso segundo perfis definidos. | Os usuários e seus perfis podem ser inseridos direto no BD para realizar os testes, assim este CRUD tem baixa prioridade.  |
| BAIXA | V.01 | COMO Visitante, EU QUERO visualizar a lista de ofertas do programa, PARA ter acesso transparente a esses dados do programa. |  |

# 

# **Pontos Importantes**

**Dúvidas sobre o documento:** Caso haja alguma dúvida sobre os requisitos, não faça suposições\!\! Envie sua pergunta para o estagiário. De acordo com essas dúvidas, o documento poderá ser atualizado para incluir novos esclarecimentos.   
