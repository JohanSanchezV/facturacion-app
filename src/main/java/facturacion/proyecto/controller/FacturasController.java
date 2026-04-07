package facturacion.proyecto.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class FacturasController {

    @FXML
    private TableView<String[]> tblFacturas;

    @FXML
    private TableColumn<String[], String> colIdFactura;

    @FXML
    private TableColumn<String[], String> colFechaFactura;

    @FXML
    private TableColumn<String[], String> colClienteFactura;

    @FXML
    private TableColumn<String[], String> colMontoFactura;

    @FXML
    public void initialize() {
        colIdFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        colFechaFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        colClienteFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
        colMontoFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[3]));
    }

    @FXML
    public void cargarFacturas() {
        ObservableList<String[]> datos = FXCollections.observableArrayList();
        datos.add(new String[]{"1", "2026-04-07", "ANA JIMENEZ VARGAS", "5650"});
        datos.add(new String[]{"2", "2026-04-07", "MARIA LOPEZ MORA", "6215"});
        datos.add(new String[]{"3", "2026-04-07", "CARLOS ARAYA SOLIS", "9605"});
        tblFacturas.setItems(datos);
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) tblFacturas.getScene().getWindow();
        stage.close();
    }
}