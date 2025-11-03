# Modelo de Entidade e Relacionamento (MER)

## Visão Geral

O sistema de agendamento de recursos possui três entidades principais e seus relacionamentos, seguindo o padrão de banco de dados relacional.

## Entidades

### 1. USUARIOS (User)

Representa os usuários/colaboradores do sistema.

**Atributos:**
- `id` (BIGINT, PK, AUTO_INCREMENT): Identificador único do usuário
- `nome` (VARCHAR, NOT NULL): Nome completo do usuário
- `email` (VARCHAR, UNIQUE, NOT NULL): E-mail do usuário (usado para login)
- `senha` (VARCHAR, NOT NULL): Senha do usuário (em produção, deve ser hash)
- `matricula` (VARCHAR, UNIQUE, NOT NULL): Matrícula do usuário
- `data_nascimento` (DATE, NOT NULL): Data de nascimento do usuário

**Regras de Negócio:**
- E-mail deve ser único no sistema
- Matrícula deve ser única no sistema
- Senha obrigatória na criação
- Senha deve ter mínimo 5 caracteres, contendo números e letras
- Data de nascimento não pode ser no futuro nem ter mais de 500 anos

### 2. RECURSOS (Resource)

Representa os espaços físicos ou equipamentos disponíveis para reserva.

**Atributos:**
- `id` (BIGINT, PK, AUTO_INCREMENT): Identificador único do recurso
- `descricao` (VARCHAR, NOT NULL): Descrição/nome do recurso
- `tipo` (VARCHAR, NOT NULL): Tipo do recurso (ex: "equipamento", "espaço")
- `dias_semana_disponivel` (VARCHAR): Dias da semana disponíveis (armazenado como string separada por vírgula)
- `data_inicial_agendamento` (DATE, NOT NULL): Data inicial do período de disponibilidade
- `data_final_agendamento` (DATE, NOT NULL): Data final do período de disponibilidade
- `hora_inicial_agendamento` (TIME, NOT NULL): Hora inicial de disponibilidade diária
- `hora_final_agendamento` (TIME, NOT NULL): Hora final de disponibilidade diária

**Regras de Negócio:**
- Descrição obrigatória
- Tipo obrigatório
- Data inicial deve ser anterior à data final
- Hora inicial deve ser anterior à hora final

### 3. RESERVAS (Reservation)

Representa as reservas realizadas por colaboradores para recursos.

**Atributos:**
- `id` (BIGINT, PK, AUTO_INCREMENT): Identificador único da reserva
- `colaborador_id` (BIGINT, FK → USUARIOS.id, NOT NULL): Referência ao usuário que realizou a reserva
- `recurso_id` (BIGINT, FK → RECURSOS.id, NOT NULL): Referência ao recurso reservado
- `data` (DATE, NOT NULL): Data da reserva
- `hora_inicial` (TIME, NOT NULL): Hora inicial da reserva
- `hora_final` (TIME, NOT NULL): Hora final da reserva
- `data_cancelamento` (DATE, NULLABLE): Data em que a reserva foi cancelada (NULL se ativa)
- `observacao` (VARCHAR(500), NULLABLE): Observação/motivo do cancelamento

**Regras de Negócio:**
- Colaborador obrigatório e deve existir na tabela USUARIOS
- Recurso obrigatório e deve existir na tabela RECURSOS
- Data obrigatória
- Hora inicial deve ser anterior à hora final
- Não pode haver conflitos de horário para o mesmo recurso
- Cancelamento só pode ser realizado com pelo menos 1 dia de antecedência
- Observação obrigatória no cancelamento
- Reservas não podem ser atualizadas, apenas canceladas

## Relacionamentos

