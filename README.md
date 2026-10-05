# SpeedFast S8

Proyecto desarrollado en Java para la gestión de pedidos, repartidores y entregas de una empresa de reparto a domicilio.

Esta versión incorpora una arquitectura en capas, acceso a datos mediante JDBC, patrón DAO, interfaz gráfica con Swing y persistencia en MySQL. Además, utiliza Maven para gestionar automáticamente las dependencias necesarias para la conexión con la base de datos.

## Funcionalidades

- Registrar, editar, eliminar y listar pedidos.
- Registrar, editar, eliminar y listar repartidores.
- Registrar, editar, eliminar y listar entregas.
- Asignar repartidores a pedidos.
- Iniciar entregas y actualizar el estado de los pedidos.
- Persistir los cambios en una base de datos MySQL.
- Validar los datos ingresados desde la interfaz gráfica.
- Manejar errores de base de datos y mostrar mensajes mediante `JOptionPane`.

## Estados de los pedidos

Los pedidos utilizan los siguientes estados:

- `PENDIENTE`
- `EN_REPARTO`
- `ENTREGADO`

## Tipos de pedido

Los pedidos pueden ser de tipo:

- `COMIDA`
- `ENCOMIENDA`
- `EXPRESS`

## Arquitectura del proyecto

El proyecto se organiza en distintas capas para separar responsabilidades:

```text
src/
├── controller/
│   ├── ControladorPedidos.java
│   ├── ControladorRepartidores.java
│   └── ControladorEntregas.java
│
├── dao/
│   ├── PedidoDAO.java
│   ├── RepartidorDAO.java
│   ├── EntregaDAO.java
│   └── impl/
│       ├── PedidoDAOImpl.java
│       ├── RepartidorDAOImpl.java
│       └── EntregaDAOImpl.java
│
├── model/
│   ├── Pedido.java
│   ├── Repartidor.java
│   ├── Entrega.java
│   ├── EstadoPedido.java
│   └── TipoPedido.java
│
├── util/
│   └── ConexionBD.java
│
├── view/
│   ├── VentanaPrincipal.java
│   ├── VentanaRegistroPedido.java
│   ├── VentanaRegistroRepartidor.java
│   ├── VentanaListaPedidos.java
│   ├── VentanaAsignacionEntrega.java
│   └── VentanaGestionEntregas.java
│
└── main/
    └── Main.java
