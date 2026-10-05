package dao;

import model.Repartidor;

import java.util.List;

/**
 * Define las operaciones de persistencia
 * disponibles para los repartidores.
 */
public interface RepartidorDAO {

    void create(Repartidor repartidor);

    List<Repartidor> readAll();

    void update(Repartidor repartidor);

    void delete(int idRepartidor);
}
