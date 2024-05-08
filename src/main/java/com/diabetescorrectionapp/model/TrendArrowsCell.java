/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.diabetescorrectionapp.model;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

/**
 *
 * @author OscarFabianHP
 */
public class TrendArrowsCell extends ListCell<TrendArrows>{

    @Override
    protected void updateItem(TrendArrows item, boolean empty) {
        super.updateItem(item, empty); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    
        if(item == null || empty){
            setGraphic(null);
            setText("NO");
        }
        else{
            setBackground(new Background(new BackgroundFill(item.getColorTrendArrow(), CornerRadii.EMPTY, Insets.EMPTY)));
            setText(item.toString());
        }
    }
    
    
}
