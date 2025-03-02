package com.ficheros;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrimaryController {
    private static ArrayList<Dispositivo> listaDispositivos = new ArrayList<>();

    @FXML
    private TextField txtID;

    @FXML
    private Button btnAñadir, btnMostrar, btnBuscar, btnEliminar, btnModificar, btnEstado, btnSalir;

    @FXML
    private TextArea txtSalida;

    // 📌 Método para obtener la ruta del archivo de manera segura
    private File obtenerArchivo() {
        try {
            URL url = getClass().getResource("/com/ficheros/dispositivos.dat");
            if (url == null) {
                System.out.println("Error: El archivo dispositivos.dat no fue encontrado en resources.");
                return null;
            }
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            System.out.println("Error de sintaxis en la URI del archivo: " + e.getMessage());
            return null;
        }
    }

    public void cargarDatos() {
        listaDispositivos.clear();
        File archivo = new File("dispositivos.dat"); // 📌 Accedemos directamente al archivo
    
        if (!archivo.exists()) {
            txtSalida.setText("No hay dispositivos guardados.");
            return;
        }
    
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int id = raf.readInt();
                String marca = raf.readUTF().trim();
                String modelo = raf.readUTF().trim();
                boolean estado = raf.readBoolean();
                int tipo = raf.readInt();
                boolean borrado = raf.readBoolean(); // 📌 Aseguramos que lo leemos correctamente
                int idAjeno = raf.readInt();
    
                if (!borrado) { // 📌 Solo agregamos si no está marcado como borrado
                    Dispositivo d;
                    if (tipo == 1) {
                        d = new Ordenador(id);
                        d.load(id);
                    } else if (tipo == 2) {
                        d = new Impresora(id);
                        d.load(id);
                    } else {
                        d = new Dispositivo(id);
                    }
    
                    d.setMarca(marca);
                    d.setModelo(modelo);
                    d.setEstado(estado);
                    d.setIdAjeno(idAjeno);
    
                    listaDispositivos.add(d);
                }
            }
    
            System.out.println("✔ Dispositivos visibles en la lista: " + listaDispositivos.size()); // 🔍 DEBUG
    
        } catch (IOException e) {
            System.out.println("❌ Error al cargar dispositivos: " + e.getMessage());
            txtSalida.setText("Error al cargar dispositivos.");
        }
    }
    
    

    @FXML
    private void añadirDispositivo() {
        try {
            URL fxmlLocation = getClass().getResource("/com/ficheros/añadir.fxml");
            if (fxmlLocation == null) {
                System.out.println("Error: No se encontró añadir.fxml en la carpeta resources/com/ficheros/");
                return;
            }
    
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
    
            Stage stage = new Stage();
            stage.setTitle("Añadir Nuevo Dispositivo");
            App.setAppIcon(stage);
            stage.setScene(new Scene(root));
    
            // 🔥 Al cerrar la ventana, recargar la lista de dispositivos sí o sí
            stage.setOnHiding(event -> {
                cargarDatos();
                mostrarDispositivos();
            });
    
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            txtSalida.setText("Error al abrir la ventana de añadir dispositivo.");
        }
    }
    
    
    

    @FXML
    public void mostrarDispositivos() {
        txtSalida.clear();
        cargarDatos();

        if (listaDispositivos.isEmpty()) {
            txtSalida.setText("No hay dispositivos guardados.");
        } else {
            StringBuilder salida = new StringBuilder();
            for (Dispositivo d : listaDispositivos) {
                salida.append(d.toString()).append("\n\n");
            }
            txtSalida.setText(salida.toString());
        }
    }

    @FXML
    private void buscarDispositivo() {
        String idTexto = txtID.getText().trim();
    
        // Validamos que el ID sea un número
        if (idTexto.isEmpty() || !idTexto.matches("\\d+")) {
            txtSalida.setText("Error: Ingrese un ID válido (número entero).");
            return;
        }
    
        int idBuscado = Integer.parseInt(idTexto);
    
        // Buscamos en la lista cargada de dispositivos
        for (Dispositivo d : listaDispositivos) {
            if (d.getId() == idBuscado) {
                txtSalida.setText("Dispositivo encontrado:\n" + d);
                return;
            }
        }
    
        // Si llegamos aquí, el dispositivo no fue encontrado
        txtSalida.setText("No se encontró ningún dispositivo con ID " + idBuscado);
    }
    

    @FXML
    private void eliminarDispositivo() {
        String idTexto = txtID.getText().trim();
    
        if (idTexto.isEmpty() || !idTexto.matches("\\d+")) {
            txtSalida.setText("Error: Ingrese un ID válido (número entero).");
            return;
        }
    
        int idBuscado = Integer.parseInt(idTexto);
        File archivo = new File("dispositivos.dat");
    
        if (!archivo.exists()) {
            txtSalida.setText("No hay dispositivos registrados.");
            return;
        }
    
        boolean encontrado = false;
    
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long posicion = raf.getFilePointer(); // 📌 Guardamos la posición inicial
                int idLeido = raf.readInt();
                raf.readUTF(); // Marca
                raf.readUTF(); // Modelo
                raf.readBoolean(); // Estado
                raf.readInt(); // Tipo
                long posBorrado = raf.getFilePointer(); // 📌 Posición del campo "borrado"
                boolean borrado = raf.readBoolean();
                raf.readInt(); // idAjeno
    
                if (idLeido == idBuscado && !borrado) {
                    raf.seek(posBorrado); // 📌 Nos posicionamos en el campo "borrado"
                    raf.writeBoolean(true); // Marcamos como eliminado
    
                    encontrado = true;
                    System.out.println("✔ Dispositivo con ID " + idBuscado + " eliminado correctamente."); // 🔍 DEBUG
                    txtSalida.setText("Dispositivo con ID " + idBuscado + " eliminado correctamente.");
                    break;
                }
            }
        } catch (IOException e) {
            txtSalida.setText("Error al eliminar dispositivo: " + e.getMessage());
        }
    
        if (!encontrado) {
            txtSalida.setText("No se encontró ningún dispositivo con ID " + idBuscado);
        }
    
        cargarDatos(); // 🔥 Aseguramos que la lista se actualiza después de eliminar
    }
    


    @FXML
    private void modificarDispositivo() {
        String idTexto = txtID.getText().trim();
    
        if (idTexto.isEmpty() || !idTexto.matches("\\d+")) {
            txtSalida.setText("Error: Ingrese un ID válido (número entero).");
            return;
        }
    
        int idBuscado = Integer.parseInt(idTexto);
    
        for (Dispositivo d : listaDispositivos) {
            if (d.getId() == idBuscado) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ficheros/modificar.fxml"));
                    Parent root = loader.load();
    
                    // 📌 Obtener el controlador de la nueva ventana
                    ModificarController controlador = loader.getController();
                    controlador.setDatos(d.getId(), d.getMarca(), d.getModelo(), this); // 🔥 Pasamos 'this'
    
                    Stage stage = new Stage();
                    stage.setTitle("Modificar Dispositivo");
                    App.setAppIcon(stage);
                    stage.setScene(new Scene(root));
    
                    // 🔥 Recargar datos tras cerrar la ventana
                    stage.setOnHiding(event -> {
                        cargarDatos();
                        mostrarDispositivos();
                    });
    
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    txtSalida.setText("Error al abrir la ventana de modificación.");
                }
                return;
            }
        }
    
        txtSalida.setText("No se encontró ningún dispositivo con ID " + idBuscado);
    }
    
    




