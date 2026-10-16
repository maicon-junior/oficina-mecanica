package br.edu.univille.poo.view.ordemservico;

import br.edu.univille.poo.control.ordemservico.ConsultaOrdensController;
import br.edu.univille.poo.model.entity.OrdemServico;
import br.edu.univille.poo.model.entity.StatusOrdem;
import br.edu.univille.poo.view.componente.ViewUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ConsultaOrdensView extends JFrame {

    private ConsultaOrdensController controller;

    private JComboBox<Object> comboStatus;
    private JTextField campoPlaca;
    private JLabel labelQuantidade;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<OrdemServico> ordens = new ArrayList<>();

    public ConsultaOrdensView() {
        controller = new ConsultaOrdensController();

        setTitle("Consulta de Ordens de Serviço");
        setSize(980, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarLayout();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                carregarOrdens(false);
            }
        });
    }

    private void configurarLayout() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Filtros
        comboStatus = new JComboBox<>();
        comboStatus.addItem("Todos");
        for (StatusOrdem status : StatusOrdem.values()) {
            comboStatus.addItem(status);
        }

        campoPlaca = new JTextField(10);
        JButton botaoPesquisar = new JButton("Pesquisar");

        botaoPesquisar.addActionListener(e -> carregarOrdens(true));
        campoPlaca.addActionListener(e -> carregarOrdens(true));

        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltros.setBorder(ViewUtil.criarBorda("Filtros"));
        painelFiltros.add(new JLabel("Status:"));
        painelFiltros.add(comboStatus);
        painelFiltros.add(new JLabel("Placa:"));
        painelFiltros.add(campoPlaca);
        painelFiltros.add(botaoPesquisar);

        // Tabela
        modeloTabela = ViewUtil.criarModeloTabela(
                "Nº", "Abertura", "Conclusão", "Cliente", "Veículo", "Mecânico", "Status", "Total");

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        ViewUtil.ajustarLarguras(tabela, 40, 90, 90, 150, 190, 130, 110, 100);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    abrirSelecionada();
                }
            }
        });

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(ViewUtil.criarBorda("Ordens encontradas"));
        painelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Rodapé
        labelQuantidade = new JLabel();

        JButton botaoNova = new JButton("Nova OS");
        JButton botaoAbrir = new JButton("Abrir OS selecionada");
        JButton botaoFechar = new JButton("Fechar");

        botaoNova.addActionListener(e -> new OrdemServicoView().setVisible(true));
        botaoAbrir.addActionListener(e -> abrirSelecionada());
        botaoFechar.addActionListener(e -> dispose());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(botaoNova);
        painelBotoes.add(botaoAbrir);
        painelBotoes.add(botaoFechar);

        JPanel painelRodape = new JPanel(new BorderLayout());
        painelRodape.add(labelQuantidade, BorderLayout.WEST);
        painelRodape.add(painelBotoes, BorderLayout.EAST);

        root.add(painelFiltros, BorderLayout.NORTH);
        root.add(painelTabela, BorderLayout.CENTER);
        root.add(painelRodape, BorderLayout.SOUTH);

        add(root);
    }

    private void carregarOrdens(boolean mostrarErros) {
        try {
            Object selecionado = comboStatus.getSelectedItem();
            StatusOrdem status = selecionado instanceof StatusOrdem s ? s : null;

            ordens = controller.pesquisar(status, campoPlaca.getText().trim());

            modeloTabela.setRowCount(0);
            for (OrdemServico ordem : ordens) {
                modeloTabela.addRow(new Object[]{
                        ordem.getId(),
                        ViewUtil.formatarData(ordem.getDataAbertura()),
                        ViewUtil.formatarData(ordem.getDataConclusao()),
                        ordem.getVeiculo().getCliente().getNome(),
                        ordem.getVeiculo().getPlaca() + " - " + ordem.getVeiculo().getModelo(),
                        ordem.getMecanico().getNome(),
                        ordem.getStatus(),
                        ViewUtil.formatarMoeda(ordem.getValorTotal())
                });
            }

            labelQuantidade.setText(ordens.size() + " ordem(ns) encontrada(s)");

        } catch (Exception e) {
            if (mostrarErros) {
                ViewUtil.mensagemErro(this, "Erro ao pesquisar ordens: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    private void abrirSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            ViewUtil.mensagemErro(this, "Selecione uma ordem na tabela.");
            return;
        }
        new OrdemServicoView(ordens.get(linha).getId()).setVisible(true);
    }
}
