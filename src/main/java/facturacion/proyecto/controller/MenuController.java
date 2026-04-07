package facturacion.proyecto.controller;

import facturacion.proyecto.App;
import facturacion.proyecto.model.SesionUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Label lblBienvenida;

    private SesionUsuario sesionUsuario;

    public void setSesionUsuario(SesionUsuario sesionUsuario) {
        this.sesionUsuario = sesionUsuario;
        lblBienvenida.setText("Bienvenido: " + sesionUsuario.getNombreUsuario()
                + " | Rol: " + sesionUsuario.getNombreRol());
    }

    @FXML
    public void abrirClientes(ActionEvent event) {
        abrirVentana("/facturacion/proyecto/clientes-view.fxml", "Clientes");
    }

    @FXML
    public void abrirProductos(ActionEvent event) {
        abrirVentana("/facturacion/proyecto/productos-view.fxml", "Productos");
    }

    @FXML
    public void abrirFacturacion(ActionEvent event) {
    abrirVentana("/facturacion/proyecto/facturacion-view.fxml", "Facturación");
    }

    @FXML
    public void salir(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/facturacion/proyecto/login-view.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) lblBienvenida.getScene().getWindow();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo volver al login: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void abrirVentana(String rutaFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(rutaFxml));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo abrir la ventana: " + e.getMessage());
            alert.showAndWait();
        }
    }
}