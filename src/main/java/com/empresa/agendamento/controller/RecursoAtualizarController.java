package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.ResourceDto;
import com.empresa.agendamento.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

/**
 * Controller responsável por atualizar dados de recursos existentes
 * Esta classe cuida de mostrar o formulário preenchido e salvar as alterações
 */
@Controller
public class RecursoAtualizarController {

    // Injeta o serviço de recursos para poder buscar e atualizar no banco
    @Autowired
    private ResourceService resourceService;

    /**
     * Método que mostra o formulário de atualização já preenchido com os dados do recurso
     * Quando o usuário clica em "Atualizar" na lista, este método é chamado
     * 
     * @param id - o número de identificação do recurso que será atualizado
     * @param model - objeto usado para passar dados para a tela HTML
     * @return o nome da página HTML do formulário de atualização
     */
    @GetMapping("/recursoatualizar/{id}")
    public String mostrarFormulario(@PathVariable Long id, Model model) {
        try {
            // Busca o recurso no banco de dados pelo ID
            ResourceDto recursoEncontrado = resourceService.buscarPorId(id);
            
            // Adiciona o recurso encontrado no model para o formulário HTML usar
            model.addAttribute("recurso", recursoEncontrado);
            
            // Busca a lista de dias da semana para mostrar no formulário
            List<String> listaDeDiasSemana = obterDiasSemana();
            model.addAttribute("diasSemana", listaDeDiasSemana);
            
            // Retorna o nome da página HTML do formulário de atualização
            return "recursos/atualizar";
            
        } catch (Exception erro) {
            // Se não encontrou o recurso ou deu erro, volta para a lista
            return "redirect:/recursolista";
        }
    }

    /**
     * Método que recebe os dados atualizados do formulário e salva no banco
     * Quando o usuário preenche o formulário e clica em atualizar, este método é chamado
     * 
     * @param id - o número de identificação do recurso que está sendo atualizado
     * @param recurso - objeto com os dados atualizados preenchidos no formulário
     * @param redirectAttributes - usado para mostrar mensagens de sucesso ou erro
     * @return redireciona para a lista de recursos se deu certo, ou volta para o formulário se deu erro
     */
    @PostMapping("/recursoatualizar/{id}")
    public String atualizar(@PathVariable Long id, ResourceDto recurso, RedirectAttributes redirectAttributes) {
        try {
            // Tenta atualizar o recurso no banco de dados
            resourceService.atualizar(id, recurso);
            
            // Se deu certo, mostra mensagem de sucesso
            redirectAttributes.addFlashAttribute("sucesso", "Recurso atualizado com sucesso!");
            
            // Redireciona para a lista de recursos
            return "redirect:/recursolista";
            
        } catch (Exception erro) {
            // Se deu erro, mostra a mensagem de erro
            redirectAttributes.addFlashAttribute("erro", erro.getMessage());
            
            // Volta para o formulário de atualização para o usuário tentar de novo
            return "redirect:/recursoatualizar/" + id;
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

