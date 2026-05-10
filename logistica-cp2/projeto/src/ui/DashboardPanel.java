package ui;

import service.SistemaLogistica;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private final SistemaLogistica sistema;

    // Stat labels
    private JLabel lblTotalEntregas, lblPendentes, lblEmRota, lblEntregues, lblCancelados;
    private JLabel lblTotalEntregadores, lblDisponiveis;

    public DashboardPanel(SistemaLogistica sistema) {
        this.sistema = sistema;
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel header = UiUtil.makeLabel("Dashboard", Theme.FONT_TITLE, Theme.TEXT_PRIMARY);
        add(header, BorderLayout.NORTH);

        // Stats grid
        JPanel grid = new JPanel(new GridLayout(2, 4, 12, 12));
        grid.setOpaque(false);

        lblTotalEntregas   = addStatCard(grid, "Total de Entregas", "0", Theme.ACCENT_BLUE);
        lblPendentes       = addStatCard(grid, "Pendentes",         "0", Theme.ACCENT_ORANGE);
        lblEmRota          = addStatCard(grid, "Em Rota",           "0", Theme.ACCENT_PURPLE);
        lblEntregues       = addStatCard(grid, "Entregues",         "0", Theme.ACCENT_GREEN);
        lblCancelados      = addStatCard(grid, "Cancelados",        "0", Theme.ACCENT_RED);
        lblTotalEntregadores = addStatCard(grid, "Entregadores",    "0", Theme.ACCENT_BLUE);
        lblDisponiveis     = addStatCard(grid, "Disponíveis",       "0", Theme.ACCENT_GREEN);
        // empty filler
        JPanel filler = new JPanel(); filler.setOpaque(false); grid.add(filler);

        add(grid, BorderLayout.CENTER);

        // Tip
        JLabel tip = UiUtil.makeLabel("Use o menu lateral para gerenciar o sistema.",
                Theme.FONT_BODY, Theme.TEXT_MUTED);
        add(tip, BorderLayout.SOUTH);

        refresh();
    }

    private JLabel addStatCard(JPanel grid, String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 1, true),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JLabel valLbl = UiUtil.makeLabel(value, new Font("Segoe UI", Font.BOLD, 28), accent);
        JLabel titLbl = UiUtil.makeLabel(title, Theme.FONT_SMALL, Theme.TEXT_MUTED);

        card.add(valLbl, BorderLayout.CENTER);
        card.add(titLbl, BorderLayout.SOUTH);
        grid.add(card);
        return valLbl;
    }

    public void refresh() {
        lblTotalEntregas.setText(String.valueOf(sistema.totalEntregas()));
        lblPendentes.setText(String.valueOf(sistema.totalPendentes()));
        lblEmRota.setText(String.valueOf(sistema.totalEmRota()));
        lblEntregues.setText(String.valueOf(sistema.totalEntregues()));
        lblCancelados.setText(String.valueOf(sistema.totalCancelados()));
        lblTotalEntregadores.setText(String.valueOf(sistema.totalEntregadores()));
        lblDisponiveis.setText(String.valueOf(sistema.totalDisponiveis()));
        revalidate();
        repaint();
    }
}
