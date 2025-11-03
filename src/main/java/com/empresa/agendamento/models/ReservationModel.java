package com.empresa.agendamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservas")
public class ReservationModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Colaborador é obrigatório")
    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private UsuarioModel colaborador;
    
    @NotNull(message = "Recurso é obrigatório")
    @ManyToOne
    @JoinColumn(name = "recurso_id", nullable = false)
    private ResourceModel recurso;
    
    @NotNull(message = "Data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;
    
    @NotNull(message = "Hora inicial é obrigatória")
    @Column(nullable = false)
    private LocalTime horaInicial;
    
    @NotNull(message = "Hora final é obrigatória")
    @Column(nullable = false)
    private LocalTime horaFinal;
    
    @Column
    private LocalDate dataCancelamento;
    
    @Column(length = 500)
    private String observacao;
    
    public ReservationModel() {
    }
    
    public ReservationModel(UsuarioModel colaborador, ResourceModel recurso, LocalDate data,
                      LocalTime horaInicial, LocalTime horaFinal) {
        this.colaborador = colaborador;
        this.recurso = recurso;
        this.data = data;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.dataCancelamento = null;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UsuarioModel getColaborador() {
        return colaborador;
    }
    
    public void setColaborador(UsuarioModel colaborador) {
        this.colaborador = colaborador;
    }
    
    public ResourceModel getRecurso() {
        return recurso;
    }
    
    public void setRecurso(ResourceModel recurso) {
        this.recurso = recurso;
    }
    
    public LocalDate getData() {
        return data;
    }
    
    public void setData(LocalDate data) {
        this.data = data;
    }
    
    public LocalTime getHoraInicial() {
        return horaInicial;
    }
    
    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }
    
    public LocalTime getHoraFinal() {
        return horaFinal;
    }
    
    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }
    
    public LocalDate getDataCancelamento() {
        return dataCancelamento;
    }
    
    public void setDataCancelamento(LocalDate dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }
    
    public String getObservacao() {
        return observacao;
    }
    
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    
    public boolean isCancelada() {
        return dataCancelamento != null;
    }
}

