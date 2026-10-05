package controller;

import dao.PedidoDAO;
import dao.impl.PedidoDAOImpl;
import model.EstadoPedido;
import model.Pedido;
import model.TipoPedido;

import java.util.List;

/**
 * Controlador encargado de la gestión de pedidos.
 */
public class ControladorPedidos {

    private final PedidoDAO pedidoDAO;

    public ControladorPedidos() {
        pedidoDAO = new PedidoDAOImpl();
    }

    /**
     * Registra un nuevo pedido.
     */
    public void registrarPedido(
            String direccion,
            TipoPedido tipo
    ) {

        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La dirección es obligatoria."
            );
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un tipo de pedido."
            );
        }

        Pedido pedido =
                new Pedido(
                        0,
                        direccion.trim(),
                        tipo
                );

        pedidoDAO.create(pedido);
    }

    /**
     * Obtiene todos los pedidos registrados.
     */
    public List<Pedido> listarPedidos() {
        return pedidoDAO.readAll();
    }

    /**
     * Actualiza un pedido existente.
     */
    public void actualizarPedido(
            int id,
            String direccion,
            TipoPedido tipo,
            EstadoPedido estado
    ) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del pedido no es válido."
            );
        }

        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La dirección es obligatoria."
            );
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un tipo de pedido."
            );
        }

        if (estado == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un estado."
            );
        }

        Pedido pedido =
                new Pedido(
                        id,
                        direccion.trim(),
                        tipo,
                        estado
                );

        pedidoDAO.update(pedido);
    }

    /**
     * Elimina un pedido.
     */
    public void eliminarPedido(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del pedido no es válido."
            );
        }

        pedidoDAO.delete(id);
    }

    /**
     * Busca un pedido por ID.
     */
    public Pedido buscarPedidoPorId(int id) {

        for (Pedido pedido : pedidoDAO.readAll()) {

            if (pedido.getId() == id) {
                return pedido;
            }
        }

        return null;
    }
}