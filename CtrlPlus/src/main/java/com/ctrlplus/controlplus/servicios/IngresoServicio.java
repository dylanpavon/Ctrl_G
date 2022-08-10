package com.ctrlplus.controlplus.servicios;

import com.ctrlplus.controlplus.entidades.Comprobante;
import com.ctrlplus.controlplus.entidades.Ingreso;
import com.ctrlplus.controlplus.entidades.Usuario;
import com.ctrlplus.controlplus.enums.CategoriaIngreso;
import com.ctrlplus.controlplus.errores.ErrorServicio;
import com.ctrlplus.controlplus.repositorios.IngresoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IngresoServicio {

    @Autowired
    private IngresoRepositorio ingresoRepositorio;
    @Autowired
    private ComprobanteServicio comprobanteServicio;

    @Transactional(propagation = Propagation.NESTED)
    public Ingreso agregar(Double monto, String descripcion, Usuario usuario, MultipartFile archivo, CategoriaIngreso categoria) throws ErrorServicio {

        validar(monto, categoria);
        
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(monto);
        ingreso.setFecha(new Date());
        ingreso.setCategoria(categoria);
        ingreso.setUsuario(usuario);
        ingreso.setDescripcion(descripcion);
        if (archivo != null) {
            Comprobante comprobante = comprobanteServicio.guardar(archivo);
            ingreso.setComprobante(comprobante);
        }

        return ingresoRepositorio.save(ingreso);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void modificar(String id, Double monto, String descripcion, MultipartFile archivo, CategoriaIngreso categoria) throws ErrorServicio {

        validar(monto, categoria);

        Optional<Ingreso> respuesta = ingresoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Ingreso ingreso = respuesta.get();
            ingreso.setMonto(monto);
            ingreso.setFecha(new Date());
            ingreso.setCategoria(categoria);
            ingreso.setDescripcion(descripcion);
            if (archivo != null) {
                Comprobante comprobante = comprobanteServicio.guardar(archivo);
                ingreso.setComprobante(comprobante);
            }

            ingresoRepositorio.save(ingreso);

        } else {
            throw new ErrorServicio("No existe el gasto que desea modificar.");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void eliminar(String id) throws ErrorServicio {

        Optional<Ingreso> respuesta = ingresoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Ingreso ingreso = respuesta.get();
            ingresoRepositorio.delete(ingreso);
        } else {
            throw new ErrorServicio("No existe el gasto que desea eliminar.");
        }
    }

    public void validar(Double monto, CategoriaIngreso categoria) throws ErrorServicio {
        if (monto == null || monto.toString().isEmpty()) {
            throw new ErrorServicio("Debe ingresar un importe.");
        }
        if (categoria == null || categoria.toString().isEmpty()) {
            throw new ErrorServicio("Debe seleccionar una Categoría.");
        }

    }

    public Ingreso buscarPorID(String id) throws ErrorServicio {
        Optional<Ingreso> respuesta = ingresoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Ingreso ingreso = respuesta.get();
            return ingreso;
        } else {
            throw new ErrorServicio("No se encontro un Ingreso con ese ID");
        }

    }

    @Transactional(readOnly = true)
    public List<Ingreso> listar(String usuarioId) {
        return (List<Ingreso>) ingresoRepositorio.listarPorUsuario(usuarioId);
    }
    
    @Transactional(readOnly = true)
    public List<Ingreso> listarPorFecha(String usuarioId) {
        return (List<Ingreso>) ingresoRepositorio.ordenarPorFechaDesc(usuarioId);
    }
}
