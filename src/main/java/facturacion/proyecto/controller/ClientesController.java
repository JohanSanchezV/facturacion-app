package facturacion.proyecto.controller;

import facturacion.proyecto.model.Cliente;
import facturacion.proyecto.service.ClienteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ClientesController {

    @FXML
    private TextField txtIdCliente;
    @FXML
    private TextField txtIdentificacion;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidoPaterno;
    @FXML
    private TextField txtApellidoMaterno;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtIdDireccion;

    @FXML
    private TableView<Cliente> tblClientes;
    @FXML
    private TableColumn<Cliente, Integer> colIdCliente;
    @FXML
    private TableColumn<Cliente, String> colNombreCompleto;
    @FXML
    private TableColumn<Cliente, String> colIdentificacion;
    @FXML
    private TableColumn<Cliente, String> colCorreo;
    @FXML
    private TableColumn<Cliente, String> colTelefono;

    private final ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
        colIdCliente.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdTercero()).asObject());
        colNombreCompleto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreCompleto()));
        colIdentificacion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdentificacion()));
        colCorreo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreo()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
    }

    @FXML
    public void buscarCliente() {
        try {
            limpiarTabla();

            String identificacion = txtIdentificacion.getText().trim();
            Cliente cliente = clienteService.buscarPorIdentificacion(identificacion);

            txtIdCliente.setText(String.valueOf(cliente.getIdTercero()));
            txtNombre.setText(cliente.getNombre());
            txtApellidoPaterno.setText(cliente.getApellidoPaterno());
            txtApellidoMaterno.setText(cliente.getApellidoMaterno());
            txtCorreo.setText(cliente.getCorreo());
            txtTelefono.setText(cliente.getTelefono());

            ObservableList<Cliente> datos = FXCollections.observableArrayList();
            datos.add(cliente);
            tblClientes.setItems(datos);

        } catch (Exception e) {
            limpiarFormulario();
            limpiarTabla();
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void guardarCliente() {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdTercero(Integer.parseInt(txtIdCliente.getText().trim()));
            cliente.setIdentificacion(txtIdentificacion.getText().trim());
            cliente.setNombre(txtNombre.getText().trim());
            cliente.setApellidoPaterno(txtApellidoPaterno.getText().trim());
            cliente.setApellidoMaterno(txtApellidoMaterno.getText().trim());
            cliente.setCorreo(txtCorreo.getText().trim());
            cliente.setTelefono(txtTelefono.getText().trim());

            int idDireccion = Integer.parseInt(txtIdDireccion.getText().trim());

            clienteService.insertar(cliente, idDireccion);
            mostrarInfo("Cliente", "Cliente guardado correctamente.");
            cargarClientes();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void editarCliente() {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdTercero(Integer.parseInt(txtIdCliente.getText().trim()));
            cliente.setIdentificacion(txtIdentificacion.getText().trim());
            cliente.setNombre(txtNombre.getText().trim());
            cliente.setApellidoPaterno(txtApellidoPaterno.getText().trim());
            cliente.setApellidoMaterno(txtApellidoMaterno.getText().trim());
            cliente.setCorreo(txtCorreo.getText().trim());
            cliente.setTelefono(txtTelefono.getText().trim());

            int idDireccion = Integer.parseInt(txtIdDireccion.getText().trim());

            clienteService.editar(cliente, idDireccion);
            mostrarInfo("Cliente", "Cliente editado correctamente.");
            cargarClientes();

        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void eliminarCliente() {
        try {
            int id = Integer.parseInt(txtIdCliente.getText().trim());

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Desea eliminar el cliente con ID " + id + "?");

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                clienteService.eliminar(id);
                mostrarInfo("Cliente", "Cliente eliminado correctamente.");
                limpiarFormulario();
                cargarClientes();
            }

        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void cargarClientes() {
        try {
            ObservableList<Cliente> datos = FXCollections.observableArrayList(clienteService.listarActivos());
            tblClientes.setItems(datos);
        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtIdCliente.clear();
        txtIdentificacion.clear();
        txtNombre.clear();
        txtApellidoPaterno.clear();
        txtApellidoMaterno.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtIdDireccion.clear();
    }

    private void limpiarTabla() {
        tblClientes.setItems(FXCollections.observableArrayList());
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) txtIdCliente.getScene().getWindow();
        stage.close();
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