package com.ctrlplus.controlplus.servicios;

import com.ctrlplus.controlplus.entidades.Gasto;
import com.ctrlplus.controlplus.entidades.Ingreso;
import com.ctrlplus.controlplus.entidades.Usuario;
import com.ctrlplus.controlplus.enums.Rol;
import com.ctrlplus.controlplus.errores.ErrorServicio;
import com.ctrlplus.controlplus.repositorios.GastoRepositorio;
import com.ctrlplus.controlplus.repositorios.IngresoRepositorio;
import com.ctrlplus.controlplus.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private IngresoRepositorio ingresoRepositorio;
    @Autowired
    private GastoRepositorio gastoRepositorio;

    @Transactional(propagation = Propagation.NESTED)
    public Usuario registrar(String mail, String nombre, String clave, String clave2) throws ErrorServicio {

        validar(mail, nombre, clave, clave2);

        Usuario usuario = new Usuario();

        usuario.setMail(mail);
        usuario.setNombre(nombre);
//        usuario.setGastos(new ArrayList<>());
//        usuario.setIngresos(new ArrayList<>());
        usuario.setRol(Rol.USUARIO);

        String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(claveEncriptada);

        return usuarioRepositorio.save(usuario);
    }

    @Transactional(propagation = Propagation.NESTED)
    public Usuario modificar(String id, String mail, String nombre, String clave, String clave2) throws ErrorServicio {

        validarSinMail(nombre, clave, clave2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            validarMail(mail, id);
            Usuario usuario = respuesta.get();
            usuario.setMail(mail);
            usuario.setNombre(nombre);

            String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(claveEncriptada);

            return usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro un usuario con ese ID.");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void eliminar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuarioRepositorio.delete(usuario);
        } else {
            throw new ErrorServicio("No se encontró un usuario con ese id.");
        }
    }

    public String conseguirNombre(String mail) {
        StringTokenizer st = new StringTokenizer(mail, "@");
        return st.nextToken();
    }

    public void validar(String mail, String nombre, String clave, String clave2) throws ErrorServicio {

        String emailFormate = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern p = Pattern.compile(emailFormate);
        String claveSegura = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern c = Pattern.compile(claveSegura);

        if (mail == null || mail.trim().isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo.");
        }

        if (usuarioRepositorio.buscarPorMail(mail) != null) {
            throw new ErrorServicio("Ya existe un usuario con ese mail.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        if (!p.matcher(mail).matches()) {
            throw new ErrorServicio("El mail no es inválido.");
        }

        if (clave == null || clave.trim().isEmpty()) {
            throw new ErrorServicio("La clave no puede ser nula.");
        }

        if (!c.matcher(clave).matches()) {
            throw new ErrorServicio("La clave debe contener al menos 8 caracteres, una mayúscula, "
                    + "una minúscula, un número y un carácter especial.");
        }

        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales.");
        }

    }

    public void validarSinMail(String nombre, String clave, String clave2) throws ErrorServicio {

        String claveSegura = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern c = Pattern.compile(claveSegura);

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        if (clave == null || clave.trim().isEmpty()) {
            throw new ErrorServicio("La clave no puede ser nula.");
        }

        if (!c.matcher(clave).matches()) {
            throw new ErrorServicio("La clave debe contener al menos 8 caracteres, una mayúscula, "
                    + "una minúscula, un número y un carácter especial.");
        }

        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales.");
        }

    }

    public void validarMail(String mail, String id) throws ErrorServicio {

        if (mail == null || mail.trim().isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo.");
        }

        if (usuarioRepositorio.buscarPorMail(mail) != null) {
            if (!usuarioRepositorio.buscarPorMail(mail).getId().equals(id)) {
                throw new ErrorServicio("Ya existe un usuario con ese mail.");
            }  
        }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority permiso = new SimpleGrantedAuthority("ROLE_USUARIO");
            permisos.add(permiso);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }

    }

    public Double saldoIngresos(String IDusuario) {
        List<Ingreso> ingresos = ingresoRepositorio.listarPorUsuario(IDusuario);
        Double sumaI = 0.0;
        for (Ingreso ingreso : ingresos) {
            sumaI += ingreso.getMonto();
        }
        return sumaI;
    }

    public Double saldoGastos(String IDusuario) {
        List<Gasto> gastos = gastoRepositorio.listarPorUsuario(IDusuario);
        Double sumaG = 0.0;
        for (Gasto gasto : gastos) {
            sumaG += gasto.getMonto();
        }
        return sumaG;
    }

    public Double calcularSaldo(String IDusuario) {

        Double sumaI = saldoIngresos(IDusuario);
        Double sumaG = saldoGastos(IDusuario);

        Double saldo = sumaI - sumaG;

        return saldo;
    }

    public Usuario buscarPorID(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            return usuario;
        } else {
            throw new ErrorServicio("No se encontro un Usuario con ese ID");
        }

    }

    @Transactional(readOnly = true)
    public List<Object> listarMovimientos(String usuarioId) {
        return (List<Object>) usuarioRepositorio.ultimosMovimientos(usuarioId);
    }
}

//    @Transactional(propagation = Propagation.NESTED)
//    public void agregarIngreso(Usuario usuario, Ingreso ingreso) {
//        usuario.getIngresos().add(ingreso);
//    }
//    @Transactional(propagation = Propagation.NESTED)
//    public void agregarGasto(Usuario usuario, Gasto gasto) {
//        usuario.getGastos().add(gasto);
//    }

