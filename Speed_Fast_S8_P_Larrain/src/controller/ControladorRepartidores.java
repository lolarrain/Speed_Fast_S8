package controller;

import dao.RepartidorDAO;
import dao.impl.RepartidorDAOImpl;
import model.Repartidor;

import java.util.List;

/**
 * Controlador encargado de la gestión de repartidores.
 */
public class ControladorRepartidores {

    private final RepartidorDAO repartidorDAO;

    public ControladorRepartidores() {
        repartidorDAO = new RepartidorDAOImpl();
    }

    /**
     * Registra un nuevo repartidor.
     */
    public void registrarRepartidor(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del repartidor es obligatorio."
            );
        }

        Repartidor repartidor =
                new Repartidor(
                        0,
                        nombre.trim()
                );

        repartidorDAO.create(repartidor);
    }

    /**
     * Obtiene todos los repartidores.
     */
    public List<Repartidor> listarRepartidores() {
        return repartidorDAO.readAll();
    }

    /**
     * Actualiza un repartidor.
     */
    public void actualizarRepartidor(
            int id,
            String nombre
    ) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del repartidor no es válido."
            );
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del repartidor es obligatorio."
            );
        }

        Repartidor repartidor =
                new Repartidor(
                        id,
                        nombre.trim()
                );

        repartidorDAO.update(repartidor);
    }

    /**
     * Elimina un repartidor.
     */
    public void eliminarRepartidor(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del repartidor no es válido."
            );
        }

        repartidorDAO.delete(id);
    }
}