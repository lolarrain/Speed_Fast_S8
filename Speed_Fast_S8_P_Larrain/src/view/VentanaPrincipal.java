package view;

import controller.ControladorEntregas;
import controller.ControladorPedidos;
import controller.ControladorRepartidores;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final ControladorPedidos controladorPedidos;

    private final ControladorRepartidores
            controladorRepartidores;

    private final ControladorEntregas
            controladorEntregas;

    public VentanaPrincipal(
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

        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {

        setTitle(
                "SpeedFast - Gestión de Entregas"
        );

        setSize(450, 430);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel lblTitulo =
                new JLabel(
                        "Sistema de Gestión de Entregas",
                        SwingConstants.CENTER
                );

        JPanel panelBotones =
                new JPanel(
                        new GridLayout(
                                5,
                                1,
                                10,
                                10
                        )
                );

        JButton btnPedidos =
                new JButton(
                        "Gestionar pedidos"
                );

        JButton btnRepartidores =
                new JButton(
                        "Gestionar repartidores"
                );

        JButton btnListar =
                new JButton(
                        "Listar pedidos"
                );

        JButton btnAsignacion =
                new JButton(
                        "Asignar repartidor / Iniciar entrega"
                );

        JButton btnEntregas =
                new JButton(
                        "Gestionar entregas"
                );

        panelBotones.add(btnPedidos);
        panelBotones.add(btnRepartidores);
        panelBotones.add(btnListar);
        panelBotones.add(btnAsignacion);
        panelBotones.add(btnEntregas);

        panelPrincipal.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                panelBotones,
                BorderLayout.CENTER
        );

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        add(panelPrincipal);

        btnPedidos.addActionListener(
                e -> new VentanaRegistroPedido(
                        controladorPedidos
                ).setVisible(true)
        );

        btnRepartidores.addActionListener(
                e -> new VentanaRegistroRepartidor(
                        controladorRepartidores
                ).setVisible(true)
        );

        btnListar.addActionListener(
                e -> new VentanaListaPedidos(
                        controladorPedidos
                ).setVisible(true)
        );

        btnAsignacion.addActionListener(
                e -> new VentanaAsignacionEntrega(
                        controladorPedidos,
                        controladorRepartidores,
                        controladorEntregas
                ).setVisible(true)
        );

        btnEntregas.addActionListener(
                e -> new VentanaGestionEntregas(
                        controladorPedidos,
                        controladorRepartidores,
                        controladorEntregas
                ).setVisible(true)
        );
    }
}