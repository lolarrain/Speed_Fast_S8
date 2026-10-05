package model;

/**
 * Representa un pedido gestionado por SpeedFast.
 *
 * Un pedido posee un identificador, dirección,
 * tipo, estado y un repartidor opcionalmente asignado.
 */
public class Pedido {

    private final int id;
    private final String direccion;
    private final TipoPedido tipo;

    /**
     * Permite que los cambios de estado realizados
     * desde distintos hilos sean visibles inmediatamente.
     */
    private volatile EstadoPedido estado;

    private Repartidor repartidor;

    /**
     * Crea un pedido nuevo.
     *
     * Todo pedido nuevo comienza en estado PENDIENTE.
     *
     * @param id identificador del pedido
     * @param direccion dirección de entrega
     * @param tipo tipo de pedido
     */
    public Pedido(
            int id,
            String direccion,
            TipoPedido tipo
    ) {

        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = EstadoPedido.PENDIENTE;
    }

    /**
     * Reconstruye un pedido desde la base de datos.
     *
     * @param id identificador del pedido
     * @param direccion dirección de entrega
     * @param tipo tipo de pedido
     * @param estado estado almacenado
     */
    public Pedido(
            int id,
            String direccion,
            TipoPedido tipo,
            EstadoPedido estado
    ) {

        this.id = id;
        this.direccion = direccion;
        this.tipo = tipo;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public TipoPedido getTipo() {
        return tipo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    /**
     * Asigna un repartidor a un pedido pendiente.
     *
     * La asignación no cambia el estado del pedido.
     * El cambio a EN_REPARTO ocurre cuando se inicia
     * efectivamente la entrega.
     *
     * @param repartidor repartidor asignado
     */
    public void asignarRepartidor(
            Repartidor repartidor
    ) {

        if (estado != EstadoPedido.PENDIENTE) {

            throw new IllegalStateException(
                    "Solo se pueden asignar pedidos pendientes."
            );
        }

        if (repartidor == null) {

            throw new IllegalArgumentException(
                    "El repartidor no puede ser nulo."
            );
        }

        this.repartidor = repartidor;
    }

    /**
     * Inicia la entrega del pedido.
     *
     * Cambia el estado de PENDIENTE a EN_REPARTO.
     */
    public void iniciarEntrega() {

        if (repartidor == null) {

            throw new IllegalStateException(
                    "El pedido debe tener un repartidor asignado."
            );
        }

        if (estado != EstadoPedido.PENDIENTE) {

            throw new IllegalStateException(
                    "Solo se pueden iniciar pedidos pendientes."
            );
        }

        estado = EstadoPedido.EN_REPARTO;
    }

    /**
     * Finaliza una entrega en curso.
     *
     * Cambia el estado de EN_REPARTO a ENTREGADO.
     */
    public void completarEntrega() {

        if (estado != EstadoPedido.EN_REPARTO) {

            throw new IllegalStateException(
                    "El pedido debe estar en reparto."
            );
        }

        estado = EstadoPedido.ENTREGADO;
    }

    /**
     * Restaura el repartidor asociado a un pedido
     * recuperado desde la base de datos.
     *
     * @param repartidor repartidor asociado
     */
    public void restaurarRepartidor(
            Repartidor repartidor
    ) {

        this.repartidor = repartidor;
    }

    @Override
    public String toString() {

        return "Pedido "
                + id
                + " - "
                + direccion;
    }
}