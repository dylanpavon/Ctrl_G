package com.ctrlplus.controlplus.servicios;

import com.ctrlplus.controlplus.entidades.Comprobante;
import com.ctrlplus.controlplus.errores.ErrorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ctrlplus.controlplus.repositorios.ComprobanteRepositorio;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ComprobanteServicio {

    @Autowired
    private ComprobanteRepositorio comprobanteRepositorio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Comprobante guardar(MultipartFile file) throws ErrorServicio {

        if (file != null) {
            Comprobante foto = new Comprobante();
            foto.setMime(file.getContentType());
            foto.setNombre(file.getName());
            try {
                foto.setContenido(file.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(ComprobanteServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
            return comprobanteRepositorio.save(foto);

        } else {

            throw new ErrorServicio("No se puede cargar la foto");

        }

    }

    @Transactional(propagation = Propagation.NESTED)
    public void eliminar(String id) throws ErrorServicio {
        Optional<Comprobante> respuesta = comprobanteRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Comprobante foto = respuesta.get();
            comprobanteRepositorio.delete(foto);
        } else {
            throw new ErrorServicio("El comprobante que busca no se encuentra entre los existentes.");
        }
    }

}
