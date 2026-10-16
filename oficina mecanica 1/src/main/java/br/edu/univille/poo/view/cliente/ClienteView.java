package br.edu.univille.poo.view.cliente;

import br.edu.univille.poo.control.cliente.ClienteController;
import br.edu.univille.poo.model.entity.Cliente;
import br.edu.univille.poo.view.componente.ViewUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteView extends JFrame {

    private ClienteController controller;

    private Integer id;

    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private JTextField campoEndereco;
    private JTextField campoPesquisa;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Cliente> clientes = new ArrayList<>();

    public ClienteView() {
        controller = new ClienteController();

        setTitle("Cadastro de Clientes");
        setSize(820, 560);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarLayout();
        carregarClientes();
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
        campoEmail = new JTextField();
        campoEndereco = new JTextField();

        JPanel formulario = new JPanel(new GridLayout(0, 4, 8, 8));
        formulario.setBorder(ViewUtil.criarBorda("Dados do cliente"));
        formulario.add(new JLabel("Código:"));
        formulario.add(campoCodigo);
        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);
        formulario.add(new JLabel("CPF:"));
        formulario.add(campoCpf);
        formulario.add(new JLabel("Telefone:"));
        formulario.add(campoTelefone);
        formulario.add(new JLabel("E-mail:"));
        formulario.add(campoEmail);
        formulario.add(new JLabel("Endereço:"));
        formulario.add(campoEndereco);

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
        botaoBuscar.addActionListener(e -> carregarClientes());
        campoPesquisa.addActionListener(e -> carregarClientes());

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelPesquisa.add(new JLabel("Pesquisar por nome:"));
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(botaoBuscar);

        configurarTabela();

        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.setBorder(ViewUtil.criarBorda("Clientes cadastrados"));
        painelLista.add(painelPesquisa, BorderLayout.NORTH);
        painelLista.add(new JScrollPane(tabela), BorderLayout.CENTER);

        root.add(painelTopo, BorderLayout.NORTH);
        root.add(painelLista, BorderLayout.CENTER);

        add(root);
    }

    private void configurarTabela() {
        modeloTabela = ViewUtil.criarModeloTabela("Código", "Nome", "CPF", "Telefone", "E-mail", "Endereço");

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        ViewUtil.ajustarLarguras(tabela, 55, 160, 110, 110, 150, 200);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCampos();
            }
        });
    }

    private void carregarClientes() {
        try {
            clientes = controller.pesquisar(campoPesquisa.getText().trim());

            modeloTabela.setRowCount(0);
            for (Cliente cliente : clientes) {
                modeloTabela.addRow(new Object[]{
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getCpf(),
                        cliente.getTelefone(),
                        cliente.getEmail(),
                        cliente.getEndereco()
                });
            }
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void preencherCampos() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Cliente cliente = clientes.get(linha);

        id = cliente.getId();
        campoCodigo.setText(String.valueOf(cliente.getId()));
        campoNome.setText(cliente.getNome());
        campoCpf.setText(cliente.getCpf());
        campoTelefone.setText(cliente.getTelefone());
        campoEmail.setText(cliente.getEmail());
        campoEndereco.setText(cliente.getEndereco());
    }

    private void limparCampos() {
        tabela.clearSelection();
        id = null;
        campoCodigo.setText("");
        campoNome.setText("");
        campoCpf.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoEndereco.setText("");
        campoNome.requestFocus();
    }

    private void salvar() {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(campoNome.getText().trim());
        cliente.setCpf(campoCpf.getText().trim());
        cliente.setTelefone(campoTelefone.getText().trim());
        cliente.setEmail(campoEmail.getText().trim());
        cliente.setEndereco(campoEndereco.getText().trim());

        try {
            controller.salvar(cliente);
            ViewUtil.mensagemSucesso(this, "Cliente salvo com sucesso.");
            carregarClientes();
            limparCampos();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }

    private void excluir() {
        if (id == null) {
            ViewUtil.mensagemErro(this, "Selecione um cliente na tabela.");
            return;
        }
        if (!ViewUtil.confirmar(this, "Deseja realmente excluir o cliente " + campoNome.getText() + "?")) {
            return;
        }
        try {
            controller.excluir(id);
            ViewUtil.mensagemSucesso(this, "Cliente excluído com sucesso.");
            carregarClientes();
            limparCampos();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }
}
