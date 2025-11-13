package com.empresa.agendamento.dtos;

// ============================================================================
// 🔐 USUARIOSESSAODTO
// ============================================================================
// O que é isso? É um DTO MUITO ESPECIAL!
//
// Este DTO é usado EXCLUSIVAMENTE pra guardar dados do usuário que fez LOGIN
// Quando alguém faz login, a gente cria um UsuarioSessaoDto
// E guarda isso na SESSÃO do navegador (cookies/memória)
//
// Fluxo de login:
// 1. Usuário preenche email e senha
// 2. Service valida no banco
// 3. Se tiver certo, cria UsuarioSessaoDto com ID e Nome
// 4. Coloca esse DTO na SESSÃO
// 5. Agora, em TODA página que o usuário acessa, a gente consegue pegar esse DTO
// 6. E saber quem tá usando (ID e Nome)
//
// Por que é especial?
// - Tem apenas ID e Nome (mínimo de informação)
// - É guardado na sessão (no navegador, protegido)
// - É usado pra verificar se usuário tá logado
// - Evita fazer buscas no banco toda vez que precisa saber quem tá logado
//
// ============================================================================

public class UsuarioSessaoDto {

    // ============================================================================
    // 🆔 ID - ID do usuário que tá logado
    // ============================================================================
    // Guardamos o ID do usuário pra poder recuperar dados dele depois
    // Se ID == 0 ou null, significa que ninguém tá logado
    // 
    private Long id;
    // Long = número grande

    // ============================================================================
    // 👤 NOME - Nome do usuário que tá logado
    // ============================================================================
    // Guardamos o nome pra poder exibir "Bem-vindo, João!" na página
    // Se nome == "", significa que ninguém tá logado
    // 
    private String nome;
    // String = texto

    // ============================================================================
    // 🏗️ CONSTRUTOR VAZIO - Cria um DTO "vazio" = ninguém logado
    // ============================================================================
    // Este construtor é especial porque inicializa com valores PADRÃO
    // ID = 0 e Nome = "" significam "ninguém logado"
    // 
    public UsuarioSessaoDto() {
        this.id = 0L;           // 0L = zero (número Long)
        this.nome = "";         // "" = string vazia (nada)
        // Quando cria sem dados, significa que não tem usuário logado
    }

    // ============================================================================
    // 🔄 GETTER DO ID - Pega quem tá logado
    // ============================================================================
    // Se retorna 0, ninguém tá logado
    // Se retorna um número > 0, alguém tá logado
    // 
    public Long getId() {
        return id;
        // Retorna o ID do usuário logado (ou 0 se não tem ninguém)
    }

    // ============================================================================
    // 🔄 SETTER DO ID - Guarda o ID do usuário logado
    // ============================================================================
    // Usado quando o usuário faz login
    // Passa aqui o ID do usuário pra guardar na sessão
    // 
    public void setId(Long id) {
        this.id = id;
        // Coloca o novo ID na sessão
    }

    // ============================================================================
    // 🔄 GETTER DO NOME - Pega o nome do usuário logado
    // ============================================================================
    // Se retorna "", ninguém tá logado
    // Se retorna um nome, alguém tá logado
    // 
    public String getNome() {
        return nome;
        // Retorna o nome do usuário logado (ou "" se não tem ninguém)
    }

    // ============================================================================
    // 🔄 SETTER DO NOME - Guarda o nome do usuário logado
    // ============================================================================
    // Usado quando o usuário faz login
    // Passa aqui o nome do usuário pra guardar na sessão
    // 
    public void setNome(String nome) {
        this.nome = nome;
        // Coloca o novo nome na sessão
    }
}
        this.nome = nome;
    }

}



