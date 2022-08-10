package com.ctrlplus.controlplus.repositorios;

import com.ctrlplus.controlplus.entidades.Ingreso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IngresoRepositorio extends JpaRepository<Ingreso, String> {
    
    @Query(value = "SELECT * FROM Ingreso i WHERE i.usuario_id = :usuario", nativeQuery = true)
    public List<Ingreso> listarPorUsuario(@Param("usuario") String usuario);
    
    //buscar ingresos por fecha
    @Query(value = "SELECT * FROM Ingreso i WHERE  i.fecha = :fecha AND i.usuario_id = :usuario", nativeQuery = true)
    public List<Ingreso> buscarPorFecha(@Param("fecha") String fecha, @Param("usuario") String usuario);
    
    //ordenar ingresos por fecha
    @Query(value = "SELECT * FROM Ingreso i WHERE i.usuario_id = :usuario ORDER BY fecha asc", nativeQuery = true)
    public List<Ingreso> ordenarPorFechaAsc(@Param("usuario") String usuario);
    
     @Query(value = "SELECT * FROM Ingreso i WHERE i.usuario_id = :usuario ORDER BY fecha desc", nativeQuery = true)
    public List<Ingreso> ordenarPorFechaDesc(@Param("usuario") String usuario);
    
    //buscar ingresos por monto
     @Query(value = "SELECT * FROM Ingreso i WHERE  i.monto = :monto AND i.usuario_id = :usuario", nativeQuery = true)
    public List<Ingreso> buscarPorMonto(@Param("monto") String monto, @Param("usuario") String usuario);
    
    //ordenar ingresos por monto
    @Query(value = "SELECT * FROM Ingreso i WHERE i.usuario_id = :usuario ORDER BY monto asc", nativeQuery = true)
    public List<Ingreso> ordenarPorMontoAsc(@Param("usuario") String usuario);
    
     @Query(value = "SELECT * FROM Ingreso i WHERE i.usuario_id = :usuario ORDER BY fecha desc", nativeQuery = true)
    public List<Ingreso> ordenarPorMontoDesc(@Param("usuario") String usuario);

}
