package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Entrega {
    private static int contadorId = 1;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final int id;
    private String descricaoProduto;
    private String enderecoDestino;
    private double distanciaKm;
    private double pesoKg;
    private StatusEntrega status;
    private Entregador entregador;
    private final LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Entrega(String descricaoProduto, String enderecoDestino, double distanciaKm, double pesoKg) {
        this.id = contadorId++;
        this.descricaoProduto = descricaoProduto;
        this.enderecoDestino = enderecoDestino;
        this.distanciaKm = distanciaKm;
        this.pesoKg = pesoKg;
        this.status = StatusEntrega.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Sobrecarga: atualiza status e diz o motivo
    public void setStatus(StatusEntrega status, String motivo) {
        this.status = status;
        this.dataAtualizacao = LocalDateTime.now();
        System.out.println("Status atualizado para " + status + ". Motivo: " + motivo);
    }

    public int getId() { return id; }
    public String getDescricaoProduto() { return descricaoProduto; }
    public void setDescricaoProduto(String d) { this.descricaoProduto = d; }
    public String getEnderecoDestino() { return enderecoDestino; }
    public void setEnderecoDestino(String e) { this.enderecoDestino = e; }
    public double getDistanciaKm() { return distanciaKm; }
    public double getPesoKg() { return pesoKg; }
    public StatusEntrega getStatus() { return status; }
    public Entregador getEntregador() { return entregador; }
    public void setEntregador(Entregador entregador) { this.entregador = entregador; }
    public String getDataCriacao() { return dataCriacao.format(FMT); }
    public String getDataAtualizacao() { return dataAtualizacao.format(FMT); }

    public double calcularValorCorrida() {
        if (entregador == null) return 0.0;
        return entregador.calcularCusto(distanciaKm);
    }

    public String getValorCorridaFormatado() {
        if (entregador == null) return "-";
        return String.format("R$ %.2f", calcularValorCorrida());
    }

    @Override
    public String toString() {
        String entregadorInfo = entregador != null ? entregador.getNome() : "Não atribuído";
        return String.format("[%d] %s → %s | %.1fkm | %.1fkg | %s | %s",
                id, descricaoProduto, enderecoDestino, distanciaKm, pesoKg, status, entregadorInfo);
    }
}
