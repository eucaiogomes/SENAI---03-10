package com.empresa.agendamento.service;

import com.empresa.agendamento.dtos.LoginDto;
import com.empresa.agendamento.dtos.UsuarioDto;
import com.empresa.agendamento.dtos.UsuarioSessaoDto;
import com.empresa.agendamento.models.UsuarioModel;
import com.empresa.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<UsuarioDto> listarTodos() {
        return userRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    public UsuarioDto buscarPorId(Long id) {
        UsuarioModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        return converterParaDTO(user);
    }
    
    public UsuarioDto salvar(UsuarioDto userDTO) {
        validarUsuario(userDTO);
        
        UsuarioModel user = converterParaEntidade(userDTO);
        user = userRepository.save(user);
        return converterParaDTO(user);
    }
    
    public UsuarioDto atualizar(Long id, UsuarioDto userDTO) {
        UsuarioModel userExistente = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        
        validarAtualizacaoUsuario(id, userDTO);
        
        userExistente.setNome(userDTO.getNome());
        userExistente.setEmail(userDTO.getEmail());
        if (userDTO.getSenha() != null && !userDTO.getSenha().isEmpty()) {
            userExistente.setSenha(userDTO.getSenha());
        }
        userExistente.setMatricula(userDTO.getMatricula());
        userExistente.setDataNascimento(userDTO.getDataNascimento());
        
        userExistente = userRepository.save(userExistente);
        return converterParaDTO(userExistente);
    }
    
    public void excluir(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        userRepository.deleteById(id);
    }
    
    public UsuarioModel buscarPorEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com e-mail: " + email));
    }
    
    public UsuarioSessaoDto validarLogin(LoginDto loginDto) {

        UsuarioSessaoDto usuarioSessao = new UsuarioSessaoDto();

        System.out.println("=== VALIDAÇÃO DE LOGIN ===");
        System.out.println("E-mail recebido: " + loginDto.getEmail());
        System.out.println("Senha recebida: " + loginDto.getSenha());
        
        //--buscar do banco de dados o usuário atraves do e-mail/login
        Optional<UsuarioModel> usuarioOP = userRepository.findByEmailAndSenha(loginDto.getEmail(), loginDto.getSenha());

        System.out.println("Usuário encontrado no banco: " + usuarioOP.isPresent());
        
        //--validar se encontrou o usuário e se a senha é igual
        if (usuarioOP.isPresent() ){
            System.out.println("ID do usuário: " + usuarioOP.get().getId());
            System.out.println("Nome do usuário: " + usuarioOP.get().getNome());
            usuarioSessao.setId(usuarioOP.get().getId());
            usuarioSessao.setNome(usuarioOP.get().getNome());
            System.out.println("Login válido! Redirecionando...");
        } else {
            System.out.println("ERRO: Usuário não encontrado ou senha incorreta!");
        }
        
        System.out.println("UsuarioSessaoDto retornado - ID: " + usuarioSessao.getId() + ", Nome: " + usuarioSessao.getNome());
        
        return usuarioSessao;
    }
    
    private void validarUsuario(UsuarioDto userDTO) {
        // Regra 1: Unicidade de e-mail
        if (userDTO.getId() == null && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado: " + userDTO.getEmail());
        }
        
        // Regra 2: Senha obrigatória na criação
        if (userDTO.getId() == null && (userDTO.getSenha() == null || userDTO.getSenha().isEmpty())) {
            throw new RuntimeException("Senha é obrigatória para criação de usuário");
        }
        
        // Regra 3: Senha deve ter no mínimo 5 caracteres, conter números e letras
        if (userDTO.getSenha() != null && !userDTO.getSenha().isEmpty()) {
            if (userDTO.getSenha().length() < 5) {
                throw new RuntimeException("Senha deve ter no mínimo 5 caracteres");
            }
            if (!userDTO.getSenha().matches(".*[0-9].*")) {
                throw new RuntimeException("Senha deve conter pelo menos um número");
            }
            if (!userDTO.getSenha().matches(".*[a-zA-Z].*")) {
                throw new RuntimeException("Senha deve conter pelo menos uma letra");
            }
        }
        
        // Regra 4: Nome obrigatório
        if (userDTO.getNome() == null || userDTO.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        
        // Regra 5: Matrícula obrigatória
        if (userDTO.getMatricula() == null || userDTO.getMatricula().trim().isEmpty()) {
            throw new RuntimeException("Matrícula é obrigatória");
        }
        
        // Regra 6: Unicidade de matrícula
        if (userDTO.getId() == null && userRepository.existsByMatricula(userDTO.getMatricula())) {
            throw new RuntimeException("Matrícula já cadastrada: " + userDTO.getMatricula());
        }
        
        // Regra 7: Validação de data de nascimento (não pode ser no futuro e não pode ter mais de 500 anos)
        if (userDTO.getDataNascimento() != null) {
            LocalDate hoje = LocalDate.now();
            if (userDTO.getDataNascimento().isAfter(hoje)) {
                throw new RuntimeException("Data de nascimento não pode ser no futuro");
            }
            Period periodo = Period.between(userDTO.getDataNascimento(), hoje);
            if (periodo.getYears() > 500) {
                throw new RuntimeException("Data de nascimento não pode ter mais de 500 anos");
            }
        }
    }
    
    private void validarAtualizacaoUsuario(Long id, UsuarioDto userDTO) {
        UsuarioModel usuarioExistente = userRepository.findById(id).orElse(null);
        
        // Valida unicidade de e-mail (exceto para o próprio usuário)
        if (usuarioExistente != null && !usuarioExistente.getEmail().equals(userDTO.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("E-mail já cadastrado: " + userDTO.getEmail());
            }
        }
        
        // Valida unicidade de matrícula (exceto para o próprio usuário)
        if (usuarioExistente != null && !usuarioExistente.getMatricula().equals(userDTO.getMatricula())) {
            if (userRepository.existsByMatricula(userDTO.getMatricula())) {
                throw new RuntimeException("Matrícula já cadastrada: " + userDTO.getMatricula());
            }
        }
        
        validarUsuario(userDTO);
    }
    
    private UsuarioDto converterParaDTO(UsuarioModel user) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setSenha(""); // Não expor senha no DTO
        dto.setMatricula(user.getMatricula());
        dto.setDataNascimento(user.getDataNascimento());
        return dto;
    }
    
    private UsuarioModel converterParaEntidade(UsuarioDto dto) {
        UsuarioModel user = new UsuarioModel();
        if (dto.getId() != null) {
            user.setId(dto.getId());
        }
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha()); // Em produção, usar hash da senha
        user.setMatricula(dto.getMatricula());
        user.setDataNascimento(dto.getDataNascimento());
        return user;
    }
}

