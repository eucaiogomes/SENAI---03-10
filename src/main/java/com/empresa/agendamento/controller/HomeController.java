package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.UsuarioSessaoDto;
import com.empresa.agendamento.sessao.ControleSessao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// @Controller = "Ó Spring, essa classe é um CONTROLADOR!"
@Controller
public class HomeController {
    // Esse controlador é responsável pela página INICIAL do app
    // Basicamente ele só:
    // 1. Pega quem tá logado
    // 2. Leva a informação pro HTML (index.html)
    
    // @GetMapping("/") = "Quando o usuário entra no raiz (/) da aplicação, execute isso"
    // "/" é a página inicial, tipo quando você entra em google.com
    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        // Model = "Objeto que leva dados do Controller pro HTML"
        // HttpServletRequest = "Informações do navegador"

        // Pega o usuário que tá logado da SESSÃO
        // Se ninguém tá logado, retorna um UsuarioSessaoDto vazio
        UsuarioSessaoDto usuario = ControleSessao.obter(request);
        
        // Leva o usuário logado pro HTML
        // Assim o HTML sabe quem tá usando e consegue mostrar a página direito
        // Por exemplo: "Bem-vindo, João!"
        model.addAttribute("usuarioLogado", usuario);
        
        // Retorna "index" = mostra o arquivo "index.html" (página inicial)
        return "index";
    }
}

