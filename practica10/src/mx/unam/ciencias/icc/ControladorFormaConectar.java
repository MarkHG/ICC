package mx.unam.ciencias.icc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Clase para el controlador del diálogo para conectar al servidor.
 */
public class ControladorFormaConectar extends ControladorForma {

    /* El campo de texto para el servidor. */
    @FXML private TextField servidor;
    /* El campo de texto para el puerto. */
    @FXML private TextField puerto;

    /* El servidor. */
    private String servidorValor;
    /* El puerto. */
    private int puertoValor;

    /* Manejador para cuando se activa el botón conectar. */
    @FXML private void conectar(ActionEvent evento) {
        if (conexionValida()) {
            aceptado = true;
            escenario.close();
        }
    }

    /* Determina si los campos son válidos. */
    private boolean conexionValida() {
        String error = "";
        String s = servidor.getText();
        error += (s == null || s.length() == 0) ? "Servidor inválido.\n" : "";
        int pv = -1;
        String p = puerto.getText();
        if (p == null || p.length() == 0) {
            error += "Puerto inválido.\n";
        } else {
            try {
                pv = Integer.parseInt(p);
            } catch (NumberFormatException nfe) {
                error += "Puerto inválido.\n";
            }
        }
        if (error.equals("")) {
            servidorValor = s;
            puertoValor = pv;
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
     * Regresa el servidor del diálogo.
     * @return el servidor del diálogo.
     */
    public String getServidor() {
        return servidorValor;
    }

    /**
     * Regresa el puerto del diálogo.
     * @return el puerto del diálogo.
     */
    public int getPuerto() {
        return puertoValor;
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        servidor.requestFocus();
    }
}
