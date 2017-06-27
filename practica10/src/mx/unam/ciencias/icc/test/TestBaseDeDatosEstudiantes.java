package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link BaseDeDatosEstudiantes}.
 */
public class TestBaseDeDatosEstudiantes {

    private Random random;
    private BaseDeDatosEstudiantes bdd;
    private int total;
    private boolean llamado;
    private int contador;

    /**
     * Crea un generador de números aleatorios para cada prueba y una base de
     * datos de estudiantes.
     */
    public TestBaseDeDatosEstudiantes() {
        random = new Random();
        bdd = new BaseDeDatosEstudiantes();
        total = 1 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosEstudiantes#BaseDeDatosEstudiantes}.
     */
    @Test public void testConstructor() {
        Lista<Estudiante> estudiantes = bdd.getRegistros();
        Assert.assertFalse(estudiantes == null);
        Assert.assertTrue(estudiantes.getLongitud() == 0);
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getNumRegistros}.
     */
    @Test public void testGetNumRegistros() {
        Assert.assertTrue(bdd.getNumRegistros() == 0);
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getNumRegistros() == i+1);
        }
        Assert.assertTrue(bdd.getNumRegistros() == total);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getRegistros}.
     */
    @Test public void testGetRegistros() {
        Lista<Estudiante> l = bdd.getRegistros();
        Lista<Estudiante> r = bdd.getRegistros();
        Assert.assertTrue(l.equals(r));
        Assert.assertFalse(l == r);
        Estudiante[] estudiantes = new Estudiante[total];
        for (int i = 0; i < total; i++) {
            estudiantes[i] = TestEstudiante.estudianteAleatorio();
            bdd.agregaRegistro(estudiantes[i]);
        }
        l = bdd.getRegistros();
        int c = 0;
        for (Estudiante e : l)
            Assert.assertTrue(estudiantes[c++].equals(e));
        l.elimina(estudiantes[0]);
        Assert.assertFalse(l.equals(bdd.getRegistros()));
        Assert.assertFalse(l.getLongitud() == bdd.getNumRegistros());
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaRegistro}.
     */
    @Test public void testAgregaRegistro() {
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            Assert.assertFalse(bdd.getRegistros().contiene(e));
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.get(l.getLongitud() - 1).equals(e));
        }
        llamado = false;
        bdd.agregaEscucha((t, e) -> {
                Assert.assertTrue(t == EventoBaseDeDatos.REGISTRO_AGREGADO);
                Assert.assertTrue(e.equals(new Estudiante("A", 1, 1, 1)));
                llamado = true;
            });
        bdd.agregaRegistro(new Estudiante("A", 1, 1, 1));
        Assert.assertTrue(llamado);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaRegistro}.
     */
    @Test public void testEliminaRegistro() {
        int ini = random.nextInt(1000000);
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio(ini + i);
            bdd.agregaRegistro(e);
        }
        while (bdd.getNumRegistros() > 0) {
            int i = random.nextInt(bdd.getNumRegistros());
            Estudiante e = bdd.getRegistros().get(i);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            bdd.eliminaRegistro(e);
            Assert.assertFalse(bdd.getRegistros().contiene(e));
        }
        llamado = false;
        Estudiante estudiante = new Estudiante("A", 1, 1, 1);
        bdd.agregaRegistro(estudiante);
        bdd.agregaEscucha((t, e) -> {
                Assert.assertTrue(t == EventoBaseDeDatos.REGISTRO_ELIMINADO);
                Assert.assertTrue(e.equals(new Estudiante("A", 1, 1, 1)));
                llamado = true;
            });
        bdd.eliminaRegistro(estudiante);
        Assert.assertTrue(llamado);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#guarda}.
     */
    @Test public void testGuarda() {
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            bdd.agregaRegistro(e);
        }
        String guardado = "";
        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            bdd.guarda(out);
            out.close();
            guardado = swOut.toString();
        } catch (IOException ioe) {
            Assert.fail();
        }
        String[] lineas = guardado.split("\n");
        Assert.assertTrue(lineas.length == total);
        Lista<Estudiante> l = bdd.getRegistros();
        int c = 0;
        for (Estudiante e : l) {
            String el = String.format("%s\t%d\t%2.2f\t%d",
                                      e.getNombre(),
                                      e.getCuenta(),
                                      e.getPromedio(),
                                      e.getEdad());
            Assert.assertTrue(lineas[c++].equals(el));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#carga}.
     */
    @Test public void testCarga() {
        int ini = random.nextInt(1000000);
        String entrada = "";
        Estudiante[] estudiantes = new Estudiante[total];
        for (int i = 0; i < total; i++) {
            estudiantes[i] = TestEstudiante.estudianteAleatorio(ini + i);
            entrada += String.format("%s\t%d\t%2.2f\t%d\n",
                                     estudiantes[i].getNombre(),
                                     estudiantes[i].getCuenta(),
                                     estudiantes[i].getPromedio(),
                                     estudiantes[i].getEdad());
            bdd.agregaRegistro(estudiantes[i]);
        }
        contador = 0;
        llamado = false;
        bdd.agregaEscucha((t, e) -> {
                if (t == EventoBaseDeDatos.BASE_LIMPIADA)
                    llamado = true;
                if (t == EventoBaseDeDatos.REGISTRO_AGREGADO)
                    contador++;
            });
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Lista<Estudiante> l = bdd.getRegistros();
        Assert.assertTrue(l.getLongitud() == total);
        int c = 0;
        for (Estudiante e : l)
            Assert.assertTrue(estudiantes[c++].equals(e));
        Assert.assertTrue(llamado);
        Assert.assertTrue(contador == total);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosEstudiantes#creaRegistro}.
     */
    @Test public void testCreaRegistro() {
        Estudiante e = bdd.creaRegistro();
        Assert.assertTrue(e.getNombre() == null);
        Assert.assertTrue(e.getCuenta() == 0);
        Assert.assertTrue(e.getPromedio() == 0.0);
        Assert.assertTrue(e.getEdad() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosEstudiantes#buscaRegistros}.
     */
    @Test public void testBuscaRegistros() {
        int ini = 1000000 + random.nextInt(999999);
        for (int i = 0; i < total; i++) {
            Estudiante estudiante =
                TestEstudiante.estudianteAleatorio(ini + i);
            bdd.agregaRegistro(estudiante);
        }

        String busqueda = String.valueOf(ini).substring(0, 2);
        Lista<Estudiante> l = bdd.buscaRegistros("cuenta", busqueda);
        Assert.assertTrue(l.getLongitud() == total);
        for (Estudiante e : l) {
            String c = String.valueOf(e.getCuenta());
            Assert.assertTrue(c.indexOf(busqueda) != -1);
        }
        busqueda = String.valueOf(9999999);
        l = bdd.buscaRegistros("cuenta", busqueda);
        Assert.assertTrue(l.getLongitud() == 0);

        busqueda = String.valueOf(1);
        l = bdd.buscaRegistros("cuenta", busqueda);
        Assert.assertTrue(l.getLongitud() == total);
        for (Estudiante e : l) {
            String c = String.valueOf(e.getCuenta());
            Assert.assertTrue(c.indexOf(busqueda) != -1);
        }
        busqueda = String.valueOf(9999999);
        l = bdd.buscaRegistros("cuenta", busqueda);
        Assert.assertTrue(l.getLongitud() == 0);

        try {
            l = bdd.buscaRegistros("no-existe", "");
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#limpia}.
     */
    @Test public void testLimpia() {
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            bdd.agregaRegistro(e);
        }
        llamado = false;
        bdd.agregaEscucha((t, e) -> {
                Assert.assertTrue(t == EventoBaseDeDatos.BASE_LIMPIADA);
                Assert.assertTrue(e == null);
                llamado = true;
            });
        bdd.limpia();
        Assert.assertTrue(llamado);
    }
}
