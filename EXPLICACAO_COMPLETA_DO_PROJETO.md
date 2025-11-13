# 📚 EXPLICAÇÃO COMPLETA DO PROJETO - SISTEMA DE AGENDAMENTO

> **Atenção**: Se você NUNCA viu programação na vida, leia este documento primeiro! Ele explica tudo de forma bem simples e informal.

---

## 🎯 O que é este projeto?

Imagina que você tem uma **sala de reunião** e muitos **funcionários** que querem usar ela. Como você controla quem pode usar quando?

Esse projeto é um **"agenda online"** pra isso! Ele permite que:

- ✅ Funcionários façam **login** (entrem no app)
- ✅ Funcionários vejam quais **recursos** (salas, equipamentos) tão disponíveis
- ✅ Funcionários **reservem** um recurso num dia e hora específicos
- ✅ Um **admin** possa **gerenciar** usuários, recursos e reservas

---

## 🏗️ ESTRUTURA DO PROJETO (As "Camadas")

Imagina um **sanduíche com 4 camadas**:

```
┌─────────────────────────────────────┐
│      HTML / FORMULÁRIOS             │  ← O que o usuário vê
│     (Templates Thymeleaf)           │    (A interface/visual)
├─────────────────────────────────────┤
│      CONTROLLERS                    │  ← O "recepcionista"
│     (Quem recebe os pedidos)        │    (Pega o que vem do formulário)
├─────────────────────────────────────┤
│      SERVICES                       │  ← O "cérebro"
│   (Onde acontece a lógica)          │    (Pensa e valida tudo)
├─────────────────────────────────────┤
│    REPOSITORY / BANCO DE DADOS      │  ← O "arquivo"
│    (Onde tudo é guardado)           │    (Salva os dados)
└─────────────────────────────────────┘
```

Tudo funciona **de cima pra baixo**:

1. Usuário preenche um **formulário** (HTML)
2. **Controller** recebe os dados
3. **Service** valida (confere se tá certo)
4. **Repository** salva no **banco de dados**
5. Tudo volta **de baixo pra cima**!

---

## 🧑‍💼 AS TRÊS PESSOAS PRINCIPAIS

### 1️⃣ UsuarioModel (Um Usuário)

Representa um **funcionário** da empresa. Ele tem:

- **ID**: Número único (tipo um CPF)
- **Nome**: O nome dele
- **Email**: Para fazer login
- **Senha**: Guarda a senha criptografada
- **Matrícula**: Número de registro na empresa
- **Data de Nascimento**: Quando ele nasceu
- **Reservas**: Lista de todas as reservas que ele fez

```java
// Exemplo de um usuário:
novo UsuarioModel("João Silva", "joao@email.com", "senha123", "MAT001", 15/03/1995)
```

---

### 2️⃣ ResourceModel (Um Recurso)

Representa algo que pode ser **reservado**. Pode ser:

- Uma **sala de reunião**
- Um **projetor**
- Um **carro** da empresa
- Qualquer coisa que precisa ser agendada!

Ele tem:

- **ID**: Número único
- **Descrição**: O que é (tipo "Sala de Reunião 101")
- **Tipo**: Classifica (tipo "sala", "equipamento")
- **Dias Disponíveis**: Que dias da semana pode agendar
- **Data Inicial/Final**: De qual data até qual data pode agendar
- **Hora Inicial/Final**: De que hora até que hora pode agendar
- **Reservas**: Lista de todas as reservas feitas pra esse recurso

```java
// Exemplo de um recurso:
novo ResourceModel(
  "Sala de Reunião 101",
  "sala",
  "segunda,terça,quarta,quinta,sexta",  // Só dias úteis
  01/01/2024,                            // Começa em janeiro
  31/12/2024,                            // Acaba em dezembro
  08:00,                                 // Pode agendar a partir de 8h
  18:00                                  // Até 6 da tarde
)
```

---

### 3️⃣ ReservationModel (Uma Reserva)

Representa um **agendamento**! Quando um usuário reserva um recurso pra um dia/hora.

Ele tem:

