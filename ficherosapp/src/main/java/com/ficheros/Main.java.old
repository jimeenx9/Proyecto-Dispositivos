import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Dispositivo> listaDispositivos = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        cargarDatos();
        menúPrincipal();
    }

    private static void menúPrincipal() {
        int opcion;
        do {
            System.out.println("\n     MENÚ PRINCIPAL");
            System.out.println("1. Añadir dispositivo");
            System.out.println("2. Mostrar dispositivos");
            System.out.println("3. Buscar dispositivo");
            System.out.println("4. Borrar dispositivo");
            System.out.println("5. Cambiar estado dispositivo");
            System.out.println("6. Modificar dispositivo");
            System.out.println("0. Salir");
            System.out.println();
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: añadirDispositivo(); break;
                case 2: mostrarDispositivos(); break;
                case 3: buscarDispositivo(); break;
                case 4: borrarDispositivo(); break;
                case 5: cambiarEstadoDispositivo(); break;
                case 6: modificarDispositivo(); break;
                case 0: System.out.println("Saliendo..."); break;
                default: System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

private static void cargarDatos() {
    listaDispositivos.clear(); // Limpiar la lista antes de cargar

    File archivo = new File("dispositivos.dat");
    if (!archivo.exists()) {
        System.out.println("No hay dispositivos guardados.");
        return;
    }

    try (RandomAccessFile raf = new RandomAccessFile("dispositivos.dat", "r")) {
        while (raf.getFilePointer() < raf.length()) {
            int id = raf.readInt();
            String marca = raf.readUTF().trim();
            String modelo = raf.readUTF().trim();
            boolean estado = raf.readBoolean();
            int tipo = raf.readInt();
            boolean borrado = raf.readBoolean();
            int idAjeno = raf.readInt();

            if (!borrado) { // Solo cargamos dispositivos que no han sido eliminados
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
    } catch (IOException e) {
        System.out.println("Error al cargar dispositivos.");
    }
}

    

    private static void añadirDispositivo() {
        System.out.print("Ingrese tipo (1=Ordenador, 2=Impresora): ");
        int tipo = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        
        System.out.print("Estado (true=funciona, false=no funciona): ");
        boolean estado = Boolean.parseBoolean(scanner.nextLine());
        
        Dispositivo dispositivo;
        if (tipo == 1) {
            System.out.print("RAM (GB): ");
            int ram = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Procesador: ");
            String procesador = scanner.nextLine();
            
            System.out.print("Tamaño Disco (GB): ");
            int tamDisco = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Tipo Disco (0=HDD, 1=SSD, 2=NVMe): ");
            int tipoDisco = scanner.nextInt();
            scanner.nextLine();
            
            dispositivo = new Ordenador(marca, modelo, estado, ram, procesador, tamDisco, tipoDisco);
        } else {
            System.out.print("Tipo (0=Láser, 1=Inyección de tinta): ");
            int tipoImpresora = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("¿Tiene color? (true/false): ");
            boolean color = Boolean.parseBoolean(scanner.nextLine());
            
            System.out.print("¿Tiene escáner? (true/false): ");
            boolean tieneScanner = Boolean.parseBoolean(scanner.nextLine());
            
            dispositivo = new Impresora(marca, modelo, estado, tipoImpresora, color, tieneScanner);
        }
        
        listaDispositivos.add(dispositivo);
        dispositivo.save();
        System.out.println();
        System.out.println("Dispositivo añadido correctamente.");
        System.out.println();
    }

    private static void mostrarDispositivos() {
        if (listaDispositivos.isEmpty()) {
            System.out.println();
            System.out.println("No hay dispositivos registrados.");
            System.out.println();
        } else {
            for (Dispositivo d : listaDispositivos) {
                System.out.println();
                System.out.println(d.toString().replace(". ", "\n"));
                System.out.println();

            }
        }
    }

    private static void buscarDispositivo() {
        System.out.println();
        System.out.print("Ingrese ID del dispositivo: ");
        System.out.println();
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println();
        System.out.println("IDs Disponibles en la lista de los Dispositivos:");
        
        for (Dispositivo d : listaDispositivos) {
            System.out.println("ID: " + d.getId() + ", Marca: " + d.getMarca());
        }

        for (Dispositivo d : listaDispositivos) {
            if (d.getId() == id) {
                System.out.println(d.toString().replace(". ", "\n"));
                return;
            }
        }
        System.out.println();
        System.out.println("El dispositivo que buscabas no se ha encontrado.");
    }
    

    private static void borrarDispositivo() {
        System.out.println();
        System.out.print("Ingrese ID del dispositivo a borrar: ");
        System.out.println();
        int id = scanner.nextInt();
        scanner.nextLine();
        for (Dispositivo d : listaDispositivos) {
            if (d.getId() == id) {
                d.delete();
                listaDispositivos.remove(d);
                System.out.println("Dispositivo eliminado.");
                return;
            }
        }
        System.out.println("Dispositivo no encontrado.");
    }

    private static void cambiarEstadoDispositivo() {
        System.out.print("Ingrese ID del dispositivo a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        for (Dispositivo d : listaDispositivos) {
            if (d.getId() == id) {
                d.setEstado(!d.isEstado());
                d.save();
                System.out.println();
                System.out.println("Estado actualizado.");
                System.out.println();
                return;
            }
        }
        System.out.println("Dispositivo no encontrado.");
    }

    private static void modificarDispositivo() {
        System.out.println();
        System.out.print("Ingrese ID del dispositivo a modificar: ");
        System.out.println();
        int id = scanner.nextInt();
        scanner.nextLine();
        for (Dispositivo d : listaDispositivos) {
            if (d.getId() == id) {
                System.out.println();
                System.out.print("Nueva marca: ");
                d.setMarca(scanner.nextLine());
                System.out.println();
                System.out.print("Nuevo modelo: ");
                d.setModelo(scanner.nextLine());
                d.save();
                System.out.println();
                System.out.println("Dispositivo actualizado.");
                return;
            }
        }
        System.out.println("Dispositivo no encontrado.");
    }
}
