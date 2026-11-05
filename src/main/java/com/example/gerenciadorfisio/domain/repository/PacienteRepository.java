package com.example.gerenciadorfisio.domain.repository;

import com.example.gerenciadorfisio.domain.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByResponsavel_Id(Long id);
}
