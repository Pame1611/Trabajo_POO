package cochera.gui;

/**
 * INTERFAZ que implementan todos los paneles (formularios) de la ventana principal.
 * La ventana no sabe qué panel concreto está mostrando: solo llama a refrescar()
 * (polimorfismo a través de una interfaz).
 */
public interface Refrescable {

    /** Vuelve a leer los datos y actualiza la pantalla. */
    void refrescar();

    /** true si el panel debe actualizarse solo cada pocos segundos (vistas "en vivo"). */
    default boolean seActualizaSolo() {
        return false;
    }
}
