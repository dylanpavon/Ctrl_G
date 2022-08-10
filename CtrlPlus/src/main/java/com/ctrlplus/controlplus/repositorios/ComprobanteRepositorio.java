package com.ctrlplus.controlplus.repositorios;

import com.ctrlplus.controlplus.entidades.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobanteRepositorio extends JpaRepository<Comprobante, String> {

}
