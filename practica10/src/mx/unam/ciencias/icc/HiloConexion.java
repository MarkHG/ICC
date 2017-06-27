package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Clase abstracta para hilos de ejecución que manejan conexiones.
 */
public abstract class HiloConexion<T extends Registro> extends Thread {

    /** La base de datos. */
    protected BaseDeDatos<T> bdd;
    /** El enchufe. */
    protected Socket enchufe;
    /** La entrada de la conexión. */
    protected BufferedReader in;
    /** La salida de la conexión. */
    protected BufferedWriter out;
    /** Bitácora. */
    protected Bitacora log;

    /**
     * Construye un nuevo hilo de ejecución para manejar conexiones.
     * @param bdd la base de datos.
     * @param enchufe el enchufe de la conexión.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public HiloConexion(BaseDeDatos<T> bdd, Socket enchufe)
        throws IOException {
        // Aquí va su código.
	log = log.getInstancia();
	in = new BufferedReader(new InputStreamReader(enchufe.getInputStream()));
	out = new BufferedWriter(new OutputStreamWriter(enchufe.getOutputStream()));
	this.bdd = bdd;
	this.enchufe = enchufe;
	
    }

    /**
     * Cierra la conexión.
     */
    public void desconecta() {
        // Aquí va su código.
	try {
		enchufe.close();
	} catch (IOException ioe) {
	}
    }

    /**
     * Regresa un registro recibido de la conexión.
     * @return un registro recibido de la conexión, o <code>null</code> si el
     *         registro es inválido.
     * @throws IOException si hay un error durante la transferencia.
     */
    protected T recibeRegistro() throws IOException {
        // Aquí va su código.
	T r = bdd.creaRegistro();
	r.carga(in);
	if(r==null)
		return null;
	return r;
    }
}
