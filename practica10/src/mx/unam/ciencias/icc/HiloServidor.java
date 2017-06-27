package mx.unam.ciencias.icc;

import java.io.IOException;
import java.net.Socket;

/**
 * Clase para hilos de ejecución que manejan conexiones del servidor a los
 * clientes.
 */
public class HiloServidor<T extends Registro> extends HiloConexion<T> {

    /* El servidor de base de datos. */
    private ServidorBaseDeDatos<T> sbdd;
    /* El nombre del cliente. */
    private String cliente;
    /* El identificador del hilo. */
    private int id;

    /**
     * Crea un nuevo hilo de ejecución para manejar la conexión del servidor a
     * un cliente.
     * @param sbdd el servidor de bases de datos.
     * @param bdd la base de datos.
     * @param enchufe el enchufe de la conexión.
     * @param id el identificador.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public HiloServidor(ServidorBaseDeDatos<T> sbdd,
                        BaseDeDatos<T> bdd,
                        Socket enchufe,
                        int id)
        throws IOException {
        // Aquí va su código.
	super(bdd, enchufe);
	this.sbdd=sbdd;
	this.id = id;
    }

    /**
     * Maneja la conexión con el cliente.
     */
    public void run() {
        // Aquí va su código.
	cliente = enchufe.getInetAddress().getCanonicalHostName();
	log.printf("Conexion recibida de (%d).\n", id);
	String linea = null;
	try {
		while((linea= in.readLine())!=null) {
			Protocolo comando = Protocolo.getComando(linea);
			switch(comando) {
				case ENVIAR_BASE_DE_DATOS:
					enviarBaseDeDatos();
					break;
				case REGISTRO_AGREGADO:
					registroAgregado();
					break;	
				case REGISTRO_ELIMINADO:
					registroEliminado();
					break;		
				case REGISTRO_MODIFICADO:
					registroModificado();
					break;	
				case DETENER_SERVICIO:
					sbdd.termina();
					break;
				case COMANDO_INVALIDO:
				default:
					log.printf("Comando invalido de (%d): %s.\n" ,id, comando);
			}
		}
    } catch (IOException ioe) {
	if(!sbdd.isTerminado()) {
		log.printf("hubo un error al comunicarnos con el cliente (%d).\n", id);
		log.printf("Nos desconectaremos del cliente (%d).\n", id);
		desconecta();
	}
	}
	log.printf("Conexion de (%d) terminada.\n", id);
	sbdd.clienteDesconectado(this);
}

	private void enviarBaseDeDatos() throws IOException {
		bdd.guarda(out);
		out.newLine();
		out.flush();
		log.printf("Base de datos enviada a(%d).\n", id);
	}
	
	private void notificaHilos(Protocolo accion, T registro) throws IOException {
		notificaHilos(accion, registro, null);
	}

	private void notificaHilos(Protocolo accion, T registro, T modificado) throws IOException {
		for(HiloServidor<T> hilo: sbdd.getHilos()) {
			if(hilo==this)
				continue;
			String comando = Protocolo.getComando(accion);
			hilo.out.write(comando);
			hilo.out.newLine();
			registro.guarda(hilo.out);
			if(modificado != null)
				modificado.guarda(hilo.out);
			hilo.out.flush();
		}
	}
	
	private void registroAgregado() throws IOException {
		log.printf("Recibiendo registro de (%d) para agregar...\n", id);
		T registro = recibeRegistro();
		if(registro == null) {
			log.printf("Registro invalido recibido"+"de (%d) para agregar.\n", id);
			return;
	}
	log.printf("registro recibido de %(d).\n", id);
	synchronized(bdd) {
		bdd.agregaRegistro(registro);
		sbdd.guarda();
	}
	notificaHilos(Protocolo.REGISTRO_AGREGADO, registro);
	log.printf("Registro agregado de (%d).\n", id);
	}

	private void registroEliminado() throws IOException {
		log.printf("Recibiendo registro de (%d) para eliminar...\n", id);
		T registro = recibeRegistro();
		if(registro == null) {
			log.printf("Registro invalido recibido "+"de (%d) para eliminar.\n", id);
			return;
		}
		log.printf("Registro recibido de (%d).\n", id);
		synchronized (bdd) {
			bdd.eliminaRegistro(registro);
			sbdd.guarda();
		}
		notificaHilos(Protocolo.REGISTRO_ELIMINADO, registro);
		log.printf("Registro agregado de (%d).\n", id);
		}

	private void registroModificado() throws IOException {
		log.printf("Recibiendo registro original de "+"(%d) para ser modificado ...\n",id);
		T original = recibeRegistro();
		if(original==null) {
			log.printf("Registro original invalido recibido de "+"(%d) para ser modificado.\n", id);
			return;
		}
		log.printf("Registro original recibido de (%d).\n", id);
		log.printf("Recibiendo registro modificado de (%d)...\n", id);
		T modificado = recibeRegistro();
		if(modificado == null) {
			log.printf("Registro modificado invalido "+"recibido de (%d).\n", id);
			return;
		}
		log.printf("Registro modificado recibido de (%d).\n", id);
		synchronized(bdd) {
			bdd.eliminaRegistro (original);
			bdd.agregaRegistro(modificado);
			sbdd.guarda();
		}
		notificaHilos(Protocolo.REGISTRO_MODIFICADO, original, modificado);
		log.printf("Registro modificado de (%d).\n", id);
		}
}
