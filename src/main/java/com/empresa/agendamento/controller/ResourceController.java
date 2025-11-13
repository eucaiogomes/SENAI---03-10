package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.ResourceDto;
import com.empresa.agendamento.dtos.UsuarioSessaoDto;
import com.empresa.agendamento.service.ResourceService;
import com.empresa.agendamento.sessao.ControleSessao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/recursos")
public class ResourceController {
    
    @Autowired
    private ResourceService resourceService;
    
    @GetMapping
    public String listar(Model model, HttpServletRequest request) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        model.addAttribute("recursos", resourceService.listarTodos());
        return "recursos/listar";
    }
    
    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model, HttpServletRequest request) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        model.addAttribute("resourceDTO", new ResourceDto());
        model.addAttribute("diasSemana", obterDiasSemana());
        return "recursos/formulario";
    }
    
    @PostMapping
    public String salvar(@Valid @ModelAttribute ResourceDto resourceDTO, BindingResult result,
                        Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("diasSemana", obterDiasSemana());
            return "recursos/formulario";
        }
        
        try {
            resourceService.salvar(resourceDTO);
            redirectAttributes.addFlashAttribute("sucesso", "Recurso cadastrado com sucesso!");
            return "redirect:/recursos";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("diasSemana", obterDiasSemana());
            return "recursos/formulario";
        }
    }
    
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        try {
            ResourceDto resourceDTO = resourceService.buscarPorId(id);
            model.addAttribute("resourceDTO", resourceDTO);
            model.addAttribute("diasSemana", obterDiasSemana());
            return "recursos/formulario";
        } catch (Exception e) {
            return "redirect:/recursos";
        }
    }
    
    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute ResourceDto resourceDTO,
                           BindingResult result, Model model, HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
        if (!validarSessao(request, model)) {
            return "redirect:/login";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("diasSemana", obterDiasSemana());
            return "recursos/formulario";
        }
        
        try {
            resourceService.atualizar(id, resourceDTO);
            redirectAttributes.addFlashAttribute("sucesso", "Recurso atualizado com sucesso!");
            return "redirect:/recursos";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("diasSemana", obterDiasSemana());
            return "recursos/formulario";
        }
    }
    
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
        if (!validarSessao(request, null)) {
            return "redirect:/login";
        }
        
        try {
            resourceService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Recurso excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/recursos";
    }
    
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

