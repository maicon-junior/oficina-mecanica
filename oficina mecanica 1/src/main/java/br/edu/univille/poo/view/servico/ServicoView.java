package br.edu.univille.poo.view.servico;

import br.edu.univille.poo.control.servico.ServicoController;
import br.edu.univille.poo.model.entity.Servico;
import br.edu.univille.poo.view.componente.ViewUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoView extends JFrame {

    private ServicoController controller;

    private Integer id;

    private JTextField campoCodigo;
    private JTextField campoDescricao;
    private JTextField campoValor;
    private JTextField campoPesquisa;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Servico> servicos = new ArrayList<>();

    public ServicoView() {
        controller = new ServicoController();

        setTitle("Cadastro de Serviços");
        setSize(680, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarLayout();
        carregarServicos();
    }

    private void configurarLayout() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Formulário
        campoCodigo = new JTextField();
        campoCodigo.setEditable(false);
        campoDescricao = new JTextField();
        campoValor = new JTextField();

        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.setBorder(ViewUtil.criarBorda("Dados do serviço"));
        formulario.add(new JLabel("Código:"));
        formulario.add(campoCodigo);
        formulario.add(new JLabel("Descrição:"));
        formulario.add(campoDescricao);
        formulario.add(new JLabel("Valor (R$):"));
        formulario.add(campoValor);

        // Botões
        JButton botaoNovo = new JButton("Novo");
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoExcluir = new JButton("Excluir");
        JButton botaoFechar = new JButton("Fechar");

        botaoNovo.addActionListener(e -> limparCampos());
        botaoSalvar.addActionListener(e -> salvar());
        botaoExcluir.addActionListener(e -> excluir());
        botaoFechar.addActionListener(e -> dispose());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(botaoNovo);
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoFechar);

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(formulario, BorderLayout.CENTER);
        painelTopo.add(painelBotoes, BorderLayout.SOUTH);

        // Tabela
        campoPesquisa = new JTextField(20);
        JButton botaoBuscar = new JButton("Buscar");
        botaoBuscar.addActionListener(e -> carregarServicos());
        campoPesquisa.addActionListener(e -> carregarServicos());

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelPesquisa.add(new JLabel("Pesquisar por descrição:"));
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(botaoBuscar);

        modeloTabela = ViewUtil.criarModeloTabela("Código", "Descrição", "Valor");
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        ViewUtil.ajustarLarguras(tabela, 60, 350, 120);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCampos();
            }
        });

        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.setBorder(ViewUtil.criarBorda("Serviços cadastrados"));
        painelLista.add(painelPesquisa, BorderLayout.NORTH);
        painelLista.add(new JScrollPane(tabela), BorderLayout.CENTER);

        root.add(painelTopo, BorderLayout.NORTH);
        root.add(painelLista, BorderLayout.CENTER);

        add(root);
    }

    private void carregarServicos() {
        try {
            servicos = controller.pesquisar(campoPesquisa.getText().trim());

            modeloTabela.setRowCount(0);
            for (Servico servico : servicos) {
                modeloTabela.addRow(new Object[]{
                        servico.getId(),
                        servico.getDescricao(),
                        ViewUtil.formatarMoeda(servico.getValor())
                });
            }
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, "Erro ao carregar serviços: " + e.getMessage());
        }
    }

    private void preencherCampos() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Servico servico = servicos.get(linha);

        id = servico.getId();
        campoCodigo.setText(String.valueOf(servico.getId()));
        campoDescricao.setText(servico.getDescricao());
        campoValor.setText(String.format("%.2f", servico.getValor()).replace(".", ","));
    }

    private void limparCampos() {
        tabela.clearSelection();
        id = null;
        campoCodigo.setText("");
        campoDescricao.setText("");
        campoValor.setText("");
        campoDescricao.requestFocus();
    }

    private void salvar() {
        try {
            Servico servico = new Servico();
            servico.setId(id);
            servico.setDescricao(campoDescricao.getText().trim());
            servico.setValor(ViewUtil.lerValor(campoValor.getText(), "Valor"));

            controller.salvar(servico);
            ViewUtil.mensagemSucesso(this, "Serviço salvo com sucesso.");
            carregarServicos();
            limparCampos();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }

    private void excluir() {
        if (id == null) {
            ViewUtil.mensagemErro(this, "Selecione um serviço na tabela.");
            return;
        }
        if (!ViewUtil.confirmar(this, "Deseja realmente excluir o serviço " + campoDescricao.getText() + "?")) {
            return;
        }
        try {
            controller.excluir(id);
            ViewUtil.mensagemSucesso(this, "Serviço excluído com sucesso.");
            carregarServicos();
            limparCampos();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }
}
