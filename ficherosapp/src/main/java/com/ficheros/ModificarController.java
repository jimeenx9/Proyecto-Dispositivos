package com.ficheros;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class ModificarController {

    @FXML
    private TextField txtID, txtMarca, txtModelo;

    private int idDispositivo; // ID del dispositivo a modificar
    private PrimaryController controladorPrincipal;

    // Método para recibir los datos desde PrimaryController
    public void setDatos(int id, String marca, String modelo, PrimaryController controlador) {
        this.idDispositivo = id;
        this.controladorPrincipal = controlador; // 🔥 Guardamos la referencia al controlador principal

        txtID.setText(String.valueOf(id)); 
        txtID.setEditable(false);
        txtMarca.setText(marca);
        txtModelo.setText(modelo);
    }

    @FXML
    private void guardarCambios() {
        File archivo = new File("dispositivos.dat");

        if (!archivo.exists()) {
            System.out.println("No se encontró el archivo de dispositivos.");
            return;
        }

        boolean encontrado = false;
        String nuevaMarca = txtMarca.getText().trim();
        String nuevoModelo = txtModelo.getText().trim();

        // Validación: No se permite marca o modelo vacíos
        if (nuevaMarca.isEmpty() || nuevoModelo.isEmpty()) {
            System.out.println("Error: La marca y el modelo no pueden estar vacíos.");
            return;
        }

        // 📌 Ajustamos el tamaño de la cadena para mantener el mismo formato
        nuevaMarca = String.format("%-20s", nuevaMarca).substring(0, 20);
        nuevoModelo = String.format("%-20s", nuevoModelo).substring(0, 20);

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long posicion = raf.getFilePointer();
                int idLeido = raf.readInt();
                long posMarca = raf.getFilePointer();
                raf.readUTF(); // Saltamos la marca actual
                long posModelo = raf.getFilePointer();
                raf.readUTF(); // Saltamos el modelo actual
                raf.readBoolean(); // Estado
                raf.readInt(); // Tipo
                raf.readBoolean(); // Borrado
                raf.readInt(); // ID Ajeno

                if (idLeido == idDispositivo) {
                    // 📌 Sobrescribimos la nueva marca y modelo en la posición correcta
                    raf.seek(posMarca);
                    raf.writeUTF(nuevaMarca);

                    raf.seek(posModelo);
                    raf.writeUTF(nuevoModelo);

                    encontrado = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al modificar el dispositivo: " + e.getMessage());
        }

        if (encontrado) {
            System.out.println("✔ Dispositivo actualizado correctamente.");
            cerrarVentana();
        } else {
            System.out.println("❌ No se encontró el dispositivo.");
        }
    }

    // 📌 Método para agregar el ícono a la ventana
    private void setIcono(Stage stage) {
        try {
            URL iconUrl = getClass().getResource("/com/ficheros/icono.png");
            if (iconUrl != null) {
                stage.getIcons().add(new Image(iconUrl.toString()));
            } else {
                System.out.println("⚠ No se encontró el icono en la ruta especificada.");
            }
        } catch (Exception e) {
            System.out.println("⚠ Error al cargar el ícono: " + e.getMessage());
        }
    }

    // Método para cerrar la ventana y actualizar la UI principal
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtID.getScene().getWindow();

        // 🔥 Agregar ícono a la ventana antes de cerrarla
        setIcono(stage);

        stage.close();

        // 🔥 Asegurar que la lista se actualiza en la interfaz principal
        if (controladorPrincipal != null) {
            controladorPrincipal.cargarDatos();
            controladorPrincipal.mostrarDispositivos();
        }
    }
}
