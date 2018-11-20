package ifsul.as.todo.ui;

import ifsul.as.todo.db.Database;
import ifsul.as.todo.db.entity.Task;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.objects.NativeArray;
import totalcross.sys.Settings;
import totalcross.ui.Button;
import totalcross.ui.ComboBox;
import totalcross.ui.Control;
import static totalcross.ui.Control.AFTER;
import static totalcross.ui.Control.CENTER;
import static totalcross.ui.Control.PREFERRED;
import totalcross.ui.Grid;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.TabbedContainer;
import totalcross.ui.Toast;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.ImageException;

/**
 * Simple example to help you create your own app with TotalCross.
 *
 * If you need more help, reach us at www.totalcross.com
 *
 * You can find the full explanation of this sample at
 * http://www.totalcross.com/documentation/getting_started.html
 */
public class FormToDo extends MainWindow {

    public static final int BKGCOLOR = 0x0A246A;
    public static final int SELCOLOR = 0x829CE2; // Color.brighter(BKGCOLOR,120);

    private TabbedContainer tp;
    private FormLogin containerLogin;

    private ComboBox cmbCategoria;
    private Grid gridTaskAbertas;
    private Grid gridTaskConcluidas;
    private Button btnAdd;
    private Button btnRemove;
    private Button btnEdit;

    public static String USUARIO_LOGADO;

    // fmH is the font size. We will set the components to have twice the size
    // of font.
    private final int COMPONENT_H = fmH * 2;

    private final static int FLAT_EDGE_MARGIN = (int) (Math.min(Settings.screenHeight,
            Settings.screenWidth) * 0.20);

    public FormToDo() {
        super("ToDo List", NO_BORDER);
        setUIStyle(Settings.MATERIAL_UI);
        Settings.uiAdjustmentsBasedOnFontHeight = true;

        setBackForeColors(Color.getRGB(255, 255, 255), Color.getRGB(50, 50, 50));

        USUARIO_LOGADO = "";
    }

    @Override
    public void initUI() {
        try {
            super.initUI();

            ToDoImages.loadImages(fmH);

            Label titleForm = new Label("                 ToDo List");
            titleForm.set3d(true);
            titleForm.setFont(Font.getFont(28).asBold());
            titleForm.setBackColor(0x285296);
            titleForm.setForeColor(Color.WHITE);
            add(titleForm, CENTER, TOP, PARENTSIZE, fmH * 2);

            cmbCategoria = new ComboBox();
            add(new Label("Categoria"), LEFT + 30, AFTER + 20, PREFERRED, PREFERRED);
            add(cmbCategoria, AFTER + 10, SAME, PARENTSIZE, PREFERRED);

            String tabCaptions[] = {"Abertas",
                "Concluídas"};
            tp = new TabbedContainer(tabCaptions);
            tp.setBackColor(Color.brighter(BKGCOLOR));
//            tp.extraTabHeight = fmH / 2;
            tp.activeTabBackColor = Color.ORANGE;
//            tp.pressedColor = Color.YELLOW;
            add(tp, LEFT, AFTER + 10, FILL - 5, FILL);

            gridTaskAbertas = prepareGrid();
            gridTaskConcluidas = prepareGrid();

            tp.setContainer(0, gridTaskAbertas);
            tp.setContainer(1, gridTaskConcluidas);

            prepareCombo();

            carregarTasks("%");

            //Aqui abre a tela de login
//            containerLogin = new FormLogin();
            //          MainWindow.getMainWindow().swap(containerLogin);
        } catch (Exception e) {
            MessageBox.showException(e, true);
            exit(0);
        }
    }

    public void carregarTasks(String categoria) {
        try {
            ArrayList<Task> tasksAbertas = Database.getInstance().getTaskList(FormToDo.USUARIO_LOGADO, Task.TASK_STATUS_ABERTA, categoria);
            ArrayList<Task> tasksConcluidas = Database.getInstance().getTaskList(FormToDo.USUARIO_LOGADO, Task.TASK_STATUS_CONCLUIDA, categoria);
            carregarGrid(gridTaskAbertas, tasksAbertas);
            carregarGrid(gridTaskConcluidas, tasksConcluidas);
        } catch (SQLException ex) {
            Logger.getLogger(FormToDo.class.getName()).log(Level.SEVERE, null, ex);
            showToast("Falha ao carregar tarefas");
        }
    }

    public void carregarGrid(Grid grid, ArrayList<Task> tasks) {
        if (tasks.size() > 0) {
            String items[][] = new String[tasks.size()][2];
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                items[i] = t.getStringForGrid();
            }

            grid.setItems(items);

            try {
                grid.setImage("@green", ToDoImages.concluidas);
                grid.setImage("@red", ToDoImages.abertas);

            } catch (ImageException e1) {
                showToast(e1.getMessage());
            }

        } else {
            showToast("Nenhuma tarefa encontrada...");
        }
    }

    public void prepareCombo() {
        ArrayList<String> categorias = Database.getInstance().getCategoriaList();
        categorias.add("Teste");
        categorias.add("Teste 1");
        String[] items = new String[]{};
        cmbCategoria.add(categorias.toArray(items));
        cmbCategoria.popupTitle = "Selecione uma Categoria";
    }

    public Grid prepareGrid() throws ImageException {
        String[] gridCaptions = {"", "ID", "Descrição"};
        int gridWidths[] = {-10, 0, -50};
        int gridAligns[] = {CENTER, CENTER, LEFT};

        Grid.useHorizontalScrollBar = true;
        Grid grid = new Grid(gridCaptions, gridWidths, gridAligns, false);

        btnAdd = new Button(ToDoImages.adicionar.hwScaledFixedAspectRatio(fmH * 3, true), BORDER_NONE);
        btnRemove = new Button(ToDoImages.remover.hwScaledFixedAspectRatio(fmH * 3, true), BORDER_NONE);
        btnAdd.transparentBackground = true;
        btnAdd.setTextShadowColor(DARKER_BACKGROUND);

        btnRemove.transparentBackground = true;
        btnRemove.setTextShadowColor(DARKER_BACKGROUND);

        add(btnRemove, RIGHT - 50, BOTTOM - 25);
        add(btnAdd, BEFORE - 10, BOTTOM - 25);
        return grid;
    }

    public void showToast(String message) {
        // fmh is the font size. Let's set the toast size to 3 times font size
        Toast.height = fmH * 3;
        // You can control position of toast too
        Toast.posY = (int) (Control.BOTTOM - 600);

        Toast.show(message, 2000);
    }

}
