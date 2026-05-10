package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class UiUtil {

    public static JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker().darker() :
                            getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(Theme.FONT_BODY.deriveFont(Font.BOLD));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 34));
        return btn;
    }

    public static JTextField makeField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(Theme.BG_INPUT);
        f.setForeground(Theme.TEXT_PRIMARY);
        f.setCaretColor(Theme.ACCENT_BLUE);
        f.setFont(Theme.FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(5, 8, 5, 8)));
        return f;
    }

    public static JLabel makeLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static JPanel card(String title) {
        JPanel p = new JPanel();
        p.setBackground(Theme.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(14, 16, 14, 16)));
        p.setLayout(new BorderLayout(0, 10));
        if (title != null && !title.isEmpty()) {
            JLabel lbl = makeLabel(title, Theme.FONT_H2, Theme.ACCENT_BLUE);
            p.add(lbl, BorderLayout.NORTH);
        }
        return p;
    }

    public static JComboBox<String> makeCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(Color.WHITE);
        cb.setForeground(Theme.TEXT_BLACK);
        cb.setFont(Theme.FONT_BODY);
        cb.setBorder(new LineBorder(Theme.BORDER, 1, true));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                label.setForeground(Theme.TEXT_BLACK);
                label.setBackground(isSelected ? new Color(220, 235, 250) : Color.WHITE);
                return label;
            }
        });
        return cb;
    }

    public static JScrollPane scrollWrap(JComponent c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBackground(Theme.BG_DARK);
        sp.getViewport().setBackground(Theme.BG_DARK);
        sp.setBorder(new LineBorder(Theme.BORDER, 1, true));
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    public static JTable makeTable(Object[][] data, String[] cols) {
        JTable t = new JTable(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        t.setBackground(Theme.BG_CARD);
        t.setForeground(Theme.TEXT_PRIMARY);
        t.setFont(Theme.FONT_BODY);
        t.setRowHeight(28);
        t.setGridColor(Theme.BORDER);
        t.setSelectionBackground(new Color(99, 179, 237, 60));
        t.setSelectionForeground(Theme.TEXT_PRIMARY);
        t.setShowVerticalLines(false);
        t.getTableHeader().setBackground(Theme.BG_INPUT);
        t.getTableHeader().setForeground(Theme.ACCENT_BLUE);
        t.getTableHeader().setFont(Theme.FONT_BODY.deriveFont(Font.BOLD));
        t.getTableHeader().setBorder(new LineBorder(Theme.BORDER));
        t.setIntercellSpacing(new Dimension(12, 4));
        return t;
    }

    public static void showMsg(Component parent, String msg, String title, int type) {
        UIManager.put("OptionPane.background", Theme.BG_CARD);
        UIManager.put("Panel.background", Theme.BG_CARD);
        UIManager.put("OptionPane.messageForeground", Theme.TEXT_PRIMARY);
        JOptionPane.showMessageDialog(parent, msg, title, type);
    }
}
