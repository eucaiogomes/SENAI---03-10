package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.ReservationDto;
import com.empresa.agendamento.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável por cancelar reservas
 * Esta classe cuida de mostrar o formulário de cancelamento e marcar a reserva como cancelada
 */
@Controller
public class ReservaCancelarController {

    // Injeta o serviço de reservas para poder buscar e cancelar no banco
    @Autowired
    private ReservationService reservationService;

    /**
     * Método que mostra o formulário de cancelamento de reserva
     * Quando o usuário clica em "Cancelar" na lista, este método é chamado
     * 
     * @param id - o número de identificação da reserva que será cancelada
     * @param model - objeto usado para passar dados para a tela HTML
     * @return o nome da página HTML do formulário de cancelamento
     */
    @GetMapping("/reservacancelar/{id}")
    public String mostrarFormulario(@PathVariable Long id, Model model) {
        try {
            // Busca a reserva no banco de dados pelo ID
            ReservationDto reservaEncontrada = reservationService.buscarPorId(id);
            
            // Se a reserva já está cancelada, não pode cancelar de novo
            if (reservaEncontrada.getDataCancelamento() != null) {
                // Volta para a lista
                return "redirect:/reservalista";
            }
            
            // Adiciona a reserva encontrada no model para o formulário HTML usar
            model.addAttribute("reserva", reservaEncontrada);
            
            // Retorna o nome da página HTML do formulário de cancelamento
            return "reservas/cancelar";
            
        } catch (Exception erro) {
            // Se não encontrou a reserva ou deu erro, volta para a lista
            return "redirect:/reservalista";
        }
    }

    /**
     * Método que recebe a observação do cancelamento e marca a reserva como cancelada
     * Quando o usuário preenche o motivo e clica em cancelar, este método é chamado
     * 
     * @param id - o número de identificação da reserva que está sendo cancelada
     * @param observacao - o motivo do cancelamento informado pelo usuário
     * @param redirectAttributes - usado para mostrar mensagens de sucesso ou erro
     * @return redireciona para a lista de reservas
     */
    @PostMapping("/reservacancelar/{id}")
    public String cancelar(@PathVariable Long id, @RequestParam String observacao, RedirectAttributes redirectAttributes) {
        try {
            // Tenta cancelar a reserva no banco de dados (marca com data de cancelamento)
            reservationService.cancelar(id, observacao);
            
            // Se deu certo, mostra mensagem de sucesso
            redirectAttributes.addFlashAttribute("sucesso", "Reserva cancelada com sucesso!");
            
        } catch (Exception erro) {
            // Se deu erro, mostra a mensagem de erro
            redirectAttributes.addFlashAttribute("erro", erro.getMessage());
        }
        
        // Sempre redireciona para a lista de reservas
        return "redirect:/reservalista";
    }
}

