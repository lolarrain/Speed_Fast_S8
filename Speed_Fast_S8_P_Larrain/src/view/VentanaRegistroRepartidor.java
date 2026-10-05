package view;

import controller.ControladorRepartidores;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaRegistroRepartidor extends JFrame {

    private final ControladorRepartidores controlador;

    private JTextField txtNombre;
    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;

    public VentanaRegistroRepartidor(
            ControladorRepartidores controlador
    ) {

        this.controlador = controlador;

        configurarVentana();
        inicializarComponentes();
        cargarRepartidores();
    }

    private void configurarVentana() {

        setTitle(
                "SpeedFast - Gestión de Repartidores"
        );

        setSize(600, 400);
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

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(1, 2, 10, 10)
                );

        txtNombre = new JTextField();

        panelFormulario.add(
                new JLabel("Nombre:")
        );

        panelFormulario.add(txtNombre);

        String[] columnas = {
                "ID",
                "Nombre"
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

        tablaRepartidores =
                new JTable(modeloTabla);

        tablaRepartidores.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scroll =
                new JScrollPane(
                        tablaRepartidores
                );

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
                e -> guardarRepartidor()
        );

        btnEditar.addActionListener(
                e -> editarRepartidor()
        );

        btnEliminar.addActionListener(
                e -> eliminarRepartidor()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );

        tablaRepartidores
                .getSelectionModel()
                .addListSelectionListener(
                        e -> cargarSeleccion()
                );
    }

    private void cargarRepartidores() {

        modeloTabla.setRowCount(0);

        for (Repartidor repartidor :
                controlador.listarRepartidores()) {

            modeloTabla.addRow(
                    new Object[]{
                            repartidor.getId(),
                            repartidor.getNombre()
                    }
            );
        }
    }

    private void guardarRepartidor() {

        try {

            controlador.registrarRepartidor(
                    txtNombre
                            .getText()
                            .trim()
            );

            cargarRepartidores();
            limpiarFormulario();

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor registrado correctamente."
            );

        } catch (RuntimeException e) {

            mostrarError(e);
        }
    }

    private void editarRepartidor() {

        int fila =
                tablaRepartidores.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un repartidor.",
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

            controlador.actualizarRepartidor(
                    id,
                    txtNombre
                            .getText()
                            .trim()
            );

            cargarRepartidores();
            limpiarFormulario();

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado correctamente."
            );

        } catch (RuntimeException e) {

            mostrarError(e);
        }
    }

    private void eliminarRepartidor() {

        int fila =
                tablaRepartidores.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un repartidor.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Eliminar el repartidor seleccionado?",
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

            controlador.eliminarRepartidor(id);

            cargarRepartidores();
            limpiarFormulario();

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado correctamente."
            );

        } catch (RuntimeException e) {

            mostrarError(e);
        }
    }

    private void cargarSeleccion() {

        int fila =
                tablaRepartidores.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtNombre.setText(
                modeloTabla
                        .getValueAt(fila, 1)
                        .toString()
        );
    }

    private void limpiarFormulario() {

        txtNombre.setText("");
        tablaRepartidores.clearSelection();
        txtNombre.requestFocus();
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