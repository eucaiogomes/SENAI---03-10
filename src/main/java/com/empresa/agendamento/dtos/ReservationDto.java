package com.empresa.agendamento.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDto {
    
    private Long id;
    
    @NotNull(message = "Colaborador é obrigatório")
    private Long colaboradorId;
    
    @NotNull(message = "Recurso é obrigatório")
    private Long recursoId;
    
    @NotNull(message = "Data é obrigatória")
    private LocalDate data;
    
    @NotNull(message = "Hora inicial é obrigatória")
    private LocalTime horaInicial;
    
    @NotNull(message = "Hora final é obrigatória")
    private LocalTime horaFinal;
    
    private LocalDate dataCancelamento;
    
    private String observacao;
    
    // Campos para exibição
    private String nomeColaborador;
    private String descricaoRecurso;
    
    public ReservationDto() {
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getColaboradorId() {
        return colaboradorId;
    }
    
    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
    }
    
    public Long getRecursoId() {
        return recursoId;
    }
    
    public void setRecursoId(Long recursoId) {
        this.recursoId = recursoId;
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
    
    public String getNomeColaborador() {
        return nomeColaborador;
    }
    
    public void setNomeColaborador(String nomeColaborador) {
        this.nomeColaborador = nomeColaborador;
    }
    
    public String getDescricaoRecurso() {
        return descricaoRecurso;
    }
    
    public void setDescricaoRecurso(String descricaoRecurso) {
        this.descricaoRecurso = descricaoRecurso;
    }
}