- **ID**: Número único
- **Colaborador**: QUAL usuário fez a reserva
- **Recurso**: QUAL recurso tá sendo reservado
- **Data**: QUE DIA é a reserva
- **Hora Inicial**: COMEÇA em que hora
- **Hora Final**: TERMINA em que hora
- **Data Cancelamento**: Se foi cancelada, quando?
- **Observação**: Notas sobre a reserva

```java
// Exemplo de uma reserva:
novo ReservationModel(
  joao,            // O usuário João
  sala101,         // Reservou a sala 101
  15/11/2024,      // Pra segunda que vem
  14:00,           // De 2 da tarde
  15:00            // Até 3 da tarde
)
```

---

## 🔄 O FLUXO PASSO A PASSO

### 📌 FLUXO 1: LOGIN (Usuário entrando no app)

```
1. Usuário vai pra página /login
   ↓
2. Controller LoginController.viewLogin() mostra o formulário vazio
   (cria um LoginDto vazio pra o HTML saber que campos criar)
   ↓
3. Usuário digita email e senha → clica em "Login"
   ↓
4. Controller LoginController.login() RECEBE os dados do formulário
   ↓
5. Chama userService.validarLogin(loginDto)
   ↓
6. Service procura no BANCO:
   "Tem algum usuário com esse email E essa senha?"
   ↓
7. Se encontrou:
   → Cria um UsuarioSessaoDto (ID + nome)
   → ControleSessao.registrar() guarda na SESSÃO do navegador
   → Redireciona pra home (/)

   Se NÃO encontrou:
   → UsuarioSessaoDto fica vazio
   → Redireciona pra /login?erro (mostra erro)
```

**Por que a sessão?** Porque depois, quando o usuário clica em outra coisa, o app já sabe quem é!

---

### 📌 FLUXO 2: CADASTRAR NOVO USUÁRIO

```
1. Usuário clica em "Novo Usuário" → vai pra /usuariocadastro
   ↓
2. Controller UsuarioCadastroController.mostrarFormulario()
   Cria um UsuarioDto VAZIO
   Manda pro HTML (th:object="${usuario}")
   ↓
3. HTML usa esse objeto pra CRIAR os campos do formulário:
   <input th:field="${usuario.nome}"/>
   <input th:field="${usuario.email}"/>
   Etc...
   ↓
4. Usuário PREENCHE o formulário e clica "Salvar"
   ↓
5. Controller UsuarioCadastroController.salvar() RECEBE os dados
   ↓
6. Chama userService.salvar(usuarioDto)
   ↓
7. Service VALIDA:
   ❌ Email já existe? (único?)
   ❌ Nome tá vazio?
   ❌ Senha tem 5+ caracteres?
   ❌ Matrícula já existe?
   Etc...

   Se tiver erro: lança RuntimeException (erro)
   ↓
8. Se passou nas validações:
   → Converte DTO → Model
   → userRepository.save() salva no BANCO DE DADOS
   → Banco GERA um ID automaticamente
   → Retorna DTO pro Controller
   ↓
9. Controller redireciona pra /usuariolista com mensagem "Sucesso!"
```

**O que é DTO?** É tipo um "formulário" só com os campos que você precisa naquele momento. A gente usa pra:

- Não expor coisas sensíveis (tipo não mandar senha pelo HTML)
- Ficar mais leve (carregar menos coisa)
- Separar a "tela" do "banco de dados"

---

### 📌 FLUXO 3: LISTAR USUÁRIOS

```
1. Usuário clica em "Ver Usuários" → vai pra /usuariolista
   ↓
2. Controller UsuarioListaController.listar()
   ↓
3. Chama userService.listarTodos()
   ↓
4. Service:
   → Faz userRepository.findAll() (pega TODOS do banco)
   → Recebe uma List<UsuarioModel>
   → Converte CADA UM em DTO
   → Retorna List<UsuarioDto>
   ↓
5. Controller coloca a lista no Model:
   model.addAttribute("usuarios", listaDeUsuarios);
   ↓
6. HTML recebe a lista e faz um LOOP pra mostrar cada um:
   <tr th:each="usuario : ${usuarios}">
     <td th:text="${usuario.nome}">...</td>
     <td th:text="${usuario.email}">...</td>
   </tr>
   ↓
7. Usuário vê uma TABELA com todos os usuários!
```

