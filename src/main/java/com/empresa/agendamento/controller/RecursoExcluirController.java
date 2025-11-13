package com.empresa.agendamento.controller;

import com.empresa.agendamento.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável por excluir recursos do sistema
 * Esta classe cuida de remover um recurso do banco de dados
 */
@Controller
public class RecursoExcluirController {

    // Injeta o serviço de recursos para poder excluir do banco
    @Autowired
    private ResourceService resourceService;

    /**
     * Método que exclui um recurso do banco de dados
     * Quando o usuário clica em "Excluir" na lista, este método é chamado
     * 
     * @param id - o número de identificação do recurso que será excluído
     * @param redirectAttributes - usado para mostrar mensagens de sucesso ou erro
     * @return redireciona para a lista de recursos
     */
    @PostMapping("/recursoexcluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Tenta excluir o recurso do banco de dados
            resourceService.excluir(id);
            
            // Se deu certo, mostra mensagem de sucesso
            redirectAttributes.addFlashAttribute("sucesso", "Recurso excluído com sucesso!");
            
        } catch (Exception erro) {
            // Se deu erro, mostra a mensagem de erro
            redirectAttributes.addFlashAttribute("erro", erro.getMessage());
        }
        
        // Sempre redireciona para a lista de recursos
        return "redirect:/recursolista";
    }
}

