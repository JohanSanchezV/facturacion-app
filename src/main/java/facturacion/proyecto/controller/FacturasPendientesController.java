package facturacion.proyecto.controller;

import facturacion.proyecto.model.FacturaResumen;
import facturacion.proyecto.service.FacturacionService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FacturasPendientesController {

    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtFechaDesde;
    @FXML private TextField txtFechaHasta;

    @FXML private TableView<FacturaResumen> tblFacturasPendientes;
    @FXML private TableColumn<FacturaResumen, Integer> colIdFactura;
    @FXML private TableColumn<FacturaResumen, String> colNombreClienteTabla;
    @FXML private TableColumn<FacturaResumen, String> colIdentificacionTabla;
    @FXML private TableColumn<FacturaResumen, LocalDate> colFechaTabla;
    @FXML private TableColumn<FacturaResumen, Double> colMontoTabla;

    private final FacturacionService facturacionService = new FacturacionService();

    @FXML
    public void initialize() {
        colIdFactura.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdFactura()).asObject());

        colNombreClienteTabla.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreCliente()));

        colIdentificacionTabla.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getIdentificacion()));

        colFechaTabla.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getFechaEmision()));

        colMontoTabla.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotalFactura()).asObject());
    }

    @FXML
    public void buscar() {
        try {
            LocalDate desde = txtFechaDesde.getText().trim().isEmpty()
                    ? null : LocalDate.parse(txtFechaDesde.getText().trim());

            LocalDate hasta = txtFechaHasta.getText().trim().isEmpty()
                    ? null : LocalDate.parse(txtFechaHasta.getText().trim());

            tblFacturasPendientes.setItems(FXCollections.observableArrayList(
                    facturacionService.listarFacturasPendientes(
                            txtNombreCliente.getText().trim(),
                            txtIdentificacion.getText().trim(),
                            desde,
                            hasta
                    )
            ));

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void emitirSeleccionada() {
        try {
            FacturaResumen seleccionada = tblFacturasPendientes.getSelectionModel().getSelectedItem();

            if (seleccionada == null) {
                throw new Exception("Debe seleccionar una factura.");
            }

            facturacionService.emitirFactura(seleccionada.getIdFactura());
            buscar();
            mostrarInfo("Factura emitida correctamente.");

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) tblFacturasPendientes.getScene().getWindow();
        stage.close();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}