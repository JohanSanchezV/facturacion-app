package facturacion.proyecto.model;

public class SesionUsuario {
    private int idUsuarioApp;
    private String nombreUsuario;
    private String nombreRol;

    public SesionUsuario() {
    }

    public SesionUsuario(int idUsuarioApp, String nombreUsuario, String nombreRol) {
        this.idUsuarioApp = idUsuarioApp;
        this.nombreUsuario = nombreUsuario;
        this.nombreRol = nombreRol;
    }

    public int getIdUsuarioApp() {
        return idUsuarioApp;
    }

    public void setIdUsuarioApp(int idUsuarioApp) {
        this.idUsuarioApp = idUsuarioApp;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}