package com.empresa.agendamento.repository;

import com.empresa.agendamento.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository = "Ó Spring, essa interface é um REPOSITORY!"
// Um Repository é quem FALA COM O BANCO DE DADOS
// É tipo um "intermediário" entre a aplicação e o banco

@Repository
// JpaRepository<UsuarioModel, Long>
// UsuarioModel = estou trabalhando com a tabela de usuários
// Long = o ID é do tipo Long (número grande)
// Com isso, JpaRepository já traz VÁRIAS operações prontas:
// - findAll() = pega todos
// - findById() = pega um pelo ID
// - save() = salva
// - deleteById() = deleta
// - existsById() = existe?
public interface UserRepository extends JpaRepository<UsuarioModel, Long> {
    
    // Método customizado: procura um usuário pelo EMAIL
    // "Ó banco, procura alguém com esse email"
    // Optional = pode achar ou não (por isso é Optional)
    Optional<UsuarioModel> findByEmail(String email);
    
    // Método customizado: procura um usuário pela MATRÍCULA
    // "Ó banco, procura alguém com essa matrícula"
    Optional<UsuarioModel> findByMatricula(String matricula);
    
    // Método customizado COM QUERY customizada
    // @Query = "Escreve uma consulta SQL personalizada"
    // "SELECT u FROM UsuarioModel u WHERE u.email = :email AND u.senha = :senha"
    // Tradução: "Seleciona um usuário (u) da tabela UsuarioModel onde:"
    //           "   email é igual ao email passado E"
    //           "   senha é igual à senha passada"
    // @Param = "Conecta o :email da query com o parâmetro 'email' do método"
    // Usa isso pro LOGIN! Verifica se email E senha existem
    @Query("SELECT u FROM UsuarioModel u WHERE u.email = :email AND u.senha = :senha")
    Optional<UsuarioModel> findByEmailAndSenha(@Param("email") String email, @Param("senha") String senha);
    
    // Método customizado: verifica se um EMAIL já existe no banco
    // Retorna true se existe, false se não existe
    // Usa isso pra VALIDAR se o email é único!
    boolean existsByEmail(String email);
    
    // Método customizado: verifica se uma MATRÍCULA já existe no banco
    // Retorna true se existe, false se não existe
    // Usa isso pra VALIDAR se a matrícula é única!
    boolean existsByMatricula(String matricula);
}

