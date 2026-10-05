package view;

import controller.ControladorEntregas;
import controller.ControladorPedidos;
import controller.ControladorRepartidores;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana para asignar repartidores e iniciar entregas.
 */
public class VentanaAsignacionEntrega extends JFrame {

    private final ControladorPedidos controladorPedidos;
    private final ControladorRepartidores controladorRepartidores;
    private final ControladorEntregas controladorEntregas;

    private JComboBox<Pedido> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;

    private JLabel lblEstado;

    private JButton btnAsignar;
    private JButton btnIniciar;
    private JButton btnActualizar;
    private JButton btnCerrar;

    private boolean cargandoDatos = false;

    public VentanaAsignacionEntrega(
            ControladorPedidos controladorPedidos,
            ControladorRepartidores controladorRepartidores,
            ControladorEntregas controladorEntregas) {

        this.controladorPedidos = controladorPedidos;
        this.controladorRepartidores = controladorRepartidores;
        this.controladorEntregas = controladorEntregas;

        configurarVentana();
        crearComponentes();

        cargarDatos(null, null);

        setVisible(true);
    }

    /**
     * Configura la ventana principal.
     */
    private void configurarVentana() {

        setTitle("SpeedFast - Asignación de Entregas");
        setSize(540, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    /**
     * Crea y distribuye los componentes.
     */
    private void crearComponentes() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(15, 20));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        20,
                        20,
                        20
                )
        );

        JPanel panelFormulario =
                new JPanel(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(7, 5, 7, 5);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        JLabel lblPedido =
                new JLabel("Pedido:");

        JLabel lblRepartidor =
                new JLabel("Repartidor:");

        JLabel lblTituloEstado =
                new JLabel("Estado:");

        comboPedidos =
                new JComboBox<>();

        comboRepartidores =
                new JComboBox<>();

        lblEstado =
                new JLabel("-");

        lblPedido.setFont(
                lblPedido.getFont()
                        .deriveFont(Font.BOLD)
        );

        lblRepartidor.setFont(
                lblRepartidor.getFont()
                        .deriveFont(Font.BOLD)
        );

        lblTituloEstado.setFont(
                lblTituloEstado.getFont()
                        .deriveFont(Font.BOLD)
        );

        lblEstado.setFont(
                lblEstado.getFont()
                        .deriveFont(Font.BOLD)
        );

        // Pedido
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;

        panelFormulario.add(
                lblPedido,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 0.7;

        panelFormulario.add(
                comboPedidos,
                gbc
        );

        // Repartidor
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;

        panelFormulario.add(
                lblRepartidor,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 0.7;

        panelFormulario.add(
                comboRepartidores,
                gbc
        );

        // Estado
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;

        panelFormulario.add(
                lblTituloEstado,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 0.7;

        panelFormulario.add(
                lblEstado,
                gbc
        );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                5,
                                0
                        )
                );

        btnAsignar =
                new JButton("Asignar repartidor");

        btnIniciar =
                new JButton("Iniciar entrega");

        btnActualizar =
                new JButton("Actualizar");

        btnCerrar =
                new JButton("Cerrar");

        panelBotones.add(btnAsignar);
        panelBotones.add(btnIniciar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        panelPrincipal.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);

        configurarEventos();
    }

    /**
     * Configura los eventos de la interfaz.
     */
    private void configurarEventos() {

        comboPedidos.addActionListener(e -> {

            if (!cargandoDatos) {
                actualizarInformacionPedido();
            }
        });

        btnAsignar.addActionListener(e ->
                asignarRepartidor()
        );

        btnIniciar.addActionListener(e ->
                iniciarEntrega()
        );

        btnActualizar.addActionListener(e -> {

            Pedido pedido =
                    (Pedido) comboPedidos.getSelectedItem();

            Integer idPedido =
                    pedido != null
                            ? pedido.getId()
                            : null;

            cargarDatos(
                    idPedido,
                    null
            );
        });

        btnCerrar.addActionListener(e ->
                dispose()
        );
    }

    /**
     * Recarga pedidos y repartidores.
     *
     * Si se entregan IDs, conserva exactamente
     * esos elementos seleccionados.
     */
    private void cargarDatos(
            Integer idPedidoSeleccionado,
            Integer idRepartidorSeleccionado) {

        cargandoDatos = true;

        comboPedidos.removeAllItems();
        comboRepartidores.removeAllItems();

        for (Pedido pedido :
                controladorPedidos.listarPedidos()) {

            comboPedidos.addItem(pedido);
        }

        for (Repartidor repartidor :
                controladorRepartidores.listarRepartidores()) {

            comboRepartidores.addItem(repartidor);
        }

        // Restaurar pedido
        if (idPedidoSeleccionado != null) {

            seleccionarPedidoPorId(
                    idPedidoSeleccionado
            );
        }

        // Restaurar repartidor
        if (idRepartidorSeleccionado != null) {

            seleccionarRepartidorPorId(
                    idRepartidorSeleccionado
            );
        }

        cargandoDatos = false;

        actualizarInformacionPedido();
    }

    /**
     * Selecciona un pedido mediante su ID.
     */
    private void seleccionarPedidoPorId(
            int idPedido) {

        for (int i = 0;
             i < comboPedidos.getItemCount();
             i++) {

            Pedido pedido =
                    comboPedidos.getItemAt(i);

            if (pedido.getId() == idPedido) {

                comboPedidos.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * Selecciona un repartidor mediante su ID.
     */
    private void seleccionarRepartidorPorId(
            int idRepartidor) {

        for (int i = 0;
             i < comboRepartidores.getItemCount();
             i++) {

            Repartidor repartidor =
                    comboRepartidores.getItemAt(i);

            if (repartidor.getId()
                    == idRepartidor) {

                comboRepartidores.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * Actualiza el estado y el repartidor
     * asociado al pedido seleccionado.
     */
    private void actualizarInformacionPedido() {

        Pedido pedido =
                (Pedido) comboPedidos.getSelectedItem();

        if (pedido == null) {

            lblEstado.setText("-");
            return;
        }

        // Estado
        lblEstado.setText(
                pedido.getEstado().name()
        );

        /*
         * Consultar en la base de datos si este pedido
         * ya posee un repartidor asignado.
         */
        Repartidor repartidorAsignado =
                controladorEntregas
                        .buscarRepartidorAsignado(
                                pedido
                        );

        if (repartidorAsignado != null) {

            seleccionarRepartidorPorId(
                    repartidorAsignado.getId()
            );
        }
    }

    /**
     * Asigna el repartidor seleccionado al pedido.
     */
    private void asignarRepartidor() {

        try {

            Pedido pedido =
                    (Pedido) comboPedidos
                            .getSelectedItem();

            Repartidor repartidor =
                    (Repartidor) comboRepartidores
                            .getSelectedItem();

            if (pedido == null) {

                throw new IllegalArgumentException(
                        "Debe seleccionar un pedido."
                );
            }

            if (repartidor == null) {

                throw new IllegalArgumentException(
                        "Debe seleccionar un repartidor."
                );
            }

            int idPedido =
                    pedido.getId();

            int idRepartidor =
                    repartidor.getId();

            controladorEntregas
                    .asignarRepartidor(
                            pedido,
                            repartidor
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor asignado correctamente.",
                    "SpeedFast",
                    JOptionPane.INFORMATION_MESSAGE
            );

            /*
             * Recarga conservando explícitamente
             * el pedido y repartidor recién utilizados.
             */
            cargarDatos(
                    idPedido,
                    idRepartidor
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Inicia la entrega del pedido seleccionado.
     */
    private void iniciarEntrega() {

        try {

            Pedido pedido =
                    (Pedido) comboPedidos
                            .getSelectedItem();

            if (pedido == null) {

                throw new IllegalArgumentException(
                        "Debe seleccionar un pedido."
                );
            }

            int idPedido =
                    pedido.getId();

            Repartidor repartidorAsignado =
                    controladorEntregas
                            .buscarRepartidorAsignado(
                                    pedido
                            );

            Integer idRepartidor = null;

            if (repartidorAsignado != null) {

                idRepartidor =
                        repartidorAsignado.getId();
            }

            controladorEntregas
                    .iniciarEntrega(pedido);

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega iniciada correctamente.",
                    "SpeedFast",
                    JOptionPane.INFORMATION_MESSAGE
            );

            /*
             * Recarga manteniendo el pedido
             * cuya entrega acaba de iniciarse.
             */
            cargarDatos(
                    idPedido,
                    idRepartidor
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}