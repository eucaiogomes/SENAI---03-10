package com.empresa.agendamento.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// ============================================================================
// 📦 RESOURCEDTO
// ============================================================================
// O que é isso? É um DTO (Data Transfer Object) = "Objeto pra Transferir Dados"
//
// Este DTO transfere dados de RECURSOS (salas, projetores, equipamentos, etc)
// Entre o formulário HTML e o banco de dados
//
// Fluxo:
// Formulário HTML → ResourceDto → Controller → Service → Banco
// Banco → Service → Controller → ResourceDto → Página HTML
//
// O que é um Recurso?
// Qualquer coisa que pode ser agendada/reservada
// Exemplos: Sala de reunião, Projetor, Equipamento, Veículo, etc
//
// ============================================================================

public class ResourceDto {
    
    // ============================================================================
    // 🆔 ID - Identificação única do recurso
    // ============================================================================
    // ID é criado automaticamente pelo banco (auto-increment)
    // 
    private Long id;

    // ============================================================================
    // 📝 DESCRIÇÃO - Nome/descrição do recurso
    // ============================================================================
    // @NotBlank = "Não pode estar vazio!"
    // Exemplo: "Sala de reunião 1", "Projetor Samsung", "Notebook Dell"
    // 
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    // ============================================================================
    // 🏷️ TIPO - Que tipo de recurso é?
    // ============================================================================
    // @NotBlank = "Não pode estar vazio!"
    // Exemplo: "sala", "projetor", "equipamento", "veículo"
    // Isso ajuda a filtrar recursos depois
    // 
    @NotBlank(message = "Tipo é obrigatório")
    private String tipo;

    // ============================================================================
    // 📅 DIAS DA SEMANA DISPONÍVEL - Que dias da semana funciona?
    // ============================================================================
    // Lista de dias: segunda-feira, terça-feira, quarta-feira, etc
    // Exemplo: [segunda-feira, terça-feira, quarta-feira, quinta-feira, sexta-feira]
    // Isso significa: funciona de segunda a sexta (não funciona sábado/domingo)
    // 
    private List<String> diasSemanaDisponivel;
    // List = lista de múltiplos itens

    // ============================================================================
    // 📅 DATA INICIAL DE AGENDAMENTO - A partir de qual data pode agendar?
    // ============================================================================
    // @NotNull = "Tem que ter uma data!"
    // Exemplo: 1 de janeiro de 2024
    // Antes dessa data, não pode agendar
    // 
    @NotNull(message = "Data inicial é obrigatória")
    private LocalDate dataInicialAgendamento;
    // LocalDate = uma data específica

    // ============================================================================
    // 📅 DATA FINAL DE AGENDAMENTO - Até qual data pode agendar?
    // ============================================================================
    // @NotNull = "Tem que ter uma data!"
    // Exemplo: 31 de dezembro de 2024
    // Depois dessa data, não pode agendar
    // 
    @NotNull(message = "Data final é obrigatória")
    private LocalDate dataFinalAgendamento;

    // ============================================================================
    // ⏰ HORA INICIAL DE AGENDAMENTO - A partir de qual hora funciona?
    // ============================================================================
    // @NotNull = "Tem que ter um horário!"
    // Exemplo: 08:00 (8 da manhã)
    // Antes dessa hora, não pode agendar
    // 
    @NotNull(message = "Hora inicial é obrigatória")
    private LocalTime horaInicialAgendamento;
    // LocalTime = um horário específico (hora e minutos)

    // ============================================================================
    // ⏰ HORA FINAL DE AGENDAMENTO - Até qual hora funciona?
    // ============================================================================
    // @NotNull = "Tem que ter um horário!"
    // Exemplo: 18:00 (6 da tarde)
    // Depois dessa hora, não pode agendar
    // 
    @NotNull(message = "Hora final é obrigatória")
    private LocalTime horaFinalAgendamento;

    // ============================================================================
    // 🏗️ CONSTRUTOR VAZIO
    // ============================================================================
    // Cria um ResourceDto vazio pra preencher depois
    // 
    public ResourceDto() {
    }

    // ============================================================================
    // 🔄 GETTERS E SETTERS
    // ============================================================================
    
    // ID
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // Descrição
    public String getDescricao() {
        return descricao;
        // Retorna a descrição do recurso
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
        // Guarda a descrição do recurso
    }

    // Tipo
    public String getTipo() {
        return tipo;
        // Retorna o tipo do recurso
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
        // Guarda o tipo do recurso
    }

    // Dias da Semana Disponível
    public List<String> getDiasSemanaDisponivel() {
        return diasSemanaDisponivel;
        // Retorna a lista de dias que o recurso funciona
    }
    public void setDiasSemanaDisponivel(List<String> diasSemanaDisponivel) {
        this.diasSemanaDisponivel = diasSemanaDisponivel;
        // Guarda a lista de dias
    }

    // Data Inicial de Agendamento
    public LocalDate getDataInicialAgendamento() {
        return dataInicialAgendamento;
        // Retorna a data inicial
    }
    public void setDataInicialAgendamento(LocalDate dataInicialAgendamento) {
        this.dataInicialAgendamento = dataInicialAgendamento;
        // Guarda a data inicial
    }

    // Data Final de Agendamento
    public LocalDate getDataFinalAgendamento() {
        return dataFinalAgendamento;
        // Retorna a data final
    }
    public void setDataFinalAgendamento(LocalDate dataFinalAgendamento) {
        this.dataFinalAgendamento = dataFinalAgendamento;
        // Guarda a data final
    }

    // Hora Inicial de Agendamento
    public LocalTime getHoraInicialAgendamento() {
        return horaInicialAgendamento;
        // Retorna a hora inicial
    }
    public void setHoraInicialAgendamento(LocalTime horaInicialAgendamento) {
        this.horaInicialAgendamento = horaInicialAgendamento;
        // Guarda a hora inicial
    }

    // Hora Final de Agendamento
    public LocalTime getHoraFinalAgendamento() {
        return horaFinalAgendamento;
        // Retorna a hora final
    }
    public void setHoraFinalAgendamento(LocalTime horaFinalAgendamento) {
        this.horaFinalAgendamento = horaFinalAgendamento;
        // Guarda a hora final
    }
}
// ============================================================================
// 🎉 FIM DO RESOURCEDTO
// ============================================================================
// Resumo: esse DTO guarda todas as informações de um RECURSO
// - ID (pra identificar)
// - Descrição (nome, pra saber qual é)
// - Tipo (que tipo é: sala, projetor, etc)
// - Dias da semana (que dias funciona)
// - Data inicial e final (qual período pode agendar)
// - Hora inicial e final (qual horário funciona)
// ============================================================================

