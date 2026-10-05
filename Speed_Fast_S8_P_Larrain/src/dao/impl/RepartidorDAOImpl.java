package dao.impl;

import dao.RepartidorDAO;
import model.Repartidor;
import util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa las operaciones CRUD de repartidores
 * utilizando JDBC y MySQL.
 */
public class RepartidorDAOImpl implements RepartidorDAO {

    @Override
    public void create(Repartidor repartidor) {

        String sql =
                "INSERT INTO repartidor (nombre) VALUES (?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(
                    1,
                    repartidor.getNombre()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al registrar repartidor: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public List<Repartidor> readAll() {

        List<Repartidor> repartidores =
                new ArrayList<>();

        String sql =
                "SELECT id, nombre FROM repartidor";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql);
             ResultSet resultado =
                     statement.executeQuery()) {

            while (resultado.next()) {

                repartidores.add(
                        new Repartidor(
                                resultado.getInt("id"),
                                resultado.getString("nombre")
                        )
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al listar repartidores: "
                            + e.getMessage(),
                    e
            );
        }

        return repartidores;
    }

    @Override
    public void update(Repartidor repartidor) {

        String sql = """
                UPDATE repartidor
                SET nombre = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(
                    1,
                    repartidor.getNombre()
            );

            statement.setInt(
                    2,
                    repartidor.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al actualizar repartidor: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public void delete(int idRepartidor) {

        String sql =
                "DELETE FROM repartidor WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(1, idRepartidor);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al eliminar repartidor: "
                            + e.getMessage(),
                    e
            );
        }
    }
}