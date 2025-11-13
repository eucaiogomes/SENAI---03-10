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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service responsável por toda a lógica de negócio relacionada a usuários
 * Esta classe faz a ponte entre os controllers e o banco de dados
 * Aqui ficam todas as regras de validação e operações com usuários
 * 
 * IMPORTÂNCIA DO SERVICE:
 * O Service é o "cérebro" da aplicação! 
 * Enquanto o Controller recebe os pedidos e o Banco guarda os dados,
 * o Service é quem PENSA e VALIDA tudo antes de salvar
 * 
 * Fluxo: Controller → Service → Repository (Banco)
 *                                    ↓
 *        Controller ← Service ← (dados do Banco)
 */

// @Service = "Ó Spring, essa classe é um SERVICE!"
// Service é um componente que fica escutando e esperando ser chamado
@Service

// @Transactional = "Ó Spring, transações aqui!"
// Uma transação é como um "pacote de operações" que ou FAZ TUDO ou DESFAZ TUDO
// Por exemplo: se tá salvando 2 usuários e um dá erro, não salva nenhum
@Transactional
public class UserService {
    
    // @Autowired = "Spring, me traz um UserRepository pronto!"
    // O Repository é aquele que sabe COMO falar com o banco de dados
    // Repositório usado para acessar o banco de dados de usuários
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Método que busca TODOS os usuários cadastrados no banco de dados
     * Pega do banco (que tá em formato Model) e transforma em DTO pra mandar pro front
     * 
     * @return uma lista com todos os usuários convertidos para DTO
     */
    public List<UsuarioDto> listarTodos() {
        // FLUXO DESSE MÉTODO:
        // 1. Vai no banco e pega TUDO
        // 2. Transforma cada um em DTO
        // 3. Retorna a lista de DTOs pro Controller
        
        // Busca todos os usuários do banco de dados (eles vêm em formato Model)
        // .findAll() = "Ó banco, me traz TODO mundo!"
        List<UsuarioModel> usuariosDoBanco = userRepository.findAll();
        
        // Cria uma lista VAZIA pra guardar os DTOs
        // ArrayList = tipo de lista que a gente consegue adicionar coisas depois
        List<UsuarioDto> listaDeUsuariosDto = new ArrayList<>();
        
        // LOOP = "pra cada usuário que a gente pegou do banco"
        // usuarioDoBanco = "o usuário atual da vez"
        for (UsuarioModel usuarioDoBanco : usuariosDoBanco) {
            // Converte o usuário do banco (Model) em DTO
            // Vamos pra um método lá embaixo que faz isso
            UsuarioDto usuarioDto = converterParaDTO(usuarioDoBanco);
            
            // Adiciona o DTO na lista
            listaDeUsuariosDto.add(usuarioDto);
        }
        
        // Retorna a lista de DTOs completa
        return listaDeUsuariosDto;
    }
    
    /**
     * Método que busca UM usuário específico pelo seu ID (número de identificação)
     * Tipo: "Pega o usuário número 5"
     * 
     * @param id - o número de identificação do usuário
     * @return o usuário encontrado convertido para DTO
     * @throws RuntimeException se o usuário não for encontrado
     */
    public UsuarioDto buscarPorId(Long id) {
        // FLUXO:
        // 1. Procura no banco um usuário com esse ID
        // 2. Se encontrou, transforma em DTO
        // 3. Se não encontrou, lança um erro
        
        // Busca o usuário no banco pelo ID
        // findById(id) = "Ó banco, procura lá um usuário com ID = 5 (por exemplo)"
        UsuarioModel usuarioDoBanco = userRepository.findById(id)
                // .orElseThrow = "Se não encontrou, joga uma exceção (erro)"
                // Exception = um objeto que representa um ERRO
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        
        // Se chegou aqui, significa que encontrou o usuário
        // Converte o Model para DTO e retorna
        return converterParaDTO(usuarioDoBanco);
    }
    
