package com.empresa.agendamento.repository;

import com.empresa.agendamento.models.ResourceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceModel, Long> {
}

