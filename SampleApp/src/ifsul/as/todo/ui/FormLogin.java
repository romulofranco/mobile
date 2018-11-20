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
import totalcross.io.IOException;
import totalcross.ui.Button;
import totalcross.ui.Check;
import totalcross.ui.Container;
import static totalcross.ui.Container.BORDER_NONE;
import static totalcross.ui.Control.AFTER;
import static totalcross.ui.Control.CENTER;
import static totalcross.ui.Control.FILL;
import static totalcross.ui.Control.LEFT;
import static totalcross.ui.Control.PARENTSIZE;
import static totalcross.ui.Control.PREFERRED;
import static totalcross.ui.Control.SAME;
import static totalcross.ui.Control.TOP;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.MainWindow;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

/**
 *
 * @author admin
 */
public class FormLogin extends BaseForm {

    private Edit edPass, edLogin;
    private Check ch;
    private Button btLogin, btRegister;

    private FormUsuario containerUsuario;

    private ImageControl ic;

    @Override
    public void initUI() {
        try {

            ic = new ImageControl(new Image("img/logo.png"));
            ic.scaleToFit = true;
            ic.centerImage = true;
            add(ic, LEFT, TOP + 100, FILL, PARENTSIZE + 30);

            edLogin = new Edit();
            edLogin.caption = "Usuário";
            edLogin.setBackColor(Color.RED);
            add(edLogin, CENTER, AFTER + 60, PARENTSIZE + 90, PREFERRED + 30);

            edPass = new Edit();
            edPass.caption = "Senha";
            edPass.setBackColor(Color.RED);
            add(edPass, SAME, AFTER + 70, PARENTSIZE + 90, PREFERRED + 30);

            ch = new Check("Mantenha-me conectado");
            add(ch, LEFT + 86, AFTER + 100, PARENTSIZE, PREFERRED + 30);

            btLogin = new Button("Login");
            btLogin.setBackColor(Color.WHITE);
            add(btLogin, CENTER, AFTER + 140, PARENTSIZE + 80, PREFERRED + 60);

            btRegister = new Button("Registrar");
            btRegister.transparentBackground = true;
            btRegister.setBorder(BORDER_NONE);
            add(btRegister, CENTER, AFTER, PARENTSIZE + 30, PREFERRED + 20);
            btRegister.addPressListener(e -> {
                containerUsuario = new FormUsuario();
                MainWindow.getMainWindow().swap(containerUsuario);
            });

            btLogin.addPressListener(new PressListener() {
                @Override
                public void controlPressed(ControlEvent ce) {
                    try {
                        checarUsuarioSenha();
                    } catch (SQLException ex) {
                         showMessage("Login", "Falha ao conectar no banco de dados");
                    }
                }
            });

            Database.getInstance().createUsuarioTable();
            
        } catch (IOException | ImageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(FormLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checarUsuarioSenha() throws SQLException {
        if (Database.getInstance().checaUsuarioSenha(edLogin.getText().trim(), edPass.getText().trim())) {
            showToast("Usuário e senhas corretos");
            FormToDo.USUARIO_LOGADO = edLogin.getText();
        } else {
            showMessage("Login", "Usuário inválido");
        }
    }

}
