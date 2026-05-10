package domain;

public class EntregadorMoto extends Entregador {

    public EntregadorMoto(String nome, String telefone) {
        super(nome, telefone);
    }

    @Override
    public double getVelocidadeMedia() { return 60.0; } // km/h

    @Override
    public double getCapacidadeCarga() { return 20.0; } // kg

    @Override
    public double calcularCusto(double distanciaKm) {
        return 5.0 + (distanciaKm * 1.5);
    }

    @Override
    public String getTipoVeiculo() { return "Moto"; }
}
