package controller;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.impl.EntregaDAOImpl;
import dao.impl.PedidoDAOImpl;

import model.Entrega;
import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controlador encargado de la gestión de entregas.
 */
public class ControladorEntregas {

    private final EntregaDAO entregaDAO;
    private final PedidoDAO pedidoDAO;

    private final ExecutorService executor;

    public ControladorEntregas() {

        entregaDAO = new EntregaDAOImpl();
        pedidoDAO = new PedidoDAOImpl();

        executor = Executors.newFixedThreadPool(3);
    }

    /**
     * Obtiene todas las entregas registradas.
     *
     * @return lista de entregas
     */
    public List<Entrega> listarEntregas() {

        return entregaDAO.readAll();
    }

    /**
     * Actualiza una entrega existente.
     */
    public void actualizarEntrega(
            int id,
            Pedido pedido,
            Repartidor repartidor,
            LocalDate fecha,
            LocalTime hora
    ) {

        validarEntrega(
                id,
                pedido,
                repartidor,
                fecha,
                hora
        );

        Entrega entrega =
                new Entrega(
                        id,
                        pedido,
                        repartidor,
                        fecha,
                        hora
                );

        entregaDAO.update(entrega);
    }

    /**
     * Elimina una entrega.
     */
    public void eliminarEntrega(
            int id
    ) {

        if (id <= 0) {

            throw new IllegalArgumentException(
                    "El identificador de la entrega no es válido."
            );
        }

        entregaDAO.delete(id);
    }

    /**
     * Asigna un repartidor a un pedido pendiente
     * y registra la relación en la tabla entrega.
     *
     * La asignación no cambia todavía el estado del pedido.
     */
    public void asignarRepartidor(
            Pedido pedido,
            Repartidor repartidor
    ) {

        if (pedido == null
                || repartidor == null) {

            throw new IllegalArgumentException(
                    "Debe seleccionar pedido y repartidor."
            );
        }

        if (pedido.getEstado()
                != EstadoPedido.PENDIENTE) {

            throw new IllegalStateException(
                    "Solo se pueden asignar pedidos pendientes."
            );
        }

        pedido.asignarRepartidor(
                repartidor
        );

        Entrega entrega =
                new Entrega(
                        0,
                        pedido,
                        repartidor,
                        LocalDate.now(),
                        LocalTime.now()
                );

        entregaDAO.create(entrega);
    }

    /**
     * Inicia la entrega de un pedido.
     *
     * El pedido pasa de PENDIENTE a EN_REPARTO.
     * La simulación se ejecuta en un hilo independiente.
     * Al finalizar, cambia a ENTREGADO.
     */
    public void iniciarEntrega(Pedido pedido) {

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un pedido."
            );
        }

        Repartidor repartidor =
                pedido.getRepartidor();

        if (repartidor == null) {

            repartidor =
                    entregaDAO.buscarRepartidorPorPedido(
                            pedido.getId()
                    );

            if (repartidor != null) {
                pedido.restaurarRepartidor(
                        repartidor
                );
            }
        }

        if (repartidor == null) {
            throw new IllegalStateException(
                    "El pedido debe tener un repartidor asignado."
            );
        }

        if (pedido.getEstado()
                != EstadoPedido.PENDIENTE) {

            throw new IllegalStateException(
                    "Solo se pueden iniciar pedidos pendientes."
            );
        }

        pedido.iniciarEntrega();

        pedidoDAO.actualizarEstado(pedido);

        executor.submit(() -> {

            try {

                Thread.sleep(3000);

                pedido.completarEntrega();

                pedidoDAO.actualizarEstado(
                        pedido
                );

            } catch (InterruptedException e) {

                Thread.currentThread()
                        .interrupt();

                System.err.println(
                        "[ERROR] La entrega del pedido "
                                + pedido.getId()
                                + " fue interrumpida."
                );
            }
        });
    }

    /**
     * Busca el repartidor asignado a un pedido.
     *
     * @param pedido pedido seleccionado
     * @return repartidor asignado o null si aún no existe una asignación
     */
    public Repartidor buscarRepartidorAsignado(Pedido pedido) {

        if (pedido == null) {
            return null;
        }

        return entregaDAO.buscarRepartidorPorPedido(
                pedido.getId()
        );
    }

    /**
     * Valida los datos necesarios para modificar
     * una entrega.
     */
    private void validarEntrega(
            int id,
            Pedido pedido,
            Repartidor repartidor,
            LocalDate fecha,
            LocalTime hora
    ) {

        if (id <= 0) {

            throw new IllegalArgumentException(
                    "El identificador de la entrega no es válido."
            );
        }

        if (pedido == null) {

            throw new IllegalArgumentException(
                    "Debe seleccionar un pedido."
            );
        }

        if (repartidor == null) {

            throw new IllegalArgumentException(
                    "Debe seleccionar un repartidor."
            );
        }

        if (fecha == null) {

            throw new IllegalArgumentException(
                    "La fecha es obligatoria."
            );
        }

        if (hora == null) {

            throw new IllegalArgumentException(
                    "La hora es obligatoria."
            );
        }
    }
}
