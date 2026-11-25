package pe.edu.unsch.banco.aplicacion;

import pe.edu.unsch.banco.aplicacion.puertos.CuentaUseCase;
import pe.edu.unsch.banco.aplicacion.puertos.MovimientoRepository;
import pe.edu.unsch.banco.dominio.CuentaBancaria;
import java.math.BigDecimal;

public class CuentaService implements CuentaUseCase {
    private CuentaBancaria cuentaActiva;
    private final MovimientoRepository movimientoRepository;

    public CuentaService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Override
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

    @Override
    public void depositar(BigDecimal monto) {
        validarCuentaActiva();
        cuentaActiva.depositar(monto);
        movimientoRepository.guardar(
                "Titular: " + cuentaActiva.getTitular() +
                        " | Deposito: S/ " + monto +
                        " | Saldo actual: S/ " + cuentaActiva.getSaldo()
        );
    }

    @Override
    public void retirar(BigDecimal monto) {
        validarCuentaActiva();
        cuentaActiva.retirar(monto);
        movimientoRepository.guardar(
                "Titular: " + cuentaActiva.getTitular() +
                        " | Retiro: S/ " + monto +
                        " | Saldo actual: S/ " + cuentaActiva.getSaldo()
        );
    }

    @Override
    public BigDecimal consultarSaldo() {
        validarCuentaActiva();
        return cuentaActiva.getSaldo();
    }

    @Override
    public String consultarTitular() {
        validarCuentaActiva();
        return cuentaActiva.getTitular();
    }

    private void validarCuentaActiva() {
        if (this.cuentaActiva == null) {
            throw new IllegalStateException("No existe ninguna cuenta activa registrada.");
        }
    }
}