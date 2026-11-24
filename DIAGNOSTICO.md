# Diagnóstico de Arquitectura - SistemaBancario (Código Inicial)

## 1. Tabla de Responsabilidades y Acoplamiento

En el código inicial (`SistemaBancario.java`), todas las responsabilidades recaen sobre una única clase monolítica ("God Class"):

| Responsabilidad | Ubicación en código inicial | Cambio de requisitos que obligaría a modificar la clase |
| :--- | :--- | :--- |
| **Capturar entrada de usuario** | Uso directo de `Scanner` en `registrarCuenta()`, `depositar()`, `retirar()` y `main()`. | Migrar la aplicación de consola a una interfaz gráfica (JavaFX/Swing) o a una API REST. |
| **Validar reglas de negocio** | Condicionales `if (monto > 0)` y `monto <= saldo` mezclados con lecturas de teclado. | Cambiar políticas bancarias (ej. cobro de comisión por retiro, límite máximo diario o saldo mínimo). |
| **Actualizar estado/saldos** | Modificación directa de los atributos `titular` y `saldo` dentro de los métodos de E/S. | Soportar múltiples cuentas simultáneas o distintos tipos de cuenta (ahorros, corriente). |
| **Persistir movimientos** | Método `guardarMovimiento()` acoplado directamente a `java.io.FileWriter`. | Reemplazar el archivo de texto `movimientos.txt` por una base de datos relacional (MySQL/H2) o un servicio externo. |
| **Presentar resultados** | Llamadas a `System.out.println()` dispersas en todos los métodos operativos. | Internacionalizar mensajes (multi-idioma) o devolver respuestas estructuradas en formato JSON. |
| **Coordinar flujo del menú** | Bucle `do-while` y `switch` dentro del método `main()`. | Agregar nuevos casos de uso (ej. transferencias) o cambiar el flujo de autenticación. |

## 2. Respuestas a Preguntas Orientadoras

* **¿Qué sucede si cambiamos el archivo por una base de datos?**
  En el diseño original, habría que modificar el método `guardarMovimiento()` dentro de `SistemaBancario`, arriesgando romper la lógica de negocio o la lectura por consola al tocar el mismo archivo fuente.
* **¿Por qué la entidad bancaria no debería utilizar `Scanner` o `FileWriter`?**
  Porque la entidad de dominio representa las reglas puras del negocio bancario (invariantes de saldo y titular). Si depende de `Scanner` o `FileWriter`, resulta imposible reutilizarla en otro entorno (web, móvil, pruebas unitarias automatizadas) sin arrastrar dependencias de consola o sistema de archivos.
* **¿Qué pasaría al registrar una cuenta distinta?**
  En el código original, sobrescribe silenciosamente las variables `titular` y `saldo`, destruyendo la cuenta anterior en memoria sin previo aviso.

## 3. Justificación Técnica: Uso de `BigDecimal`

En la refactorización se reemplazó el tipo primitivo `double` por `java.math.BigDecimal`. Los tipos de punto flotante binario (`double` y `float`) siguen el estándar IEEE 754, el cual no puede representar de forma exacta fracciones decimales base 10 (por ejemplo, `0.1 + 0.2` produce `0.30000000000000004`). En sistemas financieros y bancarios, estos errores de redondeo acumulados generan descuadres contables críticos. `BigDecimal` ofrece aritmética decimal exacta y control explícito sobre el modo de redondeo (`RoundingMode.HALF_UP`).