    /**
     * Método que salva um NOVO usuário no banco de dados
     * Aqui a gente VALIDA tudo antes de salvar (não deixa salvar dados errados!)
     * 
     * @param userDTO - objeto com os dados do usuário a ser salvo
     * @return o usuário salvo convertido para DTO
     */
    public UsuarioDto salvar(UsuarioDto userDTO) {
        // FLUXO:
        // 1. VALIDA se os dados tão ok (nome vazio? email duplicado? etc)
        // 2. Transforma DTO em Model
        // 3. Salva no banco
        // 4. Retorna o que foi salvo transformado em DTO
        
        // Primeiro VALIDA se os dados estão corretos
        // Se tiver algo errado, vai lançar uma exceção e parar aqui (não salva!)
        validarUsuario(userDTO);
        
        // Converte o DTO (formato da tela) para Model (formato do banco)
        // É como traduzir do inglês pro português
        UsuarioModel usuarioParaSalvar = converterParaEntidade(userDTO);
        
        // Salva no banco de dados 
        // O banco salva e retorna o usuário COM O ID JÁ PREENCHIDO
        // (porque o banco gera o ID automaticamente)
        UsuarioModel usuarioSalvo = userRepository.save(usuarioParaSalvar);
        
        // Converte de volta para DTO (formato para a tela) e retorna
        // Agora tem ID! porque o banco colocou um
        return converterParaDTO(usuarioSalvo);
    }
    
    /**
     * Método que ATUALIZA os dados de um usuário existente no banco
     * A gente encontra o usuário, muda os dados e salva de novo
     * 
     * @param id - o número de identificação do usuário a ser atualizado
     * @param userDTO - objeto com os novos dados do usuário
     * @return o usuário atualizado convertido para DTO
     */
    public UsuarioDto atualizar(Long id, UsuarioDto userDTO) {
        // FLUXO:
        // 1. Procura o usuário existente pelo ID
        // 2. VALIDA os dados novos
        // 3. Atualiza os campos antigos com os novos dados
        // 4. Salva tudo de novo no banco
        
        // Busca o usuário existente no banco pelo ID
        // Se não encontrou, lança um erro
        UsuarioModel usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        
        // Valida os novos dados (verifica se email/matrícula não estão duplicados, etc)
        validarAtualizacaoUsuario(id, userDTO);
        
        // Atualiza os campos do usuário existente com os novos dados
        usuarioExistente.setNome(userDTO.getNome());              // Muda o nome
        usuarioExistente.setEmail(userDTO.getEmail());            // Muda o email
        
        // Só atualiza a senha se foi informada uma nova senha
        // Se o usuário deixou a senha em branco, não muda
        if (userDTO.getSenha() != null && !userDTO.getSenha().isEmpty()) {
            usuarioExistente.setSenha(userDTO.getSenha());        // Muda a senha
        }
        
        usuarioExistente.setMatricula(userDTO.getMatricula());    // Muda a matrícula
        usuarioExistente.setDataNascimento(userDTO.getDataNascimento()); // Muda a data
        
        // Salva as alterações no banco de dados
        // O usuário tem o MESMO ID, então o banco sabe que é uma atualização
        UsuarioModel usuarioAtualizado = userRepository.save(usuarioExistente);
        
        // Converte para DTO e retorna
        return converterParaDTO(usuarioAtualizado);
    }
    
    /**
     * Método que EXCLUI um usuário do banco de dados
     * "Delete this user please"
     * 
     * @param id - o número de identificação do usuário a ser excluído
     * @throws RuntimeException se o usuário não for encontrado
     */
    public void excluir(Long id) {
        // FLUXO:
        // 1. Verifica se o usuário existe (não quer deletar algo que não existe!)
        // 2. Se existe, deleta
        
        // Verifica se o usuário existe antes de tentar excluir
        // existsById = "Esse usuário existe aí no banco?"
        if (!userRepository.existsById(id)) {
            // Se não existe, lança um erro
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        
        // Exclui o usuário do banco de dados
        userRepository.deleteById(id);
    }
    
    // Método auxiliar que busca um usuário pelo email
    public UsuarioModel buscarPorEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com e-mail: " + email));
    }
    
