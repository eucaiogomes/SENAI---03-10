package com.empresa.agendamento.sessao;

import com.empresa.agendamento.dtos.UsuarioSessaoDto;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FiltroAutenticacao implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String uri = httpReq.getRequestURI();
        String method = httpReq.getMethod();

        System.out.println("=== FILTRO AUTENTICAÇÃO ===");
        System.out.println("URI: " + uri);
        System.out.println("Method: " + method);

        //--Permite acesso à página de login (GET e POST)
        if (uri.equals("/login") || uri.equals("/logout")) {
            System.out.println("Acesso permitido a: " + uri);
            chain.doFilter(request, response);
            return;
        }

        UsuarioSessaoDto usuario = ControleSessao.obter(httpReq);

        System.out.println("Usuário na sessão: " + (usuario != null ? usuario.getNome() : "null"));

        //--Caso não seja possível determinar o usuário, realizar uma resposta forçada antes do
        //-- controlador um comando de redirect para o login!
        if (usuario == null || usuario.getId() == null || usuario.getId() == 0L) {
            System.out.println("Usuário não autenticado! Redirecionando para /login");
            httpRes.sendRedirect(httpReq.getContextPath() + "/login");
            return;
        }
        
        System.out.println("Usuário autenticado! Permitindo acesso a: " + uri);

        //-- segurança para não manter o cache ativo
        httpRes.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpRes.setHeader("Pragma", "no-cache");
        httpRes.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }

}

