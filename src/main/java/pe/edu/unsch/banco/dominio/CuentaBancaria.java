package pe.edu.unsch.banco.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CuentaBancaria {
    private final String titular;
    private BigDecimal saldo;

    public CuentaBancaria(String titular, BigDecimal saldoInicial) {
        if (titular == null || titular.trim().isEmpty()) {
            throw new IllegalArgumentException("El titular no puede estar vacío.");
        }
        if (saldoInicial == null || saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }
        this.titular = titular.trim();
        this.saldo = saldoInicial.setScale(2, RoundingMode.HALF_UP);
    }

    public void depositar(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor a cero.");
        }
        this.saldo = this.saldo.add(monto).setScale(2, RoundingMode.HALF_UP);
    }

    public void retirar(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor a cero.");
        }
        if (monto.compareTo(this.saldo) > 0) {
            throw new IllegalStateException("Fondos insuficientes para realizar el retiro.");
        }
        this.saldo = this.saldo.subtract(monto).setScale(2, RoundingMode.HALF_UP);
    }

    public String getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}