    /**
     * Método que VALIDA o login do usuário
     * Verifica se existe um usuário com aquele email E aquela senha
     * É o método mais IMPORTANTE pra segurança!
     * 
     * @param loginDto - objeto com email e senha informados pelo usuário
     * @return objeto com dados do usuário se login estiver correto, ou objeto vazio se estiver errado
     */
    public UsuarioSessaoDto validarLogin(LoginDto loginDto) {
        // FLUXO:
        // 1. Procura no banco um usuário com aquele email E aquela senha
        // 2. Se encontrou, coloca os dados numa UsuarioSessaoDto
        // 3. Retorna o objeto (cheio ou vazio, não importa)
        
        // Cria um objeto vazio para guardar os dados do usuário logado
        // Começamos vazio porque não sabemos ainda se o login tá correto
        UsuarioSessaoDto usuarioSessao = new UsuarioSessaoDto();
        
        // Busca no banco um usuário com o email E senha informados
        // findByEmailAndSenha = "Ó banco, procura alguém com esse email E essa senha ao mesmo tempo"
        Optional<UsuarioModel> usuarioEncontrado = userRepository.findByEmailAndSenha(
            loginDto.getEmail(),          // Email digitado
            loginDto.getSenha()           // Senha digitada
        );
        
        // .isPresent() = "Encontrou ou não?"
        if (usuarioEncontrado.isPresent()) {
            // Se encontrou um usuário com email e senha corretos
            
            // Preenche os dados do usuário no objeto de sessão
            usuarioSessao.setId(usuarioEncontrado.get().getId());      // Coloca o ID
            usuarioSessao.setNome(usuarioEncontrado.get().getNome());  // Coloca o nome
        }
        // Se não encontrou, o objeto fica vazio (id = 0, nome = "")
        // Assim o Controller sabe que o login FALHOU
        
        // Retorna o objeto (pode estar preenchido ou vazio)
        return usuarioSessao;
    }
    
    /**
     * Método PRIVADO que valida todas as REGRAS DE NEGÓCIO de um usuário
     * Aqui a gente checa se os dados fazem sentido no nosso app
     * Este método verifica se os dados estão corretos ANTES de salvar
     * 
     * @param userDTO - objeto com os dados do usuário a ser validado
     * @throws RuntimeException se alguma regra não for atendida
     */
    private void validarUsuario(UsuarioDto userDTO) {
        // REGRA 1: Email não pode ser duplicado
        // "Ó banco, tem algum usuário com esse email já?"
        if (userDTO.getId() == null && userRepository.existsByEmail(userDTO.getEmail())) {
            // Se tem, lança um erro com mensagem clara
            throw new RuntimeException("E-mail já cadastrado: " + userDTO.getEmail());
        }
        
        // REGRA 2: Senha é obrigatória quando cria um usuário novo
        // (na atualização a gente deixa vazio, mas na criação tem que ter)
        if (userDTO.getId() == null && (userDTO.getSenha() == null || userDTO.getSenha().isEmpty())) {
            throw new RuntimeException("Senha é obrigatória para criação de usuário");
        }
        
        // REGRA 3: Senha tem que ter no mínimo 5 caracteres
        // Senão fica muito fraca (fácil de adivinhar)
        if (userDTO.getSenha() != null && !userDTO.getSenha().isEmpty()) {
            if (userDTO.getSenha().length() < 5) {
                throw new RuntimeException("Senha deve ter no mínimo 5 caracteres");
            }
        }
        
        // REGRA 4: Nome não pode estar vazio
        // userDTO.getNome() == null = "Se a variável tá vazia"
        // .trim().isEmpty() = "Se tá vazio ou só tem espaços em branco"
        if (userDTO.getNome() == null || userDTO.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        
        // REGRA 5: Matrícula não pode estar vazia
        if (userDTO.getMatricula() == null || userDTO.getMatricula().trim().isEmpty()) {
            throw new RuntimeException("Matrícula é obrigatória");
        }
        
        // REGRA 6: Matrícula não pode ser duplicada
        // "Ó banco, tem algum usuário com essa matrícula já?"
        if (userDTO.getId() == null && userRepository.existsByMatricula(userDTO.getMatricula())) {
            throw new RuntimeException("Matrícula já cadastrada: " + userDTO.getMatricula());
        }
        
        // REGRA 7: Data de nascimento não pode ser no futuro
        // (não pode colocar que nasceu amanhã né rsrs)
        if (userDTO.getDataNascimento() != null) {
            LocalDate hoje = LocalDate.now();  // Pega a data de hoje
            // .isAfter() = "essa data é DEPOIS de hoje?"
            if (userDTO.getDataNascimento().isAfter(hoje)) {
                throw new RuntimeException("Data de nascimento não pode ser no futuro");
            }
        }
    }
    
    // Método auxiliar para validar atualização
    // Aqui a gente deixa mudar email/matrícula, mas valida pra não duplicar com OUTRO usuário
    private void validarAtualizacaoUsuario(Long id, UsuarioDto userDTO) {
        // Busca o usuário antigo
        UsuarioModel usuarioExistente = userRepository.findById(id).orElse(null);
        
        // Valida unicidade de e-mail (exceto para o próprio usuário)
        // Se a pessoa mudou o email, verifica se o novo email não tá duplicado
        if (usuarioExistente != null && !usuarioExistente.getEmail().equals(userDTO.getEmail())) {
            // Se é diferente, verifica se não tá duplicado
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("E-mail já cadastrado: " + userDTO.getEmail());
            }
        }
        
        // Valida unicidade de matrícula (exceto para o próprio usuário)
        // Se a pessoa mudou a matrícula, verifica se a nova não tá duplicada
        if (usuarioExistente != null && !usuarioExistente.getMatricula().equals(userDTO.getMatricula())) {
            if (userRepository.existsByMatricula(userDTO.getMatricula())) {
                throw new RuntimeException("Matrícula já cadastrada: " + userDTO.getMatricula());
            }
        }
        
        // Valida o resto das regras
        validarUsuario(userDTO);
    }
    
