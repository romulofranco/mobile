/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifsul.as.todo.ui;

import totalcross.io.IOException;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

/**
 *
 * @author romulo
 */
public class ToDoImages {

    private ToDoImages() {
    }

    public static Image logoCompany, concluidas, abertas, adicionar, remover, editar;

    public static void loadImages(int fmH) {
        try {
            int fator = 2;

            logoCompany = new Image("img/logo.png").smoothScaledFixedAspectRatio(fmH * fator, true);
            concluidas = new Image("img/check.png").smoothScaledFixedAspectRatio(fmH * fator, true);
            abertas = new Image("img/ativa.png");
            adicionar = new Image("img/add1.png");
            remover = new Image("img/remover.png");
            editar = new Image("img/stock.png");            
            
        } catch (ImageException | IOException e) {
            e.printStackTrace();
        }
    }
}
