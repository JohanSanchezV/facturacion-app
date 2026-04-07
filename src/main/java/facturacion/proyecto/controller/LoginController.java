package facturacion.proyecto.controller;

import facturacion.proyecto.App;
import facturacion.proyecto.model.SesionUsuario;
import facturacion.proyecto.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    private final AuthService authService = new AuthService();

    @FXML
    public void iniciarSesion(ActionEvent event) {
        try {
            String correo = txtCorreo.getText().trim();
            String password = txtPassword.getText().trim();

            if (correo.isEmpty() || password.isEmpty()) {
                lblMensaje.setText("Debe completar correo y contraseña.");
                return;
            }

            SesionUsuario sesion = authService.login(correo, password);

            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/facturacion/proyecto/menu-view.fxml"));
            Scene scene = new Scene(loader.load());

            MenuController controller = loader.getController();
            controller.setSesionUsuario(sesion);

            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            stage.setTitle("Menú Principal");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            lblMensaje.setText(e.getMessage());
        }
    }

    @FXML
    public void limpiarCampos(ActionEvent event) {
        txtCorreo.clear();
        txtPassword.clear();
        lblMensaje.setText("");
    }
}