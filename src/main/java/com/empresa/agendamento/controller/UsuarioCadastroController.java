package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.UsuarioDto;
import com.empresa.agendamento.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável por cadastrar novos usuários
 * Esta classe cuida de mostrar o formulário de cadastro e salvar os dados
 * 
 * FLUXO DE CADASTRO:
 * 1. Usuário clica em "Novo Usuário" → GET /usuariocadastro → mostra formulário vazio
 * 2. Usuário preenche os campos (nome, email, senha, matrícula, data)
 * 3. Usuário clica em "Salvar" → POST /usuariocadastro → salva no banco de dados
 * 4. Se salvou certo → redireciona pra lista de usuários (e mostra "Sucesso!")
 *    Se deu erro → volta pro formulário (e mostra "Deu erro!")
 */
@Controller
public class UsuarioCadastroController {

    // @Autowired = "Spring, me traz um UserService pronto pra usar!"
    // Injeta o serviço de usuários para poder salvar no banco
    @Autowired
    private UserService userService;

    /**
     * Método que mostra o formulário de cadastro de usuário
     * Quando o usuário acessa /usuariocadastro, este método é chamado
     * 
     * @param model - objeto usado para passar dados para a tela HTML
     * @return o nome da página HTML do formulário (usuarios/cadastro.html)
     */
    
    // @GetMapping("/usuariocadastro") = "Quando alguém entra em /usuariocadastro (só pra ver), execute"
    @GetMapping("/usuariocadastro")
    public String mostrarFormulario(Model model) {
        // Cria um objeto UsuarioDto VAZIO
        // Por que vazio? Pra o HTML saber que campos ele precisa criar no formulário!
        UsuarioDto usuarioVazio = new UsuarioDto();
        
        // Leva o objeto vazio pro HTML
        // O HTML vê isso e sabe: "Ah, é um usuário vazio, vou criar os campos nome, email, etc"
        model.addAttribute("usuario", usuarioVazio);
        
        // Retorna o nome da página HTML do formulário
        // "usuarios/cadastro" significa: mostra a página "cadastro.html" que tá dentro da pasta "usuarios"
        return "usuarios/cadastro";
    }

    /**
     * Método que recebe os dados do formulário e salva no banco
     * Quando o usuário preenche o formulário e clica em salvar, este método é chamado
     * 
     * @param usuario - objeto com os dados preenchidos no formulário
     * @param redirectAttributes - usado para mostrar mensagens de sucesso ou erro
     * @return redireciona para a lista de usuários se deu certo, ou volta para o formulário se deu erro
     */
    
    // @PostMapping("/usuariocadastro") = "Quando alguém ENVIA dados pro /usuariocadastro, execute"
    @PostMapping("/usuariocadastro")
    public String salvar(UsuarioDto usuario, RedirectAttributes redirectAttributes) {
        // UsuarioDto usuario = "O objeto que o HTML mandou com os dados preenchidos"
        // RedirectAttributes = "Objeto pra mandar mensagens de sucesso/erro quando redireciona"
        
        try {
            // TRY = "Tenta fazer isso aqui"
            
            // Chama o Service pra SALVAR o usuário no banco de dados
            // O Service faz as validações (nome vazio? email válido? etc) e salva
            userService.salvar(usuario);
            
            // Se chegou aqui sem erro, significa que salvou certo!
            
            // Adiciona uma mensagem de sucesso
            // RedirectAttributes = "Quando redirecionar, leva essa mensagem junto"
            redirectAttributes.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
            
            // Redireciona para a lista de usuários
            // Assim o usuário vê que o novo usuário foi adicionado à lista
            return "redirect:/usuariolista";
            
        } catch (Exception erro) {
            // CATCH = "Se deu algum erro, pega aqui"
            
            // Se deu erro (por exemplo: email já existe), mostra a mensagem de erro
            redirectAttributes.addFlashAttribute("erro", erro.getMessage());
            
            // Volta para o formulário de cadastro
            // Assim o usuário consegue tentar de novo (com os dados já preenchidos ainda visíveis)
            return "redirect:/usuariocadastro";
        }
    }
}

