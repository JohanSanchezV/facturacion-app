package facturacion.proyecto.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientesController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtIdentificacion;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TableView<String[]> tblClientes;

    @FXML
    private TableColumn<String[], String> colNombre;

    @FXML
    private TableColumn<String[], String> colIdentificacion;

    @FXML
    private TableColumn<String[], String> colCorreo;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        colIdentificacion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        colCorreo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
    }

    @FXML
    public void guardarCliente() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Clientes");
        alert.setHeaderText(null);
        alert.setContentText("Luego conectamos guardar cliente con Oracle.");
        alert.showAndWait();
    }

    @FXML
    public void limpiarFormulario() {
        txtNombre.clear();
        txtIdentificacion.clear();
        txtCorreo.clear();
        txtTelefono.clear();
    }

    @FXML
    public void cargarClientes() {
        ObservableList<String[]> datos = FXCollections.observableArrayList();
        datos.add(new String[]{"Ana Jiménez Vargas", "404440444", "ana@correo.com"});
        datos.add(new String[]{"Juan Pérez Rojas", "101110111", "juan@correo.com"});
        datos.add(new String[]{"María López Mora", "202220222", "maria@correo.com"});
        tblClientes.setItems(datos);
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}