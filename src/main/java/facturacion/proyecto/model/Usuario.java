package facturacion.proyecto.model;

public class Usuario {
    private int idUsuarioApp;
    private int idTercero;
    private int idEmisor;
    private String passHash;
    private int idRol;
    private int idEstado;

    private String numeroIdentificacion;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String nombreLegal;
    private String nombreRol;
    private String nombreEstado;

    public int getIdUsuarioApp() { return idUsuarioApp; }
    public void setIdUsuarioApp(int idUsuarioApp) { this.idUsuarioApp = idUsuarioApp; }

    public int getIdTercero() { return idTercero; }
    public void setIdTercero(int idTercero) { this.idTercero = idTercero; }

    public int getIdEmisor() { return idEmisor; }
    public void setIdEmisor(int idEmisor) { this.idEmisor = idEmisor; }

    public String getPassHash() { return passHash; }
    public void setPassHash(String passHash) { this.passHash = passHash; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public int getIdEstado() { return idEstado; }
    public void setIdEstado(int idEstado) { this.idEstado = idEstado; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNombreLegal() { return nombreLegal; }
    public void setNombreLegal(String nombreLegal) { this.nombreLegal = nombreLegal; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }

    public String getNombreCompleto() {
        String nom = nombre == null ? "" : nombre;
        String ap1 = apellidoPaterno == null ? "" : apellidoPaterno;
        String ap2 = apellidoMaterno == null ? "" : apellidoMaterno;
        return (nom + " " + ap1 + " " + ap2).trim().replaceAll("\\s+", " ");
    }
}
