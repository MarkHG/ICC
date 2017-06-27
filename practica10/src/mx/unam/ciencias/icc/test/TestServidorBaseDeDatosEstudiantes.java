package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.Bitacora;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.HiloServidor;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Protocolo;
import mx.unam.ciencias.icc.ServidorBaseDeDatos;
import mx.unam.ciencias.icc.ServidorBaseDeDatosEstudiantes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Clase para pruebas unitarias de la clase {@link ServidorBaseDeDatosEstudiantes}.
 */
public class TestServidorBaseDeDatosEstudiantes {

    private Random random;
    private ServidorBaseDeDatosEstudiantes sbdd;
    private Estudiante[] estudiantes;
    private int total;
    private boolean llamado;
    private int contador;
    private String archivo;
    private int puerto;

    /**
     * Crea un generador de números aleatorios para cada prueba y una base de
     * datos de estudiantes.
     */
    public TestServidorBaseDeDatosEstudiantes() {
        random = new Random();
        total = 10 + random.nextInt(100);
        puerto = 1024 + random.nextInt(64000);
        Bitacora.getInstancia().setActivada(false);
    }

    /* Método auxiliar para poder disparar el servidor con una lambda. */
    private void sirveServidor(Runnable r) {
        new Thread(r).start();
    }

    /**
     * Método que se ejecuta antes de cada prueba unitaria; crea el archivo de
     * la base de datos.
     */
    @Before public void crea() {
        try {
            estudiantes = new Estudiante[total];
            BaseDeDatosEstudiantes bdd = new BaseDeDatosEstudiantes();
            for (int i = 0; i < total; i++) {
                estudiantes[i] = TestEstudiante.estudianteAleatorio();
                bdd.agregaRegistro(estudiantes[i]);
            }
            File f = null;
            String s = String.format("test-base-de-datos-%04d.db",
                                     random.nextInt(9999));
            TemporaryFolder tf = new TemporaryFolder();
            f = tf.newFile(s);

            archivo = f.getAbsolutePath();

            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(archivo)));
            bdd.guarda(out);
            out.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Método que se ejecuta despué de cada prueba unitaria; elimina el archivo
     * de la base de datos.
     */
    @After public void elimina() {
        File f = new File(archivo);
        f.delete();
    }

