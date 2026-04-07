package facturacion.proyecto.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import facturacion.proyecto.App;

public class FacturacionController {

    @FXML
    private TextField txtIdFactura;

    @FXML
    private TextField txtIdCliente;

    @FXML
    private TextField txtIdProducto;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TableView<String[]> tblDetalleFactura;

    @FXML
    private TableColumn<String[], String> colProductoFactura;

    @FXML
    private TableColumn<String[], String> colCantidadFactura;

    @FXML
    private TableColumn<String[], String> colTotalFactura;

    @FXML
    public void initialize() {
        colProductoFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        colCantidadFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        colTotalFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
    }

    @FXML
    public void generarFactura() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Facturación");
        alert.setHeaderText(null);
        alert.setContentText("Luego conectamos esta pantalla con el paquete PKG_FIDE_FACTURACION.");
        alert.showAndWait();

        ObservableList<String[]> datos = FXCollections.observableArrayList();
        datos.add(new String[]{"Cuaderno", "2", "5650"});
        tblDetalleFactura.setItems(datos);
    }

    @FXML
    public void abrirFacturasEmitidas() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/facturacion/proyecto/facturas-view.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Facturas Emitidas");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo abrir la ventana: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) txtIdFactura.getScene().getWindow();
        stage.close();
    }
}