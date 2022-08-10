package com.ctrlplus.controlplus.entidades;

import com.ctrlplus.controlplus.enums.Categoria;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
public class Gasto {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    
    private Double monto;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @OneToOne
    private Comprobante comprobante;

    @ManyToOne
    private Usuario usuario;
}
