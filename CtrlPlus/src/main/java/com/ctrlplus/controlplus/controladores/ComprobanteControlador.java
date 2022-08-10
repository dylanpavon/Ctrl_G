package com.ctrlplus.controlplus.controladores;

import com.ctrlplus.controlplus.entidades.Gasto;
import com.ctrlplus.controlplus.entidades.Ingreso;
import com.ctrlplus.controlplus.errores.ErrorServicio;
import com.ctrlplus.controlplus.servicios.GastoServicio;
import com.ctrlplus.controlplus.servicios.IngresoServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comprobante")
public class ComprobanteControlador {
    
    @Autowired
    private GastoServicio gastoServicio;
    @Autowired
    private IngresoServicio ingresoServicio;
    
    @GetMapping ("/gasto/{id}")
    public ResponseEntity<byte[]> comprobanteGasto (@PathVariable String id){ //id del gasto
        try {
            
            Gasto gasto = gastoServicio.buscarPorID(id);
            byte[] comprobante = gasto.getComprobante().getContenido();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
            if(gasto.getComprobante() == null){
                throw new ErrorServicio("El gasto no tiene un comprobante cargado.");
            }
            
            return new ResponseEntity<>(comprobante, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(ComprobanteControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
            
    @GetMapping ("/ingreso/{id}")
    public ResponseEntity<byte[]> comprobanteIngreso (@PathVariable String id){ //id del ingreso
        try {
            
            Ingreso ingreso = ingresoServicio.buscarPorID(id);
            byte[] comprobante = ingreso.getComprobante().getContenido();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
            if(ingreso.getComprobante() == null){
                throw new ErrorServicio("El ingreso no tiene un comprobante cargado.");
            }
            
            return new ResponseEntity<>(comprobante, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(ComprobanteControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
