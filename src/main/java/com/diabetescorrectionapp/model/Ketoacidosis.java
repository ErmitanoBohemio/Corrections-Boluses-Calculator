/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.diabetescorrectionapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 *
 * @author OscarFabianHP
 */
public enum Ketoacidosis {
    NORMAL("0.5 mmol/L o menos", "Cetonas Normales", Color.MISTYROSE, Map.ofEntries(Map.entry("BG = 100-180 mg/dL", Map.ofEntries(Map.entry("action", "Administre el bolo corrector habitual con la bomba."))), Map.entry("BG = 180-250 mg/dL", Map.ofEntries(Map.entry("action", "Líquido extra. Administre el bolo corrector habitual con la bomba."))), Map.entry("BG = 250-400 mg/dL", Map.ofEntries(Map.entry("action", "Líquido extra. Administre el bolo corrector habitual con la bomba."))), Map.entry("BG > 400 mg/dL", Map.ofEntries(Map.entry("action", "Líquido extra. Administre el bolo corrector habitual con la bomba."))))),
    LIGERAMENTE_ELEVADO("0.6 a 1.5 mmol/L", "Las cetonas se están acumulando. Revise el equipo de infusión y la bomba.", Color.LIGHTSALMON, Map.ofEntries(Map.entry("BG = 100-180 mg/dL", Map.ofEntries( Map.entry("action", "Carbohidratos y líquidos adicionales. Administre las dosis habituales en bolo si el equipo de infusión está bien. Vuelva a comprobar en 2-3 horas."))), Map.entry("BG = 180-250 mg/dL", Map.ofEntries( Map.entry("extraTdd", "0.05"), Map.entry("1UeveryKg" , "40"), Map.entry("action", "Carbohidratos y líquidos adicionales. Bolo de corrección recomendado incluye dosis extra del 5% de TDD* o 1 u por cada 80 lbs (40 kgs). Vuelva a comprobar en 2-3 horas"))), Map.entry("BG = 250-400 mg/dL", Map.ofEntries(Map.entry("extraTdd", "0.10"), Map.entry("1UeveryKg","20"), Map.entry("action", "Líquido extra. Bolo de corrección recomendado incluye dosis extra del 10% de TDD* o 1 u por cada 40 lbs (20 kgs). Vuelva a comprobar en 2-3 horas."))), Map.entry("BG > 400 mg/dL", Map.ofEntries(Map.entry("extraTdd", "0.15"), Map.entry("1UeveryKg", "12"), Map.entry("action", "Líquido extra. Bolo de corrección recomendado incluye dosis extra del 15% de TDD* o 1 u por cada 25 lbs (12 kgs). Vuelva a comprobar en 2-3 horas."))))),
    RIESGO_CETOACIDOSIS("1.5 a 2.9 mmol/L", "Se está desarrollando cetoacidosis (CAD): consulte al médico. Revise la bomba, reemplace el equipo de infusión y el depósito.", Color.TOMATO, Map.ofEntries( Map.entry("BG = 100-180 mg/dL", Map.ofEntries(Map.entry("extraTdd", "0.05"), Map.entry("1UeveryKg", "40"), Map.entry("action", "Carbohidratos y líquidos adicionales. Bolo de corrección recomendado incluye dosis extra del 5% de TDD* o 1 u por cada 80 lbs (40 kg). Vuelva a comprobar en 2-3 horas."))), Map.entry("BG = 180-250 mg/dL", Map.ofEntries(Map.entry("extraTdd" , "0.10"), Map.entry("1UeveryKg", "20"), Map.entry("action", "Carbohidratos y líquidos adicionales. Bolo de corrección recomendado incluye dosis extra del 10% de TDD* o 1 u por cada 40 lbs (20 kg). Vuelva a comprobar en 2-3 horas."))), Map.entry("BG = 250-400 mg/dL", Map.ofEntries(Map.entry("extraTdd", "0.15"), Map.entry("1UeveryKg", "12"), Map.entry("action", "Líquido extra. Bolo de corrección recomendado incluye dosis extra del 15% de TDD* o 1 u por cada 25 lbs (12 kg)."))), Map.entry("BG > 400 mg/dL", Map.ofEntries(Map.entry("extraTdd", "0.20"), Map.entry("1UeveryKg", "10"), Map.entry("action", "Líquido extra. Bolo de corrección recomendado incluye dosis extra del 20% de TDD* o 1 u por cada 20 lbs (10 kg)."))))),
    ACUDIR_A_URGENCIAS("3.0 mmol/L aproximadamente", "Cetoacidosis Diabética severa (CAD): llame al médico o pídale a alguien que lo lleve a la sala de emergencias, especialmente. si comienza el vómito. Revise la bomba, reemplace el equipo de infusión y el depósito.", Color.ORANGERED, Map.ofEntries( Map.entry("BG = 100-180 mg/dL", Map.ofEntries(Map.entry("extraTdd", "0.05"), Map.entry("1UeveryKg", "40"), Map.entry("action", "Carbohidratos y líquidos adicionales. Bolo de corrección recomendado incluye dosis extra del 5% de TDD* o 1 u por cada 80 lbs (40 kg). Repita cada 2-3 horas según la glucosa en ese momento hasta que bajen las cetonas."))), Map.entry("BG = 180-250 mg/dL", Map.ofEntries(Map.entry("extraTdd", "0.15"), Map.entry("1UeveryKg", "12"), Map.entry("action", "Carbohidratos y líquidos adicionales. Bolo de corrección recomendado incluye dosis extra del 15% de TDD* o 1 u por cada 25 lbs (12 kg). Repita cada 2-3 horas hasta que bajen las cetonas."))), Map.entry("BG = 250-400 mg/dL", Map.ofEntries(Map.entry("extraTdd", "0.20"), Map.entry("1UeveryKg", "10"), Map.entry("action", "Líquido extra. Bolo de corrección recomendado incluye dosis extra del 20% de TDD* o 1 u por cada 20 lbs (10 kgs). Repita cada 2-3 horas hasta que bajen las cetonas."))), Map.entry("BG > 400 mg/dL", Map.ofEntries(Map.entry("extraTdd", "0.25"), Map.entry("1UeveryKg", "7"), Map.entry("action", "Líquido extra. Bolo de corrección recomendado incluye dosis extra del 25% de TDD* o 1 u por cada 15 lbs (7 kgs). Repita cada 2-3 horas hasta que bajen las cetonas."))))),
    RECOMMENDS(Arrays.asList("Las dosis anteriores son dosis de corrección en bolo o inyección únicamente. Es posible que se necesiten dosis mayores o menores.", 
            "¡También se debe administrar insulina basal o de acción prolongada! No detenga la administración basal incluso si no está comiendo.", 
            "No se vaya a dormir si está solo y las cetonas son de 1,5 mmol/L o más. Llama a alguien para que se quede contigo.",
            "Controle su glucosa y cetonas cada 2 horas si su última glucosa está por encima de 300 mg/dL (16,7 mmol/L). Por encima de 150 mg/dL en el embarazo."));
    
