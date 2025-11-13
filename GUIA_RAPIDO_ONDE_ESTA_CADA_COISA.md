# 🚀 GUIA RÁPIDO - ONDE ESTÁ CADA COISA?

## 📂 ESTRUTURA DE PASTAS

```
src/main/
├── java/
│   └── com/empresa/agendamento/
│       ├── models/                  ← As 3 CLASSES PRINCIPAIS
│       │   ├── UsuarioModel.java
│       │   ├── ResourceModel.java
│       │   └── ReservationModel.java
│       │
│       ├── dtos/                    ← CONVERSORES DE DADOS
│       │   ├── LoginDto.java        (só email + senha)
│       │   ├── UsuarioDto.java      (usuário sem senha)
│       │   ├── UsuarioSessaoDto.java (só ID + nome pra sessão)
│       │   └── ResourceDto.java
│       │
│       ├── controller/              ← RECEPCIONISTAS
│       │   ├── LoginController.java (login/logout)
│       │   ├── HomeController.java  (página inicial)
│       │   ├── UsuarioCadastroController.java (novo usuário)
│       │   ├── UsuarioListaController.java    (listar usuários)
│       │   ├── UsuarioAtualizarController.java (editar usuário)
│       │   ├── UsuarioExcluirController.java  (deletar usuário)
│       │   └── ... (Controllers de Recurso e Reserva)
│       │
│       ├── service/                 ← O CÉREBRO
│       │   ├── UserService.java     (valida e controla usuários)
│       │   ├── ResourceService.java (valida e controla recursos)
│       │   └── ReservationService.java (valida e controla reservas)
│       │
│       ├── repository/              ← O ARQUIVO (Banco)
│       │   ├── UserRepository.java      (busca no banco)
│       │   ├── ResourceRepository.java  (busca no banco)
│       │   └── ReservationRepository.java (busca no banco)
│       │
│       ├── sessao/                  ← GERENCIA QUEM TÓPOLOGADO
│       │   ├── ControleSessao.java (registra/obtém/encerra sessão)
│       │   ├── FiltroAutenticacao.java (filtro de segurança)
│       │   └── FiltroConfig.java (configura os filtros)
│       │
│       └── SistemaAgendamentoApplication.java (PONTO DE ENTRADA)
│
└── resources/
    ├── templates/
    │   ├── index.html            (página inicial)
    │   ├── login.html            (formulário de login)
    │   ├── layout/               (layouts reutilizáveis)
    │   ├── usuarios/             (páginas de usuários)
    │   │   ├── cadastro.html
    │   │   ├── lista.html
    │   │   └── atualizar.html
    │   ├── recursos/             (páginas de recursos)
    │   └── reservas/             (páginas de reservas)
    │
    ├── application.properties    (configurações da app)
    └── data.sql                  (dados iniciais do banco)
```

---

## 🎯 O QUE CADA ARQUIVO FAZ?

### 📌 MODELS (As 3 tabelas principais)

| Arquivo                 | O que é                | Tem                                                                 |
| ----------------------- | ---------------------- | ------------------------------------------------------------------- |
| `UsuarioModel.java`     | Um funcionário         | ID, Nome, Email, Senha, Matrícula, DataNasc, Reservas               |
| `ResourceModel.java`    | Sala, equipamento, etc | ID, Descrição, Tipo, DiasSemana, DataIni/Fim, HoraIni/Fim, Reservas |
| `ReservationModel.java` | Um agendamento         | ID, Usuário, Recurso, Data, HoraIni/Fim, DataCancelamento           |

### 🔄 DTOs (Conversores de dados)

| Arquivo                 | Usa em         | Por que DTO?                          |
| ----------------------- | -------------- | ------------------------------------- |
| `LoginDto.java`         | Login          | Só precisa email + senha              |
| `UsuarioDto.java`       | Cadastro/Lista | Não manda senha pelo HTML (segurança) |
| `UsuarioSessaoDto.java` | Sessão         | Só ID + nome (tá leve!)               |

### 🎪 CONTROLLERS (Recepcionistas)

