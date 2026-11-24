# Laboratorio 07-A: Arquitectura Limpia (Clean Architecture)
**Curso:** Construcción y Evolución de Software (IS-388) - UNSCH

## 1. Estructura del Proyecto
* `pe.edu.unsch.banco.dominio`: Contiene la entidad `CuentaBancaria` con las reglas de negocio e invariantes de saldo usando `BigDecimal`.
* `pe.edu.unsch.banco.aplicacion`: Contiene los casos de uso (`CuentaService`) y la interfaz `MovimientoRepository`.
* `pe.edu.unsch.banco.infraestructura`: Contiene `ArchivoMovimientoRepository`, implementación concreta que persiste logs en `movimientos.txt`.
* `pe.edu.unsch.banco`: Contiene `Main`, encargado de la interfaz de consola y la inyección de dependencias.

## 2. Regla de Dependencias
Las dependencias apuntan estrictamente hacia adentro:
`Infraestructura / Presentación (Main)` -> `Aplicación (CuentaService, MovimientoRepository)` -> `Dominio (CuentaBancaria)`
* **Dominio** no importa ningún paquete externo del proyecto.
* **Aplicación** conoce a Dominio, pero desconoce cómo se guardan los datos (solo conoce la interfaz `MovimientoRepository`).
* **Infraestructura** implementa la interfaz definida en Aplicación (Inversión de Dependencias).

## 3. Instrucciones de Ejecución
1. Clonar el repositorio y abrirlo en IntelliJ IDEA con JDK 17 o superior.
2. Ejecutar el método `main` de la clase `pe.edu.unsch.banco.Main`.
3. Interactuar mediante las opciones numéricas (1 al 5) en consola.

## 4. Respuestas a Preguntas de Sustentación
1. **¿Qué problema concreto resolvieron al separar `CuentaBancaria` de `Main`?**
   Se eliminó el acoplamiento entre las reglas financieras y la interfaz de usuario por consola. Ahora las validaciones de montos y saldos pueden probarse de forma aislada y reutilizarse en cualquier interfaz (web, móvil o escritorio) sin reescribir la lógica bancaria.
2. **¿Por qué `CuentaService` recibe `MovimientoRepository` en su constructor?**
   Para aplicar el Principio de Inversión de Dependencias (DIP) mediante inyección por constructor. Esto permite que el caso de uso dependa de una abstracción (interfaz) y no de una clase concreta, facilitando cambiar el medio de almacenamiento o usar *mocks* en pruebas.
3. **¿Qué archivos deberían cambiar si reemplazamos `FileWriter` por JDBC/H2?**
   Únicamente se crearía una nueva clase en `infraestructura` (por ejemplo, `JdbcMovimientoRepository` implementando `MovimientoRepository`) y se cambiaría la línea de instanciación en `Main.java`. Ni `CuentaBancaria` ni `CuentaService` sufrirían modificaciones.
4. **¿En qué capa se valida que el retiro no exceda el saldo?**
   En la **capa de Dominio**, específicamente dentro del método `retirar(BigDecimal monto)` de la entidad `CuentaBancaria`, ya que proteger la integridad del saldo es una regla de negocio fundamental (invariante de dominio).
5. **¿Qué diferencia existe entre registrar movimientos y persistir cuentas?**
   Registrar movimientos consiste en guardar una bitácora secuencial (*log* de eventos) de lo que ocurrió, mientras que persistir cuentas implica almacenar y recuperar el estado actual completo de la entidad (titular, saldo vigente, identificador) para reconstruir los objetos en memoria tras reiniciar el sistema.

## 5. Dificultades y Conclusiones
Durante el desarrollo, el principal reto consistió en desacoplar el flujo interactivo de entrada/salida y manejar adecuadamente el flujo de excepciones desde el dominio hacia la capa de presentación sin imprimir mensajes directamente desde las entidades. Asimismo, el manejo de `Scanner` con `nextLine()` evitó los errores clásicos de salto de buffer al alternar entradas numéricas y de texto.

En conclusión, la aplicación de Clean Architecture y el principio de inversión de dependencias permitió transformar un código rígido y monolítico en una arquitectura modular, mantenible y extensible. Al aislar la entidad `CuentaBancaria` y abstraer la persistencia mediante contratos en la capa de aplicación, el sistema queda preparado para evolucionar hacia bases de datos relacionales o interfaces gráficas sin comprometer la estabilidad de sus reglas de negocio.