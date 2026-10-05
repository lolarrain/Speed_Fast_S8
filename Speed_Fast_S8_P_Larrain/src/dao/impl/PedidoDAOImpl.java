package dao.impl;

import dao.PedidoDAO;
import model.EstadoPedido;
import model.Pedido;
import model.TipoPedido;
import util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa las operaciones de persistencia
 * de pedidos mediante JDBC.
 */
public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public int create(Pedido pedido) {

        String sql = """
                INSERT INTO pedido
                (direccion, tipo, estado)
                VALUES (?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setString(
                    1,
                    pedido.getDireccion()
            );

            statement.setString(
                    2,
                    pedido.getTipo().name()
            );

            statement.setString(
                    3,
                    pedido.getEstado().name()
            );

            statement.executeUpdate();

            try (ResultSet claves =
                         statement.getGeneratedKeys()) {

                if (claves.next()) {
                    return claves.getInt(1);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al registrar pedido: "
                            + e.getMessage(),
                    e
            );
        }

        throw new RuntimeException(
                "No se pudo obtener el ID del pedido."
        );
    }

    @Override
    public List<Pedido> readAll() {

        List<Pedido> pedidos =
                new ArrayList<>();

        String sql = """
                SELECT id, direccion, tipo, estado
                FROM pedido
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql);
             ResultSet resultado =
                     statement.executeQuery()) {

            while (resultado.next()) {

                Pedido pedido =
                        new Pedido(
                                resultado.getInt("id"),
                                resultado.getString("direccion"),
                                TipoPedido.valueOf(
                                        resultado.getString("tipo")
                                ),
                                EstadoPedido.valueOf(
                                        resultado.getString("estado")
                                )
                        );

                pedidos.add(pedido);
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al listar pedidos: "
                            + e.getMessage(),
                    e
            );
        }

        return pedidos;
    }

    @Override
    public void update(Pedido pedido) {

        String sql = """
                UPDATE pedido
                SET direccion = ?, tipo = ?, estado = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(
                    1,
                    pedido.getDireccion()
            );

            statement.setString(
                    2,
                    pedido.getTipo().name()
            );

            statement.setString(
                    3,
                    pedido.getEstado().name()
            );

            statement.setInt(
                    4,
                    pedido.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al actualizar pedido: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public void delete(int idPedido) {

        String sqlEliminarEntrega =
                "DELETE FROM entrega WHERE id_pedido = ?";

        String sqlEliminarPedido =
                "DELETE FROM pedido WHERE id = ?";

        Connection conexion = null;

        try {

            conexion = ConexionBD.conectar();
            conexion.setAutoCommit(false);

            try (PreparedStatement statement =
                         conexion.prepareStatement(
                                 sqlEliminarEntrega
                         )) {

                statement.setInt(1, idPedido);
                statement.executeUpdate();
            }

            try (PreparedStatement statement =
                         conexion.prepareStatement(
                                 sqlEliminarPedido
                         )) {

                statement.setInt(1, idPedido);
                statement.executeUpdate();
            }

            conexion.commit();

        } catch (SQLException e) {

            if (conexion != null) {

                try {
                    conexion.rollback();
                } catch (SQLException rollbackError) {
                    System.err.println(
                            "Error al revertir transacción: "
                                    + rollbackError.getMessage()
                    );
                }
            }

            throw new RuntimeException(
                    "Error al eliminar pedido: "
                            + e.getMessage(),
                    e
            );

        } finally {

            if (conexion != null) {

                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println(
                            "Error al cerrar conexión: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    @Override
    public void actualizarEstado(Pedido pedido) {

        String sql = """
                UPDATE pedido
                SET estado = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(
                    1,
                    pedido.getEstado().name()
            );

            statement.setInt(
                    2,
                    pedido.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al actualizar estado: "
                            + e.getMessage(),
                    e
            );
        }
    }
}
