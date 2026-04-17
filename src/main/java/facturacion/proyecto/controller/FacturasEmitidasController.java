package facturacion.proyecto.controller;

import facturacion.proyecto.model.FacturaResumen;
import facturacion.proyecto.service.FacturacionService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FacturasEmitidasController {

    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtFechaDesde;
    @FXML private TextField txtFechaHasta;
    @FXML private TableView<FacturaResumen> tblFacturasEmitidas;
    @FXML private TableColumn<FacturaResumen, String> colFactura;
    @FXML private TableColumn<FacturaResumen, String> colCliente;
    @FXML private TableColumn<FacturaResumen, String> colIdentificacionTabla;
    @FXML private TableColumn<FacturaResumen, LocalDate> colFecha;
    @FXML private TableColumn<FacturaResumen, Double> colMonto;

    private final FacturacionService facturacionService = new FacturacionService();

    @FXML
    public void initialize() {
        colFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNumeroFactura()));
        colCliente.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreCliente()));
        colIdentificacionTabla.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdentificacion()));
        colFecha.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getFechaEmision()));
        colMonto.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotalFactura()).asObject());
    }

    @FXML
    public void buscar() {
        try {
            LocalDate desde = txtFechaDesde.getText().trim().isEmpty() ? null : LocalDate.parse(txtFechaDesde.getText().trim());
            LocalDate hasta = txtFechaHasta.getText().trim().isEmpty() ? null : LocalDate.parse(txtFechaHasta.getText().trim());
            tblFacturasEmitidas.setItems(FXCollections.observableArrayList(
                    facturacionService.listarFacturasEmitidas(
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
    public void verPdf() {
        try {
            FacturaResumen seleccionada = tblFacturasEmitidas.getSelectionModel().getSelectedItem();
            if (seleccionada == null) throw new Exception("Debe seleccionar una factura.");
            facturacionService.abrirPdf(seleccionada.getRutaPdf());
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void volver() {
        ((Stage) tblFacturasEmitidas.getScene().getWindow()).close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
