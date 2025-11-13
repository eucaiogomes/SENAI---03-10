package com.empresa.agendamento.sessao;

import com.empresa.agendamento.dtos.UsuarioSessaoDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// ControleSessao = "Controlador de SESSÃO"
// Uma SESSÃO é um "arquivo temporário" que o navegador guarda sobre o usuário
// É como se o browser tivesse um "bilhete" que prova quem tá acessando!

public class ControleSessao {
    // Classe que é 100% ESTÁTICA
    // Não precisa criar um objeto dela, é só chamar: ControleSessao.registrar()

    // Método 1: REGISTRAR a sessão (o usuário fez login com sucesso!)
    public static void registrar(HttpServletRequest request, UsuarioSessaoDto usuarioSessao) {
        // request = informações do navegador/do usuário
        // usuarioSessao = os dados do usuário que logou (ID + Nome)

        // FLUXO:
        // 1. Pega a SESSÃO do navegador (ou cria uma nova se não existir)
        // 2. Guarda o ID do usuário na sessão
        // 3. Guarda o Nome do usuário na sessão
        // Pronto! Agora o navegador sabe quem é!

        // Obter a sessão da requisição ativa do momento
        // getSession(true) = "me traz a sessão, e se não existir, CRIA UMA"
        // true = cria se não existir
        // false = não cria, só retorna null se não tiver
        HttpSession session = request.getSession(true);

        // Armazenar os dados do usuário logado na SESSÃO
        // setAttribute = "guarda um valor na sessão com um nome"
        // Isso fica guardado no NAVEGADOR (não no servidor)
        session.setAttribute("codigoUsuario", usuarioSessao.getId());    // Guarda o ID
        session.setAttribute("nomeUsuario", usuarioSessao.getNome());    // Guarda o nome
    }

    // Método 2: OBTER a sessão (pega quem tá logado)
    public static UsuarioSessaoDto obter(HttpServletRequest request){
        // request = informações do navegador/do usuário

        // FLUXO:
        // 1. Procura se tem uma SESSÃO ativa
        // 2. Se tem, busca o ID e Nome guardados lá
        // 3. Retorna um UsuarioSessaoDto preenchido
        // 4. Se não tem, retorna NULL (ninguém logado!)

        // Obter a sessão EXISTENTE (não cria nova)
        // getSession(false) = "me traz a sessão se existir, senão retorna null"
        // false = não cria, só retorna null
        HttpSession session = request.getSession(false);

        // Cria um UsuarioSessaoDto vazio
        UsuarioSessaoDto usuarioSessao = new UsuarioSessaoDto();

        // Verifica se tem uma sessão ativa E se tem um "codigoUsuario" guardado nela
        // session != null = "tem uma sessão?"
        // session.getAttribute("codigoUsuario") != null = "tem um ID guardado?"
        if (session != null && session.getAttribute("codigoUsuario") != null) {
            // Se tem tudo, preenche o UsuarioSessaoDto com os dados guardados
            
            // getAttribute = "busca um valor que foi guardado"
            // (long) = "converte pra Long" (porque getAttribute retorna Object)
            usuarioSessao.setId((long) session.getAttribute("codigoUsuario"));
            usuarioSessao.setNome((String) session.getAttribute("nomeUsuario"));
        } else {
            // Se não tem uma sessão ativa ou não tem dados, retorna NULL
            usuarioSessao = null;
        }

        // Retorna o UsuarioSessaoDto (pode estar preenchido ou ser null)
        return usuarioSessao;
    }

    // Método 3: ENCERRAR a sessão (o usuário fez logout!)
    public static void encerrar(HttpServletRequest request) {
        // request = informações do navegador/do usuário

        // FLUXO:
        // 1. Procura a SESSÃO ativa
        // 2. Se encontrou, APAGA tudo (invalidate)
        // Pronto! Agora o navegador esqueceu quem era!

        // Obter a sessão EXISTENTE (não cria nova)
        HttpSession session = request.getSession(false);

        // Se tem uma sessão ativa
        if (session != null) {
            // APAGA TUDO da sessão
            // invalidate() = "apaga os dados da sessão, como se nunca tivesse existido"
            session.invalidate();
        }
        // Pronto! Usuário deslogado!
    }
}

