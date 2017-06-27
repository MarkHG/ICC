package mx.unam.ciencias.icc;

/**
 * Escucha para el hilo de ejecución del cliente.
 */
@FunctionalInterface
public interface EscuchaHiloCliente {

    /**
     * Notifica de un error que ocurrió en el hilo de ejecución del cliente, y
     * que forzó la desconexión con el servidor.
     * @param error el error que ocurrió.
     */
    public void errorHiloCliente(ErrorHiloCliente error);
}
