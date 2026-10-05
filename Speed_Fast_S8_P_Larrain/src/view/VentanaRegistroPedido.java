package view;

import controller.ControladorPedidos;
import model.EstadoPedido;
import model.Pedido;
import model.TipoPedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private final ControladorPedidos controlador;

    private JTextField txtDireccion;
    private JComboBox<TipoPedido> cmbTipo;
    private JComboBox<EstadoPedido> cmbEstado;

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    public VentanaRegistroPedido(
            ControladorPedidos controlador
    ) {

        this.controlador = controlador;

        configurarVentana();
        inicializarComponentes();
        cargarPedidos();
    }

    private void configurarVentana() {

        setTitle("SpeedFast - Gestión de Pedidos");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(10, 10));

        JPanel panelFormulario =
                new JPanel(new GridLayout(3, 2, 10, 10));

        txtDireccion = new JTextField();

        cmbTipo =
                new JComboBox<>(TipoPedido.values());

        cmbEstado =
                new JComboBox<>(EstadoPedido.values());

        panelFormulario.add(
                new JLabel("Dirección:")
        );
        panelFormulario.add(txtDireccion);

        panelFormulario.add(
                new JLabel("Tipo:")
        );
        panelFormulario.add(cmbTipo);

        panelFormulario.add(
                new JLabel("Estado:")
        );
        panelFormulario.add(cmbEstado);

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

        tablaPedidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scroll =
                new JScrollPane(tablaPedidos);

        JButton btnGuardar =
                new JButton("Guardar");

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnCerrar =
                new JButton("Cerrar");

        JPanel panelBotones =
                new JPanel(new FlowLayout());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        panelPrincipal.add(
                panelFormulario,
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

        btnGuardar.addActionListener(
                e -> guardarPedido()
        );

        btnEditar.addActionListener(
                e -> editarPedido()
        );

        btnEliminar.addActionListener(
                e -> eliminarPedido()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );

        tablaPedidos
                .getSelectionModel()
                .addListSelectionListener(
                        e -> cargarSeleccion()
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

    private void guardarPedido() {

        try {

            String direccion =
                    txtDireccion.getText().trim();

            TipoPedido tipo =
                    (TipoPedido)
                            cmbTipo.getSelectedItem();

            controlador.registrarPedido(
                    direccion,
                    tipo
            );

            cargarPedidos();
            limpiarFormulario();

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente."
            );

        } catch (RuntimeException e) {

            mostrarError(e);
        }
    }

    private void editarPedido() {

        int fila =
                tablaPedidos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            int id =
                    (int) modeloTabla.getValueAt(
                            fila,
                            0
                    );

            controlador.actualizarPedido(
                    id,
                    txtDireccion.getText().trim(),
                    (TipoPedido)
                            cmbTipo.getSelectedItem(),
                    (EstadoPedido)
                            cmbEstado.getSelectedItem()
            );

            cargarPedidos();
            limpiarFormulario();

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado correctamente."
            );

        } catch (RuntimeException e) {

            mostrarError(e);
        }
    }

    private void eliminarPedido() {

        int fila =
                tablaPedidos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Eliminar el pedido seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int id =
                    (int) modeloTabla.getValueAt(
                            fila,
                            0
                    );

            controlador.eliminarPedido(id);

            cargarPedidos();
            limpiarFormulario();

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado correctamente."
            );

        } catch (RuntimeException e) {

            mostrarError(e);
        }
    }

    private void cargarSeleccion() {

        int fila =
                tablaPedidos.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtDireccion.setText(
                modeloTabla
                        .getValueAt(fila, 1)
                        .toString()
        );

        cmbTipo.setSelectedItem(
                TipoPedido.valueOf(
                        modeloTabla
                                .getValueAt(fila, 2)
                                .toString()
                )
        );

        cmbEstado.setSelectedItem(
                EstadoPedido.valueOf(
                        modeloTabla
                                .getValueAt(fila, 3)
                                .toString()
                )
        );
    }

    private void limpiarFormulario() {

        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);

        tablaPedidos.clearSelection();

        txtDireccion.requestFocus();
    }

    private void mostrarError(
            RuntimeException e
    ) {

        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}