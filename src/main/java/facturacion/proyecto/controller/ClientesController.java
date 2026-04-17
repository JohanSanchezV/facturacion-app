package facturacion.proyecto.controller;

import facturacion.proyecto.model.Cliente;
import facturacion.proyecto.service.ClienteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class ClientesController {

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidoPaterno;
    @FXML private TextField txtApellidoMaterno;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtIdDireccion;

    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, String> colNombreCompleto;
    @FXML private TableColumn<Cliente, String> colIdentificacion;
    @FXML private TableColumn<Cliente, String> colCorreo;
    @FXML private TableColumn<Cliente, String> colTelefono;

    private final ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
        colNombreCompleto.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombreCompleto()));
        colIdentificacion.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tblClientes.setOnMouseClicked(event -> {
            Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado != null) {
                cargarFormulario(clienteSeleccionado);
            }
        });
    }

    @FXML
    public void buscarCliente() {
        try {
            String identificacion = txtIdentificacion.getText().trim();

            if (identificacion.isEmpty()) {
                cargarClientes();
                return;
            }

            Cliente cliente = clienteService.buscarPorIdentificacion(identificacion);
            ObservableList<Cliente> datos = FXCollections.observableArrayList();
            datos.add(cliente);
            tblClientes.setItems(datos);
            cargarFormulario(cliente);

        } catch (Exception e) {
            mostrarError("Buscar cliente", e.getMessage());
            tblClientes.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    public void insertarCliente() {
        try {
            Cliente cliente = construirClienteDesdeFormulario();
            clienteService.insertar(cliente);
            mostrarInfo("Cliente", "Cliente guardado correctamente.");
            cargarClientes();
            limpiarFormulario();
        } catch (Exception e) {
            mostrarError("Insertar cliente", e.getMessage());
        }
    }

    @FXML
    public void editarCliente() {
        try {
            Cliente cliente = construirClienteDesdeFormulario();
            clienteService.editar(cliente);
            mostrarInfo("Cliente", "Cliente editado correctamente.");
            cargarClientes();
            limpiarFormulario();
        } catch (Exception e) {
            mostrarError("Editar cliente", e.getMessage());
        }
    }

    @FXML
    public void eliminarCliente() {
        try {
            String identificacion = txtIdentificacion.getText().trim();

            if (identificacion.isEmpty()) {
                throw new Exception("Debe indicar o seleccionar una identificación.");
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Desea eliminar el cliente con identificación " + identificacion + "?");

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                clienteService.eliminar(identificacion);
                mostrarInfo("Cliente", "Cliente eliminado correctamente.");
                cargarClientes();
                limpiarFormulario();
            }
        } catch (Exception e) {
            mostrarError("Eliminar cliente", e.getMessage());
        }
    }

    @FXML
    public void cargarClientes() {
        try {
            List<Cliente> lista = clienteService.listarClientes();
            tblClientes.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            mostrarError("Listar clientes", e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtIdentificacion.clear();
        txtNombre.clear();
        txtApellidoPaterno.clear();
        txtApellidoMaterno.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtIdDireccion.clear();
        tblClientes.getSelectionModel().clearSelection();
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) txtIdentificacion.getScene().getWindow();
        stage.close();
    }

    private Cliente construirClienteDesdeFormulario() throws Exception {
        String identificacion = txtIdentificacion.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidoPaterno = txtApellidoPaterno.getText().trim();
        String apellidoMaterno = txtApellidoMaterno.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtIdDireccion.getText().trim();

        if (identificacion.isEmpty()) throw new Exception("Debe indicar la identificación.");
        if (nombre.isEmpty()) throw new Exception("Debe indicar el nombre.");
        if (apellidoPaterno.isEmpty()) throw new Exception("Debe indicar el apellido paterno.");
        if (correo.isEmpty()) throw new Exception("Debe indicar el correo.");
        if (telefono.isEmpty()) throw new Exception("Debe indicar el teléfono.");
        if (direccion.isEmpty()) throw new Exception("Debe indicar el ID de dirección.");

        Cliente cliente = new Cliente();
        cliente.setIdentificacion(identificacion);
        cliente.setNombre(nombre);
        cliente.setApellidoPaterno(apellidoPaterno);
        cliente.setApellidoMaterno(apellidoMaterno);
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);
        cliente.setIdDireccion(Integer.parseInt(direccion));
        return cliente;
    }

    private void cargarFormulario(Cliente cliente) {
        txtIdentificacion.setText(cliente.getIdentificacion());
        txtNombre.setText(cliente.getNombre());
        txtApellidoPaterno.setText(cliente.getApellidoPaterno());
        txtApellidoMaterno.setText(cliente.getApellidoMaterno());
        txtCorreo.setText(cliente.getCorreo());
        txtTelefono.setText(cliente.getTelefono());
        txtIdDireccion.setText(String.valueOf(cliente.getIdDireccion()));
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
