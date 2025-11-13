package com.empresa.agendamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

// @Entity = "Ó banco de dados, essa classe aqui é uma tabela de RESERVAS!"
// @Table = "A tabela vai se chamar 'reservas' lá no banco"
@Entity
@Table(name = "reservas")
public class ReservationModel {
    // Essa classe é o MODELO de uma RESERVA
    // Uma reserva é quando alguém (um usuário) marca um recurso pra usar em um certo dia e hora
    // Por exemplo: João reservou a sala 101 na segunda-feira de 10h a 11h
    
    // @Id = "Isso aqui é o identificador único da reserva" (tipo um CPF)
    // @GeneratedValue = "O banco gera automaticamente um número diferente pra cada reserva"
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @NotNull = "Tem que ter um colaborador! A reserva precisa ser de alguém"
    // @ManyToOne = "Muitas reservas podem ser do MESMO usuário!" (um usuário faz várias reservas)
    // @JoinColumn = "Cria uma coluna chamada 'colaborador_id' que aponta pro usuário"
    // Aqui guardamos QUAL usuário fez essa reserva
    @NotNull(message = "Colaborador é obrigatório")
    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private UsuarioModel colaborador;
    
    // @NotNull = "Tem que ter um recurso! A reserva é de alguma coisa"
    // @ManyToOne = "Muitas reservas podem ser do MESMO recurso!" (vários usuários podem reservar a mesma sala)
    // @JoinColumn = "Cria uma coluna chamada 'recurso_id' que aponta pro recurso"
    // Aqui guardamos QUAL recurso tá sendo reservado
    @NotNull(message = "Recurso é obrigatório")
    @ManyToOne
    @JoinColumn(name = "recurso_id", nullable = false)
    private ResourceModel recurso;
    
    // @NotNull = "A data não pode estar vazia"
    // Aqui guardamos EM QUAL DIA a reserva tá marcada
    @NotNull(message = "Data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;
    
    // @NotNull = "A hora inicial não pode estar vazia"
    // Aqui guardamos A QUE HORAS a reserva COMEÇA
    @NotNull(message = "Hora inicial é obrigatória")
    @Column(nullable = false)
    private LocalTime horaInicial;
    
    // @NotNull = "A hora final não pode estar vazia"
    // Aqui guardamos A QUE HORAS a reserva TERMINA
    @NotNull(message = "Hora final é obrigatória")
    @Column(nullable = false)
    private LocalTime horaFinal;
    
    // Se a reserva foi cancelada, aqui guardamos a data do cancelamento
    // Se NÃO foi cancelada, esse campo fica vazio (null)
    // Isso é importante pra saber: essa reserva tá ativa ou foi cancelada?
    @Column
    private LocalDate dataCancelamento;
    
    // Um campo pra deixar observações/notas sobre a reserva
    // Por exemplo: "Preciso de WiFi", "Pode ser adiada", etc
    // length = 500 = "Máximo 500 caracteres"
    @Column(length = 500)
    private String observacao;
    
    // CONSTRUTORES = "Formas de criar uma nova reserva"
    
    // Construtor 1: Vazio (sem parâmetros)
    // Quando você cria uma reserva vazia, você preenche os dados depois
    public ReservationModel() {
    }
    
    // Construtor 2: Com os dados principais
    // Você passa os dados essenciais de uma vez!
    public ReservationModel(UsuarioModel colaborador, ResourceModel recurso, LocalDate data,
                      LocalTime horaInicial, LocalTime horaFinal) {
        this.colaborador = colaborador;      // Quem tá fazendo a reserva
        this.recurso = recurso;              // Qual recurso tá sendo reservado
        this.data = data;                    // Que dia é a reserva
        this.horaInicial = horaInicial;      // De que hora começa
        this.horaFinal = horaFinal;          // Até que hora termina
        this.dataCancelamento = null;        // No início, a reserva não tá cancelada (null = vazio)
    }
    
    // GETTERS e SETTERS = "Métodos para pegar e colocar valores"
    
    // Getter do ID - retorna qual é o ID da reserva
    public Long getId() {
        return id;
    }
    
    // Setter do ID - define o ID da reserva
    public void setId(Long id) {
        this.id = id;
    }
    
    // Getter do Colaborador - retorna QUAL USUÁRIO fez a reserva
    public UsuarioModel getColaborador() {
        return colaborador;
    }
    
    // Setter do Colaborador - muda QUAL USUÁRIO é dono da reserva
    public void setColaborador(UsuarioModel colaborador) {
        this.colaborador = colaborador;
    }
    
    // Getter do Recurso - retorna QUAL RECURSO tá sendo reservado
    public ResourceModel getRecurso() {
        return recurso;
    }
    
    // Setter do Recurso - muda QUAL RECURSO tá sendo reservado
    public void setRecurso(ResourceModel recurso) {
        this.recurso = recurso;
    }
    
    // Getter da Data - retorna EM QUAL DIA a reserva é
    public LocalDate getData() {
        return data;
    }
    
    // Setter da Data - muda EM QUAL DIA a reserva é
    public void setData(LocalDate data) {
        this.data = data;
    }
    
    // Getter da Hora Inicial - retorna A QUE HORAS a reserva COMEÇA
    public LocalTime getHoraInicial() {
        return horaInicial;
    }
    
    // Setter da Hora Inicial - muda A QUE HORAS a reserva COMEÇA
    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }
    
    // Getter da Hora Final - retorna A QUE HORAS a reserva TERMINA
    public LocalTime getHoraFinal() {
        return horaFinal;
    }
    
    // Setter da Hora Final - muda A QUE HORAS a reserva TERMINA
    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }
    
    // Getter da Data de Cancelamento - retorna a data que a reserva foi cancelada (ou null se não foi)
    public LocalDate getDataCancelamento() {
        return dataCancelamento;
    }
    
    // Setter da Data de Cancelamento - coloca a data que a reserva foi cancelada
    public void setDataCancelamento(LocalDate dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }
    
    // Getter da Observação - retorna as notas/comentários sobre a reserva
    public String getObservacao() {
        return observacao;
    }
    
    // Setter da Observação - coloca notas/comentários sobre a reserva
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    
    // Método especial que verifica se a reserva foi cancelada ou não
    // Se dataCancelamento tem algum valor (não é null), significa que foi cancelada = retorna true
    // Se dataCancelamento é null, significa que NÃO foi cancelada = retorna false
    public boolean isCancelada() {
        return dataCancelamento != null;
    }
}

