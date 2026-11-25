package pe.edu.unsch.banco.aplicacion.puertos;

import java.math.BigDecimal;

public interface CuentaUseCase {
    void registrarCuenta(String titular, BigDecimal saldoInicial);
    void depositar(BigDecimal monto);
    void retirar(BigDecimal monto);
    BigDecimal consultarSaldo();
    String consultarTitular();
}