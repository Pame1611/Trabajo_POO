package cochera.servicio;

import cochera.datos.BaseDatos;
import cochera.excepcion.AccesoDenegadoException;
import cochera.excepcion.CocheraException;
import cochera.excepcion.ValidacionException;
import cochera.modelo.Administrador;
import cochera.modelo.Empleado;
import cochera.modelo.Usuario;
import cochera.util.Reloj;
import cochera.util.Texto;
import java.util.List;

/**
 * Inicio/cierre de sesión y control de acceso por rol (auth.ts + proxy.ts + RLS es_admin()).
 */
public class AutenticacionServicio {

    public static final int LONGITUD_MINIMA_CLAVE = 6;

    private final BaseDatos bd;
    private final Reloj reloj;
    private Usuario usuarioActual;

    public AutenticacionServicio(BaseDatos bd, Reloj reloj) {
        this.bd = bd;
        this.reloj = reloj;
    }

    public Usuario iniciarSesion(String email, String password) throws CocheraException {
        if (Texto.vacio(email) || password == null || password.isEmpty()) {
            throw new ValidacionException("Completa todos los campos.");
        }
        Usuario u = bd.buscarUsuarioPorEmail(email);
        if (u == null || !u.verificarPassword(password)) {
            throw new AccesoDenegadoException("Correo o contraseña incorrectos.");
        }
        usuarioActual = u;
        return u;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public boolean haySesion() {
        return usuarioActual != null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Usuario requerirSesion() throws AccesoDenegadoException {
        if (usuarioActual == null) {
            throw new AccesoDenegadoException("Sin sesión activa.");
        }
        return usuarioActual;
    }

    public Administrador requerirAdmin() throws AccesoDenegadoException {
        Usuario u = requerirSesion();
        if (!(u instanceof Administrador)) {
            throw new AccesoDenegadoException("Acceso denegado. Solo administradores.");
        }
        return (Administrador) u;
    }

    public Empleado requerirEmpleado(String mensaje) throws AccesoDenegadoException {
        Usuario u = requerirSesion();
        if (!(u instanceof Empleado)) {
            throw new AccesoDenegadoException(mensaje);
        }
        return (Empleado) u;
    }

    /**
     * Alta de un nuevo cajero (solo ADMIN). Todo usuario nuevo nace con rol EMPLEADO,
     * igual que el trigger handle_new_user() de la base de datos.
     */
    public Empleado registrarEmpleado(String email, String password, String nombreCompleto) throws CocheraException {
        requerirAdmin();
        if (Texto.vacio(email) || !email.contains("@") || email.trim().contains(" ")) {
            throw new ValidacionException("Ingresa un correo válido.");
        }
        if (password == null || password.length() < LONGITUD_MINIMA_CLAVE) {
            throw new ValidacionException("La contraseña debe tener al menos " + LONGITUD_MINIMA_CLAVE + " caracteres.");
        }
        Empleado nuevo = new Empleado(email, password, nombreCompleto, reloj.ahora());
        bd.insertarUsuario(nuevo);
        return nuevo;
    }

    public List<Usuario> listarUsuarios() throws CocheraException {
        requerirAdmin();
        return bd.listarUsuarios();
    }
}
