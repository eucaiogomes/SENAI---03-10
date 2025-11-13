package com.empresa.agendamento.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

// ============================================================================
// 📅 RESERVATIONDTO
// ============================================================================
// O que é isso? É um DTO (Data Transfer Object) = "Objeto pra Transferir Dados"
//
// Este DTO transfere dados de RESERVAS/AGENDAMENTOS
// Uma reserva = quando alguém marca uma sala, equipamento, veículo, etc
//
// Fluxo:
// Formulário HTML → ReservationDto → Controller → Service → Banco
// Banco → Service → Controller → ReservationDto → Página HTML
//
// O que é uma Reserva?
// É quando um USUÁRIO marca um RECURSO em um DIA/HORÁRIO específico
// Exemplo: João marca a Sala A no dia 10 de janeiro, 14h-15h
//
// ============================================================================

public class ReservationDto {
    
    // ============================================================================
    // 🆔 ID - Identificação única da reserva
    // ============================================================================
    // ID é criado automaticamente pelo banco
    // Cada reserva tem um número único
    // 
    private Long id;

    // ============================================================================
    // 👤 COLABORADOR ID - Quem está fazendo a reserva?
    // ============================================================================
    // @NotNull = "Tem que escolher um colaborador!"
    // É o ID do usuário que tá marcando a sala
    // Não é o nome, é o ID (número)
    // 
    @NotNull(message = "Colaborador é obrigatório")
    private Long colaboradorId;
    // Este é o ID do usuário que faz a reserva

    // ============================================================================
    // 📦 RECURSO ID - O que está sendo reservado?
    // ============================================================================
    // @NotNull = "Tem que escolher um recurso!"
    // É o ID do recurso que tá sendo marcado
    // Não é o nome, é o ID (número)
    // 
    @NotNull(message = "Recurso é obrigatório")
    private Long recursoId;
    // Este é o ID do recurso (sala, projetor, etc)

    // ============================================================================
    // 📅 DATA - Em qual dia?
    // ============================================================================
    // @NotNull = "Tem que escolher uma data!"
    // É o dia em que o recurso será usado
    // Exemplo: 10 de janeiro de 2024
    // 
    @NotNull(message = "Data é obrigatória")
    private LocalDate data;
    // LocalDate = uma data específica

    // ============================================================================
    // ⏰ HORA INICIAL - Que hora começa?
    // ============================================================================
    // @NotNull = "Tem que escolher uma hora inicial!"
    // É a hora que a reserva COMEÇA
    // Exemplo: 14:00 (2 da tarde)
    // 
    @NotNull(message = "Hora inicial é obrigatória")
    private LocalTime horaInicial;
    // LocalTime = um horário específico

    // ============================================================================
    // ⏰ HORA FINAL - Que hora termina?
    // ============================================================================
    // @NotNull = "Tem que escolher uma hora final!"
    // É a hora que a reserva TERMINA
    // Exemplo: 15:00 (3 da tarde)
    // 
    @NotNull(message = "Hora final é obrigatória")
    private LocalTime horaFinal;

    // ============================================================================
    // ❌ DATA DE CANCELAMENTO - Quando foi cancelada?
    // ============================================================================
    // Se for null (vazio), significa que a reserva ainda está ATIVA
    // Se tiver uma data, significa que foi CANCELADA naquele dia
    // Exemplo: se foi cancelada em 5 de janeiro, dataCancelamento = 5 de janeiro
    // 
    private LocalDate dataCancelamento;
    // null = não foi cancelada
    // data = foi cancelada em tal data

    // ============================================================================
    // 💬 OBSERVAÇÃO - Motivo/nota sobre a reserva
    // ============================================================================
    // Campo livre pra adicionar notas/observações
    // Quando cancela, a gente coloca o MOTIVO DO CANCELAMENTO aqui
    // Exemplo: "Reunião adiada para próxima semana"
    // 
    private String observacao;