### Relacionamento 1:N entre USUARIOS e RESERVAS
- **Tipo:** Um para Muitos (1:N)
- **Cardinalidade:** Um usuário pode ter várias reservas, mas cada reserva pertence a apenas um usuário
- **Atributo:** `colaborador_id` na tabela RESERVAS é chave estrangeira para `id` da tabela USUARIOS
- **Comportamento:** CASCADE (quando um usuário é excluído, suas reservas também são excluídas)

### Relacionamento 1:N entre RECURSOS e RESERVAS
- **Tipo:** Um para Muitos (1:N)
- **Cardinalidade:** Um recurso pode ter várias reservas, mas cada reserva refere-se a apenas um recurso
- **Atributo:** `recurso_id` na tabela RESERVAS é chave estrangeira para `id` da tabela RECURSOS
- **Comportamento:** CASCADE (quando um recurso é excluído, suas reservas também são excluídas)

## Diagrama de Relacionamento

```
┌─────────────┐         ┌─────────────┐
│   USUARIOS  │         │   RECURSOS  │
├─────────────┤         ├─────────────┤
│ id (PK)     │         │ id (PK)      │
│ nome        │         │ descricao   │
│ email (UK)  │         │ tipo        │
│ senha       │         │ dias_semana │
│ matricula   │         │ data_inicial│
│ data_nasc   │         │ data_final  │
└──────┬──────┘         │ hora_inicial│
       │                │ hora_final  │
       │                └──────┬──────┘
       │ 1                      │ 1
       │                        │
       │                       │
       │ N                    │ N
       │                       │
       └──────┬─────────────────┘
              │
       ┌──────▼──────┐
       │  RESERVAS   │
       ├─────────────┤
       │ id (PK)     │
       │ colaborador │──┐
       │    _id (FK) │  │
       │ recurso_id  │──┼──┐
       │    (FK)     │  │  │
       │ data        │  │  │
       │ hora_inicial│  │  │
       │ hora_final  │  │  │
       │ data_cancel │  │  │
       │ observacao  │  │  │
       └─────────────┘  │  │
                       │  │
                       └──┘
```

## Integridade Referencial

- **Chaves Primárias:** Todos os IDs são auto-incrementais
- **Chaves Estrangeiras:** 
  - `RESERVAS.colaborador_id` → `USUARIOS.id`
  - `RESERVAS.recurso_id` → `RECURSOS.id`
- **Constraints de Unicidade:**
  - `USUARIOS.email` (UNIQUE)
  - `USUARIOS.matricula` (UNIQUE)
- **Cascatas:**
  - Exclusão em cascata de reservas quando usuário ou recurso é excluído

## Observações de Implementação

1. **Senhas:** Em produção, as senhas devem ser armazenadas como hash (ex: BCrypt)
2. **Dias da Semana:** Armazenados como string separada por vírgula (ex: "segunda-feira,terça-feira,quarta-feira")
3. **Cancelamento:** Reservas não são fisicamente excluídas, apenas marcadas com `data_cancelamento`
4. **Validações:** A lógica de validação de conflitos e disponibilidade é implementada na camada de serviço

## Índices Sugeridos

Para melhor desempenho, recomenda-se criar índices nas seguintes colunas:

- `USUARIOS.email` (já único, mas útil para buscas de login)
- `RESERVAS.colaborador_id` (para buscar reservas de um usuário)
- `RESERVAS.recurso_id` (para buscar reservas de um recurso)
- `RESERVAS.data` (para buscas por data)
- `RESERVAS.data_cancelamento` (para filtrar reservas ativas)

## Mapeamento JPA

O mapeamento entre as entidades e o banco de dados é realizado através de anotações JPA:

- `@Entity`: Marca a classe como entidade JPA
- `@Table`: Define o nome da tabela no banco
- `@Id`: Marca o campo como chave primária
- `@GeneratedValue`: Define estratégia de geração automática do ID
- `@ManyToOne`: Define relacionamento muitos-para-um
- `@OneToMany`: Define relacionamento um-para-muitos
- `@JoinColumn`: Define a coluna de junção (chave estrangeira)

