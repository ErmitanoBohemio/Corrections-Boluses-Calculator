/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.diabetescorrectionapp.model;

import javafx.scene.paint.Color;

/**
 *
 * @author OscarFabianHP
 */
public enum TrendArrows {
    RAPIDLY_RISING("AUMENTANDO RÁPIDAMENTE >> Dexcom: ⬆⬆ | Medtronic: ⬆⬆⬆", "⬆⬆", "⬆⬆⬆" , 1.40, 80.0, Color.DEEPSKYBLUE, "Aumentando Rápidamente: Más de 3 mg/dL por minuto (180 mg/dl por hora)", "Se Aumentó la dosis en un 40% o agregó 80 mg/dL (4,4 mmol/L) a GS"),
    RISING("AUMENTANDO >> Dexcom, FreeStyle: ⬆ | Medtronic: ⬆⬆", "⬆", "⬆⬆", 1.30, 60.0, Color.LIGHTSKYBLUE, "Aumentando: 2-3 mg/dL por minuto (120-180 mg/dL por hora)", "Se Aumentó la dosis en un 30% o agregó 60 mg/dL (3,3 mmol/L) a GS"),
    SLOWLY_RISING("AUMENTANDO LENTAMENTE >> Dexcom, FreeStyle: ⬈ | Medtronic: ⬆", "⬈", "⬆", 1.20, 40.0, Color.PALETURQUOISE, "Aumentando lentamente: 1-2 mg/dL por minuto (60-120 mg/dL por hora)", "Se Aumentó la dosis en un 20% o agregó 40 mg/dL (2,2 mmol/L) a GS");
    
    private final String trendName;
    private final String dexcomTrendArrow;
    private final String medtronicTrendArrow;
    private final Double increaseDose;
    private final Double addBG;
    private final Color colorTrendArrow;
    private final String whatsMean;
    private final String adjustDose;

    private TrendArrows(String trendName, String dexcomTrendArrow, String medtronicTrendArrow, Double increaseDose, Double addBG, Color colorTrendArrow, String whatsMean, String adjustDose) {
        this.trendName = trendName;
        this.dexcomTrendArrow = dexcomTrendArrow;
        this.medtronicTrendArrow = medtronicTrendArrow;
        this.increaseDose = increaseDose;
        this.addBG = addBG;
        this.colorTrendArrow = colorTrendArrow;
        this.whatsMean = whatsMean;
        this.adjustDose = adjustDose;
    }

    public String getTrendName() {
        return trendName;
    }

    public String getDexcomTrendArrow() {
        return dexcomTrendArrow;
    }

    public String getMedtronicTrendArrow() {
        return medtronicTrendArrow;
    }

    public Double getIncreaseDose() {
        return increaseDose;
    }

    public Double getAddBG() {
        return addBG;
    }

    public Color getColorTrendArrow() {
        return colorTrendArrow;
    }
    
    public String getWhatsMean() {
        return whatsMean;
    }

    public String getAdjustDose() {
        return adjustDose;
    }

    @Override
    public String toString() {
        return trendName;
    }
}