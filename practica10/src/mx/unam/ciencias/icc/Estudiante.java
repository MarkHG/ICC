package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Clase para representar estudiantes. Un estudiante tiene nombre, número de
 * cuenta, promedio y edad. La clase implementa {@link Registro}, por lo que
 * puede cargarse y guardarse utilizando objetos de las clases {@link
 * BufferedReader} y {@link BufferedWriter} como entrada y salida
 * respectivamente.
 */
public class Estudiante implements Registro {

    /* Nombre del estudiante. */
    private StringProperty nombre;
    /* Número de cuenta. */
    private IntegerProperty cuenta;
    /* Pormedio del estudiante. */
    private DoubleProperty promedio;
    /* Edad del estudiante.*/
    private IntegerProperty edad;

    /**
     * Construye un estudiante con todas sus propiedades.
     * @param nombre el nombre del estudiante.
     * @param cuenta el número de cuenta del estudiante.
     * @param promedio el promedio del estudiante.
     * @param edad la edad del estudiante.
     */
    public Estudiante(String nombre,
                      int    cuenta,
                      double promedio,
                      int    edad) {
	this.nombre = new SimpleStringProperty (nombre);
	this.cuenta = new SimpleIntegerProperty (cuenta);
	this.promedio = new SimpleDoubleProperty (promedio);
	this.edad = new SimpleIntegerProperty (edad);
        // Aquí va su código.
    }

    /**
     * Regresa el nombre del estudiante.
     * @return el nombre del estudiante.
     */
    public String getNombre() {
	return nombre.get();
        // Aquí va su código.
    }

    /**
     * Define el nombre del estudiante.
     * @param nombre el nuevo nombre del estudiante.
     */
    public void setNombre(String nombre) {
   this.nombre.setValue(nombre);
        // Aquí va su código.
    }

    /**
     * Regresa la propiedad del nombre.
     * @return la propiedad del nombre.
     */
    public StringProperty getNombreProperty() {
	return nombre;
        // Aquí va su código.

    }

    /**
     * Regresa el número de cuenta del estudiante.
     * @return el número de cuenta del estudiante.
     */
    public int getCuenta() {
	return cuenta.get();
        // Aquí va su código.
    }

    /**
     * Define el número cuenta del estudiante.
     * @param cuenta el nuevo número de cuenta del estudiante.
     */
    public void setCuenta(int cuenta) {
	this.cuenta.setValue(cuenta);
        // Aquí va su código.
    }

    /**
     * Regresa la propiedad del número de cuenta.
     * @return la propiedad del número de cuenta.
     */
    public IntegerProperty getCuentaProperty() {
	return cuenta;
        // Aquí va su código.
    }

    /**
     * Regresa el promedio del estudiante.
     * @return el promedio del estudiante.
     */
    public double getPromedio() {
	return promedio.get();
        // Aquí va su código.
    }

    /**
     * Define el promedio del estudiante.
     * @param promedio el nuevo promedio del estudiante.
     */
    public void setPromedio(double promedio) {
	this.promedio.setValue(promedio);
        // Aquí va su código.
    }

    /**
     * Regresa la propiedad del promedio.
     * @return la propiedad del promedio.
     */
    public DoubleProperty getPromedioProperty() {
	return promedio;
        // Aquí va su código.
    }

    /**
     * Regresa la edad del estudiante.
     * @return la edad del estudiante.
     */
    public int getEdad() {
	return edad.get();
        // Aquí va su código.
    }

    /**
     * Define la edad del estudiante.
     * @param edad la nueva edad del estudiante.
     */
    public void setEdad(int edad) {
	this.edad.setValue(edad);
        // Aquí va su código.
    }

    /**
     * Regresa la propiedad de la edad.
     * @return la propiedad de la edad.
     */
    public IntegerProperty getEdadProperty() {
	return edad;
        // Aquí va su código.
    }

    /**
     * Nos dice si el objeto recibido es un estudiante igual al que manda llamar
     * el método.
     * @param o el objeto con el que el estudiante se comparará.
     * @return <tt>true</tt> si el objeto o es un estudiante con las mismas
     *         propiedades que el objeto que manda llamar al método,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
	if(o instanceof Estudiante) {
		Estudiante e = (Estudiante)o;
		if(nombre.get().equals(e.nombre.get())&&cuenta.get()==e.cuenta.get()&&edad.get()==e.edad.get()&&promedio.get()==e.promedio.get())
			return true;	
		return false;
	}
	return false;
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena del estudiante.
     * @return una representación en cadena del estudiante.
     */
    @Override public String toString() {
	return String.format("Nombre   : %s\n" +
                                      "Cuenta   : %d\n" +
                                      "Promedio : %2.2f\n" +
                                      "Edad     : %d",
                                      nombre.get(), cuenta.get(), promedio.get(), edad.get());
        // Aquí va su código.
    }

    /**
     * Guarda al estudiante en la salida recibida.
     * @param out la salida dónde hay que guardar al estudiante.
     * @throws IOException si un error de entrada/salida ocurre.
     */
    @Override public void guarda(BufferedWriter out) throws IOException {
	out.write(String.format("%s\t%d\t%2.2f\t%d\n", nombre.get(), cuenta.get(), promedio.get(), edad.get()));
        // Aquí va su código.
    }

    /**
     * Carga al estudiante de la entrada recibida.
     * @param in la entrada de dónde hay que cargar al estudiante.
     * @return <tt>true</tt> si el método carga un estudiante válido,
     *         <tt>false</tt> en otro caso.
     * @throws IOException si un error de entrada/salida ocurre.
     */
    @Override public boolean carga(BufferedReader in) throws IOException {
	String linea = in.readLine();
	if(linea==null)
	return false;
	linea=linea.trim();
	if(linea.equals(""))
	return false;
	String[] partes=linea.split("\t");
	if(partes.length!=4)
	throw new IOException("");
	nombre.setValue(partes [0]);
	try {
		cuenta.setValue(Integer.parseInt(partes[1]));
		promedio.setValue(Double.parseDouble(partes[2]));
		edad.setValue(Integer.parseInt(partes[3]));
	} catch (NumberFormatException nfe) {
		throw new IOException("");
	}
	return true;
        // Aquí va su código.
    }
}
