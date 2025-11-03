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

@Repository
public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {
    
    List<ReservationModel> findByRecurso(ResourceModel recurso);
    
    @Query("SELECT r FROM ReservationModel r WHERE r.recurso = :recurso " +
           "AND r.data = :data " +
           "AND r.dataCancelamento IS NULL " +
           "AND ((r.horaInicial <= :horaInicial AND r.horaFinal > :horaInicial) " +
           "OR (r.horaInicial < :horaFinal AND r.horaFinal >= :horaFinal) " +
           "OR (r.horaInicial >= :horaInicial AND r.horaFinal <= :horaFinal))")
    List<ReservationModel> findConflitosReserva(@Param("recurso") ResourceModel recurso,
                                           @Param("data") LocalDate data,
                                           @Param("horaInicial") LocalTime horaInicial,
                                           @Param("horaFinal") LocalTime horaFinal);
    
    @Query("SELECT r FROM ReservationModel r WHERE r.id != :reservaId " +
           "AND r.recurso = :recurso " +
           "AND r.data = :data " +
           "AND r.dataCancelamento IS NULL " +
           "AND ((r.horaInicial <= :horaInicial AND r.horaFinal > :horaInicial) " +
           "OR (r.horaInicial < :horaFinal AND r.horaFinal >= :horaFinal) " +
           "OR (r.horaInicial >= :horaInicial AND r.horaFinal <= :horaFinal))")
    List<ReservationModel> findConflitosReservaExcluindoId(@Param("reservaId") Long reservaId,
                                                      @Param("recurso") ResourceModel recurso,
                                                      @Param("data") LocalDate data,
                                                      @Param("horaInicial") LocalTime horaInicial,
                                                      @Param("horaFinal") LocalTime horaFinal);
}

