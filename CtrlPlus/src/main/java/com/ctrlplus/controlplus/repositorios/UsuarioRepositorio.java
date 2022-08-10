package com.ctrlplus.controlplus.repositorios;

import com.ctrlplus.controlplus.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
     @Query("SELECT u FROM Usuario u WHERE u.mail = :mail")
    public Usuario buscarPorMail(@Param("mail") String mail);
   
    @Query(value = "select * from Gasto g where g.usuario_id like :usuario UNION select * from ingreso i where i.usuario_id like :usuario order by fecha desc limit 10", nativeQuery = true)
    public List<Object> ultimosMovimientos(@Param("usuario") String usuario);

}
