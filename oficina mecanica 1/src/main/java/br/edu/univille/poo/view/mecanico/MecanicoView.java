package br.edu.univille.poo.view.mecanico;

import br.edu.univille.poo.control.mecanico.MecanicoController;
import br.edu.univille.poo.model.entity.Mecanico;
import br.edu.univille.poo.view.componente.ViewUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MecanicoView extends JFrame {

    private MecanicoController controller;

    private Integer id;

    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoTelefone;
    private JComboBox<String> comboEspecialidade;
    private JTextField campoPesquisa;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Mecanico> mecanicos = new ArrayList<>();

    public MecanicoView() {
        controller = new MecanicoController();

        setTitle("Cadastro de Mecânicos");
        setSize(780, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarLayout();
        carregarMecanicos();
    }

    private void configurarLayout() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Formulário
        campoCodigo = new JTextField();
        campoCodigo.setEditable(false);
        campoNome = new JTextField();
        campoCpf = new JTextField();
        campoTelefone = new JTextField();

        comboEspecialidade = new JComboBox<>(new String[]{
                "Motor", "Suspensão e freios", "Elétrica", "Funilaria e pintura", "Geral"
        });
        comboEspecialidade.setEditable(true);

        JPanel formulario = new JPanel(new GridLayout(0, 4, 8, 8));
        formulario.setBorder(ViewUtil.criarBorda("Dados do mecânico"));
        formulario.add(new JLabel("Código:"));
        formulario.add(campoCodigo);
        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);
        formulario.add(new JLabel("CPF:"));
        formulario.add(campoCpf);
        formulario.add(new JLabel("Telefone:"));
        formulario.add(campoTelefone);
        formulario.add(new JLabel("Especialidade:"));
        formulario.add(comboEspecialidade);
        formulario.add(new JLabel(""));
        formulario.add(new JLabel(""));

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
        botaoBuscar.addActionListener(e -> carregarMecanicos());
        campoPesquisa.addActionListener(e -> carregarMecanicos());

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelPesquisa.add(new JLabel("Pesquisar por nome:"));
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(botaoBuscar);

        modeloTabela = ViewUtil.criarModeloTabela("Código", "Nome", "CPF", "Telefone", "Especialidade");
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        ViewUtil.ajustarLarguras(tabela, 55, 180, 120, 120, 160);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCampos();
            }
        });

        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.setBorder(ViewUtil.criarBorda("Mecânicos cadastrados"));
        painelLista.add(painelPesquisa, BorderLayout.NORTH);
        painelLista.add(new JScrollPane(tabela), BorderLayout.CENTER);

        root.add(painelTopo, BorderLayout.NORTH);
        root.add(painelLista, BorderLayout.CENTER);

        add(root);
    }

    private void carregarMecanicos() {
        try {
            mecanicos = controller.pesquisar(campoPesquisa.getText().trim());

            modeloTabela.setRowCount(0);
            for (Mecanico mecanico : mecanicos) {
                modeloTabela.addRow(new Object[]{
                        mecanico.getId(),
                        mecanico.getNome(),
                        mecanico.getCpf(),
                        mecanico.getTelefone(),
                        mecanico.getEspecialidade()
                });
            }
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, "Erro ao carregar mecânicos: " + e.getMessage());
        }
    }

    private void preencherCampos() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Mecanico mecanico = mecanicos.get(linha);

        id = mecanico.getId();
        campoCodigo.setText(String.valueOf(mecanico.getId()));
        campoNome.setText(mecanico.getNome());
        campoCpf.setText(mecanico.getCpf());
        campoTelefone.setText(mecanico.getTelefone());
        comboEspecialidade.setSelectedItem(mecanico.getEspecialidade());
    }

    private void limparCampos() {
        tabela.clearSelection();
        id = null;
        campoCodigo.setText("");
        campoNome.setText("");
        campoCpf.setText("");
        campoTelefone.setText("");
        comboEspecialidade.setSelectedIndex(0);
        campoNome.requestFocus();
    }

    private void salvar() {
        Mecanico mecanico = new Mecanico();
        mecanico.setId(id);
        mecanico.setNome(campoNome.getText().trim());
        mecanico.setCpf(campoCpf.getText().trim());
        mecanico.setTelefone(campoTelefone.getText().trim());

        Object especialidade = comboEspecialidade.getSelectedItem();
        mecanico.setEspecialidade(especialidade == null ? "" : especialidade.toString().trim());

        try {
            controller.salvar(mecanico);
            ViewUtil.mensagemSucesso(this, "Mecânico salvo com sucesso.");
            carregarMecanicos();
            limparCampos();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }

    private void excluir() {
        if (id == null) {
            ViewUtil.mensagemErro(this, "Selecione um mecânico na tabela.");
            return;
        }
        if (!ViewUtil.confirmar(this, "Deseja realmente excluir o mecânico " + campoNome.getText() + "?")) {
            return;
        }
        try {
            controller.excluir(id);
            ViewUtil.mensagemSucesso(this, "Mecânico excluído com sucesso.");
            carregarMecanicos();
            limparCampos();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }
}
