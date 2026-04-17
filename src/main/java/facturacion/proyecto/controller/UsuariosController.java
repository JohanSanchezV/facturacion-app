package facturacion.proyecto.controller;

import facturacion.proyecto.model.Empresa;
import facturacion.proyecto.model.Estado;
import facturacion.proyecto.model.Rol;
import facturacion.proyecto.model.Usuario;
import facturacion.proyecto.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class UsuariosController {

    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidoPaterno;
    @FXML private TextField txtApellidoMaterno;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassHash;
    @FXML private ComboBox<Empresa> cmbEmpresa;
    @FXML private ComboBox<Rol> cmbRol;
    @FXML private ComboBox<Estado> cmbEstado;

    @FXML private TableView<Usuario> tblUsuarios;
    @FXML private TableColumn<Usuario, String> colIdentificacion;
    @FXML private TableColumn<Usuario, String> colNombreCompleto;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colEmpresa;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colEstado;
    @FXML private TableColumn<Usuario, String> colContrasena;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void initialize() {
        colIdentificacion.setCellValueFactory(new PropertyValueFactory<>("numeroIdentificacion"));
        colNombreCompleto.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombreCompleto()));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreLegal"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("nombreRol"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("nombreEstado"));
        colContrasena.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(enmascarar(cellData.getValue().getPassHash())));

        cargarEmpresas();
        cargarRoles();
        cargarEstados();

        tblUsuarios.setOnMouseClicked(event -> {
            Usuario usuarioSeleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado != null) {
                cargarFormulario(usuarioSeleccionado);
            }
        });
    }

    @FXML
    public void buscarUsuario() {
        try {
            String identificacion = txtIdentificacion.getText().trim();

            if (identificacion.isEmpty()) {
                cargarUsuarios();
                return;
            }

            Usuario usuario = usuarioService.buscarPorIdentificacion(identificacion);
            ObservableList<Usuario> datos = FXCollections.observableArrayList();
            datos.add(usuario);
            tblUsuarios.setItems(datos);
            cargarFormulario(usuario);

        } catch (Exception e) {
            mostrarError("Buscar usuario", e.getMessage());
            tblUsuarios.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    public void insertarUsuario() {
        try {
            Usuario usuario = construirUsuarioDesdeFormulario(false);
            usuarioService.insertar(usuario);
            mostrarInfo("Usuario", "Usuario guardado correctamente.");
            cargarUsuarios();
            limpiarFormulario();
        } catch (Exception e) {
            mostrarError("Insertar usuario", e.getMessage());
        }
    }

    @FXML
    public void editarUsuario() {
        try {
            Usuario usuario = construirUsuarioDesdeFormulario(true);
            usuarioService.editar(usuario);
            mostrarInfo("Usuario", "Usuario editado correctamente.");
            cargarUsuarios();
            limpiarFormulario();
        } catch (Exception e) {
            mostrarError("Editar usuario", e.getMessage());
        }
    }

    @FXML
    public void eliminarUsuario() {
        try {
            String identificacion = txtIdentificacion.getText().trim();
            if (identificacion.isEmpty()) {
                throw new Exception("Debe indicar o seleccionar una identificación.");
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Desea eliminar el usuario con identificación " + identificacion + "?");

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                usuarioService.eliminar(identificacion);
                mostrarInfo("Usuario", "Usuario eliminado correctamente.");
                cargarUsuarios();
                limpiarFormulario();
            }
        } catch (Exception e) {
            mostrarError("Eliminar usuario", e.getMessage());
        }
    }

    @FXML
    public void cargarUsuarios() {
        try {
            List<Usuario> lista = usuarioService.listarUsuarios();
            ObservableList<Usuario> datos = FXCollections.observableArrayList(lista);
            tblUsuarios.setItems(datos);
        } catch (Exception e) {
            mostrarError("Listar usuarios", e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtIdentificacion.clear();
        txtNombre.clear();
        txtApellidoPaterno.clear();
        txtApellidoMaterno.clear();
        txtCorreo.clear();
        txtPassHash.clear();
        cmbEmpresa.getSelectionModel().clearSelection();
        cmbRol.getSelectionModel().clearSelection();
        cmbEstado.getSelectionModel().clearSelection();
        tblUsuarios.getSelectionModel().clearSelection();
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) txtIdentificacion.getScene().getWindow();
        stage.close();
    }

    private Usuario construirUsuarioDesdeFormulario(boolean incluirEstado) throws Exception {
        String identificacion = txtIdentificacion.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidoPaterno = txtApellidoPaterno.getText().trim();
        String apellidoMaterno = txtApellidoMaterno.getText().trim();
        String correo = txtCorreo.getText().trim();
        String pass = txtPassHash.getText().trim();
        Empresa empresa = cmbEmpresa.getValue();
        Rol rol = cmbRol.getValue();
        Estado estado = cmbEstado.getValue();

        if (identificacion.isEmpty()) throw new Exception("Debe indicar la identificación.");
        if (nombre.isEmpty()) throw new Exception("Debe indicar el nombre.");
        if (apellidoPaterno.isEmpty()) throw new Exception("Debe indicar el apellido paterno.");
        if (correo.isEmpty()) throw new Exception("Debe indicar el correo.");
        if (empresa == null) throw new Exception("Debe seleccionar la empresa.");
        if (rol == null) throw new Exception("Debe seleccionar el rol.");
        if (pass.isEmpty()) throw new Exception("Debe indicar la contraseña.");
        if (incluirEstado && estado == null) throw new Exception("Debe seleccionar el estado.");

        Usuario usuario = new Usuario();
        usuario.setNumeroIdentificacion(identificacion);
        usuario.setNombre(nombre);
        usuario.setApellidoPaterno(apellidoPaterno);
        usuario.setApellidoMaterno(apellidoMaterno);
        usuario.setCorreo(correo);
        usuario.setIdEmisor(empresa.getIdEmisor());
        usuario.setIdRol(rol.getIdRol());
        usuario.setPassHash(pass);
        if (estado != null) usuario.setIdEstado(estado.getIdEstado());
        return usuario;
    }

    private void cargarFormulario(Usuario usuario) {
        txtIdentificacion.setText(usuario.getNumeroIdentificacion());
        txtNombre.setText(usuario.getNombre());
        txtApellidoPaterno.setText(usuario.getApellidoPaterno());
        txtApellidoMaterno.setText(usuario.getApellidoMaterno());
        txtCorreo.setText(usuario.getCorreo());
        txtPassHash.setText(usuario.getPassHash());
        seleccionarEmpresa(usuario.getIdEmisor());
        seleccionarRol(usuario.getIdRol());
        seleccionarEstado(usuario.getIdEstado());
    }

    private void cargarEmpresas() {
        try {
            cmbEmpresa.setItems(FXCollections.observableArrayList(usuarioService.listarEmpresas()));
        } catch (Exception e) {
            mostrarError("Empresas", "No se pudieron cargar las empresas: " + e.getMessage());
        }
    }

    private void cargarRoles() {
        try {
            cmbRol.setItems(FXCollections.observableArrayList(usuarioService.listarRoles()));
        } catch (Exception e) {
            mostrarError("Roles", "No se pudieron cargar los roles: " + e.getMessage());
        }
    }

    private void cargarEstados() {
        try {
            cmbEstado.setItems(FXCollections.observableArrayList(usuarioService.listarEstados()));
        } catch (Exception e) {
            mostrarError("Estados", "No se pudieron cargar los estados: " + e.getMessage());
        }
    }

    private void seleccionarEmpresa(int idEmisor) {
        for (Empresa empresa : cmbEmpresa.getItems()) {
            if (empresa.getIdEmisor() == idEmisor) {
                cmbEmpresa.setValue(empresa);
                break;
            }
        }
    }

    private void seleccionarRol(int idRol) {
        for (Rol rol : cmbRol.getItems()) {
            if (rol.getIdRol() == idRol) {
                cmbRol.setValue(rol);
                break;
            }
        }
    }

    private void seleccionarEstado(int idEstado) {
        for (Estado estado : cmbEstado.getItems()) {
            if (estado.getIdEstado() == idEstado) {
                cmbEstado.setValue(estado);
                break;
            }
        }
    }

    private String enmascarar(String valor) {
        if (valor == null || valor.isEmpty()) return "";
        return "••••••••";
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
