package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase para el controlador de la ventana principal de la aplicación.
 */
public class ControladorInterfazPrincipal {

    /* Vista de la forma para conectarse. */
    private static final String CONECTAR_FXML =
        "fxml/forma-conectar.fxml";
    /* Vista de la forma para realizar búsquedas de estudiantes. */
    private static final String BUSQUEDA_ESTUDIANTES_FXML =
        "fxml/forma-busqueda-estudiantes.fxml";
    /* Vista de la forma para agregar/editar estudiantes. */
    private static final String ESTUDIANTE_FXML =
        "fxml/forma-estudiante.fxml";

    /* Opción de menu para conectar. */
    @FXML private MenuItem menuConectar;
    /* Opción de menu para desconectar. */
    @FXML private MenuItem menuDesconectar;
    /* Opción de menu para agregar. */
    @FXML private MenuItem menuAgregar;
    /* Opción de menu para editar. */
    @FXML private MenuItem menuEditar;
    /* Opción de menu para eliminar. */
    @FXML private MenuItem menuEliminar;
    /* Opción de menu para buscar. */
    @FXML private MenuItem menuBuscar;
    /* El botón de agregar. */
    @FXML private Button botonAgregar;
    /* El botón de editar. */
    @FXML private Button botonEditar;
    /* El botón de eliminar. */
    @FXML private Button botonEliminar;
    /* El botón de buscar. */
    @FXML private Button botonBuscar;

    /* La ventana. */
    private Stage escenario;
    /* El controlador de tabla. */
    private ControladorTablaEstudiantes controlTE;
    /* La base de datos. */
    private BaseDeDatosEstudiantes bdd;
    /* El hilo de ejecución del cliente. */
    private HiloCliente<Estudiante> hilo;
    /* Si hay o no conexión. */
    private boolean conectado;
    /* Número de estudiantes seleccionados. */
    private int seleccionados;

    /* Inicializa el controlador. */
    @FXML private void initialize() {
        actualizaInterfaz(0);
        setBaseDeDatos(new BaseDeDatosEstudiantes());
        setConectado(false);
    }

    /* Conecta el cliente con el servidor. */
    @FXML private void conectar(ActionEvent evento) {
        if (conectado)
            return;

        String servidor = null;
        int puerto = -1;

        try {
            FXMLLoader cargador = new FXMLLoader();
            ClassLoader cl = getClass().getClassLoader();
            cargador.setLocation(cl.getResource(CONECTAR_FXML));
            AnchorPane pagina = (AnchorPane)cargador.load();

            Stage escenario = new Stage();
            escenario.initOwner(this.escenario);
            escenario.initModality(Modality.WINDOW_MODAL);
            escenario.setTitle("Conectar a servidor");
            Scene escena = new Scene(pagina);
            escenario.setScene(escena);

            ControladorFormaConectar controlador = cargador.getController();
            controlador.setEscenario(escenario);

            escenario.setOnShown(w -> controlador.defineFoco());
            escenario.setResizable(false);
            escenario.showAndWait();
            if (!controlador.isAceptado())
                return;

            servidor = controlador.getServidor();
            puerto = controlador.getPuerto();
        } catch (IOException | IllegalStateException e) {
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "cargar el diálogo '%s'.", CONECTAR_FXML);
            dialogoError("Error al cargar interfaz", mensaje);
            return;
        }

