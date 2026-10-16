package br.edu.univille.poo.model.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseDAO {

    protected Connection connection() throws SQLException {
        return ConnectionFactory.getInstance().get();
    }

}
