package facturacion.proyecto.controller;

import facturacion.proyecto.model.Producto;
import facturacion.proyecto.service.ProductoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProductosController {

    @FXML
    private TextField txtIdProducto;
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtDescripcionNombre;
    @FXML
    private TextField txtCategoriaNombre;
    @FXML
    private TextField txtMaterialNombre;
    @FXML
    private TextField txtIdDescripcion;
    @FXML
    private TextField txtIdCategoria;
    @FXML
    private TextField txtIdMaterial;

    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TableColumn<Producto, Integer> colIdProducto;
    @FXML
    private TableColumn<Producto, String> colNombreProducto;
    @FXML
    private TableColumn<Producto, Double> colPrecioProducto;
    @FXML
    private TableColumn<Producto, String> colDescripcionProducto;
    @FXML
    private TableColumn<Producto, String> colCategoriaProducto;
    @FXML
    private TableColumn<Producto, String> colMaterialProducto;

    private final ProductoService productoService = new ProductoService();

    @FXML
    public void initialize() {
        colIdProducto.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdProducto()).asObject());
        colNombreProducto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colPrecioProducto.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrecio()).asObject());
        colDescripcionProducto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescripcionNombre()));
        colCategoriaProducto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategoriaNombre()));
        colMaterialProducto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMaterialNombre()));
    }

    @FXML
    public void buscarProducto() {
        try {
            limpiarTabla();

            int id = Integer.parseInt(txtIdProducto.getText().trim());
            Producto producto = productoService.buscarPorId(id);

            txtNombreProducto.setText(producto.getNombre());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            txtDescripcionNombre.setText(producto.getDescripcionNombre());
            txtCategoriaNombre.setText(producto.getCategoriaNombre());
            txtMaterialNombre.setText(producto.getMaterialNombre());
            txtIdDescripcion.setText(String.valueOf(producto.getIdDescripcion()));
            txtIdCategoria.setText(String.valueOf(producto.getIdCategoria()));
            txtIdMaterial.setText(String.valueOf(producto.getIdMaterial()));

            ObservableList<Producto> datos = FXCollections.observableArrayList();
            datos.add(producto);
            tblProductos.setItems(datos);

        } catch (Exception e) {
            limpiarFormulario();
            limpiarTabla();
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void guardarProducto() {
        try {
            Producto producto = new Producto();
            producto.setIdProducto(Integer.parseInt(txtIdProducto.getText().trim()));
            producto.setNombre(txtNombreProducto.getText().trim());
            producto.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            producto.setIdDescripcion(Integer.parseInt(txtIdDescripcion.getText().trim()));
            producto.setIdCategoria(Integer.parseInt(txtIdCategoria.getText().trim()));
            producto.setIdMaterial(Integer.parseInt(txtIdMaterial.getText().trim()));

            productoService.insertar(producto);
            mostrarInfo("Producto", "Producto guardado correctamente.");
            cargarProductos();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void editarProducto() {
        try {
            Producto producto = new Producto();
            producto.setIdProducto(Integer.parseInt(txtIdProducto.getText().trim()));
            producto.setNombre(txtNombreProducto.getText().trim());
            producto.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            producto.setIdDescripcion(Integer.parseInt(txtIdDescripcion.getText().trim()));
            producto.setIdCategoria(Integer.parseInt(txtIdCategoria.getText().trim()));
            producto.setIdMaterial(Integer.parseInt(txtIdMaterial.getText().trim()));

            productoService.editar(producto);
            mostrarInfo("Producto", "Producto editado correctamente.");
            cargarProductos();

        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void eliminarProducto() {
        try {
            int id = Integer.parseInt(txtIdProducto.getText().trim());

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Desea eliminar el producto con código " + id + "?");

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                productoService.eliminarLogico(id);
                mostrarInfo("Producto", "Producto eliminado correctamente.");
                limpiarFormulario();
                cargarProductos();
            }

        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void cargarProductos() {
        try {
            ObservableList<Producto> datos = FXCollections.observableArrayList(productoService.listarActivos());
            tblProductos.setItems(datos);
        } catch (Exception e) {
            mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtIdProducto.clear();
        txtNombreProducto.clear();
        txtPrecio.clear();
        txtDescripcionNombre.clear();
        txtCategoriaNombre.clear();
        txtMaterialNombre.clear();
        txtIdDescripcion.clear();
        txtIdCategoria.clear();
        txtIdMaterial.clear();
    }

    private void limpiarTabla() {
        tblProductos.setItems(FXCollections.observableArrayList());
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) txtIdProducto.getScene().getWindow();
        stage.close();
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
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