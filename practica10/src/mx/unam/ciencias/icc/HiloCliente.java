package mx.unam.ciencias.icc;

import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform;

/**
 * Clase para hilos de ejecución que manejan conexiones de los clientes al
 * servidor.
 */
public class HiloCliente<T extends Registro> extends HiloConexion<T> {

    /* Lista de escuchas. */
    private Lista<EscuchaHiloCliente> escuchas;
    /* Bandera de sincronización. */
    private boolean espera;
    /* Bandera de terminación. */
    private boolean terminado;
    /* Bandera para ignorar peticiones de aviso.  */
    private boolean ignorar;

    /**
     * Crea un nuevo hilo de ejecución para manejar la conexión de un cliente al
     * servidor.
     * @param bdd la base de datos.
     * @param enchufe el enchufe conectado al servidor.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public HiloCliente(BaseDeDatos<T> bdd, Socket enchufe)
        throws IOException {
        // Aquí va su código.
	super(bdd, enchufe);
	escuchas = new Lista<EscuchaHiloCliente>();
    }

    /**
     * Método principal del hilo de ejecución.
     */
    public void run() {
        // Aquí va su código.
	try {
		out.write(Protocolo.getComando(Protocolo.ENVIAR_BASE_DE_DATOS));
		out.newLine();
		out.flush();
		espera = true;
		Platform.runLater(() -> cargaBaseDeDatos());
		while(espera) {
			try{ Thread.sleep(100);} catch (InterruptedException ie) {}
		}
		String linea = null;
		while((linea= in.readLine())!=null) {
			Protocolo comando = Protocolo.getComando(linea);
			switch(comando) {
				case ENVIAR_BASE_DE_DATOS:
					log.println("Advertencia: los clientes no "+"pueden enviar la base de datos.");
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
				case COMANDO_INVALIDO:
				default:
					log.printf("Comando invalido: %s\n" , linea);
			}
		}
		log.printf("El servidor desaparecio.\n");
		notificaEscuchas(ErrorHiloCliente.ERROR_SERVIDOR);
		} catch (IOException ioe) {
			if(!terminado) {
				log.printf("Error al recibir datos.\n");
				notificaEscuchas(ErrorHiloCliente.ERROR_AL_RECIBIR);
			}
		}
		Platform.runLater(() -> bdd.limpia());
    }

    /**
     * Avisa un cambio al servidor.
     * @param evento el evento ocurrido.
     * @param registro el registro afectado.
     */
    public void avisaServidor(EventoBaseDeDatos evento, T registro) {
        // Aquí va su código.
	if(ignorar||terminado)
		return;
	String comando="";
	switch (evento) {
		case BASE_LIMPIADA:
			return;
		case REGISTRO_AGREGADO:
			comando = Protocolo.getComando(Protocolo.REGISTRO_AGREGADO);
			break;
		case REGISTRO_ELIMINADO:
			comando = Protocolo.getComando(Protocolo.REGISTRO_ELIMINADO);
			break;
	}
	try {
		out.write(comando);
		out.newLine();
		registro.guarda(out);
		out.flush();
	} catch (IOException ioe) {
		log.printf("Error al enviar datos.");
		notificaEscuchas(ErrorHiloCliente.ERROR_AL_ENVIAR);
	}
    }

    /**
     * Cierra la conexión con el servidor.
     */
    @Override public void desconecta() {
        // Aquí va su código.
	terminado = true;
    }

    /**
     * Agrega un escucha al hilo.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscucha(EscuchaHiloCliente escucha) {
        // Aquí va su código.
	escuchas.agregaFinal(escucha);
    }
	
	private void cargaBaseDeDatos() {
		try {
			ignorar = true;
			bdd.carga(in);
			ignorar = false;
		} catch (IOException ioe) {
			log.printf("Error al cargar la base da datos");
			notificaEscuchas(ErrorHiloCliente.ERROR_AL_CARGAR);
		}
		espera = false;
	}

	private void registroAgregado() throws IOException {
		T registro = recibeRegistro();
		if(registro==null) {
			log.println("Registro invalido recibido para agregar");
			return;
		}
		Platform.runLater(() -> { 
			ignorar = true;
			bdd.agregaRegistro(registro);
			ignorar = false;
			});
	}

	private void registroEliminado() throws IOException {
			T registro = recibeRegistro();
		if(registro==null) {
			log.println("Registro invalido recibido para eliminar");
			return;
		}
		Platform.runLater(() -> { 
			ignorar = true;
			bdd.eliminaRegistro(registro);
			ignorar = false;
			});
		}
	
	private void registroModificado() throws IOException {
		T original = recibeRegistro();
		if(original == null){
			log.println("Registro original invalido recibido");
			return;
		}
		T modificado = recibeRegistro();
		if(modificado == null) {
			log.printf("Registro modificado invalido recibido");
			return;
		}
		Platform.runLater(() -> { 
			ignorar = true;
			bdd.eliminaRegistro(original);
			bdd.agregaRegistro(modificado);
			ignorar = false;
			});
		}

	private void notificaEscuchas(ErrorHiloCliente error) {
		for(EscuchaHiloCliente escucha: escuchas)
			escucha.errorHiloCliente(error);
		desconecta();
	}
}
