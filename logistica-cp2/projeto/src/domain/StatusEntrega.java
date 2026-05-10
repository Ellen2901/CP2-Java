package domain;

public enum StatusEntrega {
    PENDENTE("Pendente"),
    EM_ROTA("Em Rota"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusEntrega(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
