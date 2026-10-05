package view;

import controller.ControladorPedidos;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaListaPedidos extends JFrame {

    private final ControladorPedidos controlador;

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    public VentanaListaPedidos(
            ControladorPedidos controlador
    ) {

        this.controlador = controlador;

        configurarVentana();
        inicializarComponentes();
        cargarPedidos();
    }

    private void configurarVentana() {

        setTitle("SpeedFast - Lista de Pedidos");
        setSize(700, 400);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        String[] columnas = {
                "ID",
                "Dirección",
                "Tipo",
                "Estado"
        };

        modeloTabla =
                new DefaultTableModel(columnas, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        tablaPedidos =
                new JTable(modeloTabla);

        JScrollPane scroll =
                new JScrollPane(tablaPedidos);

        JButton btnActualizar =
                new JButton("Actualizar");

        JButton btnCerrar =
                new JButton("Cerrar");

        JPanel panelBotones =
                new JPanel(new FlowLayout());

        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        panelPrincipal.add(
                new JLabel(
                        "Pedidos registrados",
                        SwingConstants.CENTER
                ),
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                scroll,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        add(panelPrincipal);

        btnActualizar.addActionListener(
                e -> cargarPedidos()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );
    }

    private void cargarPedidos() {

        modeloTabla.setRowCount(0);

        for (Pedido pedido :
                controlador.listarPedidos()) {

            modeloTabla.addRow(
                    new Object[]{
                            pedido.getId(),
                            pedido.getDireccion(),
                            pedido.getTipo(),
                            pedido.getEstado()
                    }
            );
        }
    }
}