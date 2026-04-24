package com.biomedical.waste.demo.repository;

import com.biomedical.waste.demo.models.RutaCliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaClienteRepository extends JpaRepository<RutaCliente, Long> {
    List<RutaCliente> findByClienteId(Long clienteId);
    List<RutaCliente> findByRuta_Id(Long rutaId);
}