    // ============================================================================
    // 👤 NOME COLABORADOR - Nome do usuário (SÓ PRA EXIBIR!)
    // ============================================================================
    // Este campo é EXTRA, não vem do formulário
    // É o Service que coloca aqui o nome do colaborador
    // Serve pra exibir na tela (na listagem, por exemplo)
    // Assim não precisa procurar o usuário de novo pra saber o nome
    // 
    private String nomeColaborador;

    // ============================================================================
    // 📦 DESCRIÇÃO RECURSO - Nome do recurso (SÓ PRA EXIBIR!)
    // ============================================================================
    // Este campo é EXTRA, não vem do formulário
    // É o Service que coloca aqui a descrição do recurso
    // Serve pra exibir na tela (na listagem, por exemplo)
    // Assim não precisa procurar o recurso de novo pra saber a descrição
    // 
    private String descricaoRecurso;

    // ============================================================================
    // 🏗️ CONSTRUTOR VAZIO
    // ============================================================================
    // Cria um ReservationDto vazio pra preencher depois
    // 
    public ReservationDto() {
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

    // Colaborador ID
    public Long getColaboradorId() {
        return colaboradorId;
        // Retorna o ID do usuário que fez a reserva
    }
    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
        // Guarda o ID do usuário
    }

    // Recurso ID
    public Long getRecursoId() {
        return recursoId;
        // Retorna o ID do recurso
    }
    public void setRecursoId(Long recursoId) {
        this.recursoId = recursoId;
        // Guarda o ID do recurso
    }

    // Data
    public LocalDate getData() {
        return data;
        // Retorna a data da reserva
    }
    public void setData(LocalDate data) {
        this.data = data;
        // Guarda a data da reserva
    }

    // Hora Inicial
    public LocalTime getHoraInicial() {
        return horaInicial;
        // Retorna a hora de começo
    }
    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
        // Guarda a hora de começo
    }

    // Hora Final
    public LocalTime getHoraFinal() {
        return horaFinal;
        // Retorna a hora de termino
    }
    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
        // Guarda a hora de termino
    }

    // Data de Cancelamento
    public LocalDate getDataCancelamento() {
        return dataCancelamento;
        // Retorna quando foi cancelada (ou null se não foi)
    }
    public void setDataCancelamento(LocalDate dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
        // Coloca quando foi cancelada
    }

    // Observação
    public String getObservacao() {
        return observacao;
        // Retorna a observação/motivo
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
        // Guarda a observação/motivo
    }

    // Nome Colaborador (só pra exibir)
    public String getNomeColaborador() {
        return nomeColaborador;
        // Retorna o nome do usuário (pra exibir na tela)
    }
    public void setNomeColaborador(String nomeColaborador) {
        this.nomeColaborador = nomeColaborador;
        // Guarda o nome do usuário (pra exibir depois)
    }

    // Descrição Recurso (só pra exibir)
    public String getDescricaoRecurso() {
        return descricaoRecurso;
        // Retorna a descrição do recurso (pra exibir na tela)
    }
    public void setDescricaoRecurso(String descricaoRecurso) {
        this.descricaoRecurso = descricaoRecurso;
        // Guarda a descrição do recurso (pra exibir depois)
    }
}
// ============================================================================
// 🎉 FIM DO RESERVATIONDTO
// ============================================================================
// Resumo: esse DTO guarda todas as informações de uma RESERVA
// OBRIGATÓRIO (vem do formulário):
//   - ID (criado pelo banco)
//   - Colaborador ID (quem faz)
//   - Recurso ID (o que reserva)
//   - Data (que dia)
//   - Hora Inicial (que hora começa)
//   - Hora Final (que hora termina)
//
// OPCIONAL (pode ser null):
//   - Data de Cancelamento (quando cancelou)
//   - Observação (por quê cancelou)
//
// EXTRA (Service coloca, não vem do formulário):
//   - Nome Colaborador (pra exibir na tela)
//   - Descrição Recurso (pra exibir na tela)
// ============================================================================
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

