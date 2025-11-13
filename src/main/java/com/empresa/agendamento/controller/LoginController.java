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

// @Controller = "Ó Spring, essa classe vai ser um CONTROLADOR!"
// Um controlador é como um "recepcionista" que:
// 1. Recebe pedidos do usuário (cliques no formulário)
// 2. Faz o trabalho necessário (chama um Service)
// 3. Retorna a resposta (mostra uma página HTML pro usuário)

@Controller
public class LoginController {
    // FLUXO DE LOGIN FUNCIONA ASSIM:
    // Usuário digita email e senha → clica em login → o controlador recebe isso
    // → passa pro Service validar → Service diz se tá certo ou errado
    // → Controller guarda na sessão (se tiver certo) ou redireciona pro login de novo (se errado)

    // Injeção de dependência do UserService
    // O que é "injeção de dependência"? É quando a gente pede a Spring pra TRAZER pra gente
    // um objeto que já tá pronto pra usar! Nesse caso, o UserService
    //--Injeção de dependencia do service
    private final UserService service;

    // Construtor que recebe o UserService (Spring coloca isso automaticamente)
    // O método constructor é um método especial que roda quando a classe é criada
    public LoginController(UserService service) {
        this.service = service; // Guarda o service pra usar depois
    }

    // @GetMapping("/login") = "Quando o usuário entra em /login (digita na URL), execute isso"
    // GET = "É só pra VER algo, não tá enviando dados"
    @GetMapping("/login")
    public String viewLogin(Model model){
        // Model = "O objeto que leva dados do Controller pro HTML"

        // Criar um objeto LoginDto vazio
        // Por que vazio? Pra o Thymeleaf (HTML) saber que campo ele precisa criar!
        // É como a gente avisa: "Ó HTML, prepara um formulário com esses campos!"
        //--Criar um objeto do tipo DTO para que o thymeleaf conheça a estrutura
        LoginDto loginDto = new LoginDto();
        
        // Adicionar o LoginDto no Model
        // Isso faz o HTML enxergar o "loginDto" e conseguir criar o formulário com ele
        //--Adicionando a estrutura do dto no MODEL para processamento do HTML
        model.addAttribute("loginDto", loginDto);

        // Retorna "login" = mostra o arquivo "login.html"
        return "login";
    }

    // @PostMapping("/login") = "Quando o usuário ENVIA dados pro /login, execute isso"
    // POST = "Tá ENVIANDO dados" (diferente do GET que é só pra ver)
    @PostMapping("/login")
    public String login(@ModelAttribute("loginDto") LoginDto loginDto, HttpServletRequest request){
        // @ModelAttribute = "Pega os dados do formulário e coloca no LoginDto"
        // HttpServletRequest = "Objeto que tem informações do navegador do usuário"

        // Chama o Service pra VALIDAR se o email e senha tão corretos
        // O Service retorna um UsuarioSessaoDto (com ID e nome do usuário)
        // Se tiver errado, retorna null (vazio)
        UsuarioSessaoDto usuarioSessao = service.validarLogin(loginDto);

        // Verifica se o login foi bem-sucedido
        // usuarioSessao != null = "Se conseguiu encontrar um usuário"
        // && usuarioSessao.getId() != null = "Se tem um ID válido"
        // && usuarioSessao.getId() != 0L = "Se o ID não é zero"
        if (usuarioSessao != null && usuarioSessao.getId() != null && usuarioSessao.getId() != 0L) {
            // LOGIN DEU CERTO! Então a gente:
            
            // 1. Guarda o usuário na SESSÃO (no navegador)
            // Isso significa que a próxima vez que o usuário pedir uma página,
            // a gente já sabe quem ele é!
            ControleSessao.registrar(request, usuarioSessao);
            
            // 2. Redireciona pro HOME (página principal)
            // "redirect:/" = "Leva o usuário pra página inicial do app"
            return "redirect:/";
        } else {
            // LOGIN DEU ERRADO! Então a gente:
            
            // Redireciona de volta pro login (com erro)
            // O "?erro" na URL avisa o HTML que aconteceu um erro!
            return "redirect:/login?erro";
        }
    }

    // @GetMapping("/logout") = "Quando o usuário clica em SAIR, execute isso"
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        // Apaga o usuário da SESSÃO
        // Agora o navegador não sabe mais quem tá usando o app
        ControleSessao.encerrar(request);
        
        // Redireciona pro login
        // Assim o usuário volta pro começo!
        return "redirect:/login";
    }

}
