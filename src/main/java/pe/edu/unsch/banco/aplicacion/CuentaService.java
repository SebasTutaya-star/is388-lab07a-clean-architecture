package pe.edu.unsch.banco.aplicacion;

import pe.edu.unsch.banco.dominio.CuentaBancaria;
import java.math.BigDecimal;

public class CuentaService {
    private CuentaBancaria cuentaActiva;
    private final MovimientoRepository movimientoRepository;

    public CuentaService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    public void registrarCuenta(String titular, BigDecimal saldoInicial) {
        if (this.cuentaActiva != null) {
            throw new IllegalStateException("Ya existe una cuenta activa (" + cuentaActiva.getTitular() + "). No se puede reemplazar.");
        }
        this.cuentaActiva = new CuentaBancaria(titular, saldoInicial);
        movimientoRepository.guardar(
                "Cuenta creada | Titular: " + cuentaActiva.getTitular() +
                        " | Saldo inicial: S/ " + cuentaActiva.getSaldo()
        );
    }

    public void depositar(BigDecimal monto) {
        validarCuentaActiva();
        cuentaActiva.depositar(monto);
        movimientoRepository.guardar(
                "Titular: " + cuentaActiva.getTitular() +
                        " | Deposito: S/ " + monto +
                        " | Saldo actual: S/ " + cuentaActiva.getSaldo()
        );
    }

    public void retirar(BigDecimal monto) {
        validarCuentaActiva();
        cuentaActiva.retirar(monto);
        movimientoRepository.guardar(
                "Titular: " + cuentaActiva.getTitular() +
                        " | Retiro: S/ " + monto +
                        " | Saldo actual: S/ " + cuentaActiva.getSaldo()
        );
    }

    public CuentaBancaria consultarCuenta() {
        validarCuentaActiva();
        return cuentaActiva;
    }

    private void validarCuentaActiva() {
        if (this.cuentaActiva == null) {
            throw new IllegalStateException("No existe ninguna cuenta activa registrada.");
        }
    }
}