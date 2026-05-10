package domain;

public class EntregadorCarro extends Entregador {

    public EntregadorCarro(String nome, String telefone) {
        super(nome, telefone);
    }

    @Override
    public double getVelocidadeMedia() { return 50.0; } // km/h

    @Override
    public double getCapacidadeCarga() { return 100.0; } // kg

    @Override
    public double calcularCusto(double distanciaKm) {
        return 8.0 + (distanciaKm * 2.2);
    }

    @Override
    public String getTipoVeiculo() { return "Carro"; }
}