@FXML
private void cambiarEstado() {
    String idTexto = txtID.getText().trim();

    if (idTexto.isEmpty() || !idTexto.matches("\\d+")) {
        txtSalida.setText("Error: Ingrese un ID válido (número entero).");
        return;
    }

    int idBuscado = Integer.parseInt(idTexto);
    File archivo = new File("dispositivos.dat");

    if (!archivo.exists()) {
        txtSalida.setText("No hay dispositivos registrados.");
        return;
    }

    boolean encontrado = false;

    try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
        while (raf.getFilePointer() < raf.length()) {
            long posicion = raf.getFilePointer(); // 📌 Guardamos la posición inicial
            int idLeido = raf.readInt();
            String marca = raf.readUTF().trim();
            String modelo = raf.readUTF().trim();
            long posEstado = raf.getFilePointer(); // 📌 Guardamos la posición del campo "estado"
            boolean estado = raf.readBoolean(); // Leemos el estado actual
            int tipo = raf.readInt();
            boolean borrado = raf.readBoolean();
            int idAjeno = raf.readInt();

            if (idLeido == idBuscado && !borrado) {
                // 📌 Invertimos el estado
                raf.seek(posEstado);
                boolean nuevoEstado = !estado;
                raf.writeBoolean(nuevoEstado);

                encontrado = true;

                System.out.println("✔ Estado del dispositivo con ID " + idBuscado + " cambiado a: " + (nuevoEstado ? "Funciona" : "No funciona")); // 🔍 DEBUG
                txtSalida.setText("Estado del dispositivo con ID " + idBuscado + " cambiado a: " + (nuevoEstado ? "Funciona" : "No funciona"));

                break;
            }
        }
    } catch (IOException e) {
        txtSalida.setText("Error al modificar estado: " + e.getMessage());
    }

    if (!encontrado) {
        txtSalida.setText("No se encontró ningún dispositivo con ID " + idBuscado);
    }

    // 🔥 Asegurar que los datos se recargan después de cambiar el estado
    cargarDatos();
    mostrarDispositivos();
}

    

    @FXML
    private void cerrarVentana() {
        System.exit(0); // 🔥 Cierra completamente la aplicación
    }
    
    }
    

