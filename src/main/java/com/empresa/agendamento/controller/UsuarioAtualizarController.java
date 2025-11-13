package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.UsuarioDto;
import com.empresa.agendamento.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável por atualizar dados de usuários existentes
 * Esta classe cuida de mostrar o formulário preenchido e salvar as alterações
 * 
 * FLUXO DE ATUALIZAÇÃO:
 * 1. Usuário clica em "Editar" na lista → GET /usuarioatualizar/{id}
 * 2. Mostra o formulário JÁ PREENCHIDO com os dados antigos do usuário
 * 3. Usuário muda os dados que quer
 * 4. Usuário clica em "Atualizar" → POST /usuarioatualizar/{id}
 * 5. Se deu certo → volta pra lista (e mostra "Atualizado com sucesso!")
 *    Se deu erro → volta pro formulário (e mostra "Deu erro!")
 */
@Controller
public class UsuarioAtualizarController {

    // @Autowired = "Spring, me traz um UserService pronto pra usar!"
    // Injeta o serviço de usuários para poder buscar e atualizar no banco
    @Autowired
    private UserService userService;

    /**
     * Método que mostra o formulário de atualização já preenchido com os dados do usuário
     * Quando o usuário clica em "Atualizar" na lista, este método é chamado
     * 
     * @param id - o número de identificação do usuário que será atualizado
     * @param model - objeto usado para passar dados para a tela HTML
     * @return o nome da página HTML do formulário de atualização
     */
    
    // @GetMapping("/usuarioatualizar/{id}") = "Quando alguém entra em /usuarioatualizar/123 (por exemplo), execute"
    // {id} = "espaço para um número, tipo /usuarioatualizar/1 ou /usuarioatualizar/5"
    @GetMapping("/usuarioatualizar/{id}")
    public String mostrarFormulario(@PathVariable Long id, Model model) {
        // @PathVariable = "Pega aquele número ({id}) da URL e coloca na variável 'id'"
        // Por exemplo: /usuarioatualizar/5 → id = 5
        
        try {
            // TRY = "Tenta fazer isso aqui"
            
            // Busca o usuário no banco de dados PELO ID
            // userService.buscarPorId(id) = "Ó Service, me traz o usuário número 5!"
            UsuarioDto usuarioEncontrado = userService.buscarPorId(id);
            
            // Leva o usuário encontrado pro HTML
            // O HTML vê que já tem dados preenchidos e mostra o formulário assim
            // Assim o usuário vê os dados antigos e consegue editar
            model.addAttribute("usuario", usuarioEncontrado);
            
            // Retorna o nome da página HTML do formulário de atualização
            // "usuarios/atualizar" = mostra a página "atualizar.html" dentro da pasta "usuarios"
            return "usuarios/atualizar";
            
        } catch (Exception erro) {
            // CATCH = "Se deu algum erro, pega aqui"
            
            // Se não encontrou o usuário ou deu erro, volta para a lista
            // Assim o usuário não vê uma página quebrada, volta pra lista
            return "redirect:/usuariolista";
        }
    }

    /**
     * Método que recebe os dados atualizados do formulário e salva no banco
     * Quando o usuário preenche o formulário e clica em atualizar, este método é chamado
     * 
     * @param id - o número de identificação do usuário que está sendo atualizado
     * @param usuario - objeto com os dados atualizados preenchidos no formulário
     * @param redirectAttributes - usado para mostrar mensagens de sucesso ou erro
     * @return redireciona para a lista de usuários se deu certo, ou volta para o formulário se deu erro
     */
    
    // @PostMapping("/usuarioatualizar/{id}") = "Quando alguém ENVIA dados pro /usuarioatualizar/123 (por exemplo), execute"
    @PostMapping("/usuarioatualizar/{id}")
    public String atualizar(@PathVariable Long id, UsuarioDto usuario, RedirectAttributes redirectAttributes) {
        // @PathVariable = "Pega aquele número ({id}) da URL"
        // UsuarioDto usuario = "Os dados novos que o usuário digitou no formulário"
        
        try {
            // TRY = "Tenta fazer isso aqui"
            
            // Chama o Service pra ATUALIZAR o usuário no banco de dados
            // userService.atualizar(id, usuario) = "Ó Service, pega o usuário 5 e muda os dados dele!"
            userService.atualizar(id, usuario);
            
            // Se chegou aqui sem erro, significa que atualizou certo!
            
            // Adiciona uma mensagem de sucesso
            redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
            
            // Redireciona para a lista de usuários
            // Assim o usuário vê que os dados foram atualizados
            return "redirect:/usuariolista";
            
        } catch (Exception erro) {
            // CATCH = "Se deu algum erro, pega aqui"
            
            // Se deu erro (por exemplo: email já existe), mostra a mensagem de erro
            redirectAttributes.addFlashAttribute("erro", erro.getMessage());
            
            // Volta para o formulário de atualização
            // Assim o usuário consegue tentar de novo
            return "redirect:/usuarioatualizar/" + id;
        }
    }
}

