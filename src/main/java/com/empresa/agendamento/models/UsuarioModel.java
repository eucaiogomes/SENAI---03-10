package com.empresa.agendamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

// @Entity = "Ó banco de dados, essa classe aqui é uma tabela!" (transforma essa classe em tabela no BD)
// @Table = "A tabela vai se chamar 'usuarios' lá no banco, blz?"
// uniqueConstraints = "Vê esse campo 'email'? Pois é, não pode ter dois usuários com o mesmo email não!"
@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UsuarioModel {
    // Essa classe é o MODELO de um usuário
    // Tudo que você precisa saber de um usuário tá aqui dentro
    
    // @Id = "Isso aqui é a chave primária!" (o identificador único de cada usuário, tipo um CPF)
    // @GeneratedValue = "Deixa o banco de dados gerar automaticamente esse ID quando a gente salvar um novo usuário"
    // private Long id = "Variável que guarda o número de identificação de cada usuário"
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @NotBlank = "Esse campo não pode ficar em branco, tem que ter alguma coisa escrita"
    // @Column(nullable = false) = "Obrigatório! Tem que preencher senão salva não!"
    // O nome do usuário fica guardado aqui
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    // @NotBlank = "Email não pode estar vazio não"
    // @Email = "Valida se é realmente um email (tem que ter @ e tal)"
    // unique = true = "Não pode ter dois usuários com o mesmo email! Cada um tem o seu"
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido")
    @Column(nullable = false, unique = true)
    private String email;
    
    // Aqui fica a senha do usuário (importante guardar com segurança!)
    @Column(nullable = false)
    private String senha;
    
    // @NotBlank = "Matrícula não pode estar vazia"
    // unique = true = "Cada funcionário tem sua matrícula única, não pode repetir"
    // Esse número identifica o funcionário na empresa
    @NotBlank(message = "Matrícula é obrigatória")
    @Column(nullable = false, unique = true)
    private String matricula;
    
    // @NotNull = "Data de nascimento não pode estar vazia"
    // @Past = "A data tem que ser no passado! (não pode colocar uma data futura né, tipo você nascendo amanhã rsrs)"
    // LocalDate = Tipo especial que só guarda a data (dia, mês, ano) sem a hora
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    @Column(nullable = false)
    private LocalDate dataNascimento;
    
    // @OneToMany = "Um usuário pode ter VÁRIAS reservas!" (relacionamento 1 para muitos)
    // mappedBy = "colaborador" = "No ReservationModel tem um campo chamado 'colaborador' que aponta pra aqui"
    // cascade = CascadeType.ALL = "Se deletar um usuário, deleta todas as reservas dele também" (em cascata!)
    // Essa lista guarda todas as reservas que esse usuário fez
    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL)
    private List<ReservationModel> reservas;
    
    // CONSTRUTORES = "Formas de criar um novo usuário"
    
    // Construtor 1: Vazio (sem parâmetros)
    // Quando você cria um usuário vazio, ele depois preenche os dados
    public UsuarioModel() {
    }
    
    // Construtor 2: Com todos os dados
    // Você passa tudo de uma vez e já cria um usuário completo (mais rápido!)
    public UsuarioModel(String nome, String email, String senha, String matricula, LocalDate dataNascimento) {
        // "this.nome" = "pega o campo 'nome' dessa classe"
        // "nome" = "pega o parâmetro que foi passado"
        this.nome = nome;           // Atribui o nome que veio de fora
        this.email = email;         // Atribui o email que veio de fora
        this.senha = senha;         // Atribui a senha que veio de fora
        this.matricula = matricula; // Atribui a matrícula que veio de fora
        this.dataNascimento = dataNascimento; // Atribui a data que veio de fora
    }
    
    // GETTERS e SETTERS = "Métodos para pegar e colocar valores nas variáveis"
    // Getter = "Pega" (get = pega)
    // Setter = "Define" ou "coloca" (set = coloca)
    
    // Getter do ID - retorna qual é o ID do usuário
    public Long getId() {
        return id; // Retorna o valor do ID
    }
    
    // Setter do ID - define qual é o ID do usuário (geralmente não mexe nisso, o banco coloca)
    public void setId(Long id) {
        this.id = id; // Coloca o ID que foi passado como parâmetro
    }
    
    // Getter do Nome - retorna o nome do usuário
    public String getNome() {
        return nome;
    }
    
    // Setter do Nome - define/muda o nome do usuário
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    // Getter do Email - retorna o email do usuário
    public String getEmail() {
        return email;
    }
    
    // Setter do Email - define/muda o email do usuário
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Getter da Senha - retorna a senha do usuário
    public String getSenha() {
        return senha;
    }
    
    // Setter da Senha - define/muda a senha do usuário
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    // Getter da Matrícula - retorna a matrícula do usuário
    public String getMatricula() {
        return matricula;
    }
    
    // Setter da Matrícula - define/muda a matrícula do usuário
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    // Getter da Data de Nascimento - retorna a data de nascimento do usuário
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    // Setter da Data de Nascimento - define/muda a data de nascimento do usuário
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public List<ReservationModel> getReservas() {
        return reservas;
    }
    
    public void setReservas(List<ReservationModel> reservas) {
        this.reservas = reservas;
    }
}

