package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Random;
import org.junit.Test;
import org.junit.Assert;
import mx.unam.ciencias.icc.Estudiante;

/**
 * Clase para pruebas unitarias de la clase {@link Estudiante}.
 */
public class TestEstudiante {

    /* Nombres. */
    private static String[] NOMBRES = {
        "Aaron", "Benito", "Carlos", "Daniel", "Ernesto",
        "Fernando", "Gerardo", "Hernán", "Inmaculada", "Juan"
    };

    /* Apellidos. */
    private static String[] APELLIDOS = {
        "Zapata", "Yacaxtle", "Xola", "Wellington", "Valdés",
        "Uribe", "Tejada", "Salazar", "Romero", "Quintana"
    };

    private static Random random;

    /**
     * Genera un nombre aleatorio.
     * @return un nombre aleatorio.
     */
    public static String nombreAleatorio() {
        int n = random.nextInt(NOMBRES.length);
        int ap = random.nextInt(APELLIDOS.length);
        int am = random.nextInt(APELLIDOS.length);
        return NOMBRES[n] + " " + APELLIDOS[ap] + " " + APELLIDOS[am];
    }

    /**
     * Genera un número de cuenta aleatorio.
     * @return un número de cuenta aleatorio.
     */
    public static int cuentaAleatoria() {
        return 1000000 + random.nextInt(1000000);
    }

    /**
     * Genera un promedio aleatorio.
     * @return un promedio aleatorio.
     */
    public static double promedioAleatorio() {
        /* Estúpida precisión. */
        return random.nextInt(100) / 10.0;
    }

    /**
     * Genera una edad aleatoria.
     * @return una edad aleatoria.
     */
    public static int edadAleatoria() {
        return 17 + random.nextInt(73);
    }

    /**
     * Genera un estudiante aleatorio.
     * @return un estudiante aleatorio.
     */
    public static Estudiante estudianteAleatorio() {
        return new Estudiante(nombreAleatorio(),
                              cuentaAleatoria(),
                              promedioAleatorio(),
                              edadAleatoria());
    }

    /**
     * Genera un estudiante aleatorio con un número de cuenta dado.
     * @param cuenta el número de cuenta del nuevo estudiante.
     * @return un estudiante aleatorio.
     */
    public static Estudiante estudianteAleatorio(int cuenta) {
        return new Estudiante(nombreAleatorio(),
                              cuenta,
                              promedioAleatorio(),
                              edadAleatoria());
    }

    private Estudiante estudiante;

