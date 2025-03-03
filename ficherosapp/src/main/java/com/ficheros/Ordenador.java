package com.ficheros;

import java.io.*;

public class Ordenador extends Dispositivo {
    private int ram;
    private String procesador;
    private int tamDisco;
    private int tipoDisco; // 0 = mecánico, 1 = SSD, 2 = NVMe, 3 = otros

    // Constructor para crear un nuevo ordenador
    public Ordenador(String marca, String modelo, boolean estado, int ram, String procesador, int tamDisco, int tipoDisco) {
        super(marca, modelo, estado, 1, obtenerNuevoId()); // Tipo 1 = Ordenador
        this.ram = ram;
        this.procesador = procesador;
        this.tamDisco = tamDisco;
        this.tipoDisco = tipoDisco;
    }

    // Constructor para cargar un ordenador por ID
    public Ordenador(int id) {
        super(id);
        this.ram = 0;
        this.procesador = " ";
        this.tamDisco = 0;
        this.tipoDisco = 0;
    }

    // Getters y Setters
    public int getRam() { return ram; }
    public void setRam(int ram) { this.ram = ram; }
    public String getProcesador() { return procesador; }
    public void setProcesador(String procesador) { this.procesador = procesador; }
    public int getTamDisco() { return tamDisco; }
    public void setTamDisco(int tamDisco) { this.tamDisco = tamDisco; }
    public int getTipoDisco() { return tipoDisco; }
    public void setTipoDisco(int tipoDisco) { this.tipoDisco = tipoDisco; }

    // toString sobreescrito
    @Override
    public String toString() {
        return "ID ORDENADOR: " + getId() + "\n" + super.toString() + "\nProcesador: " + procesador + "\nMemoria RAM: " + ram + " GB\nAlmacenamiento: " +
               (tipoDisco == 0 ? "HDD " : tipoDisco == 1 ? "SSD " : tipoDisco == 2 ? "NVMe " : "Otros ") + tamDisco + " GB";
    }

    // Método para guardar el ordenador en el archivo
    @Override
    public int save() {
        if (super.save() == 1) return 1;
        try (RandomAccessFile raf = new RandomAccessFile("ordenadores.dat", "rw")) {
            raf.seek(raf.length());
            raf.writeInt(getIdAjeno());
            raf.writeUTF(String.format("%-20s", procesador));
            raf.writeInt(ram);
            raf.writeInt(tamDisco);
            raf.writeInt(tipoDisco);
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }

    // Método para cargar un ordenador desde el archivo
    @Override
    public int load(int idBuscado) {
        if (super.load(idBuscado) == 1) return 1;
        try (RandomAccessFile raf = new RandomAccessFile("ordenadores.dat", "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int idLeido = raf.readInt();
                String procesadorLeido = raf.readUTF().trim();
                int ramLeida = raf.readInt();
                int tamDiscoLeido = raf.readInt();
                int tipoDiscoLeido = raf.readInt();

                if (idLeido == this.getIdAjeno()) {
                    this.procesador = procesadorLeido;
                    this.ram = ramLeida;
                    this.tamDisco = tamDiscoLeido;
                    this.tipoDisco = tipoDiscoLeido;
                    return 0;
                }
            }
        } catch (IOException e) {
            return 1;
        }
        return 1;
    }
}
