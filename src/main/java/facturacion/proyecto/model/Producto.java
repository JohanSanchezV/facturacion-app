package facturacion.proyecto.model;

public class Producto {
    private int idProducto;
    private String nombre;
    private double precio;
    private int idDescripcion;
    private int idCategoria;
    private int idMaterial;
    private String descripcionNombre;
    private String categoriaNombre;
    private String materialNombre;
    private int idEstado;

    public Producto() {
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdDescripcion() {
        return idDescripcion;
    }

    public void setIdDescripcion(int idDescripcion) {
        this.idDescripcion = idDescripcion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getDescripcionNombre() {
        return descripcionNombre;
    }

    public void setDescripcionNombre(String descripcionNombre) {
        this.descripcionNombre = descripcionNombre;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public String getMaterialNombre() {
        return materialNombre;
    }

    public void setMaterialNombre(String materialNombre) {
        this.materialNombre = materialNombre;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
}