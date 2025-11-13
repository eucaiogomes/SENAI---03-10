package com.empresa.agendamento.repository;

import com.empresa.agendamento.models.ResourceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository = "Ó Spring, essa interface é um REPOSITORY!"
// Esse repository cuida da tabela de RECURSOS
@Repository
// JpaRepository<ResourceModel, Long>
// ResourceModel = estou trabalhando com a tabela de recursos
// Long = o ID é do tipo Long
public interface ResourceRepository extends JpaRepository<ResourceModel, Long> {
    // Não precisa de métodos customizados aqui!
    // Os básicos (findAll, findById, save, delete, exists) já servem
    // Se precisar buscar algo específico (tipo "recursos de tipo sala"),
    // a gente adiciona aqui depois!
}

