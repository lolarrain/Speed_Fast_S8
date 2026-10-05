package dao.impl;

import dao.EntregaDAO;
import model.Entrega;
import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;
import model.TipoPedido;
import util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa las operaciones de persistencia
 * de entregas mediante JDBC.
 */
public class EntregaDAOImpl implements EntregaDAO {

    @Override
    public void create(Entrega entrega) {

        String sql = """
                INSERT INTO entrega
                (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    entrega.getPedido().getId()
            );

            statement.setInt(
                    2,
                    entrega.getRepartidor().getId()
            );

            statement.setDate(
                    3,
                    java.sql.Date.valueOf(
                            entrega.getFecha()
                    )
            );

            statement.setTime(
                    4,
                    java.sql.Time.valueOf(
                            entrega.getHora()
                    )
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al registrar entrega: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public List<Entrega> readAll() {

        List<Entrega> entregas =
                new ArrayList<>();

        String sql = """
                SELECT
                    e.id AS id_entrega,
                    e.fecha,
                    e.hora,
                    p.id AS id_pedido,
                    p.direccion,
                    p.tipo,
                    p.estado,
                    r.id AS id_repartidor,
                    r.nombre
                FROM entrega e
                INNER JOIN pedido p
                    ON e.id_pedido = p.id
                INNER JOIN repartidor r
                    ON e.id_repartidor = r.id
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql);
             ResultSet resultado =
                     statement.executeQuery()) {

            while (resultado.next()) {

                Pedido pedido =
                        new Pedido(
                                resultado.getInt("id_pedido"),
                                resultado.getString("direccion"),
                                TipoPedido.valueOf(
                                        resultado.getString("tipo")
                                ),
                                EstadoPedido.valueOf(
                                        resultado.getString("estado")
                                )
                        );

                Repartidor repartidor =
                        new Repartidor(
                                resultado.getInt("id_repartidor"),
                                resultado.getString("nombre")
                        );

                entregas.add(
                        new Entrega(
                                resultado.getInt("id_entrega"),
                                pedido,
                                repartidor,
                                resultado.getDate("fecha")
                                        .toLocalDate(),
                                resultado.getTime("hora")
                                        .toLocalTime()
                        )
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al listar entregas: "
                            + e.getMessage(),
                    e
            );
        }

        return entregas;
    }

    @Override
    public void update(Entrega entrega) {

        String sql = """
                UPDATE entrega
                SET id_pedido = ?,
                    id_repartidor = ?,
                    fecha = ?,
                    hora = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    entrega.getPedido().getId()
            );

            statement.setInt(
                    2,
                    entrega.getRepartidor().getId()
            );

            statement.setDate(
                    3,
                    java.sql.Date.valueOf(
                            entrega.getFecha()
                    )
            );

            statement.setTime(
                    4,
                    java.sql.Time.valueOf(
                            entrega.getHora()
                    )
            );

            statement.setInt(
                    5,
                    entrega.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al actualizar entrega: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public void delete(int idEntrega) {

        String sql =
                "DELETE FROM entrega WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    idEntrega
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al eliminar entrega: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public Repartidor buscarRepartidorPorPedido(
            int idPedido
    ) {

        String sql = """
                SELECT r.id, r.nombre
                FROM entrega e
                INNER JOIN repartidor r
                    ON e.id_repartidor = r.id
                WHERE e.id_pedido = ?
                ORDER BY e.id DESC
                LIMIT 1
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(1, idPedido);

            try (ResultSet resultado =
                         statement.executeQuery()) {

                if (resultado.next()) {

                    return new Repartidor(
                            resultado.getInt("id"),
                            resultado.getString("nombre")
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al consultar entrega: "
                            + e.getMessage(),
                    e
            );
        }

        return null;
    }
}