package ui;

import domain.*;
import service.SistemaLogistica;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OperacoesPanel extends JPanel {
    private final SistemaLogistica sistema;
    private final Runnable onRefresh;

    private JComboBox<String> cbEntregas, cbEntregadores, cbEntregasAcao;
    private JTextField tfMotivo;
    private JTextArea taLog;

    public OperacoesPanel(SistemaLogistica sistema, Runnable onRefresh) {
        this.sistema = sistema;
        this.onRefresh = onRefresh;
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(UiUtil.makeLabel("Operações", Theme.FONT_TITLE, Theme.TEXT_PRIMARY), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 12, 0));
        center.setOpaque(false);
        center.add(buildAtribuirCard());
        center.add(buildAcoesCard());
        add(center, BorderLayout.CENTER);

        taLog = new JTextArea(6, 0);
        taLog.setEditable(false);
        taLog.setFont(Theme.FONT_MONO);
        taLog.setBackground(new Color(10, 10, 18));
        taLog.setForeground(Theme.ACCENT_GREEN);
        taLog.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        JPanel logCard = UiUtil.card("Log de Operações");
        logCard.add(UiUtil.scrollWrap(taLog), BorderLayout.CENTER);
        add(logCard, BorderLayout.SOUTH);

        refreshCombos();
    }

    private JPanel buildAtribuirCard() {
        JPanel card = UiUtil.card("Atribuir Entrega");

        JPanel fields = new JPanel(new GridLayout(0, 1, 6, 6));
        fields.setOpaque(false);

        fields.add(UiUtil.makeLabel("Entrega (Pendente):", Theme.FONT_BODY, Theme.TEXT_MUTED));
        cbEntregas = UiUtil.makeCombo(new String[]{"—"});
        fields.add(cbEntregas);

        fields.add(UiUtil.makeLabel("Entregador (Disponível):", Theme.FONT_BODY, Theme.TEXT_MUTED));
        cbEntregadores = UiUtil.makeCombo(new String[]{"—"});
        fields.add(cbEntregadores);

        card.add(fields, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.setOpaque(false);
        JButton btnAtribuir = UiUtil.makeButton("Atribuir", Theme.ACCENT_PURPLE);
        btnAtribuir.addActionListener(e -> atribuir());
        btns.add(btnAtribuir);
        card.add(btns, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildAcoesCard() {
        JPanel card = UiUtil.card("Finalizar / Cancelar");

        JPanel fields = new JPanel(new GridLayout(0, 1, 6, 6));
        fields.setOpaque(false);

        fields.add(UiUtil.makeLabel("Entrega:", Theme.FONT_BODY, Theme.TEXT_MUTED));
        cbEntregasAcao = UiUtil.makeCombo(new String[]{"—"});
        fields.add(cbEntregasAcao);

        fields.add(UiUtil.makeLabel("Digite o motivo para o cancelamento):", Theme.FONT_BODY, Theme.TEXT_MUTED));
        tfMotivo = UiUtil.makeField(15);
        fields.add(tfMotivo);

        card.add(fields, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btns.setOpaque(false);
        JButton btnFinalizar = UiUtil.makeButton(" Finalizar", Theme.ACCENT_GREEN);
        JButton btnCancelar  = UiUtil.makeButton(" Cancelar", Theme.ACCENT_RED);
        btnFinalizar.addActionListener(e -> finalizar());
        btnCancelar.addActionListener(e -> cancelar());
        btns.add(btnFinalizar);
        btns.add(btnCancelar);
        card.add(btns, BorderLayout.SOUTH);
        return card;
    }

    private int parseId(JComboBox<String> cb) {
        String s = (String) cb.getSelectedItem();
        if (s == null || s.equals("—")) return -1;
        try { return Integer.parseInt(s.split("\\[")[1].split("]")[0]); }
        catch (Exception e) { return -1; }
    }

    private void atribuir() {
        int entId  = parseId(cbEntregas);
        int entgId = parseId(cbEntregadores);
        if (entId < 0 || entgId < 0) {
            UiUtil.showMsg(this, "Selecione uma entrega e um entregador.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean ok = sistema.atribuirEntrega(entId, entgId);
        if (ok) {
            Entrega entrega = sistema.buscarEntrega(entId).orElse(null);
            String valor = entrega != null ? entrega.getValorCorridaFormatado() : "-";
            log("✔ Entrega #" + entId + " atribuída ao entregador #" + entgId);
            log("Valor da corrida: " + valor);
            onRefresh.run();
            refreshCombos();
        } else {
            UiUtil.showMsg(this, "Não foi possível atribuir. Verifique capacidade de carga,\ndisponibilidade e status da entrega.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizar() {
        int id = parseId(cbEntregasAcao);
        if (id < 0) { UiUtil.showMsg(this, "Selecione uma entrega.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        boolean ok = sistema.finalizarEntrega(id);
        if (ok) { log("✔ Entrega #" + id + " finalizada com sucesso!"); onRefresh.run(); refreshCombos(); }
        else UiUtil.showMsg(this, "Entrega não está em rota ou não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void cancelar() {
        int id = parseId(cbEntregasAcao);
        String motivo = tfMotivo.getText().trim();
        if (id < 0) { UiUtil.showMsg(this, "Selecione uma entrega.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        if (motivo.isEmpty()) motivo = "Sem motivo informado";
        boolean ok = sistema.cancelarEntrega(id, motivo);
        if (ok) { log("✖ Entrega #" + id + " cancelada. Motivo: " + motivo); tfMotivo.setText(""); onRefresh.run(); refreshCombos(); }
        else UiUtil.showMsg(this, "Não é possível cancelar uma entrega já entregue.", "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void log(String msg) {
        taLog.append(msg + "\n");
        taLog.setCaretPosition(taLog.getDocument().getLength());
    }

    public void refreshCombos() {
        cbEntregas.removeAllItems();
        cbEntregasAcao.removeAllItems();
        cbEntregadores.removeAllItems();
        cbEntregas.addItem("—"); cbEntregasAcao.addItem("—"); cbEntregadores.addItem("—");

        sistema.listarPorStatus(StatusEntrega.PENDENTE).forEach(e ->
            cbEntregas.addItem(String.format("[%d] %s → %s", e.getId(), e.getDescricaoProduto(), e.getEnderecoDestino())));

        sistema.listarEntregas().stream()
            .filter(e -> e.getStatus() == StatusEntrega.PENDENTE || e.getStatus() == StatusEntrega.EM_ROTA)
            .forEach(e -> cbEntregasAcao.addItem(String.format("[%d] %s (%s)", e.getId(), e.getDescricaoProduto(), e.getStatus())));

        sistema.listarDisponiveis().forEach(e ->
            cbEntregadores.addItem(String.format("[%d] %s (%s)", e.getId(), e.getNome(), e.getTipoVeiculo())));
    }
}
