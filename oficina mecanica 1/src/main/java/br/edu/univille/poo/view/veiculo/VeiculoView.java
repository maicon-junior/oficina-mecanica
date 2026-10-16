package br.edu.univille.poo.view.veiculo;

import br.edu.univille.poo.control.veiculo.VeiculoController;
import br.edu.univille.poo.model.entity.Cliente;
import br.edu.univille.poo.model.entity.Veiculo;
import br.edu.univille.poo.view.componente.ViewUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoView extends JFrame {

    private VeiculoController controller;

    private Integer id;

    private JTextField campoCodigo;
    private JTextField campoPlaca;
    private JTextField campoMarca;
    private JTextField campoModelo;
    private JTextField campoAno;
    private JTextField campoCor;
    private JComboBox<Cliente> comboCliente;
    private JTextField campoPesquisa;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private List<Veiculo> veiculos = new ArrayList<>();

    public VeiculoView() {
        controller = new VeiculoController();

        setTitle("Cadastro de Veículos");
        setSize(820, 580);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarLayout();
        carregarClientes();
        carregarVeiculos();
    }

    private void configurarLayout() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Formulário
        campoCodigo = new JTextField();
        campoCodigo.setEditable(false);
        campoPlaca = new JTextField();
        campoMarca = new JTextField();
        campoModelo = new JTextField();
        campoAno = new JTextField();
        campoCor = new JTextField();
        comboCliente = new JComboBox<>();

        JPanel formulario = new JPanel(new GridLayout(0, 4, 8, 8));
        formulario.setBorder(ViewUtil.criarBorda("Dados do veículo"));
        formulario.add(new JLabel("Código:"));
        formulario.add(campoCodigo);
        formulario.add(new JLabel("Placa:"));
        formulario.add(campoPlaca);
        formulario.add(new JLabel("Marca:"));
        formulario.add(campoMarca);
        formulario.add(new JLabel("Modelo:"));
        formulario.add(campoModelo);
        formulario.add(new JLabel("Ano:"));
        formulario.add(campoAno);
        formulario.add(new JLabel("Cor:"));
        formulario.add(campoCor);
        formulario.add(new JLabel("Cliente (dono):"));
        formulario.add(comboCliente);
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
        campoPesquisa = new JTextField(15);
        JButton botaoBuscar = new JButton("Buscar");
        botaoBuscar.addActionListener(e -> carregarVeiculos());
        campoPesquisa.addActionListener(e -> carregarVeiculos());

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelPesquisa.add(new JLabel("Pesquisar por placa:"));
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(botaoBuscar);

        modeloTabela = ViewUtil.criarModeloTabela("Código", "Placa", "Marca", "Modelo", "Ano", "Cor", "Cliente");
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);
        ViewUtil.ajustarLarguras(tabela, 55, 90, 110, 110, 60, 90, 180);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCampos();
            }
        });

        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.setBorder(ViewUtil.criarBorda("Veículos cadastrados"));
        painelLista.add(painelPesquisa, BorderLayout.NORTH);
        painelLista.add(new JScrollPane(tabela), BorderLayout.CENTER);

        root.add(painelTopo, BorderLayout.NORTH);
        root.add(painelLista, BorderLayout.CENTER);

        add(root);
    }

    private void carregarClientes() {
        try {
            comboCliente.removeAllItems();
            for (Cliente cliente : controller.obterClientes()) {
                comboCliente.addItem(cliente);
            }
            comboCliente.setSelectedIndex(-1);
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void carregarVeiculos() {
        try {
            veiculos = controller.pesquisar(campoPesquisa.getText().trim());

            modeloTabela.setRowCount(0);
            for (Veiculo veiculo : veiculos) {
                modeloTabela.addRow(new Object[]{
                        veiculo.getId(),
                        veiculo.getPlaca(),
                        veiculo.getMarca(),
                        veiculo.getModelo(),
                        veiculo.getAno(),
                        veiculo.getCor(),
                        veiculo.getCliente().getNome()
                });
            }
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, "Erro ao carregar veículos: " + e.getMessage());
        }
    }

    private void preencherCampos() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Veiculo veiculo = veiculos.get(linha);

        id = veiculo.getId();
        campoCodigo.setText(String.valueOf(veiculo.getId()));
        campoPlaca.setText(veiculo.getPlaca());
        campoMarca.setText(veiculo.getMarca());
        campoModelo.setText(veiculo.getModelo());
        campoAno.setText(String.valueOf(veiculo.getAno()));
        campoCor.setText(veiculo.getCor());
        comboCliente.setSelectedItem(veiculo.getCliente());
    }

    private void limparCampos() {
        tabela.clearSelection();
        id = null;
        campoCodigo.setText("");
        campoPlaca.setText("");
        campoMarca.setText("");
        campoModelo.setText("");
        campoAno.setText("");
        campoCor.setText("");
        carregarClientes();
        campoPlaca.requestFocus();
    }

    private void salvar() {
        try {
            Veiculo veiculo = new Veiculo();
            veiculo.setId(id);
            veiculo.setPlaca(campoPlaca.getText().trim());
            veiculo.setMarca(campoMarca.getText().trim());
            veiculo.setModelo(campoModelo.getText().trim());
            veiculo.setAno(ViewUtil.lerInteiro(campoAno.getText(), "Ano"));
            veiculo.setCor(campoCor.getText().trim());
            veiculo.setCliente((Cliente) comboCliente.getSelectedItem());

            controller.salvar(veiculo);
            ViewUtil.mensagemSucesso(this, "Veículo salvo com sucesso.");
            carregarVeiculos();
            limparCampos();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }

    private void excluir() {
        if (id == null) {
            ViewUtil.mensagemErro(this, "Selecione um veículo na tabela.");
            return;
        }
        if (!ViewUtil.confirmar(this, "Deseja realmente excluir o veículo " + campoPlaca.getText() + "?")) {
            return;
        }
        try {
            controller.excluir(id);
            ViewUtil.mensagemSucesso(this, "Veículo excluído com sucesso.");
            carregarVeiculos();
            limparCampos();
        } catch (Exception e) {
            ViewUtil.mensagemErro(this, e.getMessage());
        }
    }
}
