package com.empresa.agendamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// @Entity = "Ó banco de dados, essa classe aqui é uma tabela de RECURSOS!"
// @Table = "A tabela vai se chamar 'recursos' lá no banco"
@Entity
@Table(name = "recursos")
public class ResourceModel {
    // Essa classe é o MODELO de um recurso
    // Um recurso é alguma coisa que a gente pode agendar (como uma sala de reunião, projetor, etc)
    
    // @Id = "Isso aqui é o identificador único do recurso" (como um CPF)
    // @GeneratedValue = "O banco gera automaticamente um número diferente pra cada recurso"
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @NotBlank = "A descrição não pode estar vazia"
    // Aqui a gente descreve o que é o recurso (tipo: "Sala de reunião 101", "Projetor Sony", etc)
    @NotBlank(message = "Descrição é obrigatória")
    @Column(nullable = false)
    private String descricao;
    
    // @NotBlank = "Tipo não pode estar vazio"
    // O tipo classifica o recurso (tipo: "sala", "equipamento", "veículo", etc)
    @NotBlank(message = "Tipo é obrigatório")
    @Column(nullable = false)
    private String tipo;
    
    // Aqui guarda quais dias da semana esse recurso tá disponível pra agendar
    // Por exemplo: "segunda, quarta, sexta" ou "segunda a sexta"
    // length = 100 = "Máximo 100 caracteres pra essa informação"
    @Column(length = 100)
    private String diasSemanaDisponivel;
    
    // A data de início do agendamento = "A partir de qual data a gente pode agendar esse recurso"
    // Por exemplo: "01/01/2024" em diante
    @Column(nullable = false)
    private LocalDate dataInicialAgendamento;
    
    // A data final do agendamento = "Até qual data a gente pode agendar esse recurso"
    // Por exemplo: até "31/12/2024"
    @Column(nullable = false)
    private LocalDate dataFinalAgendamento;
    
    // A hora inicial do agendamento = "A partir de qual hora do dia a gente pode agendar esse recurso"
    // Por exemplo: "08:00" da manhã
    @Column(nullable = false)
    private LocalTime horaInicialAgendamento;
    
    // A hora final do agendamento = "Até qual hora do dia a gente pode agendar esse recurso"
    // Por exemplo: até "18:00" (6 da tarde)
    @Column(nullable = false)
    private LocalTime horaFinalAgendamento;
    
    // @OneToMany = "Um recurso pode ter VÁRIAS reservas!" (um para muitos)
    // mappedBy = "recurso" = "No ReservationModel tem um campo chamado 'recurso' que aponta pra aqui"
    // cascade = CascadeType.ALL = "Se deletar um recurso, deleta todas as reservas dele também"
    // Essa lista guarda todas as reservas que foram feitas pra esse recurso
    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL)
    private List<ReservationModel> reservas;
    
    // CONSTRUTORES = "Formas de criar um novo recurso"
    
    // Construtor 1: Vazio (sem parâmetros)
    // Quando você cria um recurso vazio, ele depois preenche os dados
    public ResourceModel() {
    }
    
    // Construtor 2: Com todos os dados
    // Você passa tudo de uma vez e já cria um recurso completo!
    public ResourceModel(String descricao, String tipo, String diasSemanaDisponivel,
                   LocalDate dataInicialAgendamento, LocalDate dataFinalAgendamento,
                   LocalTime horaInicialAgendamento, LocalTime horaFinalAgendamento) {
        this.descricao = descricao;                       // Atribui a descrição
        this.tipo = tipo;                                 // Atribui o tipo
        this.diasSemanaDisponivel = diasSemanaDisponivel; // Atribui os dias disponíveis
        this.dataInicialAgendamento = dataInicialAgendamento; // Atribui a data inicial
        this.dataFinalAgendamento = dataFinalAgendamento;     // Atribui a data final
        this.horaInicialAgendamento = horaInicialAgendamento; // Atribui a hora inicial
        this.horaFinalAgendamento = horaFinalAgendamento;     // Atribui a hora final
    }
    
    // GETTERS e SETTERS = "Métodos para pegar e colocar valores"
    
    // Getter do ID - retorna qual é o ID do recurso
    public Long getId() {
        return id;
    }
    
    // Setter do ID - define o ID do recurso
    public void setId(Long id) {
        this.id = id;
    }
    
    // Getter da Descrição - retorna o que é o recurso
    public String getDescricao() {
        return descricao;
    }
    
    // Setter da Descrição - muda a descrição do recurso
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    // Getter do Tipo - retorna que tipo de recurso é
    public String getTipo() {
        return tipo;
    }
    
    // Setter do Tipo - muda o tipo do recurso
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    // Getter dos Dias - retorna quais dias da semana esse recurso tá disponível
    public String getDiasSemanaDisponivel() {
        return diasSemanaDisponivel;
    }
    
    // Setter dos Dias - muda quais dias esse recurso tá disponível
    public void setDiasSemanaDisponivel(String diasSemanaDisponivel) {
        this.diasSemanaDisponivel = diasSemanaDisponivel;
    }
    
    // Getter da Data Inicial - retorna a partir de qual data pode agendar
    public LocalDate getDataInicialAgendamento() {
        return dataInicialAgendamento;
    }
    
    // Setter da Data Inicial - muda a partir de qual data pode agendar
    public void setDataInicialAgendamento(LocalDate dataInicialAgendamento) {
        this.dataInicialAgendamento = dataInicialAgendamento;
    }
    
    // Getter da Data Final - retorna até qual data pode agendar
    public LocalDate getDataFinalAgendamento() {
        return dataFinalAgendamento;
    }
    
    // Setter da Data Final - muda até qual data pode agendar
    public void setDataFinalAgendamento(LocalDate dataFinalAgendamento) {
        this.dataFinalAgendamento = dataFinalAgendamento;
    }
    
    // Getter da Hora Inicial - retorna a partir de qual hora pode agendar
    public LocalTime getHoraInicialAgendamento() {
        return horaInicialAgendamento;
    }
    
    // Setter da Hora Inicial - muda a partir de qual hora pode agendar
    public void setHoraInicialAgendamento(LocalTime horaInicialAgendamento) {
        this.horaInicialAgendamento = horaInicialAgendamento;
    }
    
    // Getter da Hora Final - retorna até qual hora pode agendar
    public LocalTime getHoraFinalAgendamento() {
        return horaFinalAgendamento;
    }
    
    // Setter da Hora Final - muda até qual hora pode agendar
    public void setHoraFinalAgendamento(LocalTime horaFinalAgendamento) {
        this.horaFinalAgendamento = horaFinalAgendamento;
    }
    
    // Getter das Reservas - retorna todas as reservas feitas pra esse recurso
    public List<ReservationModel> getReservas() {
        return reservas;
    }
    
    // Setter das Reservas - muda a lista de reservas do recurso
    public void setReservas(List<ReservationModel> reservas) {
        this.reservas = reservas;
    }
}

