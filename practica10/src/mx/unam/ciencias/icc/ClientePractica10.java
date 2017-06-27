package mx.unam.ciencias.icc;

import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * ClientePractica10: Parte del cliente para práctica 10: Hilos de ejecución y
 * enchufes.
 */
public class ClientePractica10 extends Application {

    /* Vista de la interfaz principal. */
    private static final String INTERFAZ_PRINCIPAL_FXML =
        "fxml/interfaz-principal.fxml";
    /* Vista de la tabla de estudiantes. */
    private static final String TABLA_ESTUDIANTES_FXML =
        "fxml/tabla-estudiantes.fxml";
    /* Ícono de la Facultad de Ciencias. */
    private static final String ICONO_CIENCIAS =
        "icons/ciencias.png";

    /**
     * Inicia la aplicación.
     * @param escenario la ventana principal de la aplicación.
     * @throws Exception si algo sale mal. Literalmente así está definido.
     */
    @Override public void start(Stage escenario) throws Exception {
        ClassLoader cl = getClass().getClassLoader();
        String url = cl.getResource(ICONO_CIENCIAS).toString();
        escenario.getIcons().add(new Image(url));
        escenario.setTitle("Administrador de Estudiantes");

        FXMLLoader cargador = new FXMLLoader(cl.getResource(TABLA_ESTUDIANTES_FXML));
        GridPane tablaEstudiante = (GridPane)cargador.load();
        ControladorTablaEstudiantes controlTE = cargador.getController();

        cargador = new FXMLLoader(cl.getResource(INTERFAZ_PRINCIPAL_FXML));
        BorderPane interfazPrincipal = (BorderPane)cargador.load();
        interfazPrincipal.setCenter(tablaEstudiante);
        ControladorInterfazPrincipal controlIP = cargador.getController();
        controlIP.setEscenario(escenario);
        controlIP.setControladorTablaEstudiantes(controlTE);

        Scene escena = new Scene(interfazPrincipal);
        escenario.setScene(escena);
        escenario.setOnCloseRequest(e -> controlIP.salir(null));
        escenario.show();
    }

    public static void main(String[] args) {
        /* Español de México para los elementos de la interfaz gráfica
         * predefinidos por JavaFX. */
        Locale.setDefault(new Locale("es", "MX"));
        launch(args);
    }
}
