/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifsul.as.todo.db.entity;

import totalcross.util.Date;

/**
 *
 * @author romulo
 */
public class Task {

    public static final String TASK_STATUS_CONCLUIDA = "c";
    public static final String TASK_STATUS_ABERTA = "a";

    private int id;
    private String descricao;
    private String usuario;
    private String categoria;
    private String status;
    private Date dataTask;

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getDataTask() {
        return dataTask;
    }

    public void setDataTask(Date dataTask) {
        this.dataTask = dataTask;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        if (this.status.equals(Task.TASK_STATUS_ABERTA)) {
            return "@red";
        } else {
            return "@green";
        }
    }

    public String[] getStringForGrid() {
        String[] grid = new String[3];

        grid[0] = this.getImg();
        grid[1] = "" + this.id;
        grid[2] = " " + this.descricao;
        grid[3] = " " + this.categoria;

        return grid;
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", descricao=" + descricao + ", usuario=" + usuario + ", categoria=" + categoria + ", status=" + status + ", dataTask=" + dataTask + '}';
    }

}
