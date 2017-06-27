package mx.unam.ciencias.icc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Clase para el controlador del contenido del diálogo para buscar estudiantes.
 */
public class ControladorFormaBusquedaEstudiantes extends ControladorForma {

    /* El combo del campo. */
    @FXML private ComboBox campo;
    /* El campo de texto para el valor. */
    @FXML private TextField valor;

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {
        aceptado = valor.getText().length() > 0;
        escenario.close();
    }

    /**
     * Regresa el campo seleccionado.
     * @return el campo seleccionado.
     */
    public String getCampo() {
        switch (campo.getValue().toString().trim()) {
        case "Nombre":   return "nombre";
        case "# Cuenta": return "cuenta";
        case "Promedio": return "promedio";
        case "Edad":     return "edad";
        default:         return "";
        }
    }

    /**
     * Regresa el valor ingresado.
     * @return el valor ingresado.
     */
    public String getValor() {
        return valor.getText();
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        valor.requestFocus();
    }
}
