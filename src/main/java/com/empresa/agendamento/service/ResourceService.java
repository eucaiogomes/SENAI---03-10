package com.empresa.agendamento.service;

import com.empresa.agendamento.dtos.ResourceDto;
import com.empresa.agendamento.models.ResourceModel;
import com.empresa.agendamento.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ============================================================================
// 🎯 RESOURCESERVICE
// ============================================================================
// O que é isso? É a lógica de RECURSO (sala, projetor, veículo, etc)
// 
// Imagina que você é um gerente de salas de reunião.
// Quando alguém quer cadastrar uma sala, você:
//   1. Verifica se os dados tão corretos
//   2. Verifica se a sala não tá duplicada
//   3. Salva no seu caderno (banco de dados)
//
// Esse Service faz EXATAMENTE isso! É a "mão direita" do Controller.
// O Controller recebe as requisições HTTP, o Service faz a lógica!
//
// ============================================================================

@Service
// @Service = "Ó Spring, esse é um Service! Usa como você usa outras coisas"
@Transactional
// @Transactional = "Se der erro no meio, volta tudo atrás! (Tudo ou nada)"
public class ResourceService {
    
    // ============================================================================
    // 📚 INJEÇÃO DE DEPENDÊNCIA - A magia do Spring
    // ============================================================================
    // O que é isso? É como se você pedisse um servente que tem acesso ao banco
    // Sem isso você não conseguia falar com o banco de dados!
    // @Autowired = "Spring, encontra essa coisa pra mim!"
    //
    @Autowired
    private ResourceRepository resourceRepository;
    // Essa variável = "A ponte que conecta o código com o banco de dados"
    // Quando você faz resourceRepository.findAll()
    // É como falar: "banco de dados, me dá TODOS os recursos!"
    
    // ============================================================================
    // 📋 MÉTODO: listarTodos()
    // ============================================================================
    // O que faz? Busca TODOS os recursos do banco e retorna uma lista
    // Fluxo: Controller → listarTodos() → banco → volta a lista
    //
    public List<ResourceDto> listarTodos() {
        // Step 1: Vai no banco e pede TODOS os recursos (ResourceModel)
        // findAll() = "banco, me dá tudo!"
        List<ResourceModel> recursos = resourceRepository.findAll();
        // Agora 'recursos' tem uma lista com TODOS os recursos do banco
        
        // Step 2: Cria uma lista VAZIA de DTOs
        // (DTO = versão "simplificada" do Model pra mostrar pra galera)
        List<ResourceDto> recursosDto = new ArrayList<>();
        
        // Step 3: PARA CADA recurso que pegou do banco
        for (ResourceModel recurso : recursos) {
            // Converte o Model em DTO e coloca na lista
            // (é tipo transformar um livro em um resumo do livro)
            recursosDto.add(converterParaDTO(recurso));
        }
        // Depois que o loop acabar, 'recursosDto' tem TODOS os recursos em DTO
        
        // Step 4: Retorna a lista
        // O Controller pega isso e coloca na página HTML
        return recursosDto;
    }
    
    // ============================================================================
    // 🔍 MÉTODO: buscarPorId(Long id)
    // ============================================================================
    // O que faz? Busca UM ÚNICO recurso pelo ID dele
    // Fluxo: Controller → buscarPorId(5) → banco → volta 1 recurso
    //
    public ResourceDto buscarPorId(Long id) {
        // Step 1: Vai no banco e procura um recurso com esse ID
        // findById(id) = "banco, acha o recurso com ID 5 pra mim"
        // .orElseThrow(...) = "Se não achar, EXPLODE COM ERRO!"
        ResourceModel resource = resourceRepository.findById(id)
                // Se encontrar, retorna. Se não encontrar:
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com ID: " + id));
        // RuntimeException = "Erro! Recurso não existe!"
        
        // Step 2: Converte o Model em DTO
        return converterParaDTO(resource);
    }
    
