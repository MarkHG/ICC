package mx.unam.ciencias.icc;

import java.io.IOException;

/**
 * ServidorPractica10: Parte del servidor para la práctica 10: Hilos de
 * ejecución y enchufes.
 */
public class ServidorPractica10 {

    /* Imprime un mensaje de cómo usar el programa. */
    private static void uso() {
        System.out.println("Uso: java -jar servidor-practica10.jar puerto [archivo]");
        System.exit(0);
    }

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2)
            uso();

        int puerto = 1234;
        try {
            puerto = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            uso();
        }

        String archivo = (args.length == 2) ? archivo = args[1] : null;

        try {
            ServidorBaseDeDatosEstudiantes servidor;
            servidor = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            servidor.carga();
            servidor.sirve();
        } catch (IOException ioe) {
            System.err.println("Error al crear el servidor.");
        }
    }
}
