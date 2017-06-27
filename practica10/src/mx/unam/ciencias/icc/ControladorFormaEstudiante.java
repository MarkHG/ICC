package mx.unam.ciencias.icc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Clase para el controlador del contenido del diálogo para editar y crear
 * estudiantes.
 */
public class ControladorFormaEstudiante extends ControladorForma {

    /* El campo de texto para el nombre. */
    @FXML private TextField nombre;
    /* El campo de texto para el número de cuenta. */
    @FXML private TextField cuenta;
    /* El campo de texto para el promedio. */
    @FXML private TextField promedio;
    /* El campo de texto para la edad. */
    @FXML private TextField edad;
    /* El botón para aceptar. */
    @FXML private Button botonAceptar;

    /* El estudiante creado o editado. */
    private Estudiante estudiante;

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {
        if (estudianteValido()) {
            aceptado = true;
            escenario.close();
        }
    }

    /**
     * Define el estudiante del diálogo.
     * @param estudiante el nuevo estudiante del diálogo.
     */
    public void setEstudiante(Estudiante estudiante) {
        if (estudiante == null)
            return;
        this.estudiante = new Estudiante(estudiante.getNombre(),
                                         estudiante.getCuenta(),
                                         estudiante.getPromedio(),
                                         estudiante.getEdad());
        nombre.setText(estudiante.getNombre());
        cuenta.setText(String.format("%09d", estudiante.getCuenta()));
        promedio.setText(String.format("%2.2f", estudiante.getPromedio()));
        edad.setText(String.valueOf(estudiante.getEdad()));
    }

    /* Determina si los cuatro campos son válidos. */
    private boolean estudianteValido() {
        String error = "";
        String n = nombre.getText();
        error += (n == null || n.length() == 0) ? "Nombre inválido.\n" : "";
        int c = -1;
        String s = cuenta.getText();
        if (s == null || s.length() == 0) {
            error += "Número de cuenta inválido.\n";
        } else {
            try {
                c = Integer.parseInt(s);
            } catch (NumberFormatException nfe) {
                error += "Número de cuenta inválido.\n";
            }
        }
        double p = -1.0;
        s = promedio.getText();
        if (s == null || s.length() == 0) {
            error += "Promedio inválido.\n";
        } else {
            try {
                p = Double.parseDouble(s);
            } catch (NumberFormatException nfe) {
                error += "Promedio inválido.\n";
            }
        }
        int e = -1;
        s = edad.getText();
        if (s == null || s.length() == 0) {
            error += "Edad inválida.\n";
        } else {
            try {
                e = Integer.parseInt(s);
            } catch (NumberFormatException nfe) {
                error += "Edad inválida.\n";
            }
        }
        if (error.equals("")) {
            if (estudiante != null) {
                estudiante.setNombre(n);
                estudiante.setCuenta(c);
                estudiante.setPromedio(p);
                estudiante.setEdad(e);
            } else {
                estudiante = new Estudiante(n, c, p, e);
            }
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Valores inválidos");
            alert.setHeaderText("Por favor ingrese valores válidos");
            alert.setContentText(error);
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Regresa el estudiante del diálogo.
     * @return el estudiante del diálogo.
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }

    /**
     * Define el verbo del botón de aceptar.
     * @param verbo el nuevo verbo del botón de aceptar.
     */
    public void setVerbo(String verbo) {
        botonAceptar.setText(verbo);
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        nombre.requestFocus();
    }
}
