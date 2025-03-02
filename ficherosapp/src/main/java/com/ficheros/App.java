package com.ficheros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 850, 550);
        stage.setTitle("Gestor de Dispositivos");

        // 📌 🔥 Aplica el ícono a la ventana principal
        setAppIcon(stage);

        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    // 📌 🔥 Método para establecer el ícono en cualquier ventana
    public static void setAppIcon(Stage stage) {
        try {
            URL iconUrl = App.class.getResource("/com/ficheros/icono.png");
            if (iconUrl != null) {
                stage.getIcons().add(new Image(iconUrl.toString()));
            } else {
                System.out.println("⚠ No se encontró el icono en la ruta especificada.");
            }
        } catch (Exception e) {
            System.out.println("⚠ Error al cargar el ícono: " + e.getMessage());
        }
    }
}
