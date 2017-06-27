package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Clase abstracta para bases de datos genéricas. Provee métodos para agregar y
 * eliminar registros, y para guardarse y cargarse de una entrada y salida
 * dados.
 *
 * Las clases que extiendan a BaseDeDatos deben implementar el método {@link
 * #creaRegistro}, que crea un registro genérico en blanco. También deben
 * implementar el método {@link #buscaRegistros} para hacer consultas en la base
 * de datos.
 */
public abstract class BaseDeDatos<T extends Registro> {

    /** Lista de registros en la base de datos. */
    protected Lista<T> registros;
    /** Lista de escuchas de la base de datos. */
    protected Lista<EscuchaBaseDeDatos<T>> escuchas;

    /**
     * Constructor único.
     */
    public BaseDeDatos() {
	registros= new Lista<T>();
	escuchas= new Lista<EscuchaBaseDeDatos<T>>();
        // Aquí va su código.
    }

    /**
     * Regresa el número de registros en la base de datos.
     * @return el número de registros en la base de datos.
     */
    public int getNumRegistros() {
	return registros.getLongitud();
        // Aquí va su código.
    }

    /**
     * Regresa una lista con los registros en la base de datos. Modificar esta
     * lista no cambia a la información en la base de datos.
     * @return una lista con los registros en la base de datos.
     */
    public Lista<T> getRegistros() {
	return registros.copia();
        // Aquí va su código.
    }

    /**
     * Agrega el registro recibido a la base de datos.
     * @param registro el registro que hay que agregar a la base de datos.
     */
    public void agregaRegistro(T registro) {
	registros.agregaFinal(registro);
for(EscuchaBaseDeDatos<T> escucha: escuchas){
	      escucha.baseDeDatosModificada(EventoBaseDeDatos.REGISTRO_AGREGADO,registro);}
	

        // Aquí va su código.
    }

    /**
     * Elimina el registro recibido de la base de datos.
     * @param registro el registro que hay que eliminar de la base de datos.
     */
    public void eliminaRegistro(T registro) {
	registros.elimina(registro);
for(EscuchaBaseDeDatos<T> escucha: escuchas){
	      escucha.baseDeDatosModificada(EventoBaseDeDatos.REGISTRO_ELIMINADO,registro);}
	
        // Aquí va su código.
    }

    /**
     * Guarda todos los registros en la base de datos en la salida recibida.
     * @param out la salida donde hay que guardar los registos.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public void guarda(BufferedWriter out) throws IOException {
	for (T r : registros)
		r.guarda(out);
        // Aquí va su código.
    }

    /**
     * Guarda los registros de la entrada recibida en la base de datos. Si antes
     * de llamar el método había registros en la base de datos, estos son
     * eliminados.
     * @param in la entrada de donde hay que cargar los registos.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public void carga(BufferedReader in) throws IOException {
	registros.limpia();
  for(EscuchaBaseDeDatos<T> escucha: escuchas){
	      escucha.baseDeDatosModificada(EventoBaseDeDatos.BASE_LIMPIADA,null);}
	do{
	T r=creaRegistro();
	if(!r.carga(in)) 
	break;
	
	registros.agregaFinal(r);
for(EscuchaBaseDeDatos<T> escucha: escuchas){
	      escucha.baseDeDatosModificada(EventoBaseDeDatos.REGISTRO_AGREGADO,r);}
	}while(true);
        // Aquí va su código.
    }

    /**
     * Crea un registro en blanco.
     * @return un registro en blanco.
     */
    public abstract T creaRegistro();
	

    /**
     * Busca registros por un campo específico.
     * @param campo el campo del registro por el cuál buscar.
     * @param texto el texto a buscar.
     * @return una lista con los registros tales que en el campo especificado
     *         contienen el texto recibido.
     * @throws IllegalArgumentException si el campo no es válido.
     */
    public abstract Lista<T> buscaRegistros(String campo, String texto);

    /**
     * Limpia la base de datos.
     */
    public void limpia() {
	registros.limpia();
for(EscuchaBaseDeDatos<T> escucha: escuchas){
	      escucha.baseDeDatosModificada(EventoBaseDeDatos.BASE_LIMPIADA,null);}
	
        // Aquí va su código.
    }

    /**
     * Agrega un escucha a la base de datos.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscucha(EscuchaBaseDeDatos<T> escucha) {
	escuchas.agregaFinal(escucha);

        // Aquí va su código.
    }
}
