package domain;

/**
 * Classe abstrata que representa um entregador genérico.
 * Define atributos e comportamentos comuns a todos os tipos de entregadores.
 */

public abstract class Entregador implements Entregavel {
    private static int contadorId = 1;
    private final int id;
    private String nome;
    private String telefone;
    private boolean disponivel;

    public Entregador(String nome, String telefone) {
        this.id = contadorId++;
        this.nome = nome;
        this.telefone = telefone;
        this.disponivel = true;
    }

    // Método abstrato: cada entregador define sua velocidade média
    public abstract double getVelocidadeMedia();

    // Método abstrato: cada entregador define sua capacidade de carga (kg)
    public abstract double getCapacidadeCarga();

    // Sobrescrita obrigatória via interface
    @Override
    public boolean realizarEntrega(Entrega entrega) {
        if (!disponivel) return false;
        entrega.setEntregador(this);
        entrega.setStatus(StatusEntrega.EM_ROTA);
        this.disponivel = false;
        return true;
    }

    // Sobrescrita: finaliza a entrega e libera o entregador
    public void finalizarEntrega(Entrega entrega) {
        entrega.setStatus(StatusEntrega.ENTREGUE);
        this.disponivel = true;
    }

    @Override
    public String obterDisponibilidade() {
        return disponivel ? "Disponível" : "Em entrega";
    }

    // Sobrecarga: calcular tempo com distância apenas
    public double calcularTempoEntrega(double distanciaKm) {
        return distanciaKm / getVelocidadeMedia();
    }

    // Sobrecarga: calcular tempo com distância + fator de tráfego
    public double calcularTempoEntrega(double distanciaKm, double fatorTrafego) {
        return (distanciaKm / getVelocidadeMedia()) * fatorTrafego;
    }

    public abstract String getTipoVeiculo();

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - %s", id, nome, getTipoVeiculo(), obterDisponibilidade());
    }
}
