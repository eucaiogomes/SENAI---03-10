package com.empresa.agendamento.dtos;

import jakarta.validation.constraints.NotBlank;

// ============================================================================
// 🔐 LOGINDTO
// ============================================================================
// O que é isso? É um DTO (Data Transfer Object) = "Objeto pra Transferir Dados"
//
// Mas por que a gente cria um DTO? Bom, pense assim:
// - O usuário preenche um formulário com APENAS email e senha pra fazer login
// - A gente não precisa (e não quer!) de TODOS os dados do UsuarioModel
// - Então a gente cria esse DTO que tem APENAS o que a gente precisa!
// - É como um "filtro" que só deixa passar o email e senha naquele momento
//
// Fluxo:
// Formulário HTML → LoginDto → Controller → Service → Banco
//
// Por que não usa UsuarioModel direto?
// - UsuarioModel tem TUDO: id, nome, email, senha, matricula, data
// - LoginDto tem APENAS: email e senha
// - É mais seguro (não expõe dados desnecessários)
// - É mais eficiente (transfere menos dados)
//
// ============================================================================

public class LoginDto {
    // ============================================================================
    // 📧 EMAIL - Email do usuário que vai fazer login
    // ============================================================================
    // @NotBlank = "Não pode estar vazio!"
    // message = "Se deixar em branco, mostra essa mensagem de erro"
    // 
    @NotBlank(message = "E-mail é obrigatório")
    private String email;
    // O email que o usuário digita no formulário de login

    // ============================================================================
    // 🔑 SENHA - Senha do usuário que vai fazer login
    // ============================================================================
    // @NotBlank = "Não pode estar vazio!"
    // 
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
    // A senha que o usuário digita no formulário de login

    // ============================================================================
    // 🏗️ CONSTRUTOR VAZIO
    // ============================================================================
    // Por que precisa de um construtor vazio?
    // Spring/Thymeleaf precisam criar o objeto vazio pra depois preencher
    // É tipo: "Cria um LoginDto vazio que a gente depois vai preenchendo"
    // 
    public LoginDto() {
        // Quando cria vazio, email e senha ficam null (vazios)
    }

    // ============================================================================
    // 🏗️ CONSTRUTOR COM DADOS
    // ============================================================================
    // Por que precisa desse também?
    // Pra criar um LoginDto já com dados de uma vez!
    // Exemplo: new LoginDto("joao@email.com", "senha123")
    // 
    public LoginDto(String email, String senha) {
        this.email = email;      // Coloca o email passado no construtor
        this.senha = senha;      // Coloca a senha passada no construtor
    }

    // ============================================================================
    // 🔄 GETTERS E SETTERS = "Métodos pra pegar e colocar valores"
    // ============================================================================
    // O que é getter?
    // É um método que RETORNA o valor de uma variável privada
    // Exemplo: getEmail() retorna o email guardado
    //
    // O que é setter?
    // É um método que COLOCA um valor em uma variável privada
    // Exemplo: setEmail("novo@email.com") muda o email
    //
    // Por que usar getter/setter?
    // Porque você não acessa direto a variável privada (private String email)
    // Você acessa ATRAVÉS DO MÉTODO (getEmail() ou setEmail())
    // Isso dá segurança e controle!
    // 
    
    // Getter do Email - pega o email guardado
    public String getEmail() {
        return email;
        // Retorna o email que tá guardado nesse objeto
    }

    // Setter do Email - guarda um novo email
    public void setEmail(String email) {
        // void = "não retorna nada, só executa"
        this.email = email;
        // this.email = "a variável email dessa classe"
        // email = "o email que foi passado no parâmetro"
    }

    // Getter da Senha - pega a senha guardada
    public String getSenha() {
        return senha;
        // Retorna a senha que tá guardada nesse objeto
    }

    // Setter da Senha - guarda uma nova senha
    public void setSenha(String senha) {
        this.senha = senha;
        // Coloca a nova senha que foi passada
    }
}
    }
}

