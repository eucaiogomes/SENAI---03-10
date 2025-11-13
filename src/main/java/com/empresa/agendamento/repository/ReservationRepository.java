package com.empresa.agendamento.repository;

import com.empresa.agendamento.models.ReservationModel;
import com.empresa.agendamento.models.ResourceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// @Repository = "Ó Spring, essa interface é um REPOSITORY!"
// Esse repository cuida da tabela de RESERVAS
// Aqui tem queries mais complicadas porque reservas têm conflitos!
@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {
    // JpaRepository<ReservationModel, Long>
    // ReservationModel = estou trabalhando com a tabela de reservas
    // Long = o ID é do tipo Long
    
    // Método simples: pega TODAS as reservas de um RECURSO
    // "Ó banco, que reservas tem pra essa sala?"
    List<ReservationModel> findByRecurso(ResourceModel recurso);
    
    // Método complexo com @Query customizada
    // Procura CONFLITOS de reserva
    // Um "conflito" é quando 2 reservas ocupam o MESMO recurso MESMA hora
    //
    // Exemplo de conflito:
    // Reserva 1: João reservou Sala 101 das 14:00 às 15:00
    // Reserva 2: Maria tenta reservar Sala 101 das 14:30 às 15:30
    // ❌ CONFLITO! Os horários se sobrepõem!
    //
    // A query checa TODOS OS CASOS de sobreposição:
    @Query("SELECT r FROM ReservationModel r WHERE r.recurso = :recurso " +
           "AND r.data = :data " +
           "AND r.dataCancelamento IS NULL " +  // Só reservas ATIVAS (não canceladas)
           "AND ((r.horaInicial <= :horaInicial AND r.horaFinal > :horaInicial) " +  // Caso 1: nova reserva começa dentro de uma existente
           "OR (r.horaInicial < :horaFinal AND r.horaFinal >= :horaFinal) " +         // Caso 2: nova reserva termina dentro de uma existente
           "OR (r.horaInicial >= :horaInicial AND r.horaFinal <= :horaFinal))")       // Caso 3: uma existente tá DENTRO da nova
    // Resultado: lista com TODAS as reservas que conflitam com a nova
    List<ReservationModel> findConflitosReserva(@Param("recurso") ResourceModel recurso,
                                           @Param("data") LocalDate data,
                                           @Param("horaInicial") LocalTime horaInicial,
                                           @Param("horaFinal") LocalTime horaFinal);
    
    // Método complexo pra ATUALIZAR uma reserva
    // Procura conflitos MAS EXCLUINDO a própria reserva que tá sendo editada
    // Por que? Porque quando tá editando, a reserva antiga ainda tá lá no banco!
    //
    // Exemplo:
    // Maria tinha uma reserva de 14:00 a 15:00
    // Maria quer mudar pra 14:30 a 15:30
    // Se a gente procurar conflitos normalmente, VA ACHAR CONFLITO COM A DELA MESMA! 😂
    // Então a gente exclui: WHERE r.id != :reservaId
    @Query("SELECT r FROM ReservationModel r WHERE r.id != :reservaId " +  // Exclui a própria reserva
           "AND r.recurso = :recurso " +
           "AND r.data = :data " +
           "AND r.dataCancelamento IS NULL " +  // Só reservas ATIVAS
           "AND ((r.horaInicial <= :horaInicial AND r.horaFinal > :horaInicial) " +
           "OR (r.horaInicial < :horaFinal AND r.horaFinal >= :horaFinal) " +
           "OR (r.horaInicial >= :horaInicial AND r.horaFinal <= :horaFinal))")
    List<ReservationModel> findConflitosReservaExcluindoId(@Param("reservaId") Long reservaId,
                                                      @Param("recurso") ResourceModel recurso,
                                                      @Param("data") LocalDate data,
                                                      @Param("horaInicial") LocalTime horaInicial,
                                                      @Param("horaFinal") LocalTime horaFinal);
}

