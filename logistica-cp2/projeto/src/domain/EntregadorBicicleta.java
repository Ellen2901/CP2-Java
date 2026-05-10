package domain;

public class EntregadorBicicleta extends Entregador {

    public EntregadorBicicleta(String nome, String telefone) {
        super(nome, telefone);
    }

    @Override
    public double getVelocidadeMedia() { return 20.0; } // km/h

    @Override
    public double getCapacidadeCarga() { return 10.0; } // kg

    @Override
    public double calcularCusto(double distanciaKm) {
        return 3.0 + (distanciaKm * 0.8);
    }

    @Override
    public String getTipoVeiculo() { return "Bicicleta"; }
}
