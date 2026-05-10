package ui;

import domain.*;
import service.SistemaLogistica;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class EntregasPanel extends JPanel {
    private final SistemaLogistica sistema;
    private DefaultTableModel tableModel;
    private JTextField tfProduto, tfEndereco, tfDistancia, tfPeso;

    public EntregasPanel(SistemaLogistica sistema) {
        this.sistema = sistema;
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(UiUtil.makeLabel(" Gerenciar Entregas", Theme.FONT_TITLE, Theme.TEXT_PRIMARY), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.WEST);
        add(buildTable(), BorderLayout.CENTER);
        refresh();
    }

    private JPanel buildForm() {
        JPanel card = UiUtil.card("Nova Entrega");
        card.setPreferredSize(new Dimension(270, 0));

        JPanel fields = new JPanel(new GridLayout(0, 1, 6, 6));
        fields.setOpaque(false);

        fields.add(UiUtil.makeLabel("Produto:", Theme.FONT_BODY, Theme.TEXT_MUTED));
        tfProduto = UiUtil.makeField(15); fields.add(tfProduto);

        fields.add(UiUtil.makeLabel("Endereço Destino:", Theme.FONT_BODY, Theme.TEXT_MUTED));
        tfEndereco = UiUtil.makeField(15); fields.add(tfEndereco);

        fields.add(UiUtil.makeLabel("Distância (km):", Theme.FONT_BODY, Theme.TEXT_MUTED));
        tfDistancia = UiUtil.makeField(8); fields.add(tfDistancia);

        fields.add(UiUtil.makeLabel("Peso (kg):", Theme.FONT_BODY, Theme.TEXT_MUTED));
        tfPeso = UiUtil.makeField(8); fields.add(tfPeso);

        card.add(fields, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btns.setOpaque(false);
        JButton btnCriar = UiUtil.makeButton("+ Criar", Theme.ACCENT_GREEN);
        btnCriar.addActionListener(e -> criarEntrega());
        btns.add(btnCriar);

        card.add(btns, BorderLayout.SOUTH);
        return card;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Produto", "Destino", "Dist.(km)", "Peso(kg)", "Status", "Entregador", "Valor", "Criado em"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = UiUtil.makeTable(new Object[0][0], new String[0]);
        table.setModel(tableModel);
        return UiUtil.scrollWrap(table);
    }

    private void criarEntrega() {
        try {
            String prod = tfProduto.getText().trim();
            String end  = tfEndereco.getText().trim();
            double dist = Double.parseDouble(tfDistancia.getText().trim());
            double peso = Double.parseDouble(tfPeso.getText().trim());

            if (prod.isEmpty() || end.isEmpty()) throw new IllegalArgumentException("campos vazios");

            Entrega e = new Entrega(prod, end, dist, peso);
            sistema.criarEntrega(e);
            tfProduto.setText(""); tfEndereco.setText("");
            tfDistancia.setText(""); tfPeso.setText("");
            refresh();
            UiUtil.showMsg(this, "Entrega #" + e.getId() + " criada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            UiUtil.showMsg(this, "Preencha todos os campos corretamente.\n(distância e peso devem ser numéricos)", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<Entrega> lista = sistema.listarEntregas();
        for (Entrega e : lista) {
            String ent = e.getEntregador() != null ? e.getEntregador().getNome() : "—";
            tableModel.addRow(new Object[]{
                e.getId(), e.getDescricaoProduto(), e.getEnderecoDestino(),
                e.getDistanciaKm(), e.getPesoKg(), e.getStatus(), ent, e.getValorCorridaFormatado(), e.getDataCriacao()
            });
        }
    }
}
