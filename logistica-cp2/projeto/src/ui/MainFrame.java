package ui;

import domain.*;
import service.SistemaLogistica;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final SistemaLogistica sistema = new SistemaLogistica();

    private DashboardPanel dashPanel;
    private EntregadoresPanel entregadoresPanel;
    private EntregasPanel entregasPanel;
    private OperacoesPanel operacoesPanel;

    private JPanel contentArea;
    private CardLayout cardLayout;

    private static final String CARD_DASH   = "dashboard";
    private static final String CARD_ENTGS  = "entregadores";
    private static final String CARD_ENTR   = "entregas";
    private static final String CARD_OPS    = "operacoes";

    public MainFrame() {
        setTitle("Sistema de logística de entregas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());

        preloadDemoData();
        buildSidebar();
        buildContent();
        showCard(CARD_DASH, null);
    }

    private void buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.BG_CARD);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER));

        // Logo
        JPanel logoArea = new JPanel(new BorderLayout());
        logoArea.setOpaque(false);
        logoArea.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));
        JLabel logo = UiUtil.makeLabel("Sistema de logistica", new Font("Segoe UI", Font.BOLD, 17), Theme.ACCENT_BLUE);
        logoArea.add(logo, BorderLayout.CENTER);
        sidebar.add(logoArea, BorderLayout.NORTH);

        // Nav buttons
        JPanel nav = new JPanel(new GridLayout(4, 1, 0, 4));
        nav.setOpaque(false);
        nav.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        nav.add(navBtn("Dashboard",     CARD_DASH));
        nav.add(navBtn("Entregadores",  CARD_ENTGS));
        nav.add(navBtn("Entregas",      CARD_ENTR));
        nav.add(navBtn("Operações",     CARD_OPS));

        sidebar.add(nav, BorderLayout.CENTER);

        // Footer
        JLabel footer = UiUtil.makeLabel("  FIAP · 2ESPZ · Criado por Camila, Luana, Julia Silva, julia Cabral e Sidyellen", Theme.FONT_SMALL, Theme.TEXT_MUTED);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 14, 10));
        sidebar.add(footer, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);
    }

    private JButton navBtn(String text, String card) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2.setColor(new Color(99, 179, 237, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Theme.FONT_BODY);
        btn.setForeground(Theme.TEXT_PRIMARY);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> showCard(card, btn));
        return btn;
    }

    private void buildContent() {
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BG_DARK);

        dashPanel        = new DashboardPanel(sistema);
        entregadoresPanel= new EntregadoresPanel(sistema);
        entregasPanel    = new EntregasPanel(sistema);
        operacoesPanel   = new OperacoesPanel(sistema, this::refreshAll);

        contentArea.add(dashPanel,         CARD_DASH);
        contentArea.add(entregadoresPanel, CARD_ENTGS);
        contentArea.add(entregasPanel,     CARD_ENTR);
        contentArea.add(operacoesPanel,    CARD_OPS);

        add(contentArea, BorderLayout.CENTER);
    }

    private void showCard(String card, JButton src) {
        cardLayout.show(contentArea, card);
        refreshAll();
    }

    private void refreshAll() {
        dashPanel.refresh();
        entregadoresPanel.refresh();
        entregasPanel.refresh();
        operacoesPanel.refreshCombos();
    }

    private void preloadDemoData() {
        // Entregadores de exemplo
        sistema.cadastrarEntregador(new EntregadorMoto("Carlos Silva", "(11) 99001-1234"));
        sistema.cadastrarEntregador(new EntregadorBicicleta("Ana Lima", "(11) 98765-4321"));
        sistema.cadastrarEntregador(new EntregadorCarro("Roberto Souza", "(11) 97654-3210"));

        // Entregas de exemplo
        sistema.criarEntrega(new Entrega("Caixa de amoras frescas", "Av. Paulista, 1000 - SP", 5.2, 0.5));
        sistema.criarEntrega(new Entrega("Roupas da lavanderia",      "Rua Augusta, 200 - SP",   3.8, 2.1));
        sistema.criarEntrega(new Entrega("Caixa de Livros usados",    "Al. Santos, 500 - SP",    7.0, 8.5));

        // Simular uma atribuição
        sistema.atribuirEntrega(1, 1);
    }
}
