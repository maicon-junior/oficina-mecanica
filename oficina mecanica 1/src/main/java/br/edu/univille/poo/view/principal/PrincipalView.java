package br.edu.univille.poo.view.principal;

import br.edu.univille.poo.control.principal.PrincipalController;
import br.edu.univille.poo.model.entity.Resumo;
import br.edu.univille.poo.view.cliente.ClienteView;
import br.edu.univille.poo.view.componente.ViewUtil;
import br.edu.univille.poo.view.mecanico.MecanicoView;
import br.edu.univille.poo.view.ordemservico.ConsultaOrdensView;
import br.edu.univille.poo.view.ordemservico.OrdemServicoView;
import br.edu.univille.poo.view.servico.ServicoView;
import br.edu.univille.poo.view.veiculo.VeiculoView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PrincipalView extends JFrame {

    private PrincipalController controller;

    private JLabel labelClientes;
    private JLabel labelVeiculos;
    private JLabel labelAbertas;
    private JLabel labelAndamento;
    private JLabel labelConcluidas;

    public PrincipalView() {
        controller = new PrincipalController();

        configurarJanela();
        configurarMenu();
        configurarLayout();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                atualizarResumo();
            }
        });
    }

    private void configurarJanela() {
        setTitle("Oficina Mecânica - Sistema de Gestão");
        setSize(820, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void configurarMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu cadastros = new JMenu("Cadastros");
        cadastros.setMnemonic('C');

        JMenuItem itemClientes = new JMenuItem("Clientes");
        JMenuItem itemVeiculos = new JMenuItem("Veículos");
        JMenuItem itemMecanicos = new JMenuItem("Mecânicos");
        JMenuItem itemServicos = new JMenuItem("Serviços");
        JMenuItem itemSair = new JMenuItem("Sair");

        itemClientes.addActionListener(e -> abrirClientes());
        itemVeiculos.addActionListener(e -> abrirVeiculos());
        itemMecanicos.addActionListener(e -> abrirMecanicos());
        itemServicos.addActionListener(e -> abrirServicos());
        itemSair.addActionListener(e -> System.exit(0));

        cadastros.add(itemClientes);
        cadastros.add(itemVeiculos);
        cadastros.add(itemMecanicos);
        cadastros.add(itemServicos);
        cadastros.add(new JSeparator());
        cadastros.add(itemSair);

        JMenu ordens = new JMenu("Ordens de Serviço");
        ordens.setMnemonic('O');

        JMenuItem itemNovaOrdem = new JMenuItem("Nova Ordem de Serviço");
        JMenuItem itemConsultar = new JMenuItem("Consultar Ordens");

        itemNovaOrdem.addActionListener(e -> abrirNovaOrdem());
        itemConsultar.addActionListener(e -> abrirConsulta());

        ordens.add(itemNovaOrdem);
        ordens.add(itemConsultar);

        menuBar.add(cadastros);
        menuBar.add(ordens);

        setJMenuBar(menuBar);
    }

    private void configurarLayout() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JPanel painelTitulo = new JPanel(new GridLayout(2, 1));

        JLabel titulo = new JLabel("Sistema de Gestão de Oficina Mecânica", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));

        JLabel subtitulo = new JLabel("Escolha uma opção abaixo ou use o menu", SwingConstants.CENTER);

        painelTitulo.add(titulo);
        painelTitulo.add(subtitulo);

        // Acesso rápido
        JPanel painelBotoes = new JPanel(new GridLayout(2, 3, 15, 15));
        painelBotoes.setBorder(ViewUtil.criarBorda("Acesso rápido"));

        JButton botaoClientes = new JButton("Clientes");
        JButton botaoVeiculos = new JButton("Veículos");
        JButton botaoMecanicos = new JButton("Mecânicos");
        JButton botaoServicos = new JButton("Serviços");
        JButton botaoNovaOrdem = new JButton("Nova Ordem de Serviço");
        JButton botaoConsultar = new JButton("Consultar Ordens");

        botaoClientes.addActionListener(e -> abrirClientes());
        botaoVeiculos.addActionListener(e -> abrirVeiculos());
        botaoMecanicos.addActionListener(e -> abrirMecanicos());
        botaoServicos.addActionListener(e -> abrirServicos());
        botaoNovaOrdem.addActionListener(e -> abrirNovaOrdem());
        botaoConsultar.addActionListener(e -> abrirConsulta());

        painelBotoes.add(botaoClientes);
        painelBotoes.add(botaoVeiculos);
        painelBotoes.add(botaoMecanicos);
        painelBotoes.add(botaoServicos);
        painelBotoes.add(botaoNovaOrdem);
        painelBotoes.add(botaoConsultar);

        // Resumo
        JPanel painelResumo = new JPanel(new GridLayout(1, 5, 10, 10));
        painelResumo.setBorder(ViewUtil.criarBorda("Resumo"));

        labelClientes = new JLabel();
        labelVeiculos = new JLabel();
        labelAbertas = new JLabel();
        labelAndamento = new JLabel();
        labelConcluidas = new JLabel();

        painelResumo.add(labelClientes);
        painelResumo.add(labelVeiculos);
        painelResumo.add(labelAbertas);
        painelResumo.add(labelAndamento);
        painelResumo.add(labelConcluidas);

        root.add(painelTitulo, BorderLayout.NORTH);
        root.add(painelBotoes, BorderLayout.CENTER);
        root.add(painelResumo, BorderLayout.SOUTH);

        add(root);
    }

    private void atualizarResumo() {
        try {
            Resumo resumo = controller.obterResumo();

            labelClientes.setText("Clientes: " + resumo.getClientes());
            labelVeiculos.setText("Veículos: " + resumo.getVeiculos());
            labelAbertas.setText("OS abertas: " + resumo.getOrdensAbertas());
            labelAndamento.setText("Em andamento: " + resumo.getOrdensEmAndamento());
            labelConcluidas.setText("Concluídas: " + resumo.getOrdensConcluidas());

        } catch (Exception e) {
            labelClientes.setText("Resumo indisponível");
            e.printStackTrace();
        }
    }

    private void abrirClientes() {
        new ClienteView().setVisible(true);
    }

    private void abrirVeiculos() {
        new VeiculoView().setVisible(true);
    }

    private void abrirMecanicos() {
        new MecanicoView().setVisible(true);
    }

    private void abrirServicos() {
        new ServicoView().setVisible(true);
    }

    private void abrirNovaOrdem() {
        new OrdemServicoView().setVisible(true);
    }

    private void abrirConsulta() {
        new ConsultaOrdensView().setVisible(true);
    }
}