    /**
     * Método PRIVADO que converte um Model (formato do banco) para DTO (formato para a tela)
     * É como traduzir do "idioma do banco" pro "idioma da tela"
     * DTO não deve ter senha por SEGURANÇA! (não quer mandar senha pelo HTML)
     * 
     * @param usuarioDoBanco - objeto Model vindo do banco de dados
     * @return objeto DTO pronto para ser usado na tela
     */
    private UsuarioDto converterParaDTO(UsuarioModel usuarioDoBanco) {
        // Cria um DTO novo vazio
        UsuarioDto usuarioDto = new UsuarioDto();
        
        // Copia todos os dados do Model para o DTO (um por um)
        usuarioDto.setId(usuarioDoBanco.getId());                            // Copia ID
        usuarioDto.setNome(usuarioDoBanco.getNome());                        // Copia nome
        usuarioDto.setEmail(usuarioDoBanco.getEmail());                      // Copia email
        usuarioDto.setSenha("");                                              // IMPORTANTE: NÃO copia a senha!
                                                                               // Por SEGURANÇA, não quer expor senha no DTO
        usuarioDto.setMatricula(usuarioDoBanco.getMatricula());              // Copia matrícula
        usuarioDto.setDataNascimento(usuarioDoBanco.getDataNascimento());    // Copia data
        
        // Retorna o DTO preenchido
        return usuarioDto;
    }
    
    /**
     * Método PRIVADO que converte um DTO (formato da tela) para Model (formato do banco)
     * É como traduzir do "idioma da tela" pro "idioma do banco"
     * 
     * @param usuarioDto - objeto DTO vindo da tela
     * @return objeto Model pronto para ser salvo no banco
     */
    private UsuarioModel converterParaEntidade(UsuarioDto usuarioDto) {
        // Cria um Model novo vazio
        UsuarioModel usuarioDoBanco = new UsuarioModel();
        
        // Se o DTO tem ID, significa que é ATUALIZAÇÃO (não é novo)
        // Então a gente copia o ID
        if (usuarioDto.getId() != null) {
            usuarioDoBanco.setId(usuarioDto.getId());
        }
        // Se não tem ID, é um novo usuário
        // O banco vai gerar o ID automaticamente quando salvar
        
        // Copia todos os dados do DTO para o Model
        usuarioDoBanco.setNome(usuarioDto.getNome());                          // Copia nome
        usuarioDoBanco.setEmail(usuarioDto.getEmail());                        // Copia email
        usuarioDoBanco.setSenha(usuarioDto.getSenha());                        // Copia senha
        usuarioDoBanco.setMatricula(usuarioDto.getMatricula());                // Copia matrícula
        usuarioDoBanco.setDataNascimento(usuarioDto.getDataNascimento());      // Copia data
        
        // Retorna o Model preenchido, pronto pra salvar no banco
        return usuarioDoBanco;
    }
}

