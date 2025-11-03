package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.LoginDto;
import com.empresa.agendamento.dtos.UsuarioSessaoDto;
import com.empresa.agendamento.service.UserService;
import com.empresa.agendamento.sessao.ControleSessao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    //--Injeção de dependencia do service
    private final UserService service;

    public LoginController(UserService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String viewLogin(Model model){

        //--Criar um objeto do tipo DTO para que o thymeleaf conheça a estrutura
        LoginDto loginDto = new LoginDto();
        //--Adicionando a estrutura do dto no MODEL para processamento do HTML
        model.addAttribute("loginDto", loginDto);

        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDto") LoginDto loginDto, HttpServletRequest request){

        System.out.println("=== LOGIN CONTROLLER ===");
        System.out.println("E-mail: " + loginDto.getEmail());
        System.out.println("Senha: " + loginDto.getSenha());

        UsuarioSessaoDto usuarioSessao = service.validarLogin(loginDto);

        System.out.println("UsuarioSessaoDto após validação - ID: " + usuarioSessao.getId() + ", Nome: " + usuarioSessao.getNome());

        if (usuarioSessao != null && usuarioSessao.getId() != null && usuarioSessao.getId() != 0L) {

            System.out.println("Registrando usuário na sessão...");
            ControleSessao.registrar(request, usuarioSessao);
            System.out.println("Usuário registrado! Redirecionando para /");
            // sucesso no login
            return "redirect:/";
        } else {
            System.out.println("ERRO: Login inválido! Redirecionando para login com erro");
            //--erro no login
            return "redirect:/login?erro";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        ControleSessao.encerrar(request);
        return "redirect:/login";
    }

}
