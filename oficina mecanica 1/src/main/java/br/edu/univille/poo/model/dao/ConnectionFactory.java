package br.edu.univille.poo.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionFactory {

    private static final String SERVIDOR = "jdbc:mysql://localhost:3306/";
    private static final String BANCO = "oficina";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    private static ConnectionFactory instance;

    private ConnectionFactory() {
    }

    public synchronized static ConnectionFactory getInstance() {
        if (Objects.isNull(instance)) instance = new ConnectionFactory();
        return instance;
    }

    public Connection get() throws SQLException {
        return DriverManager.getConnection(SERVIDOR + BANCO, USUARIO, SENHA);
    }

    public Connection getServidor() throws SQLException {
        return DriverManager.getConnection(SERVIDOR, USUARIO, SENHA);
    }

    public String getNomeBanco() {
        return BANCO;
    }

}