        try {
            Socket enchufe = new Socket(servidor, puerto);
            setBaseDeDatos(new BaseDeDatosEstudiantes());
            hilo = new HiloCliente<Estudiante>(bdd, enchufe);
            hilo.agregaEscucha(e -> notificaErrorHiloCliente(e));
            hilo.start();
        } catch (IOException ioe) {
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "conectarnos a %s:%d.\n", servidor, puerto);
            dialogoError("Error al establecer conexión", mensaje);
            return;
        }
        setConectado(true);
    }

    /* Desconecta el cliente del servidor. */
    @FXML private void desconectar(ActionEvent evento) {
        if (!conectado)
            return;
        setConectado(false);
        hilo.desconecta();
        hilo = null;
    }

    /**
     * Termina el programa.
     * @param evento el evento que generó la acción.
     */
    @FXML public void salir(ActionEvent evento) {
        desconectar(evento);
        Platform.exit();
    }

    /* Agrega un nuevo estudiante. */
    @FXML private void agregaEstudiante(ActionEvent evento) {
        ControladorFormaEstudiante controlador =
            construyeDialogoEstudiante("Agregar estudiante", null);
        controlador.setVerbo("Agregar");
        if (controlador == null)
            return;
        controlador.getEscenario().showAndWait();
        if (!controlador.isAceptado())
            return;
        bdd.agregaRegistro(controlador.getEstudiante());
    }

    /* Edita un estudiante. */
    @FXML private void editaEstudiante(ActionEvent evento) {
        Estudiante estudiante = controlTE.getRenglonSeleccionado();
        ControladorFormaEstudiante controlador =
            construyeDialogoEstudiante("Editar estudiante", estudiante);
        controlador.setVerbo("Actualizar");
        if (controlador == null)
            return;
        controlador.getEscenario().showAndWait();
        if (!controlador.isAceptado())
            return;
        Estudiante nuevo = controlador.getEstudiante();
        bdd.eliminaRegistro(estudiante);
        bdd.agregaRegistro(nuevo);
    }

    /* Elimina uno o varios estudiantes. */
    @FXML private void eliminaEstudiantes(ActionEvent evento) {
        String articulo = seleccionados > 1 ? "los" : "al";
        String sujeto = seleccionados > 1 ? "estudiantes" : "estudiante";
        String titulo = String.format("Eliminar %s", sujeto);
        String calificativo = seleccionados > 1 ? "seleccionados" : "seleccionado";
        String mensaje = String.format("Esto eliminará %s %s %s.",
                                       articulo, sujeto, calificativo);
        if (!dialogoDeConfirmacion(titulo, mensaje, "¿Está seguro?",
                                   "Eliminar " + sujeto,
                                   "Conservar " + sujeto))
            return;
        Lista<Estudiante> seleccionados = controlTE.getRenglonesSeleccionados();
        for (Estudiante estudiante : seleccionados)
            bdd.eliminaRegistro(estudiante);
    }

    /* Busca estudiantes. */
    @FXML private void buscaEstudiantes(ActionEvent evento) {
        try {
            ClassLoader cl = getClass().getClassLoader();
            FXMLLoader cargador =
                new FXMLLoader(cl.getResource(BUSQUEDA_ESTUDIANTES_FXML));
            AnchorPane pagina = (AnchorPane)cargador.load();

            Stage escenario = new Stage();
            escenario.setTitle("Buscar estudiantes");
            escenario.initOwner(this.escenario);
            escenario.initModality(Modality.WINDOW_MODAL);
            Scene escena = new Scene(pagina);
            escenario.setScene(escena);

            ControladorFormaBusquedaEstudiantes controlador;
            controlador = cargador.getController();
            controlador.setEscenario(escenario);

            escenario.setOnShown(w -> controlador.defineFoco());
            escenario.setResizable(false);
            escenario.showAndWait();
            if (!controlador.isAceptado())
                return;

            Lista<Estudiante> resultados =
                bdd.buscaRegistros(controlador.getCampo(),
                                   controlador.getValor());

            controlTE.seleccionaRenglones(resultados);
        } catch (IOException | IllegalStateException e) {
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "cargar el diálogo '%s'.",
                              BUSQUEDA_ESTUDIANTES_FXML);
            dialogoError("Error al cargar interfaz", mensaje);
        }
    }

    /* Muestra un diálogo con información del programa. */
    @FXML private void acercaDe(ActionEvent evento) {
        Alert dialogo = new Alert(AlertType.INFORMATION);
        dialogo.initOwner(escenario);
        dialogo.initModality(Modality.WINDOW_MODAL);
        dialogo.setTitle("Acerca de Administrador de Estudiantes.");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Aplicación para administrar estudiantes.\n"  +
                             "Copyright © 2015 Facultad de Ciencias, UNAM.");
        dialogo.showAndWait();
    }

    /**
     * Define el controlador de tabla.
     * @param controlTE el controlador de tabla.
     */
    public void
    setControladorTablaEstudiantes(ControladorTablaEstudiantes controlTE) {
        this.controlTE = controlTE;
        controlTE.agregaEscuchaSeleccion(n -> actualizaInterfaz(n));
    }

    /**
     * Define el escenario.
     * @param escenario el escenario.
     */
    public void setEscenario(Stage escenario) {
        this.escenario = escenario;
    }

    /* Actualiza la interfaz con una nueva base de datos. */
    private void setBaseDeDatos(BaseDeDatosEstudiantes bdd) {
        if (this.bdd != null)
            this.bdd.limpia();
        this.bdd = bdd;
        for (Estudiante e : bdd.getRegistros())
            controlTE.agregaRenglon(e);
        bdd.agregaEscucha((e, r) -> manejaEventoBaseDeDatos(e, r));
    }

    /* Maneja un evento de cambio en la base de datos. */
    private void manejaEventoBaseDeDatos(EventoBaseDeDatos evento,
                                         Estudiante estudiante) {
        String s = evento != null ? evento.toString() : "";

        if (conectado)
            hilo.avisaServidor(evento, estudiante);

        switch (evento) {
        case BASE_LIMPIADA:
            controlTE.limpiaTabla();
            break;
        case REGISTRO_AGREGADO:
            controlTE.agregaRenglon(estudiante);
            break;
        case REGISTRO_ELIMINADO:
            controlTE.eliminaRenglon(estudiante);
            break;
        }
    }

    /* Actualiza la interfaz dependiendo del número de renglones
     * seleccionados. */
    private void actualizaInterfaz(int seleccionados) {
        this.seleccionados = seleccionados;
        menuEliminar.setDisable(seleccionados == 0);
        menuEditar.setDisable(seleccionados != 1);
        botonEliminar.setDisable(seleccionados == 0);
        botonEditar.setDisable(seleccionados != 1);
    }

    /* Crea un diálogo con una pregunta que hay que confirmar. */
    private boolean dialogoDeConfirmacion(String titulo,
                                          String mensaje, String pregunta,
                                          String aceptar, String cancelar) {
        Alert dialogo = new Alert(AlertType.CONFIRMATION);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(mensaje);
        dialogo.setContentText(pregunta);

        ButtonType si = new ButtonType(aceptar);
        ButtonType no = new ButtonType(cancelar, ButtonData.CANCEL_CLOSE);
        dialogo.getButtonTypes().setAll(si, no);

        Optional<ButtonType> resultado = dialogo.showAndWait();
        return resultado.get() == si;
    }

    /* Construye un diálogo para crear o editar un estudiante. */
    private ControladorFormaEstudiante
    construyeDialogoEstudiante(String titulo,
                               Estudiante estudiante) {
        try {
            ClassLoader cl = getClass().getClassLoader();
            FXMLLoader cargador =
                new FXMLLoader(cl.getResource(ESTUDIANTE_FXML));
            AnchorPane pagina = (AnchorPane)cargador.load();

            Stage escenario = new Stage();
            escenario.setTitle(titulo);
            escenario.initOwner(this.escenario);
            escenario.initModality(Modality.WINDOW_MODAL);
            Scene escena = new Scene(pagina);
            escenario.setScene(escena);

            ControladorFormaEstudiante controlador = cargador.getController();
            controlador.setEscenario(escenario);
            controlador.setEstudiante(estudiante);

            escenario.setOnShown(w -> controlador.defineFoco());
            escenario.setResizable(false);
            return controlador;
        } catch (IOException | IllegalStateException e) {
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "cargar el diálogo '%s'.",
                              ESTUDIANTE_FXML);
            dialogoError("Error al cargar interfaz", mensaje);
            return null;
        }
    }

    /* Notificamos al usuario de un error de conexión. */
    private void notificaErrorHiloCliente(ErrorHiloCliente error) {
        String detalle = "";
        switch (error) {
        case ERROR_AL_CARGAR:
            detalle = "Error al recibir la base de datos";
            break;
        case ERROR_SERVIDOR:
            detalle = "El servidor nos desconectó";
            break;
        case ERROR_AL_RECIBIR:
            detalle = "Error al recibir información";
            break;
        case ERROR_AL_ENVIAR:
            detalle = "Error al enviar información";
            break;
        default: detalle = "Error desconocido";
        }
        String mensaje =
            String.format("%s.\nLa conexión ha terminado.", detalle);
        /* Esto viene del hilo. */
        Platform.runLater(() -> {
                dialogoError("Error de conexión", mensaje);
                desconectar(null);
            });
    }

    /* Muestra un diálogo de error. */
    private void dialogoError(String titulo, String mensaje) {
        Alert dialogo = new Alert(AlertType.ERROR);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(null);
        dialogo.setContentText(mensaje);
        dialogo.showAndWait();
    }

    /* Cambia la interfaz gráfica dependiendo de hay o no conexión. */
    public void setConectado(boolean conectado) {
        this.conectado = conectado;
        menuConectar.setDisable(conectado);
        menuDesconectar.setDisable(!conectado);
        menuAgregar.setDisable(!conectado);
        menuBuscar.setDisable(!conectado);
        botonAgregar.setDisable(!conectado);
        botonBuscar.setDisable(!conectado);
    }
}