    private final String bloodKetoneLevel;
    private final String whatItMeans;
    private final Color colorKetoLevel;
    private final Map<String, Map<String, String>> bloodGlucoseLevel;
    private final List<String> recommends;
    
    private Ketoacidosis(String bkLevel, String mean, Color color, Map<String, Map<String, String>> bgLevel){
        bloodKetoneLevel = bkLevel;
        whatItMeans = mean;
        colorKetoLevel = color;
        bloodGlucoseLevel = bgLevel;
        recommends = new ArrayList<>();
    }
    private Ketoacidosis(List<String> recommends){
        this.recommends = recommends;
        bloodKetoneLevel = new String();
        whatItMeans = new String();
        colorKetoLevel = Color.FLORALWHITE;
        bloodGlucoseLevel = Map.of();
    }

    public String getBloodKetoneLevel() {
        return bloodKetoneLevel;
    }

    public String getWhatItMeans() {
        return whatItMeans;
    }

    public Color getColorKetoLevel() {
        return colorKetoLevel;
    }

    
    
    public List<String> getRecommends() {
        return recommends;
    }

    public Map<String, Map<String, String>> getBloodGlucoseLevel() {
        return bloodGlucoseLevel;
    }

    @Override
    public String toString() {
        return super.toString().replace('_', ' ') + " >> " + bloodKetoneLevel;
    }  
}