| Arquivo                           | Quando é chamado              | O que faz                          |
| --------------------------------- | ----------------------------- | ---------------------------------- |
| `LoginController.java`            | URL: `/login` (GET/POST)      | Mostra form login ou valida        |
| `HomeController.java`             | URL: `/` (GET)                | Mostra página inicial              |
| `UsuarioCadastroController.java`  | URL: `/usuariocadastro`       | Mostra form cadastro ou salva novo |
| `UsuarioListaController.java`     | URL: `/usuariolista`          | Mostra tabela com todos            |
| `UsuarioAtualizarController.java` | URL: `/usuarioatualizar/{id}` | Mostra form preenchido ou atualiza |
| `UsuarioExcluirController.java`   | URL: `/usuarioexcluir/{id}`   | Deleta um usuário                  |

### 🧠 SERVICES (O cérebro)

| Arquivo                   | Métodos principais                                                             | O que valida                                        |
| ------------------------- | ------------------------------------------------------------------------------ | --------------------------------------------------- |
| `UserService.java`        | listarTodos(), buscarPorId(), salvar(), atualizar(), excluir(), validarLogin() | Email único? Senha tem 5+ char? Matrícula única?    |
| `ResourceService.java`    | CRUD de recursos                                                               | Tipo válido? Datas fazem sentido?                   |
| `ReservationService.java` | CRUD de reservas                                                               | Conflitos de horário? Recurso disponível nesse dia? |

### 💾 REPOSITORIES (Banco de dados)

| Arquivo                      | Métodos especiais                                                          | Usa para                                         |
| ---------------------------- | -------------------------------------------------------------------------- | ------------------------------------------------ |
| `UserRepository.java`        | findByEmail(), findByEmailAndSenha(), existsByEmail(), existsByMatricula() | Login, validação de duplicata                    |
| `ResourceRepository.java`    | (só usa métodos básicos)                                                   | CRUD simples                                     |
| `ReservationRepository.java` | findConflitosReserva()                                                     | Detectar reservas que conflitam no mesmo horário |

### 🔐 SESSÃO (Segurança)

| Arquivo                   | Métodos                          | O que faz                                         |
| ------------------------- | -------------------------------- | ------------------------------------------------- |
| `ControleSessao.java`     | registrar(), obter(), encerrar() | Guarda/pega/apaga dados do usuário logado         |
| `FiltroAutenticacao.java` | doFilter()                       | Checa se usuário tá logado antes de deixar entrar |
| `FiltroConfig.java`       | -                                | Configura em quais URLs o filtro deve ativar      |

---

## 🔄 FLUXO RESUMIDO DE CADA OPERAÇÃO

### 1️⃣ LOGIN

```
User digita email/senha → /login (POST)
→ LoginController.login()
→ UserService.validarLogin()
→ UserRepository.findByEmailAndSenha()
→ Banco retorna usuário
→ ControleSessao.registrar() guarda na sessão
→ Redireciona pra /home
```

### 2️⃣ CADASTRAR NOVO USUÁRIO

```
User clica novo → /usuariocadastro (GET)
→ LoginController.mostrarFormulario()
→ Mostra form vazio
→ User preenche e envia → /usuariocadastro (POST)
→ UsuarioCadastroController.salvar()
→ UserService.salvar() VALIDA
→ UserRepository.save() salva no banco
→ Redireciona pra /usuariolista
```

### 3️⃣ LISTAR USUÁRIOS

```
User clica listar → /usuariolista (GET)
→ UsuarioListaController.listar()
→ UserService.listarTodos()
→ UserRepository.findAll() busca todos do banco
→ Service converte Models → DTOs
→ Controller passa pra View (HTML)
→ HTML faz loop mostra tabela
```

### 4️⃣ EDITAR USUÁRIO

```
User clica editar (ID 5) → /usuarioatualizar/5 (GET)
→ UsuarioAtualizarController.mostrarFormulario(5)
→ UserService.buscarPorId(5)
→ UserRepository.findById(5) busca no banco
→ Service converte Model → DTO
→ Controller passa pra View já PREENCHIDO
→ User muda dados e envia → /usuarioatualizar/5 (POST)
→ UsuarioAtualizarController.atualizar()
→ UserService.atualizar() VALIDA
→ UserRepository.save() atualiza no banco
→ Redireciona pra /usuariolista
```

### 5️⃣ DELETAR USUÁRIO

```
User clica deletar (ID 5) → /usuarioexcluir/5 (GET)
→ UsuarioExcluirController.excluir(5)
→ UserService.excluir(5)
→ UserRepository.deleteById(5) apaga do banco
→ Redireciona pra /usuariolista
```