    // ============================================================================
    // ➕ MÉTODO: salvar(ResourceDto resourceDTO)
    // ============================================================================
    // O que faz? Cria um novo recurso no banco
    // Fluxo: Controller → salvar(dados) → valida → salva no banco
    //
    public ResourceDto salvar(ResourceDto resourceDTO) {
        // Step 1: VALIDA os dados (explicado abaixo)
        // Se os dados tiverem ruim, essa função EXPLODE COM ERRO
        validarRecurso(resourceDTO);
        
        // Step 2: Se passou na validação, converte DTO → Model
        // (porque o banco só salva Model!)
        ResourceModel resource = converterParaEntidade(resourceDTO);
        
        // Step 3: Salva no banco de dados
        // save() = "banco, salva esse recurso aí!"
        resource = resourceRepository.save(resource);
        // Depois de salvar, o banco retorna o objeto COM ID (auto-gerado)
        
        // Step 4: Converte de volta pra DTO e retorna
        return converterParaDTO(resource);
    }
    
    // ============================================================================
    // ✏️ MÉTODO: atualizar(Long id, ResourceDto resourceDTO)
    // ============================================================================
    // O que faz? Muda um recurso que já existe no banco
    // Fluxo: Controller → atualizar(5, novosDados) → busca → modifica → salva
    //
    public ResourceDto atualizar(Long id, ResourceDto resourceDTO) {
        // Step 1: BUSCA o recurso que quer mudar
        // Se não encontrar, explode com erro
        ResourceModel resourceExistente = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com ID: " + id));
        // Agora temos o recurso ANTIGO em 'resourceExistente'
        
        // Step 2: VALIDA os novos dados
        // Se tiver algo errado, para aqui!
        validarRecurso(resourceDTO);
        
        // Step 3: MODIFICA cada campo do recurso antigo com os novos dados
        resourceExistente.setDescricao(resourceDTO.getDescricao());
        // setDescricao() = "muda a descrição"
        
        resourceExistente.setTipo(resourceDTO.getTipo());
        // setTipo() = "muda o tipo"
        
        resourceExistente.setDiasSemanaDisponivel(
                converterDiasSemanaParaString(resourceDTO.getDiasSemanaDisponivel())
        );
        // Converte a lista de dias (segunda, terça...) em texto separado por vírgula
        
        resourceExistente.setDataInicialAgendamento(resourceDTO.getDataInicialAgendamento());
        // setDataInicialAgendamento() = "muda a data inicial"
        
        resourceExistente.setDataFinalAgendamento(resourceDTO.getDataFinalAgendamento());
        // setDataFinalAgendamento() = "muda a data final"
        
        resourceExistente.setHoraInicialAgendamento(resourceDTO.getHoraInicialAgendamento());
        // setHoraInicialAgendamento() = "muda a hora inicial"
        
        resourceExistente.setHoraFinalAgendamento(resourceDTO.getHoraFinalAgendamento());
        // setHoraFinalAgendamento() = "muda a hora final"
        
        // Step 4: Salva as mudanças no banco
        resourceExistente = resourceRepository.save(resourceExistente);
        // O banco SOBRESCREVE os dados antigos com os novos
        
        // Step 5: Retorna em formato DTO
        return converterParaDTO(resourceExistente);
    }
    
    // ============================================================================
    // 🗑️ MÉTODO: excluir(Long id)
    // ============================================================================
    // O que faz? Deleta um recurso do banco
    // Fluxo: Controller → excluir(5) → verifica → deleta
    //
    public void excluir(Long id) {
        // void = "Não retorna nada, só executa"
        
        // Step 1: Verifica se o recurso realmente existe
        // existsById() = "esse ID existe no banco?"
        if (!resourceRepository.existsById(id)) {
            // Se não existir, lança um erro
            throw new RuntimeException("Recurso não encontrado com ID: " + id);
        }
        
        // Step 2: Se existir, deleta
        resourceRepository.deleteById(id);
        // deleteById() = "banco, deleta o recurso com ID 5"
    }
    
