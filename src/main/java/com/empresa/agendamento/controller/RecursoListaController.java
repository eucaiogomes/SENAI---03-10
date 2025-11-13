package com.empresa.agendamento.controller;

import com.empresa.agendamento.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsável por mostrar a lista de todos os recursos cadastrados
 * Esta é a tela que lista todos os recursos (salas e equipamentos) do sistema
 */
@Controller
public class RecursoListaController {

    // Injeta o serviço de recursos para poder usar os métodos dele
    @Autowired
    private ResourceService resourceService;

    /**
     * Método que mostra a tela de lista de recursos
     * Quando o usuário acessa /recursolista, este método é chamado
     * 
     * @param model - objeto usado para passar dados para a tela HTML
     * @return o nome da página HTML que será mostrada (recursos/lista.html)
     */
    @GetMapping("/recursolista")
    public String listar(Model model) {
        // Busca todos os recursos cadastrados no banco de dados
        var listaDeRecursos = resourceService.listarTodos();
        
        // Adiciona a lista de recursos no model para a tela HTML poder usar
        model.addAttribute("recursos", listaDeRecursos);
        
        // Retorna o nome da página HTML que será mostrada
        return "recursos/lista";
    }
}

