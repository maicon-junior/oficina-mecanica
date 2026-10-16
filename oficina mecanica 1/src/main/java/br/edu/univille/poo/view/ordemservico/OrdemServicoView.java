package br.edu.univille.poo.view.ordemservico;

import br.edu.univille.poo.control.ordemservico.OrdemServicoController;
import br.edu.univille.poo.model.entity.*;
import br.edu.univille.poo.view.componente.ViewUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class OrdemServicoView extends JFrame {

    private OrdemServicoController controller;

    private OrdemServico ordem;

    private JTextField campoNumero;
    private JComboBox<StatusOrdem> comboStatus;
    private JComboBox<Veiculo> comboVeiculo;
    private JComboBox<Mecanico> comboMecanico;
    private JTextField campoDataAbertura;
    private JTextField campoDataConclusao;
    private JTextField campoObservacao;

    private JComboBox<Servico> comboServico;
    private JTextField campoQuantidade;
    private JTable tabelaItens;
    private DefaultTableModel modeloItens;
    private JLabel labelTotal;

    public OrdemServicoView() {
        controller = new OrdemServicoController();

        setTitle("Ordem de Serviço");
        setSize(960, 640);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarLayout();
        carregarCombos();
        novaOrdem();
    }

    public OrdemServicoView(Integer idOrdem) {
        this();
        try {
            ordem = controller.obterPeloId(idOrdem);
            mostrarOrdem();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, "Não foi possível carregar a ordem. " + e.getMessage());
        }
    }

    private void configurarLayout() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Dados da ordem
        campoNumero = new JTextField();
        campoNumero.setEditable(false);
        comboStatus = new JComboBox<>(StatusOrdem.values());
        comboVeiculo = new JComboBox<>();
        comboMecanico = new JComboBox<>();
        campoDataAbertura = new JTextField();
        campoDataConclusao = new JTextField();
        campoObservacao = new JTextField();

        campoDataAbertura.setToolTipText("Formato: dd/mm/aaaa");
        campoDataConclusao.setToolTipText("Formato: dd/mm/aaaa (deixe vazio se ainda não concluiu)");

        JPanel grade = new JPanel(new GridLayout(0, 4, 8, 8));
        grade.add(new JLabel("Nº da OS:"));
        grade.add(campoNumero);
        grade.add(new JLabel("Status:"));
        grade.add(comboStatus);
        grade.add(new JLabel("Veículo:"));
        grade.add(comboVeiculo);
        grade.add(new JLabel("Mecânico:"));
        grade.add(comboMecanico);
        grade.add(new JLabel("Data abertura:"));
        grade.add(campoDataAbertura);
        grade.add(new JLabel("Data conclusão:"));
        grade.add(campoDataConclusao);

        JPanel linhaObservacao = new JPanel(new BorderLayout(8, 8));
        linhaObservacao.add(new JLabel("Observação:"), BorderLayout.WEST);
        linhaObservacao.add(campoObservacao, BorderLayout.CENTER);

        JPanel painelDados = new JPanel(new BorderLayout(8, 8));
        painelDados.setBorder(ViewUtil.criarBorda("Dados da ordem de serviço"));
        painelDados.add(grade, BorderLayout.CENTER);
        painelDados.add(linhaObservacao, BorderLayout.SOUTH);

        // Serviços da ordem
        comboServico = new JComboBox<>();
        campoQuantidade = new JTextField("1", 4);
        JButton botaoAdicionar = new JButton("Adicionar serviço");
        JButton botaoRemover = new JButton("Remover selecionado");

        botaoAdicionar.addActionListener(e -> adicionarItem());
        botaoRemover.addActionListener(e -> removerItem());

        JPanel painelAdicionar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelAdicionar.add(new JLabel("Serviço:"));
        painelAdicionar.add(comboServico);
        painelAdicionar.add(new JLabel("Qtd:"));
        painelAdicionar.add(campoQuantidade);
        painelAdicionar.add(botaoAdicionar);
        painelAdicionar.add(botaoRemover);

        modeloItens = ViewUtil.criarModeloTabela("Serviço", "Quantidade", "Valor unitário", "Subtotal");
        tabelaItens = new JTable(modeloItens);
        tabelaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaItens.getTableHeader().setReorderingAllowed(false);
        ViewUtil.ajustarLarguras(tabelaItens, 400, 100, 150, 150);

        labelTotal = new JLabel();
        labelTotal.setFont(labelTotal.getFont().deriveFont(Font.BOLD, 16f));

        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelTotal.add(labelTotal);

        JPanel painelItens = new JPanel(new BorderLayout());
        painelItens.setBorder(ViewUtil.criarBorda("Serviços da ordem"));
        painelItens.add(painelAdicionar, BorderLayout.NORTH);
        painelItens.add(new JScrollPane(tabelaItens), BorderLayout.CENTER);
        painelItens.add(painelTotal, BorderLayout.SOUTH);

        // Botões
        JButton botaoNova = new JButton("Nova");
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoExcluir = new JButton("Excluir");
        JButton botaoFechar = new JButton("Fechar");

        botaoNova.addActionListener(e -> novaOrdem());
        botaoSalvar.addActionListener(e -> salvar());
        botaoExcluir.addActionListener(e -> excluir());
        botaoFechar.addActionListener(e -> dispose());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(botaoNova);
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoFechar);

        root.add(painelDados, BorderLayout.NORTH);
        root.add(painelItens, BorderLayout.CENTER);
        root.add(painelBotoes, BorderLayout.SOUTH);

        add(root);
    }

    private void carregarCombos() {
        try {
            comboVeiculo.removeAllItems();
            for (Veiculo veiculo : controller.obterVeiculos()) {
                comboVeiculo.addItem(veiculo);
            }

            comboMecanico.removeAllItems();
            for (Mecanico mecanico : controller.obterMecanicos()) {
                comboMecanico.addItem(mecanico);
            }

            comboServico.removeAllItems();
            for (Servico servico : controller.obterServicos()) {
                comboServico.addItem(servico);
            }
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, "Erro ao carregar listas: " + e.getMessage());
        }
    }

    private void mostrarOrdem() {
        campoNumero.setText(ordem.getId() == null ? "(nova)" : String.valueOf(ordem.getId()));
        comboStatus.setSelectedItem(ordem.getStatus());

        if (ordem.getVeiculo() != null) {
            comboVeiculo.setSelectedItem(ordem.getVeiculo());
        } else {
            comboVeiculo.setSelectedIndex(-1);
        }

        if (ordem.getMecanico() != null) {
            comboMecanico.setSelectedItem(ordem.getMecanico());
        } else {
            comboMecanico.setSelectedIndex(-1);
        }

        campoDataAbertura.setText(ViewUtil.formatarData(ordem.getDataAbertura()));
        campoDataConclusao.setText(ViewUtil.formatarData(ordem.getDataConclusao()));
        campoObservacao.setText(ordem.getObservacao() == null ? "" : ordem.getObservacao());

        atualizarItens();
    }

    private void atualizarItens() {
        modeloItens.setRowCount(0);

        for (ItemServico item : ordem.getItens()) {
            modeloItens.addRow(new Object[]{
                    item.getServico().getDescricao(),
                    item.getQuantidade(),
                    ViewUtil.formatarMoeda(item.getValorUnitario()),
                    ViewUtil.formatarMoeda(item.getSubtotal())
            });
        }

        labelTotal.setText("Total: " + ViewUtil.formatarMoeda(ordem.getValorTotal()));
    }

    private void adicionarItem() {
        try {
            Servico servico = (Servico) comboServico.getSelectedItem();
            if (servico == null) {
                throw new IllegalArgumentException("Selecione um serviço.");
            }

            int quantidade = ViewUtil.lerInteiro(campoQuantidade.getText(), "Qtd");
            if (quantidade <= 0) {
                throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
            }

            ItemServico item = new ItemServico();
            item.setServico(servico);
            item.setQuantidade(quantidade);
            item.setValorUnitario(servico.getValor());

            ordem.adicionarItem(item);

            campoQuantidade.setText("1");
            atualizarItens();

        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }

    private void removerItem() {
        int linha = tabelaItens.getSelectedRow();
        if (linha < 0) {
            ViewUtil.mensagemErro(this, "Selecione um serviço na tabela para remover.");
            return;
        }
        ordem.removerItem(linha);
        atualizarItens();
    }

    private void novaOrdem() {
        ordem = new OrdemServico();
        ordem.setStatus(StatusOrdem.ABERTA);
        ordem.setDataAbertura(LocalDate.now());

        carregarCombos();
        mostrarOrdem();
    }

    private void salvar() {
        try {
            ordem.setStatus((StatusOrdem) comboStatus.getSelectedItem());
            ordem.setVeiculo((Veiculo) comboVeiculo.getSelectedItem());
            ordem.setMecanico((Mecanico) comboMecanico.getSelectedItem());
            ordem.setDataAbertura(ViewUtil.lerData(campoDataAbertura.getText(), "Data abertura"));
            ordem.setDataConclusao(ViewUtil.lerData(campoDataConclusao.getText(), "Data conclusão"));
            ordem.setObservacao(campoObservacao.getText().trim());

            controller.salvar(ordem);

            ViewUtil.mensagemSucesso(this, "Ordem de serviço nº " + ordem.getId() + " salva com sucesso.");
            mostrarOrdem();

        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }

    private void excluir() {
        if (ordem.getId() == null) {
            ViewUtil.mensagemErro(this, "Esta ordem ainda não foi salva.");
            return;
        }
        if (!ViewUtil.confirmar(this, "Deseja realmente excluir a ordem nº " + ordem.getId() + "?")) {
            return;
        }
        try {
            controller.excluir(ordem.getId());
            ViewUtil.mensagemSucesso(this, "Ordem de serviço excluída com sucesso.");
            novaOrdem();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }
}
