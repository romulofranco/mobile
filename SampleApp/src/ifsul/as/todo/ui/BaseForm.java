/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifsul.as.todo.ui;

import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Toast;
import totalcross.ui.dialog.MessageBox;

/**
 *
 * @author admin
 */
public class BaseForm extends Container {
    
     public void showMessage(String title, String message) {
        MessageBox mb = new MessageBox(title, message);
        mb.popup();
    }

    public void showToast(String message) {
        // fmh is the font size. Let's set the toast size to 3 times font size
        Toast.height = fmH * 3;
        // You can control position of toast too
        Toast.posY = (int) (Control.BOTTOM - 600);

        Toast.show(message, 2000);
    }
}
