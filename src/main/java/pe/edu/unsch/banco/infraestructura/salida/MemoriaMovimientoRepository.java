package pe.edu.unsch.banco.infraestructura.salida;

import pe.edu.unsch.banco.aplicacion.puertos.MovimientoRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MemoriaMovimientoRepository implements MovimientoRepository {
    private final List<String> movimientos = new ArrayList<>();
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void guardar(String movimiento) {
        String fechaHora = LocalDateTime.now().format(FORMATO_FECHA);
        movimientos.add(fechaHora + " | " + movimiento);
    }

    public List<String> listarMovimientos() {
        return List.copyOf(movimientos);
    }
}