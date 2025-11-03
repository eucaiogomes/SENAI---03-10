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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ResourceRepository resourceRepository;
    
    public List<ReservationDto> listarTodos() {
        return reservationRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    public ReservationDto buscarPorId(Long id) {
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada com ID: " + id));
        return converterParaDTO(reservation);
    }
    
    public ReservationDto salvar(ReservationDto reservationDTO) {
        validarReserva(reservationDTO);
        
        UsuarioModel colaborador = userRepository.findById(reservationDTO.getColaboradorId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com ID: " + reservationDTO.getColaboradorId()));
        
        ResourceModel recurso = resourceRepository.findById(reservationDTO.getRecursoId())
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com ID: " + reservationDTO.getRecursoId()));
        
        // Validação de conflitos de reserva (RF06)
        validarConflitosReserva(null, recurso, reservationDTO.getData(),
                reservationDTO.getHoraInicial(), reservationDTO.getHoraFinal());
        
        // Validação de disponibilidade do recurso
        validarDisponibilidadeRecurso(recurso, reservationDTO.getData(),
                reservationDTO.getHoraInicial(), reservationDTO.getHoraFinal());
        
        ReservationModel reservation = new ReservationModel();
        reservation.setColaborador(colaborador);
        reservation.setRecurso(recurso);
        reservation.setData(reservationDTO.getData());
        reservation.setHoraInicial(reservationDTO.getHoraInicial());
        reservation.setHoraFinal(reservationDTO.getHoraFinal());
        reservation.setDataCancelamento(null);
        reservation.setObservacao(null);
        
        reservation = reservationRepository.save(reservation);
        return converterParaDTO(reservation);
    }
    
    public ReservationDto cancelar(Long id, String observacao) {
        ReservationModel reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada com ID: " + id));
        
        // Regra 10: Observação obrigatória no cancelamento
        if (observacao == null || observacao.trim().isEmpty()) {
            throw new RuntimeException("Observação do motivo do cancelamento é obrigatória");
        }
        
        // Regra 7: Cancelamento só pode acontecer 1 dia antes da data agendada
        LocalDate hoje = LocalDate.now();
        if (reservation.getData().isBefore(hoje.plusDays(1))) {
            throw new RuntimeException("Cancelamento só pode ser realizado com pelo menos 1 dia de antecedência");
        }
        
        // Regra 9: Não permite cancelar reserva já cancelada
        if (reservation.getDataCancelamento() != null) {
            throw new RuntimeException("Reserva já foi cancelada");
        }
        
        reservation.setDataCancelamento(LocalDate.now());
        reservation.setObservacao(observacao);
        
        reservation = reservationRepository.save(reservation);
        return converterParaDTO(reservation);
    }
    
    private void validarReserva(ReservationDto reservationDTO) {
        // Regra 1: Colaborador obrigatório
        if (reservationDTO.getColaboradorId() == null) {
            throw new RuntimeException("Colaborador é obrigatório");
        }
        
        // Regra 2: Recurso obrigatório
        if (reservationDTO.getRecursoId() == null) {
            throw new RuntimeException("Recurso é obrigatório");
        }
        
        // Regra 3: Data obrigatória
        if (reservationDTO.getData() == null) {
            throw new RuntimeException("Data é obrigatória");
        }
        
        // Regra 5: Validar se colaborador existe
        if (!userRepository.existsById(reservationDTO.getColaboradorId())) {
            throw new RuntimeException("Colaborador não encontrado com ID: " + reservationDTO.getColaboradorId());
        }
        
        // Regra 6: Validar se recurso existe
        if (!resourceRepository.existsById(reservationDTO.getRecursoId())) {
            throw new RuntimeException("Recurso não encontrado com ID: " + reservationDTO.getRecursoId());
        }
        
        // Validação de horários
        if (reservationDTO.getHoraInicial() != null && reservationDTO.getHoraFinal() != null) {
            if (reservationDTO.getHoraInicial().isAfter(reservationDTO.getHoraFinal()) ||
                reservationDTO.getHoraInicial().equals(reservationDTO.getHoraFinal())) {
                throw new RuntimeException("Hora inicial deve ser anterior à hora final");
            }
        }
    }
    
    private void validarConflitosReserva(Long reservaId, ResourceModel recurso, LocalDate data,
                                        LocalTime horaInicial, LocalTime horaFinal) {
        List<ReservationModel> conflitos;
        
        if (reservaId != null) {
            conflitos = reservationRepository.findConflitosReservaExcluindoId(
                    reservaId, recurso, data, horaInicial, horaFinal);
        } else {
            conflitos = reservationRepository.findConflitosReserva(
                    recurso, data, horaInicial, horaFinal);
        }
        
        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Conflito de reserva: Já existe uma reserva para este recurso no mesmo horário");
        }
    }
    
    private void validarDisponibilidadeRecurso(ResourceModel recurso, LocalDate data,
                                              LocalTime horaInicial, LocalTime horaFinal) {
        // Validação de data inicial e final do recurso
        if (data.isBefore(recurso.getDataInicialAgendamento()) ||
            data.isAfter(recurso.getDataFinalAgendamento())) {
            throw new RuntimeException("Data da reserva está fora do período de disponibilidade do recurso");
        }
        
        // Validação de horários do recurso
        if (horaInicial.isBefore(recurso.getHoraInicialAgendamento()) ||
            horaFinal.isAfter(recurso.getHoraFinalAgendamento())) {
            throw new RuntimeException("Horário da reserva está fora do período de disponibilidade do recurso");
        }
        
        // Validação de dia da semana
        if (recurso.getDiasSemanaDisponivel() != null && !recurso.getDiasSemanaDisponivel().isEmpty()) {
            String diaSemana = obterDiaSemana(data);
            // Verifica se o dia da semana está na lista de dias disponíveis
            String[] diasDisponiveis = recurso.getDiasSemanaDisponivel().split(",");
            boolean diaDisponivel = false;
            for (String dia : diasDisponiveis) {
                if (dia.trim().equalsIgnoreCase(diaSemana)) {
                    diaDisponivel = true;
                    break;
                }
            }
            if (!diaDisponivel) {
                throw new RuntimeException("Recurso não está disponível para " + diaSemana);
            }
        }
    }
    
    private String obterDiaSemana(LocalDate data) {
        return switch (data.getDayOfWeek()) {
            case MONDAY -> "segunda-feira";
            case TUESDAY -> "terça-feira";
            case WEDNESDAY -> "quarta-feira";
            case THURSDAY -> "quinta-feira";
            case FRIDAY -> "sexta-feira";
            case SATURDAY -> "sábado";
            case SUNDAY -> "domingo";
        };
    }
    
    private ReservationDto converterParaDTO(ReservationModel reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setColaboradorId(reservation.getColaborador().getId());
        dto.setRecursoId(reservation.getRecurso().getId());
        dto.setData(reservation.getData());
        dto.setHoraInicial(reservation.getHoraInicial());
        dto.setHoraFinal(reservation.getHoraFinal());
        dto.setDataCancelamento(reservation.getDataCancelamento());
        dto.setObservacao(reservation.getObservacao());
        dto.setNomeColaborador(reservation.getColaborador().getNome());
        dto.setDescricaoRecurso(reservation.getRecurso().getDescricao());
        return dto;
    }
}

