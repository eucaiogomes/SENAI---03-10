package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.ReservationDto;
import com.empresa.agendamento.dtos.UsuarioSessaoDto;
import com.empresa.agendamento.service.ReservationService;
import com.empresa.agendamento.service.ResourceService;
import com.empresa.agendamento.service.UserService;
import com.empresa.agendamento.sessao.ControleSessao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservas")
public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ResourceService resourceService;
    
    @GetMapping
    public String listar(Model model, HttpServletRequest request) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        model.addAttribute("reservas", reservationService.listarTodos());
        return "reservas/listar";
    }
    
    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model, HttpServletRequest request) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        model.addAttribute("reservationDTO", new ReservationDto());
        model.addAttribute("usuarios", userService.listarTodos());
        model.addAttribute("recursos", resourceService.listarTodos());
        return "reservas/formulario";
    }
    
    @PostMapping
    public String salvar(@Valid @ModelAttribute ReservationDto reservationDTO, BindingResult result,
                        Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("usuarios", userService.listarTodos());
            model.addAttribute("recursos", resourceService.listarTodos());
            return "reservas/formulario";
        }
        
        try {
            reservationService.salvar(reservationDTO);
            redirectAttributes.addFlashAttribute("sucesso", "Reserva cadastrada com sucesso!");
            return "redirect:/reservas";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuarios", userService.listarTodos());
            model.addAttribute("recursos", resourceService.listarTodos());
            return "reservas/formulario";
        }
    }
    
    @GetMapping("/{id}")
    public String visualizar(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        try {
            ReservationDto reservationDTO = reservationService.buscarPorId(id);
            model.addAttribute("reservationDTO", reservationDTO);
            return "reservas/visualizar";
        } catch (Exception e) {
            return "redirect:/reservas";
        }
    }
    
    @GetMapping("/{id}/cancelar")
    public String mostrarTelaCancelamento(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        try {
            ReservationDto reservationDTO = reservationService.buscarPorId(id);
            if (reservationDTO.getDataCancelamento() != null) {
                return "redirect:/reservas";
            }
            model.addAttribute("reservationDTO", reservationDTO);
            return "reservas/cancelar";
        } catch (Exception e) {
            return "redirect:/reservas";
        }
    }
    
    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, @RequestParam String observacao,
                          RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!validarSessao(request, null)) {
            return "redirect:/login";
        }
        
        try {
            reservationService.cancelar(id, observacao);
            redirectAttributes.addFlashAttribute("sucesso", "Reserva cancelada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/reservas";
    }
    
    private boolean validarSessao(HttpServletRequest request, Model model) {
        UsuarioSessaoDto usuario = ControleSessao.obter(request);
        if (usuario == null || usuario.getId() == null) {
            return false;
        }
        if (model != null) {
            model.addAttribute("usuarioLogado", usuario);
        }
        return true;
    }
}

