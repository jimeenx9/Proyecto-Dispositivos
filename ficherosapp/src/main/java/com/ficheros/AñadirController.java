package com.ficheros;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class AñadirController {

    @FXML
    private ChoiceBox<String> choiceTipo;
    @FXML
    private ChoiceBox<Integer> choiceTipoImpresora;
    @FXML
    private CheckBox checkColor, checkScanner;
    @FXML
    private TextField txtMarca, txtModelo, txtRam, txtProcesador, txtDisco, txtTipoDisco;
    @FXML
    private Button btnGuardar, btnCancelar;

    @FXML
    private void initialize() {
        choiceTipo.getItems().addAll("Ordenador", "Impresora");
        choiceTipo.setOnAction(event -> actualizarCampos());

        choiceTipoImpresora.getItems().addAll(0, 1); // 0 = Láser, 1 = Inyección de tinta
    }

    private void actualizarCampos() {
        boolean esOrdenador = "Ordenador".equals(choiceTipo.getValue());
        txtRam.setDisable(!esOrdenador);
        txtProcesador.setDisable(!esOrdenador);
        txtDisco.setDisable(!esOrdenador);
        txtTipoDisco.setDisable(!esOrdenador);
        
        boolean esImpresora = "Impresora".equals(choiceTipo.getValue());
        choiceTipoImpresora.setDisable(!esImpresora);
        checkColor.setDisable(!esImpresora);
        checkScanner.setDisable(!esImpresora);
    }
    
    @FXML
    private void guardarDispositivo() {
        String tipoSeleccionado = choiceTipo.getValue();
        String marca = txtMarca.getText().trim();
        String modelo = txtModelo.getText().trim();
    
        if (marca.isEmpty() || modelo.isEmpty() || tipoSeleccionado == null) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }
    
        boolean guardado = false; // Variable para comprobar si el guardado fue exitoso
    
        try {
            if ("Ordenador".equals(tipoSeleccionado)) {
                int ram = Integer.parseInt(txtRam.getText().trim());
                String procesador = txtProcesador.getText().trim();
                int tamDisco = Integer.parseInt(txtDisco.getText().trim());
                int tipoDisco = Integer.parseInt(txtTipoDisco.getText().trim());
    
                Ordenador nuevoOrdenador = new Ordenador(marca, modelo, true, ram, procesador, tamDisco, tipoDisco);
                guardado = (nuevoOrdenador.save() == 0); // Comprobamos si se guardó correctamente
            } else if ("Impresora".equals(tipoSeleccionado)) {
                int tipo = choiceTipoImpresora.getValue();
                boolean color = checkColor.isSelected();
                boolean scanner = checkScanner.isSelected();
    
                Impresora nuevaImpresora = new Impresora(marca, modelo, true, tipo, color, scanner);
                guardado = (nuevaImpresora.save() == 0); // Comprobamos si se guardó correctamente
            }
        } catch (NumberFormatException e) {
            mostrarError("Error: Asegúrate de ingresar valores numéricos en los campos correspondientes.");
            return;
        }
    
        if (!guardado) {
            mostrarError("Error al guardar el dispositivo.");
            return;
        }
    
        cerrarVentana();
    }
    
    

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
