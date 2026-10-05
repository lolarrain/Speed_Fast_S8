package dao;

import model.Pedido;

import java.util.List;

/**
 * Define las operaciones de persistencia
 * disponibles para los pedidos.
 */
public interface PedidoDAO {

    int create(Pedido pedido);

    List<Pedido> readAll();

    void update(Pedido pedido);

    void delete(int idPedido);

    void actualizarEstado(Pedido pedido);
}