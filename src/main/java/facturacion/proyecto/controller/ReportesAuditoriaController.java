package facturacion.proyecto.controller;

import facturacion.proyecto.model.AuditoriaRegistro;
import facturacion.proyecto.model.ReporteFacturaCliente;
import facturacion.proyecto.model.ReporteVentaFecha;
import facturacion.proyecto.service.ReportesAuditoriaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ReportesAuditoriaController {

    @FXML private DatePicker dpVentasDesde;
    @FXML private DatePicker dpVentasHasta;
    @FXML private TableView<ReporteVentaFecha> tblVentasFechas;
    @FXML private TableColumn<ReporteVentaFecha, LocalDate> colFechaVenta;
    @FXML private TableColumn<ReporteVentaFecha, Integer> colCantidadFacturasVenta;
    @FXML private TableColumn<ReporteVentaFecha, Double> colTotalVentasVenta;

    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtIdentificacionCliente;
    @FXML private DatePicker dpClienteDesde;
    @FXML private DatePicker dpClienteHasta;
    @FXML private TableView<ReporteFacturaCliente> tblFacturasCliente;
    @FXML private TableColumn<ReporteFacturaCliente, String> colNumeroFacturaCliente;
    @FXML private TableColumn<ReporteFacturaCliente, String> colNombreClienteTabla;
    @FXML private TableColumn<ReporteFacturaCliente, String> colIdentificacionClienteTabla;
    @FXML private TableColumn<ReporteFacturaCliente, LocalDate> colFechaFacturaCliente;
    @FXML private TableColumn<ReporteFacturaCliente, Double> colMontoFacturaCliente;

    @FXML private ChoiceBox<String> chModuloAuditoria;
    @FXML private ChoiceBox<String> chTablaAuditoria;
    @FXML private DatePicker dpAuditoriaDesde;
    @FXML private DatePicker dpAuditoriaHasta;
    @FXML private TableView<AuditoriaRegistro> tblAuditoria;
    @FXML private TableColumn<AuditoriaRegistro, Integer> colIdAuditoria;
    @FXML private TableColumn<AuditoriaRegistro, String> colModuloAuditoria;
    @FXML private TableColumn<AuditoriaRegistro, String> colAccionAuditoria;
    @FXML private TableColumn<AuditoriaRegistro, String> colTablaAuditoria;
    @FXML private TableColumn<AuditoriaRegistro, String> colRegistroAuditoria;
    @FXML private TableColumn<AuditoriaRegistro, String> colDetalleAuditoria;
    @FXML private TableColumn<AuditoriaRegistro, String> colUsuarioAuditoria;
    @FXML private TableColumn<AuditoriaRegistro, java.time.LocalDateTime> colFechaHoraAuditoria;

    private final ReportesAuditoriaService service = new ReportesAuditoriaService();

    @FXML
    public void initialize() {
        colFechaVenta.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getFecha()));
        colCantidadFacturasVenta.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCantidadFacturas()).asObject());
        colTotalVentasVenta.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotalVentas()).asObject());

        colNumeroFacturaCliente.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNumeroFactura()));
        colNombreClienteTabla.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreCliente()));
        colIdentificacionClienteTabla.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdentificacion()));
        colFechaFacturaCliente.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getFechaEmision()));
        colMontoFacturaCliente.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotalFactura()).asObject());

        colIdAuditoria.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdAuditoria()).asObject());
        colModuloAuditoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getModulo()));
        colAccionAuditoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAccion()));
        colTablaAuditoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTablaAfectada()));
        colRegistroAuditoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdRegistro()));
        colDetalleAuditoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDetalle()));
        colUsuarioAuditoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreUsuario()));
        colFechaHoraAuditoria.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getFechaHora()));

        chModuloAuditoria.setItems(FXCollections.observableArrayList("", "FACTURACION", "CLIENTES", "PRODUCTOS", "USUARIOS"));
        chTablaAuditoria.setItems(FXCollections.observableArrayList("", "FIDE_FACTURA_VENTA_TB", "FIDE_VENTA_DETALLE_TB", "FIDE_PRODUCTO_TB", "FIDE_TERCERO_TB", "FIDE_USUARIO_APP_TB"));
        chModuloAuditoria.setValue("");
        chTablaAuditoria.setValue("");
    }

    @FXML
    public void buscarVentasPorFechas() {
        try {
            tblVentasFechas.setItems(FXCollections.observableArrayList(service.reporteVentasPorFechas(dpVentasDesde.getValue(), dpVentasHasta.getValue())));
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void buscarFacturasCliente() {
        try {
            tblFacturasCliente.setItems(FXCollections.observableArrayList(service.reporteVentasPorCliente(txtNombreCliente.getText().trim(), txtIdentificacionCliente.getText().trim(), dpClienteDesde.getValue(), dpClienteHasta.getValue())));
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void abrirPdfFacturaCliente() {
        try {
            ReporteFacturaCliente seleccionada = tblFacturasCliente.getSelectionModel().getSelectedItem();
            if (seleccionada == null) throw new Exception("Debe seleccionar una factura.");
            service.abrirPdf(seleccionada.getRutaPdf());
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void buscarAuditoria() {
        try {
            String modulo = chModuloAuditoria.getValue();
            String tabla = chTablaAuditoria.getValue();
            if (modulo != null && modulo.isBlank()) modulo = null;
            if (tabla != null && tabla.isBlank()) tabla = null;
            tblAuditoria.setItems(FXCollections.observableArrayList(service.reporteAuditoria(modulo, tabla, dpAuditoriaDesde.getValue(), dpAuditoriaHasta.getValue())));
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void limpiarTodo() {
        dpVentasDesde.setValue(null);
        dpVentasHasta.setValue(null);
        tblVentasFechas.setItems(FXCollections.observableArrayList());
        txtNombreCliente.clear();
        txtIdentificacionCliente.clear();
        dpClienteDesde.setValue(null);
        dpClienteHasta.setValue(null);
        tblFacturasCliente.setItems(FXCollections.observableArrayList());
        chModuloAuditoria.setValue("");
        chTablaAuditoria.setValue("");
        dpAuditoriaDesde.setValue(null);
        dpAuditoriaHasta.setValue(null);
        tblAuditoria.setItems(FXCollections.observableArrayList());
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) tblVentasFechas.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
