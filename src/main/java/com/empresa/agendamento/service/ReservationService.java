package com.empresa.agendamento.service;

import com.empresa.agendamento.dtos.ReservationDto;
import com.empresa.agendamento.models.ReservationModel;
import com.empresa.agendamento.models.ResourceModel;
import com.empresa.agendamento.models.UsuarioModel;
import com.empresa.agendamento.repository.ReservationRepository;
import com.empresa.agendamento.repository.ResourceRepository;
import com.empresa.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// ============================================================================
// 🎯 RESERVATIONSERVICE - O MAIS COMPLEXO!
// ============================================================================
// O que é isso? É a lógica de RESERVA (agendamento)
// 
// Imagina que você é o gerente de agendamentos de uma empresa.
// Quando alguém quer marcar uma sala:
//   1. Verifica se o usuário existe
//   2. Verifica se a sala existe
//   3. Verifica se já tem alguém marcado naquele horário (conflito!)
//   4. Verifica se a sala está disponível naquele dia da semana
//   5. Verifica se tá dentro do horário de funcionamento
//   6. Se tudo certo, salva no caderno (banco)
//
// Esse é o Service mais CHATÃO porque tem MUITA validação!
// Mas é o mais importante porque evita problemas.
//
// ============================================================================

@Service
// @Service = "Ó Spring, esse é um Service!"
@Transactional
// @Transactional = "Se der erro, volta tudo! (Tudo ou nada!)"
public class ReservationService {
    
    // ============================================================================
    // 📚 INJEÇÃO DE DEPENDÊNCIA - 3 Repositórios!
    // ============================================================================
    // Por que 3? Porque a gente precisa validar 3 coisas diferentes!
    //
    @Autowired
    private ReservationRepository reservationRepository;
    // Esse = acesso às RESERVAS no banco
    // "Me dá uma reserva", "Delete uma reserva", etc
    
    @Autowired
    private UserRepository userRepository;
    // Esse = acesso aos USUÁRIOS no banco
    // "Existe esse usuário?", "Me dá o usuário com ID 5", etc
    
    @Autowired
    private ResourceRepository resourceRepository;
    // Esse = acesso aos RECURSOS no banco
    // "Existe essa sala?", "Me dá a sala com ID 3", etc
    
    // ============================================================================
    // 📋 MÉTODO: listarTodos()
    // ============================================================================
    // O que faz? Busca TODAS as reservas do banco
    // Fluxo: Controller → listarTodos() → banco → volta lista
    //
    public List<ReservationDto> listarTodos() {
        // Step 1: Vai no banco e pede TODAS as reservas
        // findAll() = "me dá tudo!"
        List<ReservationModel> reservas = reservationRepository.findAll();
        
        // Step 2: Cria uma lista vazia de DTOs
        List<ReservationDto> reservasDto = new ArrayList<>();
        
        // Step 3: Para cada reserva, converte pra DTO
        for (ReservationModel reserva : reservas) {
            reservasDto.add(converterParaDTO(reserva));
        }
        
        // Step 4: Retorna a lista
        return reservasDto;
    }
    
    // ============================================================================
    // 🔍 MÉTODO: buscarPorId(Long id)
    // ============================================================================
    // O que faz? Busca UMA ÚNICA reserva pelo ID
    // Fluxo: Controller → buscarPorId(7) → banco → volta 1 reserva
    //
    public ReservationDto buscarPorId(Long id) {
        // Step 1: Busca a reserva no banco
        // findById(id) = "acha esse ID pra mim"
        // .orElseThrow() = "se não achar, EXPLODE!"
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada com ID: " + id));
        
        // Step 2: Converte pra DTO
        return converterParaDTO(reservation);
    }
    
    // ============================================================================
    // ➕ MÉTODO: salvar(ReservationDto reservationDTO)
    // ============================================================================
    // O que faz? Cria uma nova reserva no banco
    // Fluxo: Controller → salvar(dados) → valida TUDO → salva
    //
    // ⚠️ ESSE É O MÉTODO MAIS IMPORTANTE! Tem MUITAS validações!
    //
    public ReservationDto salvar(ReservationDto reservationDTO) {
        // ========== VALIDAÇÃO 1: DADOS BÁSICOS ==========
        // Verifica se os dados vieram preenchidos (não podem estar vazios)
        validarReserva(reservationDTO);
        // Se falhar aqui, explode com erro e não continua!
        
        // ========== VALIDAÇÃO 2: USUÁRIO EXISTE? ==========
        // Procura o usuário que quer fazer a reserva
        // Se não encontrar, explode!
        UsuarioModel colaborador = userRepository.findById(reservationDTO.getColaboradorId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + reservationDTO.getColaboradorId()));
        // Agora temos o usuário guardado em 'colaborador'
        