---

## 🔍 ONDE ENCONTRAR COISA ESPECÍFICA?

### ❓ "Onde valida se o email é duplicado?"

→ `UserService.java` método `validarUsuario()` → `userRepository.existsByEmail()`

### ❓ "Onde faz o login?"

→ `LoginController.java` → `UserService.validarLogin()` → `UserRepository.findByEmailAndSenha()`

### ❓ "Onde protege pra só deixar logado acessar?"

→ `FiltroAutenticacao.java` → `ControleSessao.obter()` → se null redireciona pro login

### ❓ "Onde lista todos os usuários?"

→ `UsuarioListaController.java` → `UserService.listarTodos()` → `UserRepository.findAll()`

### ❓ "Onde converte Model em DTO?"

→ `UserService.java` método `converterParaDTO()` (é privado!)

### ❓ "Onde salva no banco de dados?"

→ `UserRepository.save()` (ou qualquer Repository)

### ❓ "Onde tá a tabela do banco?"

→ Não tá aqui! Tá no banco de dados! Mas a estrutura tá em `models/`

---

## 🎓 CICLO DE VIDA DE UMA REQUISIÇÃO

```
1. Usuário digita /login na URL
   ↓
2. Navegador envia requisição HTTP pro servidor
   ↓
3. [FILTRO] FiltroAutenticacao checa: "tá logado?"
   → Se não → redireciona pra /login
   → Se sim → deixa passar
   ↓
4. Spring procura qual Controller vai tratar essa URL
   → Encontra: LoginController tem @GetMapping("/login")
   ↓
5. Chama: LoginController.viewLogin(Model model)
   ↓
6. Controller cria UsuarioDto vazio
   ↓
7. Controller coloca no Model: model.addAttribute("usuario", vazio)
   ↓
8. Controller retorna "login" (nome da View/HTML)
   ↓
9. Spring procura o arquivo: src/resources/templates/login.html
   ↓
10. Thymeleaf transforma o HTML com os dados do Model
    → <input th:field="${usuario.email}"/> = cria um input pra email
    ↓
11. HTML é renderizado e enviado pro navegador
    ↓
12. Usuário VÊ a página com o formulário!
```

---

## 📊 QUADRO COMPARATIVO

| Operação      | Model        | DTO        | Controller                 | Service                                               | Repository                              |
| ------------- | ------------ | ---------- | -------------------------- | ----------------------------------------------------- | --------------------------------------- |
| **Login**     | UsuarioModel | LoginDto   | LoginController            | UserService.validarLogin()                            | UserRepository.findByEmailAndSenha()    |
| **Criar**     | UsuarioModel | UsuarioDto | UsuarioCadastroController  | UserService.salvar() + validarUsuario()               | UserRepository.save() + existsByEmail() |
| **Ler**       | UsuarioModel | UsuarioDto | UsuarioListaController     | UserService.listarTodos()                             | UserRepository.findAll()                |
| **Atualizar** | UsuarioModel | UsuarioDto | UsuarioAtualizarController | UserService.atualizar() + validarAtualizacaoUsuario() | UserRepository.findById() + save()      |
| **Deletar**   | UsuarioModel | (não usa)  | UsuarioExcluirController   | UserService.excluir()                                 | UserRepository.deleteById()             |

---

## 🧪 PRÓXIMOS PASSOS

Agora que você entendeu tudo, você consegue:

1. ✅ Criar um novo **Controller** pra gerenciar outra coisa
2. ✅ Adicionar um novo **método no Service** pra validar algo
3. ✅ Escrever uma **Query customizada no Repository**
4. ✅ Criar um novo **Model** pra uma nova tabela
5. ✅ Entender **por que** cada coisa tá onde tá

**Dica**: Quando tiver dúvida, use a busca (Ctrl+F) e procure por keywords tipo "findAll", "@PostMapping", "validar", etc!

---

## 📞 RESUMO EM UMA LINHA

```
Usuário entra na URL → Spring chama Controller → Controller chama Service
→ Service valida tudo → Service chama Repository → Repository busca/salva no Banco
→ Tudo volta pra View (HTML) → HTML mostra pro usuário!
```

Simples assim! 🎉
