/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifsul.as.todo.db;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import totalcross.db.sqlite.SQLiteUtil;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.sys.Settings;
import totalcross.sys.Vm;

/**
 *
 * @author admin
 */
public class Database {

    private SQLiteUtil util;
    private static Database instance;

    private Database() {
        try {
            util = new SQLiteUtil(Settings.appPath, "database.db");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            return new Database();
        } else {
            return instance;
        }
    }

    public void createLoginTable() throws SQLException {
        Vm.debug(util.fullPath);
        Statement st = util.con().createStatement();
        st.execute("create table if not exists person (nome varchar(50), "
                + "login varchar(20), password varchar(20), manterLogado varchar(1))");
        st.close();

    }

    public void createUsuarioTable() throws SQLException {
        Statement st = util.con().createStatement();
        st.execute("create table if not exists usuario (nome varchar(50), "
                + "login varchar(20), senha varchar(20), manterLogado varchar(1))");
        st.close();
    }

    public void inserirUsuario(String nome, String login, String senha) throws SQLException {
        Statement st = util.con().createStatement();
        st.executeUpdate("insert into usuario values('" + nome + "','"
                + login + "','" + senha + "','N')");
        st.close();
    }
    
    public boolean checaUsuarioSenha(String login, String senha) throws SQLException {
        PreparedStatement ps = util.con().prepareStatement("select login, senha from usuario where login=? and senha=?");
        ps.setString(1, login);
        ps.setString(2, senha);
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        } else return false;
    }
}

