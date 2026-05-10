package ui;

import domain.*;
import service.SistemaLogistica;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EntregadoresPanel extends JPanel {
    private final SistemaLogistica sistema;
    private DefaultTableModel tableModel;
    private JTextField tfNome, tfTelefone;
    private JComboBox<String> cbTipo;

    public EntregadoresPanel(SistemaLogistica sistema) {
        this.sistema = sistema;
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(UiUtil.makeLabel(" Gerenciar Entregadores", Theme.FONT_TITLE, Theme.TEXT_PRIMARY), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.WEST);
        add(buildTable(), BorderLayout.CENTER);
        refresh();
    }

    private JPanel buildForm() {
        JPanel card = UiUtil.card("Cadastrar Entregador");
        card.setPreferredSize(new Dimension(260, 0));

        JPanel fields = new JPanel(new GridLayout(0, 1, 6, 6));
        fields.setOpaque(false);

        fields.add(UiUtil.makeLabel("Nome:", Theme.FONT_BODY, Theme.TEXT_MUTED));
        tfNome = UiUtil.makeField(15);
        fields.add(tfNome);

        fields.add(UiUtil.makeLabel("Telefone:", Theme.FONT_BODY, Theme.TEXT_MUTED));
        tfTelefone = UiUtil.makeField(15);
        fields.add(tfTelefone);

        fields.add(UiUtil.makeLabel("Tipo de Veículo:", Theme.FONT_BODY, Theme.TEXT_MUTED));
        cbTipo = UiUtil.makeCombo(new String[]{"Moto", "Bicicleta", "Carro",});
        cbTipo.setForeground(Theme.TEXT_BLACK);
        fields.add(cbTipo);

        card.add(fields, BorderLayout.CENTER);

        JButton btnCadastrar = UiUtil.makeButton("+ Cadastrar", Theme.ACCENT_GREEN);
        btnCadastrar.addActionListener(e -> cadastrar());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setOpaque(false);
        btnPanel.add(btnCadastrar);
        card.add(btnPanel, BorderLayout.SOUTH);
        return card;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nome", "Telefone", "Veículo", "Velocidade", "Cap.(kg)", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = UiUtil.makeTable(new Object[0][0], new String[0]);
        table.setModel(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        return UiUtil.scrollWrap(table);
    }

    private void cadastrar() {
        String nome = tfNome.getText().trim();
        String tel  = tfTelefone.getText().trim();
        if (nome.isEmpty() || tel.isEmpty()) {
            UiUtil.showMsg(this, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tipo = (String) cbTipo.getSelectedItem();
        Entregador e = switch (tipo) {
            case "Bicicleta" -> new EntregadorBicicleta(nome, tel);
            case "Carro"     -> new EntregadorCarro(nome, tel);
            default          -> new EntregadorMoto(nome, tel);
        };
        sistema.cadastrarEntregador(e);
        tfNome.setText(""); tfTelefone.setText("");
        refresh();
        UiUtil.showMsg(this, "Entregador cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<Entregador> lista = sistema.listarEntregadores();
        for (Entregador e : lista) {
            tableModel.addRow(new Object[]{
                e.getId(), e.getNome(), e.getTelefone(), e.getTipoVeiculo(),
                e.getVelocidadeMedia() + " km/h", e.getCapacidadeCarga() + " kg",
                e.obterDisponibilidade()
            });
        }
    }
}