    /**
     * Prueba unitaria para {@link
     * ServidorBaseDeDatosEstudiantes#ServidorBaseDeDatosEstudiantes}.
     */
    @Test public void testConstructor() {
        try {
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == 0);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatos#carga}.
     */
    @Test public void testCarga() {
        try {
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == 0);
            sbdd.carga();
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == total);
            Lista<Estudiante> l = sbdd.getRegistros();
            for (Estudiante e : estudiantes)
                Assert.assertTrue(l.contiene(e));
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatos#guarda}.
     */
    @Test public void testGuarda() {
        try {
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == 0);
            sbdd.carga();
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == total);

            Lista<Estudiante> l = sbdd.getRegistros();
            for (Estudiante e : estudiantes)
                Assert.assertTrue(l.contiene(e));
            sirveServidor(() -> sbdd.sirve());
            Thread.sleep(100);
            Assert.assertFalse(sbdd.isTerminado());

            Socket enchufe = new Socket("localhost", puerto);
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                        enchufe.getOutputStream()));

            Estudiante e = new Estudiante("Fulano" + random.nextInt(10),
                                          1000 + random.nextInt(1000),
                                          (50 + random.nextInt(50)) / 10.0,
                                          18 + random.nextInt(15));

            out.write(Protocolo.getComando(Protocolo.REGISTRO_AGREGADO));
            out.newLine();
            e.guarda(out);
            out.flush();
            Thread.sleep(100);

            BaseDeDatosEstudiantes bdd = new BaseDeDatosEstudiantes();
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(archivo)));
            bdd.carga(in);
            in.close();

            Lista<Estudiante> l1 = bdd.getRegistros();
            Lista<Estudiante> l2 = sbdd.getRegistros();
            Assert.assertTrue(l1.getLongitud() == l2.getLongitud());
            Assert.assertTrue(l1.equals(l2));
            Iterator<Estudiante> i1 = l1.iterator();
            Iterator<Estudiante> i2 = l2.iterator();
            while (i1.hasNext() && i2.hasNext())
                Assert.assertFalse(i1.next() == i2.next());
        } catch (IOException | InterruptedException e) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirve() {
        try {
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == 0);
            sbdd.carga();
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == total);

            Lista<Estudiante> l = sbdd.getRegistros();
            for (Estudiante e : estudiantes)
                Assert.assertTrue(l.contiene(e));
            sirveServidor(() -> sbdd.sirve());
            Thread.sleep(100);
            Assert.assertFalse(sbdd.isTerminado());

            Socket enchufe = new Socket("localhost", puerto);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(
                        enchufe.getInputStream()));
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                        enchufe.getOutputStream()));

            BaseDeDatosEstudiantes bdd = new BaseDeDatosEstudiantes();
            out.write(Protocolo.getComando(Protocolo.ENVIAR_BASE_DE_DATOS));
            out.newLine();
            out.flush();
            Thread.sleep(100);
            bdd.carga(in);

            l = bdd.getRegistros();
            for (Estudiante e : estudiantes)
                l.contiene(e);

            Estudiante e = new Estudiante("Fulano" + random.nextInt(10),
                                          1000 + random.nextInt(1000),
                                          (50 + random.nextInt(50)) / 10.0,
                                          18 + random.nextInt(15));

            out.write(Protocolo.getComando(Protocolo.REGISTRO_AGREGADO));
            out.newLine();
            e.guarda(out);
            out.flush();
            Thread.sleep(100);
            Assert.assertTrue(sbdd.getRegistros().contiene(e));

            Estudiante f = new Estudiante(e.getNombre() + "Z", e.getCuenta(),
                                          e.getPromedio(), e.getEdad());

            out.write(Protocolo.getComando(Protocolo.REGISTRO_MODIFICADO));
            out.newLine();
            e.guarda(out);
            f.guarda(out);
            out.flush();
            Thread.sleep(100);
            Assert.assertFalse(sbdd.getRegistros().contiene(e));
            Assert.assertTrue(sbdd.getRegistros().contiene(f));

            out.write(Protocolo.getComando(Protocolo.REGISTRO_ELIMINADO));
            out.newLine();
            f.guarda(out);
            out.flush();
            Thread.sleep(100);
            Assert.assertFalse(sbdd.getRegistros().contiene(f));

            out.write(Protocolo.getComando(Protocolo.DETENER_SERVICIO));
            out.newLine();
            out.flush();
            Thread.sleep(100);
            Assert.assertTrue(sbdd.isTerminado());
        } catch (IOException | InterruptedException e) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatos#clienteDesconectado}.
     */
    @Test public void testClienteDesconectado() {
        try {
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == 0);
            sbdd.carga();
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == total);

            Lista<Estudiante> l = sbdd.getRegistros();
            for (Estudiante e : estudiantes)
                Assert.assertTrue(l.contiene(e));
            sirveServidor(() -> sbdd.sirve());
            Thread.sleep(100);
            Assert.assertFalse(sbdd.isTerminado());

            Assert.assertTrue(sbdd.getHilos().getLongitud() == 0);
            Socket enchufe = new Socket("localhost", puerto);
            Thread.sleep(100);
            Lista<HiloServidor<Estudiante>> hilos = sbdd.getHilos();
            Assert.assertTrue(hilos.getLongitud() == 1);
            sbdd.clienteDesconectado(hilos.getPrimero());
            Assert.assertTrue(sbdd.getHilos().getLongitud() == 0);
        } catch (IOException | InterruptedException e) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatos#termina}.
     */
    @Test public void testTermina() {
        try {
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == 0);
            sbdd.carga();
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == total);

            Assert.assertFalse(sbdd.isTerminado());
            sbdd.termina();
            Thread.sleep(100);
            Assert.assertTrue(sbdd.isTerminado());
        } catch (IOException | InterruptedException e) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatos#getHilos}.
     */
    @Test public void testGetHilos() {
        try {
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == 0);
            sbdd.carga();
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == total);

            Lista<Estudiante> l = sbdd.getRegistros();
            for (Estudiante e : estudiantes)
                Assert.assertTrue(l.contiene(e));
            sirveServidor(() -> sbdd.sirve());
            Thread.sleep(100);
            Assert.assertFalse(sbdd.isTerminado());

            Assert.assertTrue(sbdd.getHilos().getLongitud() == 0);
            Socket enchufe = new Socket("localhost", puerto);
            Thread.sleep(100);
            Assert.assertTrue(sbdd.getHilos().getLongitud() == 1);
        } catch (IOException | InterruptedException e) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatos#getRegistros}.
     */
    @Test public void testGetRegistros() {
        try {
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == 0);
            sbdd.carga();
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == total);

            Lista<Estudiante> l = sbdd.getRegistros();
            for (Estudiante e : estudiantes)
                Assert.assertTrue(l.contiene(e));
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatos#isTerminado}.
     */
    @Test public void testIsTerminado() {
        try {
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == 0);
            sbdd.carga();
            Assert.assertTrue(sbdd.getRegistros().getLongitud() == total);
            Assert.assertFalse(sbdd.isTerminado());
            sbdd.termina();
            Thread.sleep(100);
            Assert.assertTrue(sbdd.isTerminado());
        } catch (IOException | InterruptedException e) {
            Assert.fail();
        }
    }
}
