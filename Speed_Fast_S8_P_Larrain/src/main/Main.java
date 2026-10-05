package main;

import controller.ControladorEntregas;
import controller.ControladorPedidos;
import controller.ControladorRepartidores;
import view.VentanaPrincipal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ControladorPedidos
                    controladorPedidos =
                    new ControladorPedidos();

            ControladorRepartidores
                    controladorRepartidores =
                    new ControladorRepartidores();

            ControladorEntregas
                    controladorEntregas =
                    new ControladorEntregas();

            VentanaPrincipal ventana =
                    new VentanaPrincipal(
                            controladorPedidos,
                            controladorRepartidores,
                            controladorEntregas
                    );

            ventana.setVisible(true);
        });
    }
}