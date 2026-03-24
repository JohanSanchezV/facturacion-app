package facturacion.proyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/facturacion/proyecto/principal-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("Sistema de Facturación");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}