package com.empresa.agendamento.service;

import com.empresa.agendamento.dtos.ResourceDto;
import com.empresa.agendamento.models.ResourceModel;
import com.empresa.agendamento.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ResourceService {
    
    @Autowired
    private ResourceRepository resourceRepository;
    
    public List<ResourceDto> listarTodos() {
        return resourceRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    public ResourceDto buscarPorId(Long id) {
        ResourceModel resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com ID: " + id));
        return converterParaDTO(resource);
    }
    
    public ResourceDto salvar(ResourceDto resourceDTO) {
        validarRecurso(resourceDTO);
        
        ResourceModel resource = converterParaEntidade(resourceDTO);
        resource = resourceRepository.save(resource);
        return converterParaDTO(resource);
    }
    
    public ResourceDto atualizar(Long id, ResourceDto resourceDTO) {
        ResourceModel resourceExistente = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com ID: " + id));
        
        validarRecurso(resourceDTO);
        
        resourceExistente.setDescricao(resourceDTO.getDescricao());
        resourceExistente.setTipo(resourceDTO.getTipo());
        resourceExistente.setDiasSemanaDisponivel(
                converterDiasSemanaParaString(resourceDTO.getDiasSemanaDisponivel())
        );
        resourceExistente.setDataInicialAgendamento(resourceDTO.getDataInicialAgendamento());
        resourceExistente.setDataFinalAgendamento(resourceDTO.getDataFinalAgendamento());
        resourceExistente.setHoraInicialAgendamento(resourceDTO.getHoraInicialAgendamento());
        resourceExistente.setHoraFinalAgendamento(resourceDTO.getHoraFinalAgendamento());
        
        resourceExistente = resourceRepository.save(resourceExistente);
        return converterParaDTO(resourceExistente);
    }
    
    public void excluir(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new RuntimeException("Recurso não encontrado com ID: " + id);
        }
        resourceRepository.deleteById(id);
    }
    
    private void validarRecurso(ResourceDto resourceDTO) {
        // Regra 1: Descrição obrigatória
        if (resourceDTO.getDescricao() == null || resourceDTO.getDescricao().trim().isEmpty()) {
            throw new RuntimeException("Descrição do recurso é obrigatória");
        }
        
        // Regra 2: Tipo obrigatório
        if (resourceDTO.getTipo() == null || resourceDTO.getTipo().trim().isEmpty()) {
            throw new RuntimeException("Tipo do recurso é obrigatório");
        }
        
        // Validação de datas
        if (resourceDTO.getDataInicialAgendamento() != null && 
            resourceDTO.getDataFinalAgendamento() != null) {
            if (resourceDTO.getDataInicialAgendamento().isAfter(resourceDTO.getDataFinalAgendamento())) {
                throw new RuntimeException("Data inicial não pode ser posterior à data final");
            }
        }
        
        // Validação de horários
        if (resourceDTO.getHoraInicialAgendamento() != null && 
            resourceDTO.getHoraFinalAgendamento() != null) {
            if (resourceDTO.getHoraInicialAgendamento().isAfter(resourceDTO.getHoraFinalAgendamento()) ||
                resourceDTO.getHoraInicialAgendamento().equals(resourceDTO.getHoraFinalAgendamento())) {
                throw new RuntimeException("Hora inicial deve ser anterior à hora final");
            }
        }
    }
    
    private ResourceDto converterParaDTO(ResourceModel resource) {
        ResourceDto dto = new ResourceDto();
        dto.setId(resource.getId());
        dto.setDescricao(resource.getDescricao());
        dto.setTipo(resource.getTipo());
        dto.setDiasSemanaDisponivel(converterDiasSemanaParaLista(resource.getDiasSemanaDisponivel()));
        dto.setDataInicialAgendamento(resource.getDataInicialAgendamento());
        dto.setDataFinalAgendamento(resource.getDataFinalAgendamento());
        dto.setHoraInicialAgendamento(resource.getHoraInicialAgendamento());
        dto.setHoraFinalAgendamento(resource.getHoraFinalAgendamento());
        return dto;
    }
    
    private ResourceModel converterParaEntidade(ResourceDto dto) {
        ResourceModel resource = new ResourceModel();
        if (dto.getId() != null) {
            resource.setId(dto.getId());
        }
        resource.setDescricao(dto.getDescricao());
        resource.setTipo(dto.getTipo());
        resource.setDiasSemanaDisponivel(converterDiasSemanaParaString(dto.getDiasSemanaDisponivel()));
        resource.setDataInicialAgendamento(dto.getDataInicialAgendamento());
        resource.setDataFinalAgendamento(dto.getDataFinalAgendamento());
        resource.setHoraInicialAgendamento(dto.getHoraInicialAgendamento());
        resource.setHoraFinalAgendamento(dto.getHoraFinalAgendamento());
        return resource;
    }
    
    private String converterDiasSemanaParaString(List<String> dias) {
        if (dias == null || dias.isEmpty()) {
            return "";
        }
        return String.join(",", dias);
    }
    
    private List<String> converterDiasSemanaParaLista(String dias) {
        if (dias == null || dias.trim().isEmpty()) {
            return List.of();
        }
        return Stream.of(dias.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}

