package com.ctrlplus.controlplus.entidades;

import com.ctrlplus.controlplus.enums.Rol;
import javax.persistence.Entity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
public class Usuario {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @Column(unique = true)
    private String mail;
    
    @Column(nullable = false)
    private String nombre;
    
    private String clave;
    
    private Rol rol;

}
