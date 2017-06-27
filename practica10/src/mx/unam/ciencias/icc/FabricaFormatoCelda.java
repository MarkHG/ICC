package mx.unam.ciencias.icc;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

/**
 * Clase para fabricar el formato de celdas en una tabla.
 */
public class FabricaFormatoCelda<S, T>
    implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    /* La alineación de la celda. */
    private TextAlignment alineacion;
    /* El formato de la celda. */
    private String formato;

    /**
     * Regresa la alineación de la celda.
     * @return la alineación de la celda.
     */
    public TextAlignment getAlineacion() {
        return alineacion;
    }

    /**
     * Define la alineación de la celda.
     * @param alineacion la alineación de la celda.
     */
    public void setAlineacion(TextAlignment alineacion) {
        this.alineacion = alineacion;
    }

    /**
     * Regresa el formato de la celda.
     * @return el formato de la celda.
     */
    public String getFormato() {
        return formato;
    }

    /**
     * Define el formato de la celda.
     * @param formato el formato de la celda.
     */
    public void setFormato(String formato) {
        this.formato = formato;
    }

    /* Regresa el nodo correspondiente al elemento. */
    private Node getGrafico(T elemento) {
        if (elemento instanceof Node)
            return (Node)elemento;
        return null;
    }

    /* Regresa el texto correspondiente al elemento. */
    private String getTexto(T elemento) {
        if (elemento == null)
            return null;
        if (formato != null &&
            (elemento instanceof Integer || elemento instanceof Double))
            return String.format(formato, elemento);
        return elemento.toString();
    }

    /**
     * Sobrecarga el método que define el texto en la celda dependiendo del
     * valor dentro de la misma.
     * @param columna la columa de la celda.
     */
    @Override public TableCell<S, T> call(TableColumn<S, T> columna) {
        /* Clase interna anónima para sobrecargar la definición de texto de la
         * celda. */
        TableCell<S, T> cell = new TableCell<S, T>() {
                @Override public void updateItem(T elemento, boolean vacio) {
                    super.updateItem(elemento, vacio);
                    super.setText(getTexto(elemento));
                    super.setGraphic(getGrafico(elemento));
                }
            };
        cell.setTextAlignment(alineacion);
        switch (alineacion) {
        case CENTER: cell.setAlignment(Pos.CENTER); break;
        case RIGHT: cell.setAlignment(Pos.CENTER_RIGHT); break;
        default: cell.setAlignment(Pos.CENTER_LEFT); break;
        }
        return cell;
    }
}