    // ============================================================================
    // ✅ MÉTODO: validarRecurso(ResourceDto resourceDTO) - PRIVADO
    // ============================================================================
    // O que faz? Verifica se os dados do recurso tão corretos
    // Isso é PRIVADO = só pode ser usado dentro dessa classe (Service)
    // Fluxo: valida cada regra uma por uma
    //
    private void validarRecurso(ResourceDto resourceDTO) {
        // REGRA 1: Descrição obrigatória (tem que ter descrição)
        if (resourceDTO.getDescricao() == null || resourceDTO.getDescricao().trim().isEmpty()) {
            // getDescricao() == null = "não tem nada"
            // .trim() = "remove espaços em branco"
            // .isEmpty() = "tá vazio?"
            throw new RuntimeException("Descrição do recurso é obrigatória");
            // Se falhar: EXPLODE COM ERRO
        }
        
        // REGRA 2: Tipo obrigatório (tem que ter tipo: sala, projetor, etc)
        if (resourceDTO.getTipo() == null || resourceDTO.getTipo().trim().isEmpty()) {
            throw new RuntimeException("Tipo do recurso é obrigatório");
        }
        
        // REGRA 3: Data inicial não pode ser depois da data final
        if (resourceDTO.getDataInicialAgendamento() != null && 
            resourceDTO.getDataFinalAgendamento() != null) {
            // Só valida se TEM datas (pode ser que não tenha)
            if (resourceDTO.getDataInicialAgendamento().isAfter(resourceDTO.getDataFinalAgendamento())) {
                // isAfter() = "é depois de?"
                throw new RuntimeException("Data inicial não pode ser posterior à data final");
            }
        }
        
        // REGRA 4: Hora inicial não pode ser igual ou depois da hora final
        if (resourceDTO.getHoraInicialAgendamento() != null && 
            resourceDTO.getHoraFinalAgendamento() != null) {
            // Só valida se TEM horas
            if (resourceDTO.getHoraInicialAgendamento().isAfter(resourceDTO.getHoraFinalAgendamento()) ||
                resourceDTO.getHoraInicialAgendamento().equals(resourceDTO.getHoraFinalAgendamento())) {
                // equals() = "são iguais?"
                throw new RuntimeException("Hora inicial deve ser anterior à hora final");
            }
        }
        // Se todas as regras passaram aqui, tá tudo certo!
    }
    
    // ============================================================================
    // 🔄 MÉTODO: converterParaDTO(ResourceModel resource) - PRIVADO
    // ============================================================================
    // O que faz? Pega um Model (do banco) e vira um DTO (pra mostrar)
    // É tipo pegar um livro inteiro e fazer um resumo
    // Fluxo: Model (tudo) → DTO (o que a gente quer mostrar)
    //
    private ResourceDto converterParaDTO(ResourceModel resource) {
        // Step 1: Cria um DTO vazio
        ResourceDto dto = new ResourceDto();
        
        // Step 2: Copia cada campo do Model pro DTO
        // setId() = "coloca o ID"
        dto.setId(resource.getId());
        
        // setDescricao() = "coloca a descrição"
        dto.setDescricao(resource.getDescricao());
        
        // setTipo() = "coloca o tipo"
        dto.setTipo(resource.getTipo());
        
        // Os dias da semana vêm como texto (segunda,terça,...)
        // Transforma em lista pra DTO entender
        dto.setDiasSemanaDisponivel(converterDiasSemanaParaLista(resource.getDiasSemanaDisponivel()));
        
        // Coloca as datas
        dto.setDataInicialAgendamento(resource.getDataInicialAgendamento());
        dto.setDataFinalAgendamento(resource.getDataFinalAgendamento());
        
        // Coloca as horas
        dto.setHoraInicialAgendamento(resource.getHoraInicialAgendamento());
        dto.setHoraFinalAgendamento(resource.getHoraFinalAgendamento());
        
        // Step 3: Retorna o DTO pronto
        return dto;
    }
    
