package mx.unam.ciencias.icc;

/**
 * Clase para manejar la salida de nuestro servidor, y poder apagarla en
 * nuestras pruebas unitarias.
 */
public class Bitacora {

    /* La única instancia de la clase (singleton). */
    private static Bitacora bitacora;
    /* Bandera de activación. */
    private boolean activada;

    /* Constructor privado para que no pueda ser llamado fuera de la clase. */
    private Bitacora() {
        activada = true;
    }

    /**
     * Activa o desactiva la bitácora.
     * @param activada si la bitácora está activada o no.
     */
    public void setActivada(boolean activada) {
        this.activada = activada;
    }

    /**
     * Si la bitácora está activada, imprime el formato con los parámetros
     * correspondientes. Si no, no hace nada.
     * @param formato el formato a imprimir.
     * @param parametros los parámetros del formato.
     */
    public void printf(String formato, Object ... parametros) {
        if (activada)
            System.err.printf(formato, parametros);
    }

    /**
     * Si la bitácora está activada, imprime el mensaje con un salto de
     * línea. Si no, no hace nada.
     * @param mensaje el mensaje a imprimir.
     */
    public void println(String mensaje) {
        if (activada)
            System.err.println(mensaje);
    }

    /**
     * Regresa la única instancia de la clase.
     * @return la única instancia de la clase.
     */
    public static Bitacora getInstancia() {
        if (bitacora == null)
            bitacora = new Bitacora();
        return bitacora;
    }
}
