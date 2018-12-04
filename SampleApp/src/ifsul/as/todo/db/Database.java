/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifsul.as.todo.db;

import ifsul.as.todo.db.entity.Task;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import totalcross.db.sqlite.SQLiteUtil;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.sys.Settings;

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

    public void createTaskTable() throws SQLException {
        Statement st = util.con().createStatement();
        st.execute("create table if not exists task (id INTEGER PRIMARY KEY AUTOINCREMENT, descricao varchar(150), "
                + "usuario varchar(20), categoria varchar(30), status varchar(1), datatask datetime)");
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

    public int inserirTask(Task task) throws SQLException {
        PreparedStatement ps = util.con().prepareStatement("insert into task (descricao, usuario, categoria, status, datatask) values (?,?,?,?,?)");
        ps.setString(1, task.getDescricao());
        ps.setString(2, task.getUsuario());
        ps.setString(3, task.getCategoria());
        ps.setString(4, task.getStatus().substring(0, 1));
        ps.setDate(5, task.getDataTask());
        int rows = ps.executeUpdate();
        ps.close();
        return rows;
    }

    public boolean checaUsuarioSenha(String login, String senha) throws SQLException {
        PreparedStatement ps = util.con().prepareStatement("select login, senha from usuario where login=? and senha=?");
        ps.setString(1, login);
        ps.setString(2, senha);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            rs.close();
            ps.close();
            return true;
        } else {
            rs.close();
            ps.close();
            return false;
        }
    }

    public ArrayList<String> getCategoriaList() {
        return new ArrayList<>();
    }

    public Task getTaskByID(int id) throws SQLException {
        PreparedStatement ps = util.con().prepareStatement("select * from task where id = ?");
        ps.setInt(1, id);
        
        ResultSet rs = ps.executeQuery();
        Task t = new Task();
        if (rs.next()) {
            t = new Task();
            t.setCategoria(rs.getString("categoria"));
            t.setDescricao(rs.getString("descricao"));
            t.setId(rs.getInt("id"));
            t.setDataTask(rs.getDate("datatask"));
            t.setStatus(rs.getString("status"));
            t.setUsuario(rs.getString("usuario"));

        }

        ps.close();
        return t;
    }

    public ArrayList<Task> getTaskList(String usuario, String categoria, String status) throws SQLException {
        PreparedStatement ps = util.con().prepareStatement("select * from task where status like ? and usuario=? and categoria like ? order by datatask");
        ps.setString(1, status.substring(0, 1) + "%");
        ps.setString(2, usuario);
        ps.setString(3, "%");

        ResultSet rs = ps.executeQuery();
        ArrayList<Task> taskList = new ArrayList<>();
        while (rs.next()) {
            Task t = new Task();
            t.setCategoria(rs.getString("categoria"));
            t.setDescricao(rs.getString("descricao"));
            t.setId(rs.getInt("id"));
            t.setDataTask(rs.getDate("datatask"));
            t.setStatus(rs.getString("status"));
            t.setUsuario(rs.getString("usuario"));
            taskList.add(t);
        }

        ps.close();
        return taskList;
    }

    public int getId(String tabela) throws SQLException {
        PreparedStatement ps = util.con().prepareStatement("select max(id) from " + tabela);
        ResultSet rs = ps.executeQuery();
        int numID = 1;
        if (rs.next()) {
            numID = rs.getInt(1) + 1;
        }

        ps.close();
        return numID;
    }

}
