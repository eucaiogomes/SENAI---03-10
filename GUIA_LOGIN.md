# 🔐 GUIA DE LOGIN - Sistema de Agendamento de Recursos

## Como Fazer Login no Sistema

### 1. Acesse a Tela de Login

Abra seu navegador e acesse:
```
http://localhost:8080/login
```

Ou simplesmente:
```
http://localhost:8080
```
(Você será redirecionado automaticamente para a tela de login se não estiver autenticado)

### 2. Credenciais para Login

O sistema possui **3 usuários pré-cadastrados** no banco de dados:

#### 👤 Usuário Administrador
- **E-mail:** `admin@empresa.com`
- **Senha:** `admin123`
- **Nome:** Administrador
- **Matrícula:** 00001

#### 👤 Usuária Maria Silva
- **E-mail:** `maria.silva@empresa.com`
- **Senha:** `123456`
- **Nome:** Maria Silva
- **Matrícula:** 00002

#### 👤 Usuário João Pereira
- **E-mail:** `joao.pereira@empresa.com`
- **Senha:** `654321`
- **Nome:** João Pereira
- **Matrícula:** 00003

### 3. Passo a Passo do Login

1. **Acesse a URL:** `http://localhost:8080/login`
2. **Digite o E-mail:** Use um dos e-mails acima
3. **Digite a Senha:** Use a senha correspondente
4. **Clique em "Entrar"**

### 4. Após o Login

Se o login for bem-sucedido, você será redirecionado para a página inicial (`/`) do sistema, onde poderá:
- Ver o menu de navegação
- Acessar **Usuários** para gerenciar colaboradores
- Acessar **Recursos** para gerenciar espaços e equipamentos
- Acessar **Reservas** para gerenciar agendamentos

### 5. Logout

Para sair do sistema:
- Clique no seu nome no canto superior direito
- Clique em **"Sair"** no menu dropdown
- Você será redirecionado para a tela de login

---

## ⚠️ Problemas Comuns e Soluções

### Problema: "E-mail ou senha inválidos"

**Soluções:**
1. Verifique se está usando o **e-mail correto** (não é o nome, é o e-mail!)
2. Verifique se está usando a **senha correta** (as senhas são case-sensitive)
3. Verifique se os **usuários foram criados no banco de dados**

### Como Verificar se os Usuários Existem no Banco

Execute no MySQL Workbench:
```sql
SELECT * FROM usuarios;
```

Você deve ver 3 usuários cadastrados. Se não houver, execute manualmente:
```sql
INSERT IGNORE INTO usuarios (nome, email, senha, matricula, data_nascimento)
VALUES
('Administrador', 'admin@empresa.com', 'admin123', '00001', '1990-01-01'),
('Maria Silva', 'maria.silva@empresa.com', '123456', '00002', '1992-04-15'),
('João Pereira', 'joao.pereira@empresa.com', '654321', '00003', '1988-11-30');
```

### Problema: Redirecionamento infinito ou erro 404

**Solução:**
- Certifique-se de que o MySQL está rodando
- Verifique se o banco `agendamento_db` existe
- Verifique os logs do console para erros

### Problema: Não consigo acessar nenhuma página (redireciona para login)

**Solução:**
- Faça login primeiro usando as credenciais acima
- O sistema usa autenticação por sessão (via `ControleSessao`)
- Você precisa estar logado para acessar as páginas protegidas

---

## 📝 Criar Novo Usuário

Se você quiser criar um novo usuário para fazer login:

1. **Faça login** com uma das credenciais acima
2. Vá em **Usuários** → **Novo Usuário**
3. Preencha os dados:
   - **Nome** (obrigatório)
   - **E-mail** (obrigatório e único)
   - **Senha** (obrigatório, mínimo 5 caracteres, deve conter números e letras)
   - **Matrícula** (obrigatório e único)
   - **Data de Nascimento** (obrigatório, não pode ser no futuro)
4. Clique em **Salvar**
5. Use o novo e-mail e senha para fazer login

---

## 🔧 Configurações Importantes

- **Porta:** 8080 (padrão)
- **Banco de Dados:** MySQL na porta 3306
- **Nome do Banco:** `agendamento_db`
- **Usuário MySQL:** `root`
- **Senha MySQL:** `root` (configurada no `application.properties`)

---

## ✅ Teste Rápido

1. Execute o projeto: `mvn spring-boot:run`
2. Acesse: `http://localhost:8080/login`
3. Use:
   - **E-mail:** `admin@empresa.com`
   - **Senha:** `admin123`
4. Clique em **Entrar**
5. Se tudo estiver correto, você verá a página inicial!

---

**Dúvidas?** Verifique os logs do console para mensagens de erro específicas.

