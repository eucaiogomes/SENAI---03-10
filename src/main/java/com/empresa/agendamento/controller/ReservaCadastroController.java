package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.ReservationDto;
import com.empresa.agendamento.service.ReservationService;
import com.empresa.agendamento.service.ResourceService;
import com.empresa.agendamento.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsável por cadastrar novas reservas
 * Esta classe cuida de mostrar o formulário de cadastro e salvar os dados
 */
@Controller
public class ReservaCadastroController {

    // Injeta o serviço de reservas para poder salvar no banco
    @Autowired
    private ReservationService reservationService;

    // Injeta o serviço de usuários para buscar a lista de usuários
    @Autowired
    private UserService userService;

    // Injeta o serviço de recursos para buscar a lista de recursos
    @Autowired
    private ResourceService resourceService;

    /**
     * Método que mostra o formulário de cadastro de reserva
     * Quando o usuário acessa /reservacadastro, este método é chamado
     * 
     * @param model - objeto usado para passar dados para a tela HTML
     * @return o nome da página HTML do formulário (reservas/cadastro.html)
     */
    @GetMapping("/reservacadastro")
    public String mostrarFormulario(Model model) {
        // Cria um objeto vazio para o formulário
        ReservationDto reservaVazia = new ReservationDto();
        model.addAttribute("reserva", reservaVazia);
        
        // Busca todos os usuários para preencher o select de colaborador
        var listaDeUsuarios = userService.listarTodos();
        model.addAttribute("usuarios", listaDeUsuarios);
        
        // Busca todos os recursos para preencher o select de recurso
        var listaDeRecursos = resourceService.listarTodos();
        model.addAttribute("recursos", listaDeRecursos);
        
        // Retorna o nome da página HTML do formulário
        return "reservas/cadastro";
    }

    /**
     * Método que recebe os dados do formulário e salva no banco
     * Quando o usuário preenche o formulário e clica em salvar, este método é chamado
     * 
     * @param reserva - objeto com os dados preenchidos no formulário
     * @param redirectAttributes - usado para mostrar mensagens de sucesso ou erro
     * @return redireciona para a lista de reservas se deu certo, ou volta para o formulário se deu erro
     */
    @PostMapping("/reservacadastro")
    public String salvar(ReservationDto reserva, RedirectAttributes redirectAttributes) {
        try {
            // Tenta salvar a reserva no banco de dados
            reservationService.salvar(reserva);
            
            // Se deu certo, mostra mensagem de sucesso
            redirectAttributes.addFlashAttribute("sucesso", "Reserva cadastrada com sucesso!");
            
            // Redireciona para a lista de reservas
            return "redirect:/reservalista";
            
        } catch (Exception erro) {
            // Se deu erro, mostra a mensagem de erro
            redirectAttributes.addFlashAttribute("erro", erro.getMessage());
            
            // Volta para o formulário para o usuário tentar de novo
            return "redirect:/reservacadastro";
        }
    }
}