---

### 📌 FLUXO 4: ATUALIZAR/EDITAR USUÁRIO

```
1. Na lista, usuário clica em "Editar" (botão de algum usuário)
   URL: /usuarioatualizar/5  (onde 5 é o ID)
   ↓
2. Controller UsuarioAtualizarController.mostrarFormulario(5)
   @PathVariable Long id = 5
   ↓
3. Chama userService.buscarPorId(5)
   ↓
4. Service:
   → userRepository.findById(5) procura no banco
   → Se não encontrou: lança erro
   → Se encontrou: converte Model → DTO
   → Retorna o DTO preenchido
   ↓
5. Controller coloca no Model:
   model.addAttribute("usuario", usuarioDTO);
   ↓
6. HTML mostra o MESMO FORMULÁRIO, mas JÁ PREENCHIDO:
   <input th:field="${usuario.nome}" value="João Silva"/>
   ↓
7. Usuário MUDA os dados e clica "Salvar"
   ↓
8. Controller UsuarioAtualizarController.atualizar(5, usuarioDTO)
   ↓
9. Service:
   → Busca o usuário antigo no banco
   → VALIDA os novos dados
   → ATUALIZA os campos:
     usuarioAntigo.setNome(usuarioNovo.getNome())
     usuarioAntigo.setEmail(usuarioNovo.getEmail())
     Etc...
   → Salva de novo no banco (mesmo ID = atualização)
   ↓
10. Controller redireciona pra lista com "Atualizado com sucesso!"
```

---

### 📌 FLUXO 5: RESERVAR RECURSO

Basicamente é igual ao cadastro/atualização, mas com **3 relacionamentos**:

```
┌─────────────────┐
│   USUÁRIO       │  ← Quem tá fazendo a reserva
│  (Colaborador)  │
└────────┬────────┘
         │
         │ Reserva
         │
┌────────▼────────┐
│  RECURSO        │  ← O que vai reservar
│  (Sala, Equip)  │
└─────────────────┘
         +
         │
         │ Com data/hora
         │
┌────────▼────────┐
│  RESERVA        │  ← O agendamento
│                 │
└─────────────────┘
```

Quando você faz uma reserva:

1. **Escolhe qual USUÁRIO** (você mesmo, via sessão)
2. **Escolhe qual RECURSO** (sala, projetor, etc)
3. **Escolhe QUANDO** (que dia e que hora)
4. System **valida**:
   - O recurso tá disponível naquele dia?
   - Aquela hora tá dentro do horário?
   - Não tem outra reserva no mesmo horário?
5. Se ok → salva uma **NOVA RESERVA**

---

## 📊 BANCO DE DADOS

O banco tem **3 tabelas principais**:

### 📋 Tabela: usuarios

```
ID (chave) | Nome        | Email           | Senha | Matrícula | DataNascimento
1          | João Silva  | joao@email.com  | 123.. | MAT001    | 1995-03-15
2          | Maria Costa | maria@email.com | 456.. | MAT002    | 1998-07-22
```

### 📋 Tabela: recursos

```
ID (chave) | Descrição       | Tipo | DiasSemana | DataInicial | DataFinal | HoraInicial | HoraFinal
1          | Sala 101        | sala | Seg-Sex    | 2024-01-01  | 2024-12-31| 08:00       | 18:00
2          | Projetor Sony   | equip| Seg-Sex    | 2024-01-01  | 2024-12-31| 08:00       | 18:00
```

### 📋 Tabela: reservas

```
ID | ColaboradorID | RecursoID | Data       | HoraInicial | HoraFinal | DataCancelamento
1  | 1             | 1         | 2024-11-15 | 14:00       | 15:00     | NULL (ativa)
2  | 2             | 2         | 2024-11-16 | 10:00       | 11:30     | NULL (ativa)
```

**Relacionamentos**:

