package com.empresa.agendamento.repository;

import com.empresa.agendamento.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsuarioModel, Long> {
    
    Optional<UsuarioModel> findByEmail(String email);
    
    Optional<UsuarioModel> findByMatricula(String matricula);
    
    @Query("SELECT u FROM UsuarioModel u WHERE u.email = :email AND u.senha = :senha")
    Optional<UsuarioModel> findByEmailAndSenha(@Param("email") String email, @Param("senha") String senha);
    
    boolean existsByEmail(String email);
    
    boolean existsByMatricula(String matricula);
}

