/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.diabetescorrectionapp.model;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author OscarFabianHP
 */
public class KetoCells extends ListCell<Ketoacidosis>{

    ComboBox<Ketoacidosis> comboBox;
    
    public KetoCells(ComboBox ketoCombox) {
        comboBox = ketoCombox;
    }

    
    @Override
    protected void updateItem(Ketoacidosis item, boolean vacio) {
        super.updateItem(item, vacio); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        StackPane arrow = (StackPane) comboBox.lookup(".arrow-button");
        if(item == null || vacio){ //si el elemento seleccionado es null o vacio
            setGraphic(null);
            setText("NO"); //pone en la celda correspondiente "NO"
        }
        else{ //si elemento seleccionado del ComboBox es diferente de null, pinta la celda del elemento correspondiente
            setBackground(new Background(new BackgroundFill(item.getColorKetoLevel(), CornerRadii.EMPTY, Insets.EMPTY)));
            setText(item.toString());
            //comboBox.setBackground(new Background(new BackgroundFill(item.getColorKetoLevel(), CornerRadii.EMPTY, Insets.EMPTY)));
            //.setBackground(getBackground());
            //arrow.setBackground(new Background(new BackgroundFill(item.getColorKetoLevel(), CornerRadii.EMPTY, Insets.EMPTY)));
        }
                    
    }   
}
