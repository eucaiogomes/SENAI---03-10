package com.empresa.agendamento.controller;

import com.empresa.agendamento.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável por excluir usuários do sistema
 * Esta classe cuida de remover um usuário do banco de dados
 */
@Controller
public class UsuarioExcluirController {

    // Injeta o serviço de usuários para poder excluir do banco
    @Autowired
    private UserService userService;

    /**
     * Método que exclui um usuário do banco de dados
     * Quando o usuário clica em "Excluir" na lista, este método é chamado
     * 
     * @param id - o número de identificação do usuário que será excluído
     * @param redirectAttributes - usado para mostrar mensagens de sucesso ou erro
     * @return redireciona para a lista de usuários
     */
    @PostMapping("/usuarioexcluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Tenta excluir o usuário do banco de dados
            userService.excluir(id);
            
            // Se deu certo, mostra mensagem de sucesso
            redirectAttributes.addFlashAttribute("sucesso", "Usuário excluído com sucesso!");
            
        } catch (Exception erro) {
            // Se deu erro, mostra a mensagem de erro
            redirectAttributes.addFlashAttribute("erro", erro.getMessage());
        }
        
        // Sempre redireciona para a lista de usuários
        return "redirect:/usuariolista";
    }
}

