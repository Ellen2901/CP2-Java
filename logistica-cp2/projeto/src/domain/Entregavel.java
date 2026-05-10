package domain;

/**
 * Interface que define o comportamento de entrega.
 * Qualquer entidade capaz de realizar uma entrega deve implementá-la.
 */
public interface Entregavel {
    boolean realizarEntrega(Entrega entrega);
    double calcularCusto(double distanciaKm);
    String obterDisponibilidade();
}
