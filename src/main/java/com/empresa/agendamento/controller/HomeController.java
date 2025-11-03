package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.UsuarioSessaoDto;
import com.empresa.agendamento.sessao.ControleSessao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        System.out.println("=== HOME CONTROLLER ===");
        UsuarioSessaoDto usuario = ControleSessao.obter(request);
        
        System.out.println("Usuário na sessão: " + (usuario != null ? usuario.getNome() : "null"));
        System.out.println("ID do usuário: " + (usuario != null ? usuario.getId() : "null"));
        
        if (usuario == null || usuario.getId() == null || usuario.getId() == 0L) {
            System.out.println("Usuário não autenticado! Redirecionando para login");
            return "redirect:/login";
        }
        
        System.out.println("Usuário autenticado! Mostrando página inicial");
        model.addAttribute("usuarioLogado", usuario);
        return "index";
    }
}

