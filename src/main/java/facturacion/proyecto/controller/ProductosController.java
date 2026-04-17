package facturacion.proyecto.controller;

import facturacion.proyecto.model.Categoria;
import facturacion.proyecto.model.Material;
import facturacion.proyecto.model.Producto;
import facturacion.proyecto.service.ProductoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class ProductosController {

    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCantidadDisponible;
    @FXML private TextField txtDescripcionNombre;

    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private ComboBox<Material> cmbMaterial;
    @FXML private ComboBox<String> cmbIva;

    @FXML private Label lblImpuestoCalculado;
    @FXML private Label lblTotalCalculado;

    @FXML private TableView<Producto> tblProductos;
    @FXML private TableColumn<Producto, String> colCodigoProducto;
    @FXML private TableColumn<Producto, String> colNombreProducto;
    @FXML private TableColumn<Producto, Double> colPrecioProducto;
    @FXML private TableColumn<Producto, Double> colCantidadProducto;
    @FXML private TableColumn<Producto, Double> colIvaProducto;
    @FXML private TableColumn<Producto, String> colDescripcionProducto;
    @FXML private TableColumn<Producto, String> colCategoriaProducto;
    @FXML private TableColumn<Producto, String> colMaterialProducto;

    private final ProductoService productoService = new ProductoService();

    @FXML
    public void initialize() {
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecioProducto.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidadProducto.setCellValueFactory(new PropertyValueFactory<>("cantidadDisponible"));
        colIvaProducto.setCellValueFactory(new PropertyValueFactory<>("porcentajeIva"));
        colDescripcionProducto.setCellValueFactory(new PropertyValueFactory<>("descripcionNombre"));
        colCategoriaProducto.setCellValueFactory(new PropertyValueFactory<>("categoriaNombre"));
        colMaterialProducto.setCellValueFactory(new PropertyValueFactory<>("materialNombre"));

        cmbIva.setItems(FXCollections.observableArrayList("0", "2", "13"));
        cmbIva.setValue("13");

        cargarCategorias();
        cargarMateriales();

        txtPrecio.textProperty().addListener((obs, oldVal, newVal) -> recalcularMontos());
        txtCantidadDisponible.textProperty().addListener((obs, oldVal, newVal) -> recalcularMontos());
        cmbIva.valueProperty().addListener((obs, oldVal, newVal) -> recalcularMontos());

        tblProductos.setOnMouseClicked(event -> {
            Producto producto = tblProductos.getSelectionModel().getSelectedItem();
            if (producto != null) {
                cargarFormulario(producto);
            }
        });

        recalcularMontos();
    }

    @FXML
    public void buscarProducto() {
        try {
            String codigo = txtCodigoProducto.getText().trim();

            if (codigo.isEmpty()) {
                cargarProductos();
                return;
            }

            Producto producto = productoService.buscarPorCodigo(codigo);

            ObservableList<Producto> datos = FXCollections.observableArrayList();
            datos.add(producto);
            tblProductos.setItems(datos);
            cargarFormulario(producto);

        } catch (Exception e) {
            limpiarTabla();
            mostrarError("Buscar producto", e.getMessage());
        }
    }

    @FXML
    public void guardarProducto() {
        try {
            Producto producto = construirProductoDesdeFormulario();
            productoService.insertar(producto);

            mostrarInfo("Producto", "Producto guardado correctamente.");
            cargarProductos();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Guardar producto", e.getMessage());
        }
    }

    @FXML
    public void editarProducto() {
        try {
            Producto producto = construirProductoDesdeFormulario();
            productoService.editar(producto);

            mostrarInfo("Producto", "Producto editado correctamente.");
            cargarProductos();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError("Editar producto", e.getMessage());
        }
    }

    @FXML
    public void eliminarProducto() {
        try {
            String codigo = txtCodigoProducto.getText().trim();

            if (codigo.isEmpty()) {
                throw new Exception("Debe indicar o seleccionar un código de producto.");
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Desea eliminar el producto con código " + codigo + "?");

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                productoService.eliminar(codigo);
                mostrarInfo("Producto", "Producto eliminado correctamente.");
                cargarProductos();
                limpiarFormulario();
            }

        } catch (Exception e) {
            mostrarError("Eliminar producto", e.getMessage());
        }
    }

    @FXML
    public void cargarProductos() {
        try {
            List<Producto> lista = productoService.listarProductos();
            tblProductos.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            mostrarError("Listar productos", e.getMessage());
        }
    }

    @FXML
    public void crearCategoria() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nueva categoría");
            dialog.setHeaderText(null);
            dialog.setContentText("Digite el nombre de la categoría:");

            dialog.showAndWait().ifPresent(nombre -> {
                try {
                    if (nombre.trim().isEmpty()) {
                        throw new Exception("El nombre de la categoría es obligatorio.");
                    }

                    productoService.crearCategoria(nombre.trim());
                    cargarCategorias();
                    mostrarInfo("Categoría", "Categoría creada correctamente.");
                } catch (Exception e) {
                    mostrarError("Crear categoría", e.getMessage());
                }
            });

        } catch (Exception e) {
            mostrarError("Crear categoría", e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtPrecio.clear();
        txtCantidadDisponible.clear();
        txtDescripcionNombre.clear();

        cmbCategoria.getSelectionModel().clearSelection();
        cmbMaterial.getSelectionModel().clearSelection();
        cmbIva.setValue("13");

        lblImpuestoCalculado.setText("0.00");
        lblTotalCalculado.setText("0.00");

        tblProductos.getSelectionModel().clearSelection();
    }

    @FXML
    public void volver() {
        Stage stage = (Stage) txtCodigoProducto.getScene().getWindow();
        stage.close();
    }

    private Producto construirProductoDesdeFormulario() throws Exception {
        String codigo = txtCodigoProducto.getText().trim();
        String nombre = txtNombreProducto.getText().trim();
        String descripcion = txtDescripcionNombre.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String cantidadTexto = txtCantidadDisponible.getText().trim();
        String ivaTexto = cmbIva.getValue();

        Categoria categoria = cmbCategoria.getValue();
        Material material = cmbMaterial.getValue();

        if (codigo.isEmpty()) {
            throw new Exception("Debe indicar el código del producto.");
        }
        if (nombre.isEmpty()) {
            throw new Exception("Debe indicar el nombre del producto.");
        }
        if (descripcion.isEmpty()) {
            throw new Exception("Debe indicar la descripción.");
        }
        if (precioTexto.isEmpty()) {
            throw new Exception("Debe indicar el precio.");
        }
        if (cantidadTexto.isEmpty()) {
            throw new Exception("Debe indicar la cantidad.");
        }
        if (categoria == null) {
            throw new Exception("Debe seleccionar la categoría.");
        }
        if (material == null) {
            throw new Exception("Debe seleccionar el material.");
        }

        double precio = Double.parseDouble(precioTexto);
        double cantidad = Double.parseDouble(cantidadTexto);
        double iva = Double.parseDouble(ivaTexto);

        Producto producto = new Producto();
        producto.setCodigoProducto(codigo);
        producto.setNombre(nombre);
        producto.setDescripcionNombre(descripcion);
        producto.setPrecio(precio);
        producto.setCantidadDisponible(cantidad);
        producto.setPorcentajeIva(iva);
        producto.setIdCategoria(categoria.getIdCategoria());
        producto.setIdMaterial(material.getIdMaterial());

        return producto;
    }

    private void cargarFormulario(Producto producto) {
        txtCodigoProducto.setText(producto.getCodigoProducto());
        txtNombreProducto.setText(producto.getNombre());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        txtCantidadDisponible.setText(String.valueOf(producto.getCantidadDisponible()));
        txtDescripcionNombre.setText(producto.getDescripcionNombre());
        cmbIva.setValue(formatearIva(producto.getPorcentajeIva()));

        seleccionarCategoria(producto.getIdCategoria());
        seleccionarMaterial(producto.getIdMaterial());

        recalcularMontos();
    }

    private void cargarCategorias() {
        try {
            List<Categoria> lista = productoService.listarCategorias();
            cmbCategoria.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            mostrarError("Categorías", "No se pudieron cargar las categorías: " + e.getMessage());
        }
    }

    private void cargarMateriales() {
        try {
            List<Material> lista = productoService.listarMateriales();
            cmbMaterial.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            mostrarError("Materiales", "No se pudieron cargar los materiales: " + e.getMessage());
        }
    }

    private void seleccionarCategoria(int idCategoria) {
        for (Categoria categoria : cmbCategoria.getItems()) {
            if (categoria.getIdCategoria() == idCategoria) {
                cmbCategoria.setValue(categoria);
                break;
            }
        }
    }

    private void seleccionarMaterial(int idMaterial) {
        for (Material material : cmbMaterial.getItems()) {
            if (material.getIdMaterial() == idMaterial) {
                cmbMaterial.setValue(material);
                break;
            }
        }
    }

    private void recalcularMontos() {
        try {
            double precio = txtPrecio.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtPrecio.getText().trim());
            double cantidad = txtCantidadDisponible.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtCantidadDisponible.getText().trim());
            double iva = cmbIva.getValue() == null ? 0 : Double.parseDouble(cmbIva.getValue());

            double impuesto = Math.round((precio * cantidad * (iva / 100.0)) * 100.0) / 100.0;
            double total = Math.round(((precio * cantidad) + impuesto) * 100.0) / 100.0;

            lblImpuestoCalculado.setText(String.format("%.2f", impuesto));
            lblTotalCalculado.setText(String.format("%.2f", total));
        } catch (Exception e) {
            lblImpuestoCalculado.setText("0.00");
            lblTotalCalculado.setText("0.00");
        }
    }

    private String formatearIva(double iva) {
        if (iva == 0) return "0";
        if (iva == 2) return "2";
        return "13";
    }

    private void limpiarTabla() {
        tblProductos.setItems(FXCollections.observableArrayList());
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
