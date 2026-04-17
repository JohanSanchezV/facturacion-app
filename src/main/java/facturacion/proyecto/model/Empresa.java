package facturacion.proyecto.model;

public class Empresa {
    private int idEmisor;
    private String nombreLegal;

    public int getIdEmisor() { return idEmisor; }
    public void setIdEmisor(int idEmisor) { this.idEmisor = idEmisor; }

    public String getNombreLegal() { return nombreLegal; }
    public void setNombreLegal(String nombreLegal) { this.nombreLegal = nombreLegal; }

    @Override
    public String toString() {
        return nombreLegal;
    }
}