        // ========== VALIDAÇÃO 3: RECURSO EXISTE? ==========
        // Procura o recurso (sala, projetor, etc) que quer reservar
        // Se não encontrar, explode!
        ResourceModel recurso = resourceRepository.findById(reservationDTO.getRecursoId())
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com ID: " + reservationDTO.getRecursoId()));
        // Agora temos o recurso guardado em 'recurso'
        
        // ========== VALIDAÇÃO 4: CONFLITO DE AGENDAMENTO! ==========
        // SUPER IMPORTANTE! Verifica se alguém JÁ marcou nesse horário
        // null = não é uma atualização, é uma nova reserva
        // Se houver conflito, explode!
        validarConflitosReserva(null, recurso, reservationDTO.getData(),
                reservationDTO.getHoraInicial(), reservationDTO.getHoraFinal());
        
        // ========== VALIDAÇÃO 5: RECURSO DISPONÍVEL? ==========
        // Verifica se o recurso tá disponível naquele dia/horário
        // (fecha em alguns dias? só funciona até certa hora? etc)
        // Se não tiver disponível, explode!
        validarDisponibilidadeRecurso(recurso, reservationDTO.getData(),
                reservationDTO.getHoraInicial(), reservationDTO.getHoraFinal());
        
        // ========== SE CHEGOU AQUI, ESTÁ TUDO CERTO! ==========
        
        // Step 1: Cria uma nova reserva (objeto vazio)
        ReservationModel reservation = new ReservationModel();
        
        // Step 2: Coloca os dados nessa reserva nova
        reservation.setColaborador(colaborador);
        // Coloca qual usuário quer a reserva
        
        reservation.setRecurso(recurso);
        // Coloca qual recurso quer
        
        reservation.setData(reservationDTO.getData());
        // Coloca a data
        
        reservation.setHoraInicial(reservationDTO.getHoraInicial());
        // Coloca a hora que começa
        
        reservation.setHoraFinal(reservationDTO.getHoraFinal());
        // Coloca a hora que termina
        
        reservation.setDataCancelamento(null);
        // Começava sem cancelamento (está marcada!)
        
        reservation.setObservacao(null);
        // Começava sem observação
        
        // Step 3: Salva no banco
        reservation = reservationRepository.save(reservation);
        // O banco retorna a reserva COM ID auto-gerado
        
