package com.empresa.agendamento.controller;

import com.empresa.agendamento.dtos.UsuarioDto;
import com.empresa.agendamento.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listar(Model model, HttpServletRequest request) {
       
        model.addAttribute("usuarios", userService.listarTodos());
        return "usuarios/listar";
    }
    
    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model, HttpServletRequest request) {
        
        model.addAttribute("userDTO", new UsuarioDto());
        return "usuarios/formulario";
    }
    
    @PostMapping
    public String salvar(@Valid @ModelAttribute UsuarioDto userDTO, BindingResult result,
                        Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
       
        
        if (result.hasErrors()) {
            return "usuarios/formulario";
        }
        
        try {
            userService.salvar(userDTO);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "usuarios/formulario";
        }
    }
    
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model, HttpServletRequest request) {
     
        try {
            UsuarioDto userDTO = userService.buscarPorId(id);
            model.addAttribute("userDTO", userDTO);
            return "usuarios/formulario";
        } catch (Exception e) {
            return "redirect:/usuarios";
        }
    }
    
    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute UsuarioDto userDTO,
                           BindingResult result, Model model, HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
       
        
        if (result.hasErrors()) {
            return "usuarios/formulario";
        }
        
        try {
            userService.atualizar(id, userDTO);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "usuarios/formulario";
        }
    }
    
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
    
        
        try {
            userService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }
    

}

