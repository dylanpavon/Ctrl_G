package com.ctrlplus.controlplus.servicios;

import com.ctrlplus.controlplus.entidades.Comprobante;
import com.ctrlplus.controlplus.entidades.Gasto;
import com.ctrlplus.controlplus.entidades.Usuario;
import com.ctrlplus.controlplus.enums.Categoria;
import com.ctrlplus.controlplus.errores.ErrorServicio;
import com.ctrlplus.controlplus.repositorios.GastoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GastoServicio {

    @Autowired
    private GastoRepositorio gastoRepositorio;
    @Autowired
    private ComprobanteServicio comprobanteServicio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}) //agregar metodo agregarGasto proveniente de UsuarioServicio
    public Gasto agregar(Double monto, Categoria categoria, String descripcion, Usuario usuario, MultipartFile archivo) throws ErrorServicio {

        validar(monto, categoria);

        Gasto gasto = new Gasto();

        gasto.setMonto(monto);
        gasto.setFecha(new Date());
        gasto.setCategoria(categoria);
        gasto.setDescripcion(descripcion);
        gasto.setUsuario(usuario);
        if (archivo != null) {
            Comprobante comprobante = comprobanteServicio.guardar(archivo);
            gasto.setComprobante(comprobante);
        }

        return gastoRepositorio.save(gasto);
    }

//    @Transactional(propagation = Propagation.NESTED)
//    public void asignarAlUsuario(Usuario usuario, Gasto gasto) {
//        
//    }
    @Transactional(propagation = Propagation.NESTED)
    public void modificar(String id, Double monto, Categoria categoria, String descripcion, MultipartFile archivo) throws ErrorServicio {

        validar(monto, categoria);

        Optional<Gasto> respuesta = gastoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Gasto gasto = respuesta.get();
            gasto.setMonto(monto);
            gasto.setFecha(new Date());
            gasto.setCategoria(categoria);
            gasto.setDescripcion(descripcion);
            if (archivo != null) {
                Comprobante comprobante = comprobanteServicio.guardar(archivo);
                gasto.setComprobante(comprobante);
            }

            gastoRepositorio.save(gasto);

        } else {
            throw new ErrorServicio("No existe el gasto que desea modificar.");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void eliminar(String id) throws ErrorServicio {

        Optional<Gasto> respuesta = gastoRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Gasto gasto = respuesta.get();
            gastoRepositorio.delete(gasto);
        } else {
            throw new ErrorServicio("No existe el gasto que desea eliminar.");
        }
    }

    public void validar(Double monto, Categoria categoria) throws ErrorServicio {
        if (monto == null || monto.toString().isEmpty()) {
            throw new ErrorServicio("Debe ingresar un importe.");
        }

        if (categoria == null || categoria.toString().isEmpty()) {
            throw new ErrorServicio("Debe seleccionar una Categoría.");
        }
    }

    @Transactional(readOnly = true)
    public Gasto buscarPorID(String id) throws ErrorServicio {
        Optional<Gasto> respuesta = gastoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Gasto gasto = respuesta.get();
            return gasto;
        } else {
            throw new ErrorServicio("No se encontro un Gasto con ese ID");
        }

    }
    
    @Transactional(readOnly = true)
    public List<Gasto> listar(String usuarioId) {
        return (List<Gasto>) gastoRepositorio.listarPorUsuario(usuarioId);
    }
    
    @Transactional(readOnly = true)
    public List<Gasto> listarPorFecha(String usuarioId) {
        return (List<Gasto>) gastoRepositorio.ordenarPorFechaDesc(usuarioId);
    }
}
