package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.ResourceDto;
import com.empresa.agendamento.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

/**
 * Controller responsável por cadastrar novos recursos
 * Esta classe cuida de mostrar o formulário de cadastro e salvar os dados
 */
@Controller
public class RecursoCadastroController {

    // Injeta o serviço de recursos para poder salvar no banco
    @Autowired
    private ResourceService resourceService;

    /**
     * Método que mostra o formulário de cadastro de recurso
     * Quando o usuário acessa /recursocadastro, este método é chamado
     * 
     * @param model - objeto usado para passar dados para a tela HTML
     * @return o nome da página HTML do formulário (recursos/cadastro.html)
     */
    @GetMapping("/recursocadastro")
    public String mostrarFormulario(Model model) {
        // Cria um objeto vazio para o formulário
        ResourceDto recursoVazio = new ResourceDto();
        
        // Adiciona o objeto vazio no model para o formulário HTML usar
        model.addAttribute("recurso", recursoVazio);
        
        // Busca a lista de dias da semana para mostrar no formulário
        List<String> listaDeDiasSemana = obterDiasSemana();
        model.addAttribute("diasSemana", listaDeDiasSemana);
        
        // Retorna o nome da página HTML do formulário
        return "recursos/cadastro";
    }

    /**
     * Método que recebe os dados do formulário e salva no banco
     * Quando o usuário preenche o formulário e clica em salvar, este método é chamado
     * 
     * @param recurso - objeto com os dados preenchidos no formulário
     * @param redirectAttributes - usado para mostrar mensagens de sucesso ou erro
     * @return redireciona para a lista de recursos se deu certo, ou volta para o formulário se deu erro
     */
    @PostMapping("/recursocadastro")
    public String salvar(ResourceDto recurso, RedirectAttributes redirectAttributes) {
        try {
            // Tenta salvar o recurso no banco de dados
            resourceService.salvar(recurso);
            
            // Se deu certo, mostra mensagem de sucesso
            redirectAttributes.addFlashAttribute("sucesso", "Recurso cadastrado com sucesso!");
            
            // Redireciona para a lista de recursos
            return "redirect:/recursolista";
            
        } catch (Exception erro) {
            // Se deu erro, mostra a mensagem de erro
            redirectAttributes.addFlashAttribute("erro", erro.getMessage());
            
            // Volta para o formulário para o usuário tentar de novo
            return "redirect:/recursocadastro";
        }
    }

    /**
     * Método auxiliar que retorna a lista de dias da semana
     * Usado para preencher os checkboxes no formulário
     * 
     * @return lista com os nomes dos dias da semana
     */
    private List<String> obterDiasSemana() {
        return Arrays.asList(
            "segunda-feira",
            "terça-feira",
            "quarta-feira",
            "quinta-feira",
            "sexta-feira",
            "sábado",
            "domingo"
        );
    }
}

