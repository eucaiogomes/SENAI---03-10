package com.empresa.agendamento.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ResourceDto {
    
    private Long id;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    
    @NotBlank(message = "Tipo é obrigatório")
    private String tipo;
    
    private List<String> diasSemanaDisponivel;
    
    @NotNull(message = "Data inicial é obrigatória")
    private LocalDate dataInicialAgendamento;
    
    @NotNull(message = "Data final é obrigatória")
    private LocalDate dataFinalAgendamento;
    
    @NotNull(message = "Hora inicial é obrigatória")
    private LocalTime horaInicialAgendamento;
    
    @NotNull(message = "Hora final é obrigatória")
    private LocalTime horaFinalAgendamento;
    
    public ResourceDto() {
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
    
    public List<String> getDiasSemanaDisponivel() {
        return diasSemanaDisponivel;
    }
    
    public void setDiasSemanaDisponivel(List<String> diasSemanaDisponivel) {
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
}

