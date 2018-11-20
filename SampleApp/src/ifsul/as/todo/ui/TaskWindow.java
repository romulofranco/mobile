/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifsul.as.todo.ui;

import ifsul.as.todo.db.entity.Task;
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
import totalcross.ui.MultiEdit;
import totalcross.ui.Window;
import static totalcross.ui.Window.ROUND_BORDER;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;

class TaskWindow extends Window {

    Button btnOk;
    private Container content;
    private Edit edId;
    private MultiEdit edDesc;
    private Edit edUsuario;
    private ComboBox cmbCategoria;
    private ComboBox cmbStatus;

    public TaskWindow(Task task) {
        super("Detalhe Tarefa", ROUND_BORDER); // with caption and borders
        setRect(CENTER, CENTER, Settings.screenWidth - 50, (Settings.screenHeight) - fmH * 5);

        content = new Container();

        add(content, LEFT, TOP, FILL, FILL);

        AlignedLabelsContainer ac = new AlignedLabelsContainer(
                new String[]{"ID", "Descrição", "Usuário", "Categoria", "Status"}, 40);
        edId = new Edit();
        edDesc = new MultiEdit(5, 2);
        edUsuario = new Edit();
        cmbCategoria = new ComboBox();
        cmbStatus = new ComboBox(new String[]{"Aberta", "Concluída"});

        edId.setText("" + task.getId());
        edDesc.setText(task.getDescricao());
        edUsuario.setText(task.getUsuario());
        cmbCategoria.setSelectedItem(task.getCategoria());
        cmbStatus.setSelectedItem(task.getStatus());

        add(ac, LEFT + 25, TOP + 5, FILL - 20, PREFERRED);

        ac.add(edId, LEFT + 10, ac.getLineY(0), PREFERRED - 10, PREFERRED);
        ac.add(edDesc, LEFT + 10, ac.getLineY(1), PREFERRED - 10, PREFERRED);
        ac.add(edUsuario, LEFT + 10, ac.getLineY(4), PREFERRED - 10, PREFERRED);
        ac.add(cmbCategoria, LEFT + 10, ac.getLineY(5), PREFERRED - 10, PREFERRED);
        ac.add(cmbStatus, LEFT + 10, ac.getLineY(6), PREFERRED - 10, PREFERRED);

        ac.setEnabled(false);

        btnOk = new Button("OK", ToDoImages.concluidas, Button.RIGHT, fmH);

        add(btnOk, RIGHT - 50, BOTTOM - 40, PREFERRED + 20, fmH * 2);

        btnOk.addPressListener(new PressListener() {

            @Override
            public void controlPressed(ControlEvent e) {
                unpop();
            }
        });
    }

}
