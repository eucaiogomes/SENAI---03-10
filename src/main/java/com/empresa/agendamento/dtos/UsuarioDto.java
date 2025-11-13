package com.empresa.agendamento.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

// ============================================================================
// 👤 USUARIODTO
// ============================================================================
// O que é isso? É um DTO (Data Transfer Object) = "Objeto pra Transferir Dados"
//
// Esse DTO é usado pra transferir dados de usuários entre:
// - Formulário HTML (o usuário preenche)
// - Controller (recebe os dados)
// - Service (valida e salva)
// - Banco de dados (guarda tudo)
// - De volta pra página HTML (mostra os dados)
//
// Diferença entre DTO e Model:
// - DTO: é um "mensageiro", leve, pra transferir dados
// - Model: é ligado ao banco de dados (tem @Entity)
//
// Por que tem UsuarioModel E UsuarioDto?
// Segurança! Assim a gente não expõe dados internos do banco pro HTML
// Exemplo: Model pode ter campos que o DTO não tem, e vice-versa
//
// ============================================================================

public class UsuarioDto {
    
    // ============================================================================
    // 🆔 ID - Identificação única do usuário
    // ============================================================================
    // ID é criado automaticamente pelo banco (auto-increment)
    // Quando é novo usuário, ID é null
    // Quando já existe, ID tem um número
    // 
    private Long id;
    // Long = número grande (pode ser 1, 2, 1000000, etc)
    // null = vazio (ainda não tem ID)
    
    // ============================================================================
    // 📝 NOME - Nome completo do usuário
    // ============================================================================
    // @NotBlank = "Não pode estar vazio ou só com espaços!"
    // message = "Mensagem que mostra se deixar em branco"
    // 
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    // String = texto (qualquer quantidade de caracteres)
    
    // ============================================================================
    // 📧 EMAIL - Email do usuário
    // ============================================================================
    // @NotBlank = "Não pode estar vazio!"
    // @Email = "Tem que ser um email válido! (tem que ter @)"
    // 
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido")
    private String email;
    // O email do usuário (exemplo: joao@empresa.com)
    
    // ============================================================================
    // 🔑 SENHA - Senha do usuário
    // ============================================================================
    // Aqui a gente guarda a senha pra transferir entre telas
    // MAS CUIDADO! Nunca deveria mostrar a senha em uma listagem!
    // A gente só coloca em branco quando retorna pra segurança
    // 
    private String senha;
    // A senha que o usuário digita (nunca deveria ser mostrada)
    
    // ============================================================================
    // 🏢 MATRÍCULA - Número de matrícula (como um ID interno da empresa)
    // ============================================================================
    // @NotBlank = "Não pode estar vazio!"
    // 
    @NotBlank(message = "Matrícula é obrigatória")
    private String matricula;
    // Exemplo: "2024001", "EMP-12345", etc
    
    // ============================================================================
    // 📅 DATA DE NASCIMENTO - Data em que o usuário nasceu
    // ============================================================================
    // @NotNull = "Não pode ser nulo (tem que ter uma data)!"
    // @Past = "Tem que ser uma data no PASSADO (não pode ser hoje ou futuro)!"
    // 
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;
    // LocalDate = uma data específica (dia, mês, ano)
    // Exemplo: 15 de março de 1990
    
    // ============================================================================
    // 🏗️ CONSTRUTOR VAZIO
    // ============================================================================
    // Usado quando a gente precisa criar um UsuarioDto vazio
    // Depois vai preenchendo campo por campo
    // 
    public UsuarioDto() {
        // Todos os campos ficam null ou vazio
    }
    
    // ============================================================================
    // 🏗️ CONSTRUTOR COM TODOS OS DADOS
    // ============================================================================
    // Usado quando a gente já tem todos os dados e quer criar um UsuarioDto completo
    // Exemplo: new UsuarioDto(1L, "João", "joao@email.com", "senha123", "2024001", dataNasc)
    // 
    public UsuarioDto(Long id, String nome, String email, String senha, String matricula, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
    }

    // ============================================================================
    // 🔄 GETTERS E SETTERS
    // ============================================================================
    // Getter do ID - pega o ID do usuário
    public Long getId() {
        return id;
        // Retorna o ID guardado
    }

    // Setter do ID - guarda um novo ID
    public void setId(Long id) {
        this.id = id;
    }

    // Getter do Nome - pega o nome do usuário
    public String getNome() {
        return nome;
        // Retorna o nome guardado
    }

    // Setter do Nome - guarda um novo nome
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter do Email - pega o email do usuário
    public String getEmail() {
        return email;
        // Retorna o email guardado
    }

    // Setter do Email - guarda um novo email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter da Senha - pega a senha do usuário
    public String getSenha() {
        return senha;
        // Retorna a senha guardada
    }

    // Setter da Senha - guarda uma nova senha
    public void setSenha(String senha) {
        this.senha = senha;
    }

    // Getter da Matrícula - pega a matrícula do usuário
    public String getMatricula() {
        return matricula;
        // Retorna a matrícula guardada
    }

    // Setter da Matrícula - guarda uma nova matrícula
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    // Getter da Data de Nascimento - pega a data de nascimento do usuário
    public LocalDate getDataNascimento() {
        return dataNascimento;
        // Retorna a data de nascimento guardada
    }

    // Setter da Data de Nascimento - guarda uma nova data de nascimento
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}