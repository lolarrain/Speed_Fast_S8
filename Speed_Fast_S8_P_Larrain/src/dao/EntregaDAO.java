package dao;

import model.Entrega;
import model.Repartidor;

import java.util.List;

/**
 * Define las operaciones de persistencia
 * disponibles para las entregas.
 */
public interface EntregaDAO {

    void create(Entrega entrega);

    List<Entrega> readAll();

    void update(Entrega entrega);

    void delete(int idEntrega);

    Repartidor buscarRepartidorPorPedido(
            int idPedido
    );
}