package com.ficheros;

import java.io.*;

public class Impresora extends Dispositivo {
    private int tipo; // 0 = láser, 1 = inyección de tinta, 2 = otros
    private boolean color;
    private boolean tieneScanner;

    // Constructor para crear una nueva impresora
    public Impresora(String marca, String modelo, boolean estado, int tipo, boolean color, boolean tieneScanner) {
        super(marca, modelo, estado, 2, obtenerNuevoId()); // Tipo 2 = Impresora
        this.tipo = tipo;
        this.color = color;
        this.tieneScanner = tieneScanner;
    }

    // Constructor para cargar una impresora por ID
    public Impresora(int id) {
        super(id);
        this.tipo = 0;
        this.color = false;
        this.tieneScanner = false;
    }

    // Getters y Setters
    public int getTipo() { return tipo; }
    public void setTipo(int tipo) { this.tipo = tipo; }
    public boolean isColor() { return color; }
    public void setColor(boolean color) { this.color = color; }
    public boolean hasScanner() { return tieneScanner; }
    public void setScanner(boolean scanner) { this.tieneScanner = scanner; }

    // toString sobreescrito
    @Override
    public String toString() {
        return "ID IMPRESORA: " + getId() + "\n" + super.toString() + "\nTipo: " + (tipo == 0 ? "Láser" : tipo == 1 ? "Inyección de tinta" : "Otros") +
               "\nColor: " + (color ? "Sí" : "No") + "\nScanner incluido: " + (tieneScanner ? "Sí" : "No");
    }
    
    // Método para guardar la impresora en el archivo
    @Override
    public int save() {
        if (super.save() == 1) return 1;
        try (RandomAccessFile raf = new RandomAccessFile("impresoras.dat", "rw")) {
            raf.seek(raf.length());
            raf.writeInt(getIdAjeno());
            raf.writeInt(tipo);
            raf.writeBoolean(color);
            raf.writeBoolean(tieneScanner);
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }

    // Método para cargar una impresora desde el archivo
    @Override
    public int load(int idBuscado) {
        if (super.load(idBuscado) == 1) return 1;
        try (RandomAccessFile raf = new RandomAccessFile("impresoras.dat", "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int idLeido = raf.readInt();
                int tipoLeido = raf.readInt();
                boolean colorLeido = raf.readBoolean();
                boolean scannerLeido = raf.readBoolean();

                if (idLeido == this.getIdAjeno()) {
                    this.tipo = tipoLeido;
                    this.color = colorLeido;
                    this.tieneScanner = scannerLeido;
                    return 0;
                }
            }
        } catch (IOException e) {
            return 1;
        }
        return 1;
    }
}
