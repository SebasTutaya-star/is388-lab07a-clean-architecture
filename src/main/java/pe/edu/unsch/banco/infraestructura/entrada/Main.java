package pe.edu.unsch.banco.infraestructura.entrada;

import pe.edu.unsch.banco.aplicacion.CuentaService;
import pe.edu.unsch.banco.aplicacion.puertos.CuentaUseCase;
import pe.edu.unsch.banco.aplicacion.puertos.MovimientoRepository;
import pe.edu.unsch.banco.infraestructura.salida.ArchivoMovimientoRepository;
import pe.edu.unsch.banco.infraestructura.salida.MemoriaMovimientoRepository;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // --- PUNTO DE COMPOSICIÓN DE ADAPTADORES (Intercambiar comentando una línea) ---
        // Configuración A: Adaptador de archivo TXT
        // MovimientoRepository repository = new ArchivoMovimientoRepository();

        // Configuración B: Adaptador en memoria (ArrayList)
        MovimientoRepository repository = new MemoriaMovimientoRepository();

        // Puerto de entrada conectado al servicio de aplicación
        CuentaUseCase servicio = new CuentaService(repository);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Adaptador activo: " + repository.getClass().getSimpleName());

        int opcion = 0;
        do {
            System.out.println("\n=== SISTEMA BANCARIO (ARQUITECTURA HEXAGONAL) ===");
            System.out.println("1. Registrar cuenta");
            System.out.println("2. Depositar dinero");
            System.out.println("3. Retirar dinero");
            System.out.println("4. Consultar saldo");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese titular: ");
                        String titular = scanner.nextLine();
                        System.out.print("Ingrese saldo inicial: ");
                        BigDecimal saldoInicial = new BigDecimal(scanner.nextLine());
                        servicio.registrarCuenta(titular, saldoInicial);
                        System.out.println("Cuenta registrada exitosamente.");
                        break;

                    case 2:
                        System.out.print("Monto a depositar: ");
                        BigDecimal montoDeposito = new BigDecimal(scanner.nextLine());
                        servicio.depositar(montoDeposito);
                        System.out.println("Deposito exitoso.");
                        break;

                    case 3:
                        System.out.print("Monto a retirar: ");
                        BigDecimal montoRetiro = new BigDecimal(scanner.nextLine());
                        servicio.retirar(montoRetiro);
                        System.out.println("Retiro exitoso.");
                        break;

                    case 4:
                        System.out.println("Titular: " + servicio.consultarTitular());
                        System.out.println("Saldo: S/ " + servicio.consultarSaldo());
                        break;

                    case 5:
                        if (repository instanceof MemoriaMovimientoRepository memRepo) {
                            System.out.println("\n--- Historial guardado en Memoria (ArrayList) ---");
                            memRepo.listarMovimientos().forEach(System.out::println);
                        }
                        System.out.println("Saliendo del sistema...");
                        break;

                    default:
                        System.out.println("Opcion invalida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error de entrada: Ingrese un valor numerico valido.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Operacion rechazada: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        } while (opcion != 5);

        scanner.close();
    }
}