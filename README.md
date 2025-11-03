# Sistema de Agendamento de Recursos

Sistema web desenvolvido em Java Spring Boot para gerenciamento de reservas de espaços e equipamentos compartilhados de uma organização.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **Thymeleaf** (Template Engine)
- **H2 Database** (Desenvolvimento)
- **MySQL** (Produção - opcional)
- **Bootstrap 5** (Interface)
- **Maven** (Gerenciamento de Dependências)

## Funcionalidades

### RF01 - Gerenciamento de Usuários
- Cadastro, listagem, atualização e exclusão de usuários/colaboradores
- Validações: unicidade de e-mail e matrícula, senha com mínimo 5 caracteres contendo números e letras, data de nascimento válida

### RF02 - Gerenciamento de Recursos
- Cadastro, listagem, atualização e exclusão de recursos (espaços e equipamentos)
- Configuração de dias da semana disponíveis, período de agendamento e horários de disponibilidade

### RF04 - Gerenciamento de Reservas
- Registro, visualização e cancelamento de reservas
- Validação de conflitos de horário
- Validação de disponibilidade do recurso (data, horário e dia da semana)
- Cancelamento apenas com 1 dia de antecedência

### RF05 - Autenticação e Controle de Sessão
- Sistema de login com controle de sessão
- Proteção de rotas com interceptador de sessão
- Redirecionamento automático para login quando não autenticado

### RF06 - Modelo de Entidade e Relacionamento (MER)
- **User (Usuários)**: id, nome, email, senha, matrícula, dataNascimento
- **Resource (Recursos)**: id, descricao, tipo, diasSemanaDisponivel, dataInicialAgendamento, dataFinalAgendamento, horaInicialAgendamento, horaFinalAgendamento
- **Reservation (Reservas)**: id, colaborador (FK User), recurso (FK Resource), data, horaInicial, horaFinal, dataCancelamento, observacao

## Requisitos

- Java JDK 17 ou superior
- Maven 3.6 ou superior
- (Opcional) MySQL 8.0 ou superior (para produção)

## Instalação e Execução

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd "Projeto Final"
```

### 2. Compile o projeto

```bash
mvn clean install
```

### 3. Execute o projeto

```bash
mvn spring-boot:run
```

Ou execute diretamente a classe `SistemaAgendamentoApplication.java` a partir de uma IDE (Eclipse, IntelliJ IDEA, etc.).

### 4. Acesse o sistema

O sistema estará disponível em: `http://localhost:8080`

Você será redirecionado para a tela de login: `http://localhost:8080/login`

## Configuração do Banco de Dados

### H2 (Desenvolvimento - Padrão)

O sistema está configurado para usar o banco H2 em memória. As configurações estão em `src/main/resources/application.properties`.

Para acessar o console do H2:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:agendamento_db`
- Usuário: `sa`
- Senha: (deixe em branco)

### MySQL (Produção - Opcional)

Para usar MySQL em produção:

1. Descomente e configure as linhas no arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/agendamento_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=America/Sao_Paulo
spring.datasource.username=root
spring.datasource.password=sua_senha
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

2. Comente ou remova as configurações do H2

3. Certifique-se de ter o MySQL instalado e rodando

## Uso do Sistema

### Primeiro Acesso

Como o sistema inicia com banco vazio, você precisará criar um usuário primeiro. Você pode:

1. Acessar o console H2 em `http://localhost:8080/h2-console` e inserir manualmente um usuário
2. Ou desabilitar temporariamente o interceptor de sessão para criar um usuário diretamente pela interface

**Nota:** Para facilitar o primeiro acesso, recomenda-se criar um usuário administrador via console H2 ou SQL script.

### Exemplo de inserção via H2 Console:

```sql
INSERT INTO usuarios (nome, email, senha, matricula, data_nascimento) 
VALUES ('Administrador', 'admin@empresa.com', 'admin123', '00001', '1990-01-01');
```

