package com.empresa.agendamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "recursos")
public class ResourceModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Column(nullable = false)
    private String descricao;
    
    @NotBlank(message = "Tipo é obrigatório")
    @Column(nullable = false)
    private String tipo;
    
    @Column(length = 100)
    private String diasSemanaDisponivel;
    
    @Column(nullable = false)
    private LocalDate dataInicialAgendamento;
    
    @Column(nullable = false)
    private LocalDate dataFinalAgendamento;
    
    @Column(nullable = false)
    private LocalTime horaInicialAgendamento;
    
    @Column(nullable = false)
    private LocalTime horaFinalAgendamento;
    
    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL)
    private List<ReservationModel> reservas;
    
    public ResourceModel() {
    }
    
    public ResourceModel(String descricao, String tipo, String diasSemanaDisponivel,
                   LocalDate dataInicialAgendamento, LocalDate dataFinalAgendamento,
                   LocalTime horaInicialAgendamento, LocalTime horaFinalAgendamento) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.diasSemanaDisponivel = diasSemanaDisponivel;
        this.dataInicialAgendamento = dataInicialAgendamento;
        this.dataFinalAgendamento = dataFinalAgendamento;
        this.horaInicialAgendamento = horaInicialAgendamento;
        this.horaFinalAgendamento = horaFinalAgendamento;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getDiasSemanaDisponivel() {
        return diasSemanaDisponivel;
    }
    
    public void setDiasSemanaDisponivel(String diasSemanaDisponivel) {
        this.diasSemanaDisponivel = diasSemanaDisponivel;
    }
    
    public LocalDate getDataInicialAgendamento() {
        return dataInicialAgendamento;
    }
    
    public void setDataInicialAgendamento(LocalDate dataInicialAgendamento) {
        this.dataInicialAgendamento = dataInicialAgendamento;
    }
    
    public LocalDate getDataFinalAgendamento() {
        return dataFinalAgendamento;
    }
    
    public void setDataFinalAgendamento(LocalDate dataFinalAgendamento) {
        this.dataFinalAgendamento = dataFinalAgendamento;
    }
    
    public LocalTime getHoraInicialAgendamento() {
        return horaInicialAgendamento;
    }
    
    public void setHoraInicialAgendamento(LocalTime horaInicialAgendamento) {
        this.horaInicialAgendamento = horaInicialAgendamento;
    }
    
    public LocalTime getHoraFinalAgendamento() {
        return horaFinalAgendamento;
    }
    
    public void setHoraFinalAgendamento(LocalTime horaFinalAgendamento) {
        this.horaFinalAgendamento = horaFinalAgendamento;
    }
    
    public List<ReservationModel> getReservas() {
        return reservas;
    }
    
    public void setReservas(List<ReservationModel> reservas) {
        this.reservas = reservas;
    }
}

