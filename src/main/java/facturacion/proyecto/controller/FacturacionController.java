package facturacion.proyecto.controller;

import facturacion.proyecto.model.Cliente;
import facturacion.proyecto.model.FacturaDetalle;
import facturacion.proyecto.model.FacturaResumen;
import facturacion.proyecto.service.FacturacionService;
import facturacion.proyecto.service.PdfFacturaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class FacturacionController {

    @FXML private Label lblFacturaActual;
    @FXML private TextField txtFecha;
    @FXML private TextField txtIdentificacionCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtCorreoCliente;
    @FXML private TextField txtTelefonoCliente;

    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtDescripcionEditable;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtSubtotalLinea;
    @FXML private TextField txtImpuestoLinea;
    @FXML private TextField txtTotalLinea;
    @FXML private ComboBox<String> cmbIva;

    @FXML private Label lblSubtotalFactura;
    @FXML private Label lblImpuestoFactura;
    @FXML private Label lblTotalFactura;

    @FXML private TableView<FacturaDetalle> tblDetalleFactura;
    @FXML private TableColumn<FacturaDetalle, String> colCodigoProducto;
    @FXML private TableColumn<FacturaDetalle, String> colProductoFactura;
    @FXML private TableColumn<FacturaDetalle, String> colDescripcionFactura;
    @FXML private TableColumn<FacturaDetalle, Integer> colCantidadFactura;
    @FXML private TableColumn<FacturaDetalle, Double> colIvaFactura;
    @FXML private TableColumn<FacturaDetalle, Double> colSubtotalFacturaTabla;
    @FXML private TableColumn<FacturaDetalle, Double> colImpuestoFacturaTabla;
    @FXML private TableColumn<FacturaDetalle, Double> colTotalFacturaTabla;

    private final FacturacionService facturacionService = new FacturacionService();
    private final PdfFacturaService pdfFacturaService = new PdfFacturaService();
    private final ObservableList<FacturaDetalle> detalleObservable = FXCollections.observableArrayList();

    private Cliente clienteSeleccionado = null;
    private Integer idFacturaActual = null;
    private String numeroFacturaActual = null;

    @FXML
    public void initialize() {
        txtFecha.setText(LocalDate.now().toString());
        lblFacturaActual.setText("Sin guardar");
        cmbIva.setItems(FXCollections.observableArrayList("0", "2", "13"));
        cmbIva.setValue("13");

        colCodigoProducto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCodigoProducto()));
        colProductoFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreProducto()));
        colDescripcionFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescripcionEditable()));
        colCantidadFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCantidad()).asObject());
        colIvaFactura.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPorcentajeIva()).asObject());
        colSubtotalFacturaTabla.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getSubtotal()).asObject());
        colImpuestoFacturaTabla.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getImpuesto()).asObject());
        colTotalFacturaTabla.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotal()).asObject());

        tblDetalleFactura.setItems(detalleObservable);
        recalcularTotalesPantalla();
    }

    @FXML
    public void buscarCliente() {
        try {
            String criterio = txtIdentificacionCliente.getText().trim();
            if (criterio.isEmpty()) {
                throw new Exception("Debe indicar identificación o nombre del cliente.");
            }

            List<Cliente> clientes = facturacionService.buscarClientes(criterio);
            if (clientes.isEmpty()) {
                throw new Exception("No se encontró un cliente con ese criterio.");
            }

            clienteSeleccionado = clientes.get(0);
            txtIdentificacionCliente.setText(clienteSeleccionado.getIdentificacion());
            txtNombreCliente.setText(clienteSeleccionado.getNombreCompleto());
            txtCorreoCliente.setText(clienteSeleccionado.getCorreo());
            txtTelefonoCliente.setText(clienteSeleccionado.getTelefono());

        } catch (Exception e) {
            clienteSeleccionado = null;
            txtNombreCliente.clear();
            txtCorreoCliente.clear();
            txtTelefonoCliente.clear();
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void buscarProducto() {
        try {
            String codigo = txtCodigoProducto.getText().trim();
            if (codigo.isEmpty()) throw new Exception("Debe indicar el código del producto.");
            if (txtCantidad.getText().trim().isEmpty()) throw new Exception("Debe indicar la cantidad.");

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double iva = Double.parseDouble(cmbIva.getValue());

            FacturaDetalle detalle = facturacionService.buscarProductoFacturacion(codigo, cantidad, iva);
            txtNombreProducto.setText(detalle.getNombreProducto());
            txtDescripcionEditable.setText(detalle.getDescripcionEditable());
            txtSubtotalLinea.setText(String.format("%.2f", detalle.getSubtotal()));
            txtImpuestoLinea.setText(String.format("%.2f", detalle.getImpuesto()));
            txtTotalLinea.setText(String.format("%.2f", detalle.getTotal()));

        } catch (Exception e) {
            txtNombreProducto.clear();
            txtDescripcionEditable.clear();
            txtSubtotalLinea.clear();
            txtImpuestoLinea.clear();
            txtTotalLinea.clear();
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void agregarLinea() {
        try {
            String codigo = txtCodigoProducto.getText().trim();
            if (codigo.isEmpty()) throw new Exception("Debe indicar el código del producto.");
            if (txtCantidad.getText().trim().isEmpty()) throw new Exception("Debe indicar la cantidad.");
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double iva = Double.parseDouble(cmbIva.getValue());

            FacturaDetalle detalle = facturacionService.buscarProductoFacturacion(codigo, cantidad, iva);
            if (!txtDescripcionEditable.getText().trim().isEmpty()) {
                detalle.setDescripcionEditable(txtDescripcionEditable.getText().trim());
            }
            detalleObservable.add(detalle);
            recalcularTotalesPantalla();
            limpiarLinea();

        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void guardarPreliminar() {
        try {
            validarFacturaBase();

            FacturaResumen resumen = facturacionService.crearFacturaPreliminar(
                    1,
                    clienteSeleccionado.getIdTercero(),
                    1,
                    LocalDate.parse(txtFecha.getText().trim())
            );

            for (FacturaDetalle d : detalleObservable) {
                facturacionService.agregarLineaFactura(
                        resumen.getIdFactura(),
                        d.getCodigoProducto(),
                        d.getCantidad(),
                        d.getPorcentajeIva(),
                        d.getDescripcionEditable()
                );
            }

            idFacturaActual = resumen.getIdFactura();
            numeroFacturaActual = resumen.getNumeroFactura();
            lblFacturaActual.setText(numeroFacturaActual);

            mostrarInfo("Factura", "Factura preliminar guardada correctamente. Número: " + numeroFacturaActual);
        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void emitirFactura() {
        try {
            if (idFacturaActual == null) throw new Exception("Primero debe guardar la factura preliminar.");

            facturacionService.emitirFactura(idFacturaActual, 1);

            FacturaResumen factura = facturacionService.obtenerFacturaEmitidaPorId(idFacturaActual);
            List<FacturaDetalle> detalle = facturacionService.obtenerDetalleFactura(idFacturaActual);
            String rutaPdf = pdfFacturaService.generarPdfFactura(factura, clienteSeleccionado, detalle);
            facturacionService.guardarRutaPdf(idFacturaActual, rutaPdf);

            mostrarInfo("Factura", "Factura emitida correctamente. PDF generado.");
        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void abrirPendientes() {
        abrirVentana("/facturacion/proyecto/facturas-pendientes-view.fxml", "Facturas Pendientes");
    }

    @FXML
    public void abrirEmitidas() {
        abrirVentana("/facturacion/proyecto/facturas-emitidas-view.fxml", "Facturas Emitidas");
    }

    @FXML
    public void limpiarTodo() {
        txtFecha.setText(LocalDate.now().toString());
        txtIdentificacionCliente.clear();
        txtNombreCliente.clear();
        txtCorreoCliente.clear();
        txtTelefonoCliente.clear();
        clienteSeleccionado = null;
        idFacturaActual = null;
        numeroFacturaActual = null;
        lblFacturaActual.setText("Sin guardar");
        limpiarLinea();
        detalleObservable.clear();
        recalcularTotalesPantalla();
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) lblFacturaActual.getScene().getWindow();
        stage.close();
    }

    private void abrirVentana(String rutaFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            mostrarError("Error", "No se pudo abrir la ventana:\n" + e.getMessage());
        }
    }

    private void limpiarLinea() {
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtDescripcionEditable.clear();
        txtCantidad.clear();
        txtSubtotalLinea.clear();
        txtImpuestoLinea.clear();
        txtTotalLinea.clear();
        cmbIva.setValue("13");
    }

    private void recalcularTotalesPantalla() {
        double subtotal = detalleObservable.stream().mapToDouble(FacturaDetalle::getSubtotal).sum();
        double impuesto = detalleObservable.stream().mapToDouble(FacturaDetalle::getImpuesto).sum();
        double total = detalleObservable.stream().mapToDouble(FacturaDetalle::getTotal).sum();
        lblSubtotalFactura.setText(String.format("%.2f", subtotal));
        lblImpuestoFactura.setText(String.format("%.2f", impuesto));
        lblTotalFactura.setText(String.format("%.2f", total));
    }

    private void validarFacturaBase() throws Exception {
        if (clienteSeleccionado == null) throw new Exception("Debe buscar y seleccionar un cliente válido.");
        if (detalleObservable.isEmpty()) throw new Exception("Debe agregar al menos una línea de detalle.");
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
