package pe.edu.unsch.banco;

import pe.edu.unsch.banco.aplicacion.CuentaService;
import pe.edu.unsch.banco.aplicacion.MovimientoRepository;
import pe.edu.unsch.banco.dominio.CuentaBancaria;
import pe.edu.unsch.banco.infraestructura.ArchivoMovimientoRepository;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inyección de dependencias manual (Composition Root)
        MovimientoRepository repositorio = new ArchivoMovimientoRepository("movimientos.txt");
        CuentaService cuentaService = new CuentaService(repositorio);
        Scanner scanner = new Scanner(System.in);

        System.out.println("AVISO: El sistema inicia en memoria. El archivo 'movimientos.txt' solo guarda historial y no restaura cuentas previas.");

        int opcion = 0;
        do {
            System.out.println("\n=== SISTEMA BANCARIO (CLEAN ARCHITECTURE) ===");
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
                        cuentaService.registrarCuenta(titular, saldoInicial);
                        System.out.println("Cuenta registrada exitosamente.");
                        break;

                    case 2:
                        System.out.print("Monto a depositar: ");
                        BigDecimal montoDeposito = new BigDecimal(scanner.nextLine());
                        cuentaService.depositar(montoDeposito);
                        System.out.println("Deposito exitoso.");
                        break;

                    case 3:
                        System.out.print("Monto a retirar: ");
                        BigDecimal montoRetiro = new BigDecimal(scanner.nextLine());
                        cuentaService.retirar(montoRetiro);
                        System.out.println("Retiro exitoso.");
                        break;

                    case 4:
                        CuentaBancaria cuenta = cuentaService.consultarCuenta();
                        System.out.println("Titular: " + cuenta.getTitular());
                        System.out.println("Saldo: S/ " + cuenta.getSaldo());
                        break;

                    case 5:
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