# FisioCare — roteiro de testes da atualização

Este repositório é o **ambiente de teste**. O repositório `karenads/gerenciador-fisio` não foi alterado.

## Preparação

1. Inicie o PostgreSQL configurado em `src/main/resources/application.properties`.
2. Use um banco de teste limpo para avaliar o **primeiro cadastro**. Não apague dados de produção.
3. Inicie o backend Spring Boot (`./mvnw spring-boot:run`).
4. Para o JavaFX, execute o projeto em `front/back`.
5. Para o Next.js, execute `npm ci` e `npm run dev` dentro de `front`.

## Primeiro administrador (JavaFX)

- Na janela inicial, informe a chave local `1234` e deixe e-mail e senha vazios.
- Abra o cadastro de administrador e preencha os dados.
- No campo **Chave do admin**, utilize o valor configurado no backend na propriedade `spring.secret`. **Não exponha essa chave em prints ou commits.**
- Essa operação só funciona quando não há usuários cadastrados.
- Depois de criar o administrador, faça login no Next.js em `/login` com e-mail e senha.

## Roles e permissões

- `ROLE_ADMIN`: gerencia usuários, pacientes, tratamentos e sessões.
- `ROLE_FISIOTERAPEUTA`: acessa pacientes sob sua responsabilidade e seus tratamentos e sessões.
- `ROLE_RECEPCAO`: acessa cadastro e consulta de pacientes sob sua responsabilidade.
- Usuários antigos com `ROLE_USER` recebem a mesma permissão de recepção durante a transição.

Para testar as três roles:
1. Entre como administrador e cadastre dois usuários (inicialmente Recepção).
2. Na lista de usuários, altere o perfil de um deles para Fisioterapeuta.
3. Faça login como cada usuário e confira as rotas bloqueadas (403).
4. Cadastre um paciente com o usuário A, saia e entre com o usuário B.
5. Verifique que a lista de pacientes de B não contém os dados de A e que GET/PUT/DELETE de um ID de A não funcionam para B.

## Relacionamentos

- Paciente é associado automaticamente ao usuário autenticado.
- Tratamento precisa receber um `pacienteId` existente e autorizado.
- Sessão precisa receber um `tratamentoId` existente e autorizado.
- O ID do responsável **não** vem do frontend; o Service obtém o usuário autenticado.

## Segurança e Redux

- `POST /auth/login` gera token JWT e armazena o token no banco.
- `POST /auth/logout` revoga o token no banco.
- O token no navegador permanece no Redux (em memória, sem localStorage/cookies).
- Após recarregar a página, é necessário fazer login novamente.
- Uma requisição com token inválido/revogado deve retornar 401 e executar logout no frontend.
- O frontend trata 403 como acesso negado, sem apagar a sessão.
- GET /usuarios exige administrador; as outras rotas dependem da role.

## Integração externa

- No formulário de Paciente, informe um CEP e clique em **Buscar endereço**.
- A tela consulta `GET /pacientes/cep/{cep}` no backend, que chama o ViaCEP.
- O endereço é preenchido no formulário e pode ser alterado manualmente.

## Atenção

O processo precisa ser testado localmente com JavaFX, Spring Boot, Next.js e PostgreSQL. A atualização introduz relações JPA; recomenda-se usar **banco de teste** e revisar o SQL de migração antes de aplicá-lo a dados existentes. Não apague a tabela de usuários do seu projeto principal.

A implementação preserva o fluxo de autenticação por e-mail/senha do projeto de referência e as classes do Gerenciador Fisio. A proteção das senhas no banco merece uma revisão de segurança antes de uso em produção.
