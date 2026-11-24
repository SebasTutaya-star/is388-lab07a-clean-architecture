package pe.edu.unsch.banco.infraestructura;

import pe.edu.unsch.banco.aplicacion.MovimientoRepository;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArchivoMovimientoRepository implements MovimientoRepository {
    private final String rutaArchivo;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ArchivoMovimientoRepository(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void guardar(String movimiento) {
        try (FileWriter archivo = new FileWriter(rutaArchivo, true)) {
            String fechaHora = LocalDateTime.now().format(FORMATO_FECHA);
            archivo.write(fechaHora + " | " + movimiento + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException("Error de infraestructura al escribir en " + rutaArchivo + ": " + e.getMessage(), e);
        }
    }
}