package com.ficheros;

import javafx.fxml.FXML; // Importa las anotaciones FXML para enlazar con Scene Builder
import javafx.scene.control.Button; // Importa la clase Button para manejar los botones
import javafx.scene.control.TextArea; // Importa la clase TextArea para mostrar información
import javafx.scene.control.TextField; // Importa la clase TextField para escribir el ID
import javafx.stage.Stage; // Importa la clase Stage para cerrar la ventana

public class PrimaryController {

    // 📌 ENLAZAMOS LOS ELEMENTOS DE SCENE BUILDER CON JAVA
    @FXML
    private TextField txtID; // Campo de texto donde el usuario escribe el ID del dispositivo

    @FXML
    private Button btnAñadir, btnMostrar, btnBuscar, btnEliminar, btnModificar, btnEstado, btnSalir;
    // 📌 Botones de la interfaz

    @FXML
    private TextArea txtSalida; // Área de texto donde se mostrará la información

    // 📌 MÉTODO QUE SE EJECUTA AUTOMÁTICAMENTE AL INICIAR LA VENTANA
    @FXML
    public void initialize() {
        // Cuando el usuario haga clic en un botón, llamamos a su respectivo método
        btnAñadir.setOnAction(event -> añadirDispositivo());
        btnMostrar.setOnAction(event -> mostrarDispositivos());
        btnBuscar.setOnAction(event -> buscarDispositivo());
        btnEliminar.setOnAction(event -> eliminarDispositivo());
        btnModificar.setOnAction(event -> modificarDispositivo());
        btnEstado.setOnAction(event -> cambiarEstado());
        btnSalir.setOnAction(event -> cerrarVentana());
    }

    // 📌 MÉTODO PARA AÑADIR UN DISPOSITIVO
    private void añadirDispositivo() {
        txtSalida.setText("Añadir Dispositivo (Falta Implementación)");
    }

    // 📌 MÉTODO PARA MOSTRAR TODOS LOS DISPOSITIVOS
    private void mostrarDispositivos() {
        txtSalida.setText("Mostrar Dispositivos (Falta Implementación)");
    }

    // 📌 MÉTODO PARA BUSCAR UN DISPOSITIVO POR ID
    private void buscarDispositivo() {
        String id = txtID.getText(); // Obtiene el ID escrito por el usuario
        txtSalida.setText("Buscar Dispositivo con ID: " + id + " (Falta Implementación)");
    }

    // 📌 MÉTODO PARA ELIMINAR UN DISPOSITIVO POR ID
    private void eliminarDispositivo() {
        String id = txtID.getText();
        txtSalida.setText("Eliminar Dispositivo con ID: " + id + " (Falta Implementación)");
    }

    // 📌 MÉTODO PARA MODIFICAR UN DISPOSITIVO POR ID
    private void modificarDispositivo() {
        String id = txtID.getText();
        txtSalida.setText("Modificar Dispositivo con ID: " + id + " (Falta Implementación)");
    }

    // 📌 MÉTODO PARA CAMBIAR EL ESTADO DE UN DISPOSITIVO
    private void cambiarEstado() {
        String id = txtID.getText();
        txtSalida.setText("Cambiar Estado de Dispositivo con ID: " + id + " (Falta Implementación)");
    }

    // 📌 MÉTODO PARA CERRAR LA APLICACIÓN
    private void cerrarVentana() {
        Stage stage = (Stage) btnSalir.getScene().getWindow(); // Obtiene la ventana actual
        stage.close(); // La cierra
    }
}
