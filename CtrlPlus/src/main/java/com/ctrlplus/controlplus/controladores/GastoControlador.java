package com.ctrlplus.controlplus.controladores;

import com.ctrlplus.controlplus.entidades.Gasto;
import com.ctrlplus.controlplus.entidades.Usuario;
import com.ctrlplus.controlplus.enums.Categoria;
import com.ctrlplus.controlplus.errores.ErrorServicio;
import com.ctrlplus.controlplus.servicios.GastoServicio;
import com.ctrlplus.controlplus.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO')")
@RequestMapping("/gasto")
public class GastoControlador {

    @Autowired
    private GastoServicio gastoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/ingresar")
    public String ingresar(ModelMap modelo,
            HttpSession session,
            @RequestParam() Double monto,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) MultipartFile archivo,
            @RequestParam Categoria categoria) {

        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if (logeado == null) {
            return "login";
        }
        try {
            gastoServicio.agregar(monto, categoria, descripcion, logeado, archivo);

            return "redirect:/inicio";
        } catch (ErrorServicio e) {

            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("monto", monto);
            modelo.addAttribute("categoria", categoria);
            if (descripcion != null) {
                modelo.addAttribute("descripcion", descripcion);
            }
            if (archivo != null) {
                modelo.addAttribute("comprobante", archivo);
            } //in God we trust
            return "index";
        }
    }

    @PostMapping("/modificar")
    public String modificar(ModelMap modelo, @RequestParam() Double monto, @RequestParam(required = false) String descripcion, @RequestParam(required = false) MultipartFile archivo, @RequestParam() String id, @RequestParam Categoria categoria) {
        try {

            gastoServicio.modificar(id, monto, categoria, descripcion, archivo);
            return "redirect:/gasto/listar";
        } catch (ErrorServicio e) {

            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("monto", monto);
            if (descripcion != null) {
                modelo.addAttribute("descripcion", descripcion);
            }
            if (archivo != null) {
                modelo.addAttribute("comprobante", archivo);
            } //in God we trust
            modelo.addAttribute("id", id);
            return "index";
        }

    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo, HttpSession session) {
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if (logeado == null) {
            return "/login";
        }
        modelo.addAttribute("gastos", gastoServicio.listarPorFecha(logeado.getId()));
        modelo.addAttribute("categorias", Categoria.values());
        return "gastos";// devolver donde se vea
    }

    @PostMapping("/eliminar")
    public String eliminar(ModelMap modelo, @RequestParam String id, HttpSession session) {
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if (logeado == null) {
            return "/login";
        }
        try {
            gastoServicio.eliminar(id);
            return "redirect:/gasto/listar";
        } catch (ErrorServicio ex) {

            modelo.addAttribute("error", ex.getMessage());
            modelo.addAttribute("id", id);// creo que no hace falta devolverlo
            modelo.addAttribute("ingresos", gastoServicio.listarPorFecha(logeado.getId()));
            return "/gasto/listar";
        }

    }
}