    // ============================================================================
    // 🔄 MÉTODO: converterParaEntidade(ResourceDto dto) - PRIVADO
    // ============================================================================
    // O que faz? Pega um DTO (do formulário) e vira um Model (pra salvar no banco)
    // É o CONTRÁRIO da função anterior!
    // Fluxo: DTO (vem do Controller) → Model (vai pro banco)
    //
    private ResourceModel converterParaEntidade(ResourceDto dto) {
        // Step 1: Cria um Model vazio
        ResourceModel resource = new ResourceModel();
        
        // Step 2: Se o DTO tem ID (quando é atualização), copia
        if (dto.getId() != null) {
            resource.setId(dto.getId());
        }
        // Se não tem ID, o banco vai auto-gerar quando salvar (novo recurso)
        
        // Step 3: Copia cada campo do DTO pro Model
        resource.setDescricao(dto.getDescricao());
        resource.setTipo(dto.getTipo());
        
        // Os dias vêm como lista no DTO
        // Transforma em texto separado por vírgula pro Model entender
        resource.setDiasSemanaDisponivel(converterDiasSemanaParaString(dto.getDiasSemanaDisponivel()));
        
        // Coloca as datas
        resource.setDataInicialAgendamento(dto.getDataInicialAgendamento());
        resource.setDataFinalAgendamento(dto.getDataFinalAgendamento());
        
        // Coloca as horas
        resource.setHoraInicialAgendamento(dto.getHoraInicialAgendamento());
        resource.setHoraFinalAgendamento(dto.getHoraFinalAgendamento());
        
        // Step 4: Retorna o Model pronto pra salvar no banco
        return resource;
    }
    
    // ============================================================================
    // 📝 MÉTODO: converterDiasSemanaParaString(List<String> dias) - PRIVADO
    // ============================================================================
    // O que faz? Transforma uma LISTA em TEXTO separado por vírgula
    // Exemplo: [segunda, terça, quarta] → "segunda,terça,quarta"
    // Por que? O banco guarda texto, não lista!
    //
    private String converterDiasSemanaParaString(List<String> dias) {
        // Step 1: Se a lista for vazia ou nula, retorna texto vazio
        if (dias == null || dias.isEmpty()) {
            return "";
            // "" = string vazia (nada)
        }
        
        // Step 2: Se tem dias, junta todos com vírgula
        return String.join(",", dias);
        // String.join() = "junta todos os elementos com separador"
        // Exemplo: join(",", ["a", "b", "c"]) → "a,b,c"
    }
    
    // ============================================================================
    // 📝 MÉTODO: converterDiasSemanaParaLista(String dias) - PRIVADO
    // ============================================================================
    // O que faz? Transforma TEXTO em LISTA
    // Exemplo: "segunda,terça,quarta" → [segunda, terça, quarta]
    // É o CONTRÁRIO da função anterior!
    // Por que? O DTO trabalha com lista!
    //
    private List<String> converterDiasSemanaParaLista(String dias) {
        // Step 1: Se o texto for vazio ou nulo, retorna lista vazia
        if (dias == null || dias.trim().isEmpty()) {
            return new ArrayList<>();
            // ArrayList = lista vazia
        }
        
        // Step 2: Divide o texto pelos vírgulas
        String[] diasArray = dias.split(",");
        // split() = "divide em pedaços"
        // Exemplo: "segunda,terça,quarta".split(",") → [segunda, terça, quarta]
        
        // Step 3: Transforma o array em lista
        List<String> diasLista = new ArrayList<>();
        // Cria uma lista vazia
        
        for (String dia : diasArray) {
            // Para cada dia que pegou do split:
            diasLista.add(dia.trim());
            // Adiciona na lista (trim remove espaços extras)
        }
        
        // Step 4: Retorna a lista
        return diasLista;
    }
}
// ============================================================================
// 🎉 FIM DO RESOURCESERVICE
// ============================================================================
// Resumo: esse Service é responsável por:
//   ✅ Listar todos os recursos
//   ✅ Buscar um recurso específico
//   ✅ Criar um novo recurso (com validação)
//   ✅ Atualizar um recurso existente (com validação)
//   ✅ Deletar um recurso
//   ✅ Validar se os dados tão corretos
//   ✅ Converter entre Model e DTO
//   ✅ Converter dias da semana (lista ↔ texto)
//
// Todos os dados passam por validação ANTES de ir pro banco!
// Por isso é seguro e não tem dados ruins no banco.
// ============================================================================

