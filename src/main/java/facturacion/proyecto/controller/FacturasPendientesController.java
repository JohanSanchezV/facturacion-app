package facturacion.proyecto.controller;

import facturacion.proyecto.model.Cliente;
import facturacion.proyecto.model.FacturaDetalle;
import facturacion.proyecto.model.FacturaResumen;
import facturacion.proyecto.service.FacturacionService;
import facturacion.proyecto.service.PdfFacturaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class FacturasPendientesController {

    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtFechaDesde;
    @FXML private TextField txtFechaHasta;
    @FXML private TableView<FacturaResumen> tblFacturasPendientes;
    @FXML private TableColumn<FacturaResumen, String> colFactura;
    @FXML private TableColumn<FacturaResumen, String> colCliente;
    @FXML private TableColumn<FacturaResumen, String> colIdentificacionTabla;
    @FXML private TableColumn<FacturaResumen, LocalDate> colFecha;
    @FXML private TableColumn<FacturaResumen, Double> colMonto;

    private final FacturacionService facturacionService = new FacturacionService();
    private final PdfFacturaService pdfFacturaService = new PdfFacturaService();

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
            if (seleccionada == null) throw new Exception("Debe seleccionar una factura.");

            facturacionService.emitirFactura(seleccionada.getIdFactura(), 1);
            Cliente cliente = facturacionService.buscarClientes(seleccionada.getIdentificacion()).get(0);
            List<FacturaDetalle> detalle = facturacionService.obtenerDetalleFactura(seleccionada.getIdFactura());
            FacturaResumen emitida = facturacionService.obtenerFacturaEmitidaPorId(seleccionada.getIdFactura());
            String ruta = pdfFacturaService.generarPdfFactura(emitida, cliente, detalle);
            facturacionService.guardarRutaPdf(seleccionada.getIdFactura(), ruta);

            buscar();
            mostrarInfo("Factura emitida correctamente.");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void verPdf() {
        try {
            FacturaResumen seleccionada = tblFacturasPendientes.getSelectionModel().getSelectedItem();
            if (seleccionada == null) throw new Exception("Debe seleccionar una factura.");
            facturacionService.abrirPdf(seleccionada.getRutaPdf());
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void volver() {
        ((Stage) tblFacturasPendientes.getScene().getWindow()).close();
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
