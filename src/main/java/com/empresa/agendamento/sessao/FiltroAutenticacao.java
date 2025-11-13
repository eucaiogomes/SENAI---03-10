package com.empresa.agendamento.sessao;

import com.empresa.agendamento.dtos.UsuarioSessaoDto;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// FiltroAutenticacao = "Filtro de AUTENTICAÇÃO"
// Um FILTRO é algo que fica no "meio do caminho"
// Toda requisição passa pelo filtro ANTES de chegar no Controller!
//
// Pensa assim:
// Requisição (do browser) → [FILTRO] → Controller
//
// O filtro checa: "Você tá logado? Se não, volta pro login!"

public class FiltroAutenticacao implements Filter {
    // implements Filter = "essa classe é um Filtro do Java"

    // doFilter = "método que executa PRÉ O CONTROLLER"
    // ServletRequest = requisição genérica
    // ServletResponse = resposta genérica
    // FilterChain = a "corrente" de filtros/controllers
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // FLUXO:
        // 1. Converte request/response em HTTP (pra ficar mais específico)
        // 2. Busca o usuário da SESSÃO
        // 3. Se não tá logado, REDIRECIONA pro login
        // 4. Se tá logado, deixa passar (chain.doFilter)

        // Converte request genérico em HttpServletRequest específico
        // Por que? Porque a gente precisa dos métodos HTTP
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        // Busca o usuário que tá logado (via sessão)
        // Se não tá ninguém logado, retorna null
        UsuarioSessaoDto usuario = ControleSessao.obter(httpReq);

        // Verifica se NÃO CONSEGUIU DETERMINAR o usuário
        // usuario == null = "não achou ninguém logado"
        // usuario.getId() == null = "ou conseguiu achar mas o ID é null"
        if (usuario == null || usuario.getId() == null) {
            // Se não tá logado:
            
            // FORÇA um redirect pro login!
            // httpRes.sendRedirect = "redireciona pra outra URL"
            // httpReq.getContextPath() = "pega o caminho base da aplicação"
            // getContextPath() + "/login" = "vai pro /login"
            httpRes.sendRedirect(httpReq.getContextPath() + "/login");
            
            // IMPORTANTE: "return" pra não executar o resto do filtro!
            // Senão ia tentar deixar passar (chain.doFilter) e daria erro
            return;
        }

        // Se chegou aqui, significa que TÁ LOGADO! ✅
        
        // Segurança: NÃO manter o cache (histórico) ativo
        // Isso evita que alguém consiga ver os dados voltando no navegador
        // Se voltar, a página pede pra carregar de novo (não tira do cache)
        httpRes.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpRes.setHeader("Pragma", "no-cache");
        httpRes.setDateHeader("Expires", 0);

        // DEIXA A REQUISIÇÃO PASSAR! 🟢
        // chain.doFilter = "continua a corrente, deixa ir pro Controller"
        chain.doFilter(request, response);
    }
}