### Fluxo de Uso

1. **Login**: Acesse `http://localhost:8080/login` e faça login com suas credenciais

2. **Cadastrar Usuários**: 
   - Navegue para "Usuários" → "Novo Usuário"
   - Preencha todos os campos obrigatórios
   - A senha deve ter no mínimo 5 caracteres, contendo números e letras

3. **Cadastrar Recursos**:
   - Navegue para "Recursos" → "Novo Recurso"
   - Defina descrição, tipo, dias da semana disponíveis, período de agendamento e horários

4. **Criar Reservas**:
   - Navegue para "Reservas" → "Nova Reserva"
   - Selecione colaborador, recurso, data e horário
   - O sistema validará automaticamente conflitos e disponibilidade

5. **Cancelar Reservas**:
   - Na listagem de reservas, clique em "Cancelar" na reserva desejada
   - Informe o motivo do cancelamento (obrigatório)
   - O cancelamento só é permitido com pelo menos 1 dia de antecedência

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/empresa/agendamento/
│   │   ├── config/          # Configurações (Security, WebMvc)
│   │   ├── controller/      # Controllers REST/Web
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── interceptor/    # Interceptadores (Session)
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositórios JPA
│   │   └── service/        # Lógica de Negócio
│   └── resources/
│       ├── templates/      # Templates Thymeleaf
│       └── application.properties
└── test/
```

## Validações Implementadas

### Usuários (RF01)
- ✅ Unicidade de e-mail
- ✅ Senha obrigatória na criação
- ✅ Senha com mínimo 5 caracteres, contendo números e letras
- ✅ Nome obrigatório
- ✅ Matrícula obrigatória e única
- ✅ Data de nascimento válida (não pode ser no futuro, não pode ter mais de 500 anos)

### Recursos (RF02)
- ✅ Descrição obrigatória
- ✅ Tipo obrigatório
- ✅ Validação de período de datas
- ✅ Validação de horários

### Reservas (RF04)
- ✅ Colaborador obrigatório e válido
- ✅ Recurso obrigatório e válido
- ✅ Data obrigatória
- ✅ Validação de conflitos de horário
- ✅ Validação de disponibilidade do recurso
- ✅ Cancelamento apenas 1 dia antes
- ✅ Observação obrigatória no cancelamento
- ✅ Bloqueio de atualização (apenas cancelamento permitido)

## Arquitetura

O sistema segue o padrão MVC (Model-View-Controller) com as seguintes camadas:

- **Model**: Entidades JPA representando as tabelas do banco
- **Repository**: Camada de acesso a dados (Spring Data JPA)
- **Service**: Lógica de negócio e validações
- **Controller**: Controladores web que processam requisições HTTP
- **DTO**: Objetos para transferência de dados entre camadas
- **View**: Templates Thymeleaf renderizando HTML

## Desenvolvimento e Testes

### Executar testes

```bash
mvn test
```

### Compilar sem testes

```bash
mvn clean package -DskipTests
```

## Solução de Problemas

### Erro: "Port 8080 already in use"

Altere a porta no arquivo `application.properties`:
```properties
server.port=8081
```

### Erro: "Table not found"

O H2 em memória cria as tabelas automaticamente. Se estiver usando MySQL, certifique-se de que o banco existe e a configuração está correta.

### Erro: "Session expired" ou redirecionamento contínuo para login

Verifique se o cookie de sessão está habilitado no navegador. Em alguns casos, é necessário limpar os cookies.

## Contribuindo

Este é um projeto acadêmico desenvolvido para atender aos requisitos especificados. Para contribuições ou melhorias, entre em contato com a equipe de desenvolvimento.

## Licença

Este projeto foi desenvolvido para fins acadêmicos.

## Contato

Para dúvidas ou suporte, entre em contato com a equipe de desenvolvimento.

---

**Desenvolvido com ❤️ usando Spring Boot**

"# SENAI---03-10" 
