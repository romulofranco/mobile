/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifsul.as.todo.ui;

import ifsul.as.todo.db.Database;
import ifsul.as.todo.db.entity.Task;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import totalcross.sys.Settings;
import totalcross.ui.AlignedLabelsContainer;
import totalcross.ui.Button;
import totalcross.ui.ComboBox;
import totalcross.ui.Container;
import static totalcross.ui.Control.BOTTOM;
import static totalcross.ui.Control.CENTER;
import static totalcross.ui.Control.FILL;
import static totalcross.ui.Control.LEFT;
import static totalcross.ui.Control.PREFERRED;
import static totalcross.ui.Control.RIGHT;
import static totalcross.ui.Control.TOP;
import totalcross.ui.Edit;
import totalcross.ui.Label;
import totalcross.ui.MultiEdit;
import totalcross.ui.Window;
import static totalcross.ui.Window.ROUND_BORDER;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.util.Date;

class TaskWindow extends Window {

    private Button btnOk;
    private Button btnCancel;
    private Button btnRemover;
    private Container content;
    private Edit edId;
    private MultiEdit edDesc;
    private Edit edUsuario;
    private ComboBox cmbCategoria;
    private ComboBox cmbStatus;

    public TaskWindow(Task task) {
        super("Detalhe Tarefa", ROUND_BORDER); // with caption and borders
        setRect(CENTER, CENTER, Settings.screenWidth - 30, (Settings.screenHeight) - 20);

        content = new Container();

        add(content, LEFT, TOP, FILL, FILL);

        edId = new Edit();
        edDesc = new MultiEdit(4, 5);
        edUsuario = new Edit();
        cmbCategoria = new ComboBox();
        cmbStatus = new ComboBox(new String[]{"Aberta", "Concluída"});

        if (task != null) {
            edId.setText("" + task.getId());
            edDesc.setText(task.getDescricao());
            
            cmbCategoria.setSelectedItem(task.getCategoria());
            cmbStatus.setSelectedItem(task.getStatus());
        } else 
        {
            try {
                edId.setText("" + Database.getInstance().getId("task"));
                edId.setEnabled(false);
            } catch (SQLException ex) {
                Logger.getLogger(TaskWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        edUsuario.setText(FormToDo.USUARIO_LOGADO);
        edUsuario.setEnabled(false);

        add(new Label("ID"), LEFT + 20, TOP + 10, PREFERRED, PREFERRED);
        add(edId, LEFT + 20, AFTER + 10, FILL - 20, PREFERRED);

        add(new Label("Descrição"), LEFT + 20, AFTER + 40, PREFERRED, PREFERRED);
        add(edDesc, LEFT + 20, AFTER + 10, FILL - 20, PREFERRED);

        add(new Label("Usuário"), LEFT + 20, AFTER + 40, PREFERRED, PREFERRED);
        add(edUsuario, LEFT + 20, AFTER + 10, FILL - 20, PREFERRED);
       
        add(new Label("Categoria"), LEFT + 20, AFTER + 40, PREFERRED, PREFERRED);
        add(cmbCategoria, LEFT + 20, AFTER + 10, FILL - 20, PREFERRED);

        add(new Label("Status"), LEFT + 20, AFTER + 40, PREFERRED, PREFERRED);
        add(cmbStatus, LEFT + 20, AFTER + 10, FILL - 20, PREFERRED);

        btnOk = new Button("  OK  ");
        btnCancel = new Button("Cancelar");

        add(btnCancel, RIGHT - 50, BOTTOM - 40, PREFERRED + 20, fmH * 2);
        add(btnOk, BEFORE, BOTTOM - 40, PREFERRED + 20, fmH * 2);

        if (task != null) {
            btnRemover = new Button(ToDoImages.remover);
            add(btnRemover, LEFT + 40, BOTTOM - 40, PREFERRED + 20, fmH * 2);
        }

        btnOk.addPressListener(new PressListener() {

            @Override
            public void controlPressed(ControlEvent e) {
                if (task != null) {
                    task.setCategoria(cmbCategoria.getSelectedItem().toString());
                    task.setDataTask(new Date());
                    task.setDescricao(edDesc.getText());
                    task.setStatus(cmbStatus.getSelectedItem().toString());
                    task.setUsuario(FormToDo.USUARIO_LOGADO);
                } else {
                    try {
                        Task ts = new Task();
                        ts.setCategoria(" " + cmbCategoria.getSelectedItem().toString());
                        ts.setDataTask(new Date());
                        ts.setDescricao(edDesc.getText());
                        ts.setId(Database.getInstance().getId("task"));
                        ts.setStatus(cmbStatus.getSelectedItem().toString());
                        ts.setUsuario(FormToDo.USUARIO_LOGADO);

                        Database.getInstance().inserirTask(ts);
                        FormToDo formTodo = (FormToDo) FormToDo.getMainWindow();
                        formTodo.carregarTasks("%");                        
                    } catch (SQLException ex) {
                        Logger.getLogger(TaskWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                unpop();
            }
        });

        btnCancel.addPressListener(new PressListener() {

            @Override
            public void controlPressed(ControlEvent e) {
                unpop();
            }
        });
    }

}
