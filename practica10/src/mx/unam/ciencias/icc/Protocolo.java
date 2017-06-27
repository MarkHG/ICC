package mx.unam.ciencias.icc;

/**
 * Enumeración para los mensajes del protocolo que se usa en la comunicación
 * entre el servidor y el cliente de bases de datos.
 */
public enum Protocolo {

    /**
     * El cliente solicita toda la base de datos. Si este mensaje es recibido
     * por el servidor, debe contestar enviando toda la base de datos al
     * cliente. Un cliente ignorará este mensaje después de imprimir una
     * advertencia en consola.
     */
    ENVIAR_BASE_DE_DATOS,

    /**
     * El interlocutor agregó un registro. Si este mensaje es recibido por el
     * servidor o el cliente, inmediatamente después recibirá un registro que
     * debe agregar a la base de datos.
     */
    REGISTRO_AGREGADO,

    /**
     * El interlocutor eliminó un registro. Si este mensaje es recibido por el
     * servidor o el cliente, inmediatamente después recibirá un registro que
     * debe eliminar de la base de datos.
     */
    REGISTRO_ELIMINADO,

    /**
     * El interlocutor modificó un registro. Si este mensaje es recibido por el
     * servidor o el cliente, inmediatamente después recibirá dos registros: el
     * primero será el registro original, y el segundo los nuevos valores de
     * dicho registro.
     */
    REGISTRO_MODIFICADO,

    /**
     * El servidor debe detenerse, desconectando a todos los clientes que
     * pudieran estar conectados.
     */
    DETENER_SERVICIO,

    /**
     * El comando recibido no es reconocido.
     */
    COMANDO_INVALIDO;

    /**
     * Descifra un comando recibido.
     * @param linea la línea de texto con el comando. La cadena recibida debe
     *        comenzar con "|=COMANDO:", seguido del comando, de otra forma se
     *        le considerará inválido.
     * @return el comando del protocolo correspondiente a la línea.
     */
    public static Protocolo getComando(String linea) {
        if (!linea.startsWith("|=COMANDO:"))
            return COMANDO_INVALIDO;
        String[] t = linea.split(":");
        if (t.length != 2)
            return COMANDO_INVALIDO;
        switch (t[1]) {
        case "ENVIAR_BASE_DE_DATOS": return ENVIAR_BASE_DE_DATOS;
        case "REGISTRO_AGREGADO":    return REGISTRO_AGREGADO;
        case "REGISTRO_ELIMINADO":   return REGISTRO_ELIMINADO;
        case "REGISTRO_MODIFICADO":  return REGISTRO_MODIFICADO;
        case "DETENER_SERVICIO":     return DETENER_SERVICIO;
        case "COMANDO_INVALIDO":
        default:                     return COMANDO_INVALIDO;
        }
    }

    /**
     * Genera un comando válido.
     * @param comando el comando del protocolo que queremos generar.
     * @return la cadena correspondiente al comando del protocolo.
     */
    public static String getComando(Protocolo comando) {
        String cmd = "|=COMANDO:";
        switch (comando) {
        case ENVIAR_BASE_DE_DATOS: return cmd + "ENVIAR_BASE_DE_DATOS";
        case REGISTRO_AGREGADO:    return cmd + "REGISTRO_AGREGADO";
        case REGISTRO_ELIMINADO:   return cmd + "REGISTRO_ELIMINADO";
        case REGISTRO_MODIFICADO:  return cmd + "REGISTRO_MODIFICADO";
        case DETENER_SERVICIO:     return cmd + "DETENER_SERVICIO";
        case COMANDO_INVALIDO:
        default:                   return cmd + "COMANDO_INVALIDO";
        }
    }
}
