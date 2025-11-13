package com.empresa.agendamento.controller;

import com.empresa.agendamento.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsável por mostrar a lista de todos os usuários cadastrados
 * Esta é a tela que lista todos os usuários do sistema
 * 
 * FLUXO DE LISTAGEM:
 * 1. Usuário clica em "Listar Usuários" → GET /usuariolista
 * 2. Spring chama esse método
 * 3. Ele busca TODOS os usuários do banco de dados
 * 4. Leva a lista pro HTML
 * 5. HTML mostra todos os usuários em uma tabela bonita
 */
@Controller
public class UsuarioListaController {

    // @Autowired = "Spring, me traz um UserService pronto pra usar!"
    // Injeta o serviço de usuários para poder usar os métodos dele
    @Autowired
    private UserService userService;

    /**
     * Método que mostra a tela de lista de usuários
     * Quando o usuário acessa /usuariolista, este método é chamado
     * 
     * @param model - objeto usado para passar dados para a tela HTML
     * @return o nome da página HTML que será mostrada (usuarios/lista.html)
     */
    
    // @GetMapping("/usuariolista") = "Quando alguém entra em /usuariolista, execute isso"
    @GetMapping("/usuariolista")
    public String listar(Model model) {
        // Busca TODOS os usuários cadastrados no banco de dados
        // userService.listarTodos() = "Vai no Service pedir: me traz TODOS os usuários!"
        // O Service vai no banco, pega tudo e retorna uma LISTA
        var listaDeUsuarios = userService.listarTodos();
        
        // "var" = "deixa o Java descobrir qual é o tipo" (nesse caso é List<UsuarioDto>)
        
        // Leva a lista de usuários pro HTML
        // O HTML vai ver "usuarios" e conseguir fazer um loop pra mostrar cada um
        model.addAttribute("usuarios", listaDeUsuarios);
        
        // Retorna o nome da página HTML que será mostrada
        // "usuarios/lista" = mostra a página "lista.html" dentro da pasta "usuarios"
        return "usuarios/lista";
    }
}