    /**
     * Prueba unitaria para {@link
     * Estudiante#Estudiante(String,int,double,int)}.
     */
    @Test public void testConstructor() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getNombre().equals(nombre));
        Assert.assertTrue(estudiante.getCuenta() == cuenta);
        Assert.assertTrue(estudiante.getPromedio() == promedio);
        Assert.assertTrue(estudiante.getEdad() == edad);
    }

    /**
     * Prueba unitaria para {@link Estudiante#setNombre} y {@link
     * Estudiante#getNombre}.
     */
    @Test public void testSetGetNombre() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getNombre().equals(nombre));
        String nuevoNombre = nombre + " Segundo";
        estudiante.setNombre(nuevoNombre);
        Assert.assertTrue(estudiante.getNombre().equals(nuevoNombre));
        Assert.assertFalse(estudiante.getNombre().equals(nombre));
    }

    /**
     * Prueba unitaria para {@link Estudiante#getNombreProperty}.
     */
    @Test public void testGetNombreProperty() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getNombreProperty().get().equals(nombre));
    }

    /**
     * Prueba unitaria para {@link Estudiante#setCuenta} y {@link
     * Estudiante#getCuenta}.
     */
    @Test public void testSetGetCuenta() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getCuenta() == cuenta);
        int nuevaCuenta = cuenta + 100;
        estudiante.setCuenta(nuevaCuenta);
        Assert.assertTrue(estudiante.getCuenta() == nuevaCuenta);
        Assert.assertFalse(estudiante.getCuenta() == cuenta);
    }

    /**
     * Prueba unitaria para {@link Estudiante#getCuentaProperty}.
     */
    @Test public void testGetCuentaProperty() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getCuentaProperty().get() == cuenta);
    }

    /**
     * Prueba unitaria para {@link Estudiante#setPromedio} y {@link
     * Estudiante#getPromedio}.
     */
    @Test public void testSetGetPromedio() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getPromedio() == promedio);
        double nuevoPromedio = promedio + 1.0;
        estudiante.setPromedio(nuevoPromedio);
        Assert.assertTrue(estudiante.getPromedio() == nuevoPromedio);
        Assert.assertFalse(estudiante.getPromedio() == promedio);
    }

    /**
     * Prueba unitaria para {@link Estudiante#getPromedioProperty}.
     */
    @Test public void testGetPromedioProperty() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getPromedioProperty().get() == promedio);
    }

    /**
     * Prueba unitaria para {@link Estudiante#setEdad} y {@link
     * Estudiante#getEdad}.
     */
    @Test public void testSetGetEdad() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getEdad() == edad);
        int nuevaEdad = edad + 50;
        estudiante.setEdad(nuevaEdad);
        Assert.assertTrue(estudiante.getEdad() == nuevaEdad);
        Assert.assertFalse(estudiante.getEdad() == edad);
    }

    /**
     * Prueba unitaria para {@link Estudiante#getEdadProperty}.
     */
    @Test public void testGetEdadProperty() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.getEdadProperty().get() == edad);
    }

    /**
     * Prueba unitaria para {@link Estudiante#equals}.
     */
    @Test public void testEquals() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        Estudiante igual = new Estudiante(new String(nombre),
                                          cuenta, promedio, edad);
        Assert.assertTrue(estudiante.equals(igual));
        String otroNombre = nombre + " Segundo";
        int otraCuenta = cuenta + 1;
        double otroPromedio = promedio / 10.0;
        int otraEdad = edad + 1;
        Estudiante distinto = new Estudiante(otroNombre, cuenta, promedio, edad);
        Assert.assertFalse(estudiante.equals(distinto));
        distinto = new Estudiante(nombre, otraCuenta, promedio, edad);
        Assert.assertFalse(estudiante.equals(distinto));
        distinto = new Estudiante(nombre, cuenta, otroPromedio, edad);
        Assert.assertFalse(estudiante.equals(distinto));
        distinto = new Estudiante(nombre, cuenta, promedio, otraEdad);
        Assert.assertFalse(estudiante.equals(distinto));
        distinto = new Estudiante(otroNombre, otraCuenta, otroPromedio, otraEdad);
        Assert.assertFalse(estudiante.equals(distinto));
        Assert.assertFalse(estudiante.equals("Una cadena"));
        Assert.assertFalse(estudiante.equals(null));
    }

    /**
     * Prueba unitaria para {@link Estudiante#toString}.
     */
    @Test public void testToString() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);
        String cadena = String.format("Nombre   : %s\n" +
                                      "Cuenta   : %d\n" +
                                      "Promedio : %2.2f\n" +
                                      "Edad     : %d",
                                      nombre, cuenta, promedio, edad);
        Assert.assertTrue(estudiante.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link Estudiante#guarda}.
     */
    @Test public void testGuarda() {
        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();
        estudiante = new Estudiante(nombre, cuenta, promedio, edad);

        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            estudiante.guarda(out);
            out.close();
            String guardado = swOut.toString();
            String s = String.format("%s\t%d\t%2.2f\t%d\n",
                                     nombre, cuenta, promedio, edad);
                Assert.assertTrue(guardado.equals(s));
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link Estudiante#carga}.
     */
    @Test public void testCarga() {
        estudiante = new Estudiante(null, 0, 0.0, 0);

        String nombre = nombreAleatorio();
        int cuenta = cuentaAleatoria();
        double promedio = promedioAleatorio();
        int edad = edadAleatoria();

        String entrada = String.format("%s\t%d\t%2.2f\t%d\n",
                                       nombre, cuenta, promedio, edad);

        try {
            StringReader srIn = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srIn);
            Assert.assertTrue(estudiante.carga(in));
            in.close();
            Assert.assertTrue(estudiante.getNombre().equals(nombre));
            Assert.assertTrue(estudiante.getCuenta() == cuenta);
            Assert.assertTrue(estudiante.getPromedio() == promedio);
            Assert.assertTrue(estudiante.getEdad() == edad);
        } catch (IOException ioe) {
            Assert.fail();
        }

        entrada = "";
        try {
            StringReader srIn = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srIn);
            Assert.assertFalse(estudiante.carga(in));
            in.close();
            Assert.assertTrue(estudiante.getNombre().equals(nombre));
            Assert.assertTrue(estudiante.getCuenta() == cuenta);
            Assert.assertTrue(estudiante.getPromedio() == promedio);
            Assert.assertTrue(estudiante.getEdad() == edad);
        } catch (IOException ioe) {
            Assert.fail();
        }

        entrada = "a\ta\ta\ta";
        try {
            StringReader srIn = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srIn);
            estudiante.carga(in);
            Assert.fail();
        } catch (IOException ioe) {}

        entrada = "a\ta";
        try {
            StringReader srIn = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srIn);
            estudiante.carga(in);
            Assert.fail();
        } catch (IOException ioe) {}
    }

    /* Inicializa el generador de números aleatorios. */
    static {
        random = new Random();
    }
}
