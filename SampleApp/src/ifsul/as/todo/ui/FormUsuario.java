/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifsul.as.todo.ui;

import ifsul.as.todo.db.Database;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import static totalcross.ui.Control.AFTER;
import static totalcross.ui.Control.FILL;
import static totalcross.ui.Control.LEFT;
import static totalcross.ui.Control.SAME;
import totalcross.ui.Edit;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.Toast;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.util.InvalidDateException;

/**
 *
 * @author admin
 */
public class FormUsuario extends Container {
    
    private Edit edNome, edLogin, edSenha;
    private Button btnSalvar, btnCancelar;
    
    FormLogin containerLogin = new FormLogin();    
    
    private final int COMPONENT_H = fmH * 2;
    private final static int FLAT_EDGE_MARGIN = (int) (Math.min(Settings.screenHeight,
            Settings.screenWidth) * 0.20);
    
    @Override
    public void initUI() {
        try {
            
            Label titleForm = new Label("ToDo List - Usuários");
            titleForm.set3d(true);
            titleForm.setFont(Font.getFont(28).asBold());
            add(titleForm, CENTER, AFTER + 20, PREFERRED, COMPONENT_H);

            // regular font is not bold
            add(edNome = new Edit(), LEFT + FLAT_EDGE_MARGIN, AFTER + 100, FILL
                    - FLAT_EDGE_MARGIN, COMPONENT_H);
            edNome.caption = "Name";
            edNome.captionColor = 0x049CEE;
            
            add(edLogin = new Edit(), LEFT + FLAT_EDGE_MARGIN, AFTER + 100, FILL
                    - FLAT_EDGE_MARGIN, COMPONENT_H);
            edLogin.caption = "Usuário";
            edLogin.captionColor = 0x049CEE;
            
            add(edSenha = new Edit(), SAME, AFTER + FLAT_EDGE_MARGIN, FILL
                    - FLAT_EDGE_MARGIN, SAME);
            edSenha.setMode(Edit.PASSWORD);
            edSenha.caption = "Senha";
            edSenha.captionColor = 0x049CEE;
            
            btnSalvar = new Button("Salvar");
            btnSalvar.setBackForeColors(Color.getRGB(111, 186, 255), Color.WHITE);
            
            btnCancelar = new Button("Cancelar");
            btnCancelar.setBackForeColors(Color.getRGB(233, 154, 255), Color.WHITE);
            
            add(btnSalvar, CENTER, AFTER + 50, SAME, SAME);
            add(btnCancelar, CENTER, AFTER + 50, SAME, SAME);
            
            btnSalvar.addPressListener(new PressListener() {
                @Override
                public void controlPressed(ControlEvent ce) {
                    try {
                        //Salvar no banco de dados
                        doInsert();
                        MainWindow.getMainWindow().swap(containerLogin);
                    } catch (SQLException ex) {
                        Logger.getLogger(FormUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidDateException ex) {
                        Logger.getLogger(FormUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            btnCancelar.addPressListener(new PressListener() {
                @Override
                public void controlPressed(ControlEvent ce) {                    
                    MainWindow.getMainWindow().swap(containerLogin);
                }
            });
            
        } catch (Exception e) {
            Vm.debug(e.getMessage());
        }
    }
    
    private void doInsert() throws SQLException, InvalidDateException {
        if (edNome.getLength() == 0 || edLogin.getLength() == 0
                || edSenha.getLength() == 0) {
            showToast("Preencha todos os campos!");
        } else {
            // simple example of how you can insert data into SQLite..
            String nome = edNome.getText();
            String login = edLogin.getText();
            String senha = edSenha.getText();

            //Checa se o usuario ja existe na base de dados antes de inserir
            //dbCheckUsuario(login);
            Database.getInstance().inserirUsuario(nome, login, senha);
            showToast("Usuário inserido com sucesso!");
        }
    }
    
    public void showToast(String message) {
        // fmh is the font size. Let's set the toast size to 3 times font size
        Toast.height = fmH * 3;
        // You can control position of toast too
        Toast.posY = (int) (Control.BOTTOM - 600);
        
        Toast.show(message, 2000);
    }
}
