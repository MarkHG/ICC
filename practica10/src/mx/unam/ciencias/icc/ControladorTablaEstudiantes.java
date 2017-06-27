package mx.unam.ciencias.icc;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;

/**
 * Clase para el controlador de la tabla para mostrar la base de datos.
 */
public class ControladorTablaEstudiantes {

    /* La tabla. */
    @FXML private TableView<Estudiante> tabla;

    /* La columna del nombre. */
    @FXML private TableColumn<Estudiante, String> nombre;
    /* La columna del número de cuenta. */
    @FXML private TableColumn<Estudiante, Number> cuenta;
    /* La columna del promedio. */
    @FXML private TableColumn<Estudiante, Number> promedio;
    /* La columna de la edad. */
    @FXML private TableColumn<Estudiante, Number> edad;

    /* El modelo de la selección. */
    TableView.TableViewSelectionModel<Estudiante> modeloSeleccion;
    /* La selección. */
    private ObservableList<TablePosition> seleccion;
    /* Lista de escuchas de selección. */
    private Lista<EscuchaSeleccion> escuchas;
    /* Los renglones en la tabla. */
    private ObservableList<Estudiante> renglones;

    /* Inicializa el controlador. */
    @FXML private void initialize() {
        tabla.setFocusTraversable(false);
        renglones = tabla.getItems();
        modeloSeleccion = tabla.getSelectionModel();
        modeloSeleccion.setSelectionMode(SelectionMode.MULTIPLE);
        seleccion = modeloSeleccion.getSelectedCells();
        seleccion.addListener(new ListChangeListener<TablePosition>() {
                public void onChanged(Change c) {
                    cambioEnSeleccion();
                }
            });
        nombre.setCellValueFactory(c -> c.getValue().getNombreProperty());
        cuenta.setCellValueFactory(c -> c.getValue().getCuentaProperty());
        promedio.setCellValueFactory(c -> c.getValue().getPromedioProperty());
        edad.setCellValueFactory(c -> c.getValue().getEdadProperty());
        escuchas = new Lista<EscuchaSeleccion>();
    }

    /* Notifica a los escuchas que hubo un cambio en la selección. */
    private void cambioEnSeleccion() {
        for (EscuchaSeleccion escucha : escuchas)
            escucha.renglonesSeleccionados(seleccion.size());
    }

    /**
     * Limpia la tabla.
     */
    public void limpiaTabla() {
        renglones.clear();
    }

    /**
     * Agrega un renglón a la tabla.
     * @param estudiante el renglón a agregar.
     */
    public void agregaRenglon(Estudiante estudiante) {
        renglones.add(estudiante);
    }

    /**
     * Elimina un renglón de la tabla.
     * @param estudiante el renglón a eliminar.
     */
    public void eliminaRenglon(Estudiante estudiante) {
        renglones.remove(estudiante);
    }

    /**
     * Selecciona renglones de la tabla.
     * @param estudiantes los renglones a seleccionar.
     */
    public void seleccionaRenglones(Lista<Estudiante> estudiantes) {
        modeloSeleccion.clearSelection();
        for (Estudiante estudiante : estudiantes)
            modeloSeleccion.select(estudiante);
    }

    /**
     * Regresa una lista con los registros seleccionados de la tabla.
     * @return una lista con los registros seleccionados de la tabla.
     */
    public Lista<Estudiante> getRenglonesSeleccionados() {
        Lista<Estudiante> seleccionados = new Lista<Estudiante>();
        for (TablePosition tp : seleccion) {
            int r = tp.getRow();
            seleccionados.agregaFinal(renglones.get(r));
        }
        return seleccionados;
    }

    /**
     * Regresa el estudiante seleccionado en la tabla.
     * @return el estudiante seleccionado en la tabla.
     */
    public Estudiante getRenglonSeleccionado() {
        int r = seleccion.get(0).getRow();
        return renglones.get(r);
    }

    /**
     * Agrega un escucha de selección.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscuchaSeleccion(EscuchaSeleccion escucha) {
        escuchas.agregaFinal(escucha);
    }
}