        // Step 4: Converte pra DTO e retorna
        return converterParaDTO(reservation);
    }
    
    // ============================================================================
    // ❌ MÉTODO: cancelar(Long id, String observacao)
    // ============================================================================
    // O que faz? Cancela uma reserva (marca como cancelada)
    // Fluxo: Controller → cancelar(7, "motivo") → valida → marca cancelada
    //
    // Importante: Não DELETA! Só marca como cancelada!
    // (porque precisa de registro histórico)
    //
    public ReservationDto cancelar(Long id, String observacao) {
        // Step 1: Busca a reserva que quer cancelar
        // Se não encontrar, explode!
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada com ID: " + id));
        
        // ========== VALIDAÇÃO 1: OBSERVAÇÃO OBRIGATÓRIA ==========
        // Precisa dizer o MOTIVO do cancelamento
        if (observacao == null || observacao.trim().isEmpty()) {
            throw new RuntimeException("Observação do motivo do cancelamento é obrigatória");
        }
        // Se não tiver motivo, não cancela!
        
        // ========== VALIDAÇÃO 2: COM ANTECEDÊNCIA MÍNIMA ==========
        // Regra: Só pode cancelar COM ANTECEDÊNCIA!
        // Não pode cancelar reserva que tá pra acontecer hoje!
        
        LocalDate hoje = LocalDate.now();
        // Hoje = a data de hoje
        
        if (reservation.getData().isBefore(hoje.plusDays(1))) {
            // isBefore() = "é antes de?"
            // plusDays(1) = "hoje + 1 dia" = amanhã
            // Se a reserva for ANTES de amanhã, já passou!
            throw new RuntimeException("Cancelamento só pode ser realizado com pelo menos 1 dia de antecedência");
        }
        // Ou seja: Só pode cancelar se faltam 1 dia ou mais
        
        // ========== VALIDAÇÃO 3: NÃO PODE CANCELAR DUAS VEZES ==========
        // Se já foi cancelada, não cancela de novo!
        if (reservation.getDataCancelamento() != null) {
            // getDataCancelamento() != null = "já tem data de cancelamento"
            throw new RuntimeException("Reserva já foi cancelada");
        }
        
        // ========== SE CHEGOU AQUI, PODE CANCELAR! ==========
        
        // Step 2: Coloca a data de hoje como data de cancelamento
        reservation.setDataCancelamento(LocalDate.now());
        // Marca "foi cancelada em [data de hoje]"
        
        // Step 3: Coloca a observação (motivo do cancelamento)
        reservation.setObservacao(observacao);
        // "cancelada porque X"
        
        // Step 4: Salva as mudanças no banco
        reservation = reservationRepository.save(reservation);
        
        // Step 5: Retorna em formato DTO
        return converterParaDTO(reservation);
    }
    
    // ============================================================================
    // ✅ MÉTODO: validarReserva(ReservationDto reservationDTO) - PRIVADO
    // ============================================================================
    // O que faz? Verifica se os dados BÁSICOS da reserva tão corretos
    // Fluxo: valida cada regra uma por uma
    // Se alguma falhar, EXPLODE e não continua!
    //
    private void validarReserva(ReservationDto reservationDTO) {
        // REGRA 1: COLABORADOR OBRIGATÓRIO
        // Tem que saber QUEM quer fazer a reserva
        if (reservationDTO.getColaboradorId() == null) {
            throw new RuntimeException("Colaborador é obrigatório");
        }
        
        // REGRA 2: RECURSO OBRIGATÓRIO
        // Tem que saber O QUE quer reservar
        if (reservationDTO.getRecursoId() == null) {
            throw new RuntimeException("Recurso é obrigatório");
        }
        
        // REGRA 3: DATA OBRIGATÓRIA
        // Tem que saber QUANDO quer reservar
        if (reservationDTO.getData() == null) {
            throw new RuntimeException("Data é obrigatória");
        }
        
        // REGRA 4: VERIFICA SE USUÁRIO EXISTE
        // Não é só saber o ID, tem que o ID realmente estar no banco!
        if (!userRepository.existsById(reservationDTO.getColaboradorId())) {
            throw new RuntimeException("Colaborador não encontrado com ID: " + reservationDTO.getColaboradorId());
        }
        
        // REGRA 5: VERIFICA SE RECURSO EXISTE
        // Não é só saber o ID, tem que o ID realmente estar no banco!
        if (!resourceRepository.existsById(reservationDTO.getRecursoId())) {
            throw new RuntimeException("Recurso não encontrado com ID: " + reservationDTO.getRecursoId());
        }
        
        // REGRA 6: HORÁRIOS TÊM QUE SER VÁLIDOS
        // A hora de começar tem que ser ANTES da hora de terminar!
        if (reservationDTO.getHoraInicial() != null && reservationDTO.getHoraFinal() != null) {
            if (reservationDTO.getHoraInicial().isAfter(reservationDTO.getHoraFinal()) ||
                reservationDTO.getHoraInicial().equals(reservationDTO.getHoraFinal())) {
                // isAfter() = "é depois de?"
                // equals() = "é igual?"
                throw new RuntimeException("Hora inicial deve ser anterior à hora final");
            }
            // Se passou aqui, as horas tão OK!
        }
    }
    
    // ============================================================================
    // ⚡ MÉTODO: validarConflitosReserva(Long reservaId, ResourceModel recurso, ...) - PRIVADO
    // ============================================================================
    // O que faz? Verifica se JÁ TEM ALGUÉM AGENDADO NO MESMO HORÁRIO!
    // 
    // Imagina:
    //   - Alguém marca Sala A, segunda, 10h-11h
    //   - Outro tenta marcar Sala A, segunda, 10:30h-11:30h
    //   - CONFLITO! Não pode!
    //
    // Fluxo: procura no banco, se achar alguém no mesmo tempo, EXPLODE!
    //
    private void validarConflitosReserva(Long reservaId, ResourceModel recurso, LocalDate data,
                                        LocalTime horaInicial, LocalTime horaFinal) {
        // Step 1: Decide qual busca fazer (depende se é nova ou atualização)
        List<ReservationModel> conflitos;
        
        if (reservaId != null) {
            // É uma ATUALIZAÇÃO (já tem ID)
            // Busca conflitos MENOS ESSA RESERVA (que a gente tá mudando)
            // (senão ela conflitaria consigo mesma!)
            conflitos = reservationRepository.findConflitosReservaExcluindoId(
                    reservaId, recurso, data, horaInicial, horaFinal);
        } else {
            // É uma NOVA RESERVA (sem ID)
            // Busca conflitos normalmente
            conflitos = reservationRepository.findConflitosReserva(
                    recurso, data, horaInicial, horaFinal);
        }
        
        // Step 2: Se encontrou conflitos, explode!
        if (!conflitos.isEmpty()) {
            // !isEmpty() = "se tem alguma coisa?"
            throw new RuntimeException("Conflito de reserva: Já existe uma reserva para este recurso no mesmo horário");
        }
        // Se passou aqui, não tem ninguém agendado! Pode prosseguir!
    }
    
    // ============================================================================
    // 📅 MÉTODO: validarDisponibilidadeRecurso(...) - PRIVADO
    // ============================================================================
    // O que faz? Verifica se o RECURSO está DISPONÍVEL naquele dia/horário
    // 
    // O recurso pode ter restrições:
    //   - Só funciona em certos dias (exemplo: segunda a sexta)
    //   - Só funciona em certo horário (exemplo: 8h às 18h)
    //   - Só funciona em certo período (exemplo: janeiro a dezembro)
    //
    // Fluxo: verifica cada restrição uma por uma
    //
    private void validarDisponibilidadeRecurso(ResourceModel recurso, LocalDate data,
                                              LocalTime horaInicial, LocalTime horaFinal) {
        // ========== VALIDAÇÃO 1: ESTÁ NA FAIXA DE DATA? ==========
        // Exemplo: Sala A só pode ser reservada entre 1 de janeiro e 31 de dezembro
        // Se tentar em 15 de janeiro, tá OK
        // Se tentar em 15 de dezembro, tá OK
        // Se tentar em 32 de janeiro, ERRO!
        
        if (data.isBefore(recurso.getDataInicialAgendamento()) ||
            data.isAfter(recurso.getDataFinalAgendamento())) {
            // isBefore() = "é antes de?"
            // isAfter() = "é depois de?"
            throw new RuntimeException("Data da reserva está fora do período de disponibilidade do recurso");
        }
        // Se passou aqui, a data tá OK!
        
        // ========== VALIDAÇÃO 2: ESTÁ NA FAIXA DE HORÁRIO? ==========
        // Exemplo: Sala A funciona de 8h às 18h
        // Se tentar 9h-10h, tá OK
        // Se tentar 17h-18h, tá OK
        // Se tentar 19h-20h, ERRO!
        
        if (horaInicial.isBefore(recurso.getHoraInicialAgendamento()) ||
            horaFinal.isAfter(recurso.getHoraFinalAgendamento())) {
            // isBefore() = "é antes de?"
            // isAfter() = "é depois de?"
            throw new RuntimeException("Horário da reserva está fora do período de disponibilidade do recurso");
        }
        // Se passou aqui, o horário tá OK!
        
        // ========== VALIDAÇÃO 3: É UM DIA QUE FUNCIONA? ==========
        // Exemplo: Sala A funciona segunda, terça, quarta (não funciona sábado/domingo)
        // Se tentar segunda, tá OK
        // Se tentar sábado, ERRO!
        
        if (recurso.getDiasSemanaDisponivel() != null && !recurso.getDiasSemanaDisponivel().isEmpty()) {
            // getDiasSemanaDisponivel() = pega a string com os dias
            // Exemplo: "segunda-feira,terça-feira,quarta-feira"
            
            // Step 1: Descobrir que dia da semana é a data da reserva
            String diaSemana = obterDiaSemana(data);
            // Exemplo: 10 de janeiro de 2025 é uma sexta-feira?
            // diaSemana = "sexta-feira"
            
            // Step 2: Pega a lista de dias que o recurso funciona
            String[] diasDisponiveis = recurso.getDiasSemanaDisponivel().split(",");
            // split(",") = divide pelos vírgulas
            // Exemplo: "segunda,terça,quarta".split(",") → ["segunda", "terça", "quarta"]
            
            // Step 3: Procura se o dia da reserva tá nessa lista
            boolean diaDisponivel = false;
            // Começa acreditando que NÃO tá disponível
            
            for (String dia : diasDisponiveis) {
                // Para cada dia que o recurso funciona:
                if (dia.trim().equalsIgnoreCase(diaSemana)) {
                    // trim() = remove espaços
                    // equalsIgnoreCase() = "é igual? (ignore maiúsculas/minúsculas)"
                    
                    diaDisponivel = true;
                    // "Achei! O dia é disponível!"
                    break;
                    // Sai do loop (já achou, não precisa procurar mais)
                }
            }
            
            // Step 4: Se o dia não foi encontrado, explode!
            if (!diaDisponivel) {
                throw new RuntimeException("Recurso não está disponível para " + diaSemana);
            }
        }
        // Se passou aqui, o dia da semana tá OK!
    }
    
    // ============================================================================
    // 📆 MÉTODO: obterDiaSemana(LocalDate data) - PRIVADO
    // ============================================================================
    // O que faz? Transforma uma data em nome do dia da semana
    // Entrada: 10 de janeiro de 2025
    // Saída: "sexta-feira"
    //
    private String obterDiaSemana(LocalDate data) {
        // data.getDayOfWeek() = pega qual dia da semana é
        // Retorna um número: MONDAY, TUESDAY, WEDNESDAY, etc
        
        // switch = "de acordo com o valor, faz isso"
        return switch (data.getDayOfWeek()) {
            case MONDAY -> "segunda-feira";
            // Se for segunda, retorna "segunda-feira"
            
            case TUESDAY -> "terça-feira";
            // Se for terça, retorna "terça-feira"
            
            case WEDNESDAY -> "quarta-feira";
            // Se for quarta, retorna "quarta-feira"
            
            case THURSDAY -> "quinta-feira";
            // Se for quinta, retorna "quinta-feira"
            
            case FRIDAY -> "sexta-feira";
            // Se for sexta, retorna "sexta-feira"
            
            case SATURDAY -> "sábado";
            // Se for sábado, retorna "sábado"
            
            case SUNDAY -> "domingo";
            // Se for domingo, retorna "domingo"
        };
        // Exemplo: data = 10 janeiro 2025 (sexta)
        // getDayOfWeek() = FRIDAY
        // Retorna "sexta-feira"
    }
    
    // ============================================================================
    // 🔄 MÉTODO: converterParaDTO(ReservationModel reservation) - PRIVADO
    // ============================================================================
    // O que faz? Transforma uma RESERVA do banco em DTO
    // (do banco para mostrar na página)
    //
    private ReservationDto converterParaDTO(ReservationModel reservation) {
        // Step 1: Cria um DTO vazio
        ReservationDto dto = new ReservationDto();
        
        // Step 2: Copia cada campo do Model pro DTO
        dto.setId(reservation.getId());
        // Copia o ID
        
        dto.setColaboradorId(reservation.getColaborador().getId());
        // Copia o ID do colaborador
        
        dto.setRecursoId(reservation.getRecurso().getId());
        // Copia o ID do recurso
        
        dto.setData(reservation.getData());
        // Copia a data
        
        dto.setHoraInicial(reservation.getHoraInicial());
        // Copia a hora inicial
        
        dto.setHoraFinal(reservation.getHoraFinal());
        // Copia a hora final
        
        dto.setDataCancelamento(reservation.getDataCancelamento());
        // Copia a data de cancelamento (pode ser null se não foi cancelada)
        
        dto.setObservacao(reservation.getObservacao());
        // Copia a observação
        
        // Step 3: Coloca dados ADICIONAIS (que não tão no Model)
        // Essas linhas pegam o NOME do colaborador (não só ID)
        dto.setNomeColaborador(reservation.getColaborador().getNome());
        // getNome() = vai lá no objeto Colaborador e pega o nome dele
        
        // E coloca a DESCRIÇÃO do recurso (não só ID)
        dto.setDescricaoRecurso(reservation.getRecurso().getDescricao());
        // getDescricao() = vai lá no objeto Recurso e pega a descrição dele
        
        // Step 4: Retorna o DTO pronto pra mostrar
        return dto;
    }
}
// ============================================================================
// 🎉 FIM DO RESERVATIONSERVICE
// ============================================================================
// Resumo: esse Service é o MAIS IMPORTANTE! Responsável por:
//   ✅ Listar todas as reservas
//   ✅ Buscar uma reserva específica
//   ✅ CRIAR uma nova reserva (COM TONELADA DE VALIDAÇÃO!)
//   ✅ CANCELAR uma reserva (COM VALIDAÇÃO!)
//   ✅ Validar dados básicos
//   ✅ Validar CONFLITOS (alguém já marcou?)
//   ✅ Validar DISPONIBILIDADE (recurso funciona nesse dia/hora?)
//   ✅ Converter entre Model e DTO
//
// FLUXO DE CRIAR UMA RESERVA (o mais complexo):
//   1. Controller envia dados
//   2. Service valida dados básicos (tá tudo preenchido?)
//   3. Service busca usuário (existe?)
//   4. Service busca recurso (existe?)
//   5. Service valida conflitos (alguém já marcou no mesmo horário?)
//   6. Service valida disponibilidade (recurso funciona nesse dia/hora?)
//   7. Se tudo passou, cria objeto Model e salva no banco
//   8. Retorna o DTO pro Controller mostrar
//
// Se ALGUMA dessas validações falhar, TUDO É CANCELADO (Transactional)
// Por isso é super seguro!
// ============================================================================

