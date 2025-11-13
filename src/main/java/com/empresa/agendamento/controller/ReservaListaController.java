package com.empresa.agendamento.controller;

import com.empresa.agendamento.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsável por mostrar a lista de todas as reservas cadastradas
 * Esta é a tela que lista todas as reservas de recursos do sistema
 */
@Controller
public class ReservaListaController {

    // Injeta o serviço de reservas para poder usar os métodos dele
    @Autowired
    private ReservationService reservationService;

    /**
     * Método que mostra a tela de lista de reservas
     * Quando o usuário acessa /reservalista, este método é chamado
     * 
     * @param model - objeto usado para passar dados para a tela HTML
     * @return o nome da página HTML que será mostrada (reservas/lista.html)
     */
    @GetMapping("/reservalista")
    public String listar(Model model) {
        // Busca todas as reservas cadastradas no banco de dados
        var listaDeReservas = reservationService.listarTodos();
        
        // Adiciona a lista de reservas no model para a tela HTML poder usar
        model.addAttribute("reservas", listaDeReservas);
        
        // Retorna o nome da página HTML que será mostrada
        return "reservas/lista";
    }
}

