package cochera.modelo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Usuario del sistema (tabla perfiles + auth.users de Supabase).
 *
 * Clase ABSTRACTA: no existe un "usuario genérico", siempre es un
 * Administrador o un Empleado (herencia). La contraseña nunca se guarda
 * en texto plano, solo su hash SHA-256 (encapsulamiento).
 */
public abstract class Usuario {

    private final String id;
    private final String email;
    private final String passwordHash;
    private final String nombreCompleto;
    private final LocalDateTime creadoEn;

    protected Usuario(String email, String password, String nombreCompleto, LocalDateTime creadoEn) {
        this.id = UUID.randomUUID().toString();
        this.email = email.trim().toLowerCase();
        this.passwordHash = hashear(password);
        this.nombreCompleto = nombreCompleto == null ? "" : nombreCompleto.trim();
        this.creadoEn = creadoEn;
    }

    /** Rol del usuario: cada subclase define el suyo (polimorfismo). */
    public abstract Rol getRol();

    /** ¿Puede abrir turno, registrar entradas y cobrar? */
    public abstract boolean puedeOperarCaja();

    public boolean esAdministrador() {
        return getRol() == Rol.ADMIN;
    }

    public boolean verificarPassword(String password) {
        return password != null && passwordHash.equals(hashear(password));
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNombreCompleto() {
        return nombreCompleto.isEmpty() ? email : nombreCompleto;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + getRol() + ")";
    }

    private static String hashear(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
