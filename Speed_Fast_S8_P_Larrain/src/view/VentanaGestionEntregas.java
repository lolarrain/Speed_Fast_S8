package view;

import controller.ControladorEntregas;
import controller.ControladorPedidos;
import controller.ControladorRepartidores;

import model.Entrega;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class VentanaGestionEntregas extends JFrame {

    private final ControladorPedidos controladorPedidos;
    private final ControladorRepartidores controladorRepartidores;
    private final ControladorEntregas controladorEntregas;

    private final List<Entrega> entregasMostradas;

    private JComboBox<Pedido> cmbPedido;
    private JComboBox<Repartidor> cmbRepartidor;

    private JTextField txtFecha;
    private JTextField txtHora;

    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;

    public VentanaGestionEntregas(
            ControladorPedidos controladorPedidos,
            ControladorRepartidores controladorRepartidores,
            ControladorEntregas controladorEntregas
    ) {

        this.controladorPedidos =
                controladorPedidos;

        this.controladorRepartidores =
                controladorRepartidores;

        this.controladorEntregas =
                controladorEntregas;

        entregasMostradas =
                new ArrayList<>();

        configurarVentana();
        inicializarComponentes();
        cargarDatos();
    }

    private void configurarVentana() {

        setTitle(
                "SpeedFast - Gestión de Entregas"
        );

        setSize(800, 500);

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
                        new GridLayout(4, 2, 10, 10)
                );

        cmbPedido = new JComboBox<>();
        cmbRepartidor = new JComboBox<>();

        txtFecha = new JTextField();
        txtHora = new JTextField();

        panelFormulario.add(
                new JLabel("Pedido:")
        );
        panelFormulario.add(cmbPedido);

        panelFormulario.add(
                new JLabel("Repartidor:")
        );
        panelFormulario.add(
                cmbRepartidor
        );

        panelFormulario.add(
                new JLabel(
                        "Fecha (AAAA-MM-DD):"
                )
        );
        panelFormulario.add(txtFecha);

        panelFormulario.add(
                new JLabel(
                        "Hora (HH:MM):"
                )
        );
        panelFormulario.add(txtHora);

        String[] columnas = {
                "ID",
                "Pedido",
                "Repartidor",
                "Fecha",
                "Hora"
        };

        modeloTabla =
                new DefaultTableModel(
                        columnas,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        tablaEntregas =
                new JTable(modeloTabla);

        tablaEntregas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnActualizar =
                new JButton("Actualizar");

        JButton btnCerrar =
                new JButton("Cerrar");

        JPanel panelBotones =
                new JPanel(new FlowLayout());

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        panelPrincipal.add(
                panelFormulario,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                new JScrollPane(
                        tablaEntregas
                ),
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

        tablaEntregas
                .getSelectionModel()
                .addListSelectionListener(
                        e -> cargarSeleccion()
                );

        btnEditar.addActionListener(
                e -> editarEntrega()
        );

        btnEliminar.addActionListener(
                e -> eliminarEntrega()
        );

        btnActualizar.addActionListener(
                e -> cargarDatos()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );
    }

    private void cargarDatos() {

        cargarCombos();
        cargarTabla();
    }

    private void cargarCombos() {

        cmbPedido.removeAllItems();

        for (Pedido pedido :
                controladorPedidos
                        .listarPedidos()) {

            cmbPedido.addItem(pedido);
        }

        cmbRepartidor.removeAllItems();

        for (Repartidor repartidor :
                controladorRepartidores
                        .listarRepartidores()) {

            cmbRepartidor.addItem(
                    repartidor
            );
        }
    }

    private void cargarTabla() {

        entregasMostradas.clear();

        entregasMostradas.addAll(
                controladorEntregas
                        .listarEntregas()
        );

        modeloTabla.setRowCount(0);

        for (Entrega entrega :
                entregasMostradas) {

            modeloTabla.addRow(
                    new Object[]{
                            entrega.getId(),
                            entrega.getPedido(),
                            entrega.getRepartidor(),
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }

    private void cargarSeleccion() {

        int fila =
                tablaEntregas.getSelectedRow();

        if (fila == -1
                || fila >= entregasMostradas.size()) {

            return;
        }

        Entrega entrega =
                entregasMostradas.get(fila);

        seleccionarPedido(
                entrega
                        .getPedido()
                        .getId()
        );

        seleccionarRepartidor(
                entrega
                        .getRepartidor()
                        .getId()
        );

        txtFecha.setText(
                entrega
                        .getFecha()
                        .toString()
        );

        txtHora.setText(
                entrega
                        .getHora()
                        .toString()
        );
    }

    private void seleccionarPedido(
            int id
    ) {

        for (int i = 0;
             i < cmbPedido.getItemCount();
             i++) {

            Pedido pedido =
                    cmbPedido.getItemAt(i);

            if (pedido.getId() == id) {

                cmbPedido.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarRepartidor(
            int id
    ) {

        for (int i = 0;
             i < cmbRepartidor.getItemCount();
             i++) {

            Repartidor repartidor =
                    cmbRepartidor.getItemAt(i);

            if (repartidor.getId() == id) {

                cmbRepartidor
                        .setSelectedIndex(i);

                return;
            }
        }
    }

    private void editarEntrega() {

        int fila =
                tablaEntregas.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una entrega.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            int id =
                    (int) modeloTabla
                            .getValueAt(fila, 0);

            Pedido pedido =
                    (Pedido)
                            cmbPedido
                                    .getSelectedItem();

            Repartidor repartidor =
                    (Repartidor)
                            cmbRepartidor
                                    .getSelectedItem();

            LocalDate fecha =
                    LocalDate.parse(
                            txtFecha
                                    .getText()
                                    .trim()
                    );

            LocalTime hora =
                    LocalTime.parse(
                            txtHora
                                    .getText()
                                    .trim()
                    );

            controladorEntregas
                    .actualizarEntrega(
                            id,
                            pedido,
                            repartidor,
                            fecha,
                            hora
                    );

            cargarDatos();

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega actualizada correctamente."
            );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese una fecha y hora válidas.",
                    "Error de formato",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (RuntimeException e) {

            mostrarError(e);
        }
    }

    private void eliminarEntrega() {

        int fila =
                tablaEntregas.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una entrega.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Eliminar la entrega seleccionada?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int id =
                    (int) modeloTabla
                            .getValueAt(fila, 0);

            controladorEntregas
                    .eliminarEntrega(id);

            cargarDatos();

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada correctamente."
            );

        } catch (RuntimeException e) {

            mostrarError(e);
        }
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