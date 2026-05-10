package service;

import domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SistemaLogistica {
    private final List<Entregador> entregadores = new ArrayList<>();
    private final List<Entrega> entregas = new ArrayList<>();

    // ---- ENTREGADORES ----

    public void cadastrarEntregador(Entregador e) {
        entregadores.add(e);
    }

    public List<Entregador> listarEntregadores() {
        return new ArrayList<>(entregadores);
    }

    public List<Entregador> listarDisponiveis() {
        return entregadores.stream()
                .filter(Entregador::isDisponivel)
                .collect(Collectors.toList());
    }

    public Optional<Entregador> buscarEntregador(int id) {
        return entregadores.stream().filter(e -> e.getId() == id).findFirst();
    }

    // ---- ENTREGAS ----

    public void criarEntrega(Entrega entrega) {
        entregas.add(entrega);
    }

    public List<Entrega> listarEntregas() {
        return new ArrayList<>(entregas);
    }

    public List<Entrega> listarPorStatus(StatusEntrega status) {
        return entregas.stream()
                .filter(e -> e.getStatus() == status)
                .collect(Collectors.toList());
    }

    public Optional<Entrega> buscarEntrega(int id) {
        return entregas.stream().filter(e -> e.getId() == id).findFirst();
    }

    public boolean atribuirEntrega(int entregaId, int entregadorId) {
        Optional<Entrega> entrega = buscarEntrega(entregaId);
        Optional<Entregador> entregador = buscarEntregador(entregadorId);

        if (entrega.isEmpty() || entregador.isEmpty()) return false;
        if (entrega.get().getStatus() != StatusEntrega.PENDENTE) return false;
        if (!entregador.get().isDisponivel()) return false;

        // Verificar capacidade de carga
        if (entrega.get().getPesoKg() > entregador.get().getCapacidadeCarga()) return false;

        return entregador.get().realizarEntrega(entrega.get());
    }

    public boolean finalizarEntrega(int entregaId) {
        Optional<Entrega> entrega = buscarEntrega(entregaId);
        if (entrega.isEmpty()) return false;
        if (entrega.get().getStatus() != StatusEntrega.EM_ROTA) return false;

        Entregador entregador = entrega.get().getEntregador();
        if (entregador == null) return false;

        entregador.finalizarEntrega(entrega.get());
        return true;
    }

    public boolean cancelarEntrega(int entregaId, String motivo) {
        Optional<Entrega> entrega = buscarEntrega(entregaId);
        if (entrega.isEmpty()) return false;
        if (entrega.get().getStatus() == StatusEntrega.ENTREGUE) return false;

        // libera o entregador se estava em rota
        if (entrega.get().getStatus() == StatusEntrega.EM_ROTA) {
            Entregador e = entrega.get().getEntregador();
            if (e != null) e.setDisponivel(true);
        }

        entrega.get().setStatus(StatusEntrega.CANCELADO, motivo);
        return true;
    }

    // ---- ESTATÍSTICAS ----

    public int totalEntregas() { return entregas.size(); }
    public long totalPendentes() { return entregas.stream().filter(e -> e.getStatus() == StatusEntrega.PENDENTE).count(); }
    public long totalEmRota() { return entregas.stream().filter(e -> e.getStatus() == StatusEntrega.EM_ROTA).count(); }
    public long totalEntregues() { return entregas.stream().filter(e -> e.getStatus() == StatusEntrega.ENTREGUE).count(); }
    public long totalCancelados() { return entregas.stream().filter(e -> e.getStatus() == StatusEntrega.CANCELADO).count(); }
    public int totalEntregadores() { return entregadores.size(); }
    public long totalDisponiveis() { return entregadores.stream().filter(Entregador::isDisponivel).count(); }
}