- 1 Usuário pode ter **MUITAS** Reservas
- 1 Recurso pode ter **MUITAS** Reservas
- 1 Reserva tem **1** Usuário e **1** Recurso

---

## 🔐 SEGURANÇA

### Login

1. Usuário digita email + senha
2. System procura no banco: "tem alguém com AQUELE email E AQUELA senha?"
3. Se sim → cria sessão no navegador
4. Pronto! Agora o browser sabe quem é o usuário

### Proteção da Senha

- Na conversão Model → DTO, a gente NÃO copia a senha
- Assim não sai senha pelo HTML

### Validações

- Email não pode repetir
- Matrícula não pode repetir
- Data de nascimento não pode ser no futuro
- Senha tem que ter no mínimo 5 caracteres

---

## 📝 TRADUÇÃO DE TERMOS (Dicionário)

| Termo          | Significa                                  | Exemplo                                  |
| -------------- | ------------------------------------------ | ---------------------------------------- |
| **Model**      | A "tabela" do banco de dados               | UsuarioModel = a tabela usuarios         |
| **DTO**        | Objeto só pro transporte de dados          | UsuarioDto = só preciso do nome e email  |
| **Controller** | Quem recebe os pedidos do usuário          | LoginController = cuida do login         |
| **Service**    | Quem valida e faz a lógica                 | UserService = valida usuários            |
| **Repository** | Quem fala com o banco de dados             | UserRepository = busca usuarios no banco |
| **Entity**     | Sinônimo de Model                          | @Entity = é uma tabela                   |
| **Transação**  | Pacote de operações "tudo ou nada"         | Salva 2 usuários ou nenhum               |
| **Sessão**     | Arquivo temporário no navegador do usuário | Guarda quem tá logado                    |
| **Validação**  | Checagem se os dados tão certos            | Email tem @ ?                            |
| **Thymeleaf**  | Motor de templates HTML do Spring          | Transforma DTOs em formulários           |

---

## 🎓 RESUMO FINAL

```
USUÁRIO                                    APP
  │                                         │
  └──→ Digita /login ─────────────────────→ LoginController.viewLogin()
       (quer fazer login)                   └──→ Mostra formulário
  │                                         │
  └──→ Digita email+senha ────────────────→ LoginController.login()
       (envia formulário)                   └──→ Chama UserService
                                            └──→ UserService busca no banco
                                            └──→ Se encontrou → cria sessão
  │                                         │
  ←──← Vê página /home ←────────────────── HomeController.home()
       (já logado!)                         └──→ Pega usuário da sessão
  │                                         │
  └──→ Clica "Novo Usuário" ─────────────→ UsuarioCadastroController.mostrarFormulario()
       (quer criar usuário)                 └──→ Mostra formulário vazio
  │                                         │
  └──→ Preenche e clica "Salvar" ────────→ UsuarioCadastroController.salvar()
                                           └──→ Chama UserService.salvar()
                                           └──→ Service VALIDA
                                           └──→ Se ok → salva no banco
  │                                         │
  ←──← Vê "Usuário cadastrado com sucesso!" ← Redireciona pra lista
       (novo usuário criado!)               │

  (E assim continua com listar, editar, deletar... tudo igual!)
```

---

## ✨ CONCLUSÃO

Esse projeto é como um **"gerenciador de agenda"** na web!

A gente tem:

- ✅ **3 coisas principais**: Usuários, Recursos, Reservas
- ✅ **4 camadas**: HTML → Controller → Service → Banco
- ✅ **Validações**: Pra não salvar dados errados
- ✅ **Segurança**: Login, sessão, proteção de senha
- ✅ **CRUD completo**: Create (criar), Read (ler), Update (editar), Delete (deletar)

**E como tudo se conecta?**

1. **HTML** é o que o usuário vê
2. **Controller** recebe o que o usuário enviou
3. **Service** pensa "isso tá certo?" e faz a lógica
4. **Repository** salva/busca no **Banco de Dados**
5. Tudo volta pro **HTML** pra mostrar pra o usuário!

Simples assim! 🎉

---

**Dúvidas?** Releia este documento ou veja os comentários no código!
