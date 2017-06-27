package mx.unam.ciencias.icc;

/**
 * Enumeración para los errores generados en el hilo de ejecución del cliente.
 */
public enum ErrorHiloCliente {

    /** Hubo un error al cargar la base de datos. */
    ERROR_AL_CARGAR,
    /** El servidor desapareció. */
    ERROR_SERVIDOR,
    /** Hubo un error al recibir datos. */
    ERROR_AL_RECIBIR,
    /** Hubo un error al enviar datos. */
    ERROR_AL_ENVIAR
}
