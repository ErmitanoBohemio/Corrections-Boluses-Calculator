/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.diabetescorrectionapp.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author OscarFabianHP
 */
public class Correction {
    private BigDecimal tdd;
    private BigDecimal gsCurrently;
    private BigDecimal gsTarget;
    private BigDecimal a1c_fsi;
    private Boolean fsiKnown;
    private Boolean low;
    private Boolean high;
    private Boolean sports;
    private TrendArrows trendArrow;
    private Ketoacidosis keto;
    
    private String keyGS;
    private BigDecimal fsiCalculado;
    private boolean targetGlucoseLimited;
    private BigDecimal correctionBolusRecommended;

    //Constructor cuando se proporciona dato A1c
    public Correction(BigDecimal tdd, BigDecimal gsCurrently, BigDecimal gsTarget, BigDecimal a1c_fsi, TrendArrows trendArrow, Ketoacidosis keto, Boolean fsiKnown, Boolean low, Boolean high, Boolean sports) {
        this.tdd = tdd;
        this.gsCurrently = gsCurrently;
        this.gsTarget = gsTarget;
        //this.a1c_fsi = a1c_fsi;
        //Si no conoce FSI y low y high es false se usa valor a1c ingresado, de lo 
        this.a1c_fsi = (!fsiKnown)? a1c_fsi : ( low.equals(Boolean.FALSE) && high.equals(Boolean.FALSE))? a1c_fsi : (low)? a1c_fsi.multiply(BigDecimal.valueOf(1.10)): a1c_fsi.multiply(BigDecimal.valueOf(0.90));
        this.trendArrow = trendArrow;
        this.keto = keto;
        this.fsiKnown = fsiKnown;
        this.low = low;
        this.high = high;
        this.sports = sports;
        correctionBolusRecommended = correction();
    }
    
    private BigDecimal factorSensibilityInsulin(){
        double number=1960; //valor por defecto para realizar calculo de FSI
        BigDecimal fsi = BigDecimal.ONE; //Variable local que almacena FSI calculado
        if(a1c_fsi.compareTo(BigDecimal.valueOf(6.6))<0){
            if(a1c_fsi.compareTo(BigDecimal.valueOf(6.0))>=0)
                number = 2200;
            else
                number = 2400;
        }
        else if(a1c_fsi.compareTo(BigDecimal.valueOf(6.6))>=0 && a1c_fsi.compareTo(BigDecimal.valueOf(7.0))<0){
            if(a1c_fsi.compareTo(BigDecimal.valueOf(6.8))<0)
                number = 2100;
            else
                number=2000;
        }
        else if(a1c_fsi.compareTo(BigDecimal.valueOf(7.0))>=0 && a1c_fsi.compareTo(BigDecimal.valueOf(8.0))<0){
            if(a1c_fsi.compareTo(BigDecimal.valueOf(7.5))<0)
                number = 1900;
            else 
                number = 1800; 
        }
        else if(a1c_fsi.compareTo(BigDecimal.valueOf(8.0))>=0 && a1c_fsi.compareTo(BigDecimal.valueOf(10))<=0){
            if(a1c_fsi.compareTo(BigDecimal.valueOf(9.0))<0)
                number = 1700;
            else
                number = 1500;
        }
        else if(a1c_fsi.compareTo(BigDecimal.valueOf(10))>0){
            number = 1450;
        }
      System.out.println("number choised: "+number);
        if(low.equals(Boolean.TRUE)) //Si señala presentar hipoglicemia despues de una dosis de corrección aumenta el factor de correción un 10%
           fsi = BigDecimal.valueOf(number).divide(tdd, 0, RoundingMode.CEILING).multiply(BigDecimal.valueOf(1.10));
        else if(high.equals(Boolean.TRUE)) //Si señala presentar Hiperglicemia de 3h a 4h despues de dar una correción disminuye en 10% el factor de corrección o sensibilidad calculado
            fsi = BigDecimal.valueOf(number).divide(tdd, 0, RoundingMode.CEILING).multiply(BigDecimal.valueOf(0.90));
        else
           fsi = BigDecimal.valueOf(number).divide(tdd, 0, RoundingMode.CEILING);
        System.out.println("FSI Preciso: "+fsi);
        //Calculos para elegir un FSI en el extremo superior del rango en el que el FSI calculado(más arriba) se encuentre. (Ej: si fsi=52 then fsi=55)
        BigDecimal residuo = fsi.remainder(BigDecimal.valueOf(5), new MathContext(0)); //calcula (fsi mod 5)
        BigDecimal faltanteTo5 = BigDecimal.valueOf(5).subtract(residuo); //Resta 5 - (fsi mod 5)
        fsi = fsi.add(faltanteTo5); //Como medida de precaución mejor inclinarse a un factor de sesibilidad hacia el extremo superior del rango es decir si fsi=47 then choose fsi=50
        fsiCalculado = fsi;
        System.out.println("FSI moderado: "+fsi);
        return fsi;
    }
    
    private BigDecimal correction(){
        System.out.println("***Valor a1c Or fsi: "+a1c_fsi+"***");
        //BigDecimal correctionBolusRecommended = BigDecimal.ZERO; //Almacena valor calculado de Dosis de correción
        
        if(trendArrow == null){ //Si no presenta Flechas de tendecnia de glucosa
            if(fsiKnown.equals(Boolean.TRUE)){ //si conoce Factor de corrección o sensibilidad, usa el FSI proporcionado por usuario
                if(gsCurrently.subtract(gsTarget).compareTo(BigDecimal.valueOf(200))<0) //si es menor a 200 mg/dL la correción 
                    correctionBolusRecommended = gsCurrently.subtract(gsTarget).divide(a1c_fsi, 2, RoundingMode.HALF_EVEN); //realiza correción de la diferncia entre glucosa actual y la glucosa objetivo
                else {
                    correctionBolusRecommended = BigDecimal.valueOf(200).divide(a1c_fsi, 2, RoundingMode.HALF_EVEN); //realiza corrección limitada a 200 mg/dL  
                    targetGlucoseLimited = true;
                }
            }
            else {//si no conoce el factor de correción o sensibilidad, usa FSI calculado
                if(gsCurrently.subtract(gsTarget).compareTo(BigDecimal.valueOf(200))<0) //Realiza un corrección para traer la glucosa al nivel objetivo seleccionada
                    correctionBolusRecommended = gsCurrently.subtract(gsTarget).divide(factorSensibilityInsulin(), 2, RoundingMode.HALF_EVEN);
                else{ //Realiza un correción de maximo 200 mg/dL para no reducirla de golpe
                    correctionBolusRecommended = BigDecimal.valueOf(200).divide(factorSensibilityInsulin(), 2, RoundingMode.HALF_EVEN);
                    targetGlucoseLimited = true;
                }                        
            }
        }
        //Como la correccion con flecha de tendencia suma a GS actual un valor segun tendencia actual presentada, se verifica que no sobrepase el limite de 200 mg/dL de glucosa a corregir.
        else { //si selecciona presentar linea de tendencia de glucosa
            if(gsCurrently.subtract(gsTarget).add(BigDecimal.valueOf(trendArrow.getAddBG())).compareTo(BigDecimal.valueOf(200)) <= 0 ){ //Verifica si al sumar la glucosa extra según flecha de tendenia a la diferencia entre la glucosa actual y la objetivo resulta ser menor de 200 mg/dL
                if(fsiKnown.equals(Boolean.TRUE)) //si conoce FSI
                    correctionBolusRecommended = gsCurrently.subtract(gsTarget).add(BigDecimal.valueOf(trendArrow.getAddBG())).divide(a1c_fsi, 2, RoundingMode.HALF_EVEN); //usa formula de sumar glucosa según la tendencia
                else //si no conoce FSI
                    correctionBolusRecommended = gsCurrently.subtract(gsTarget).add(BigDecimal.valueOf(trendArrow.getAddBG())).divide(factorSensibilityInsulin(), 2, RoundingMode.HALF_EVEN); //Calcula FSI en base a A1c y suma glucosa según la tendecnia
            }
            else if(gsCurrently.subtract(gsTarget).compareTo(BigDecimal.valueOf(200)) > 0 ){//si la glucosa a corregir es mayor o igual a 200mg/dL aún sin sumar la glucosa por tendencia
                targetGlucoseLimited = true;
                if(fsiKnown.equals(Boolean.TRUE)) //si conoce FSI
                    correctionBolusRecommended = BigDecimal.valueOf(200).divide(a1c_fsi, 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(trendArrow.getIncreaseDose())); //limita correcion a 200mg/dL y usa formula de incrementar dosis un % según tendencia 
                else //si no conoce FSI
                    correctionBolusRecommended = BigDecimal.valueOf(200).divide(factorSensibilityInsulin(), 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(trendArrow.getIncreaseDose())); //Limita corrección  a 200mg/dL y usa formula de incrementar dosis un % según tendencia
            }
            else { //si no es mayor a 200 mg/dL la glucosa a corregir sin sumar tedencia
                if(fsiKnown.equals(Boolean.TRUE)) //Si conoce FSI
                    correctionBolusRecommended = gsCurrently.subtract(gsTarget).divide(a1c_fsi, 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(trendArrow.getIncreaseDose())); //Usa FSI suministrado por usuario, no limita correcion y usa incremento % dosis por tendencia
                else //Si no conoce FSI
                    correctionBolusRecommended = gsCurrently.subtract(gsTarget).divide(factorSensibilityInsulin(), 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(trendArrow.getIncreaseDose())); //Calcula FSI a partir del A1c, no limita correción e incremnta dosis de correcion por tendencia
            }
        }
        if(keto != null && gsCurrently.compareTo(BigDecimal.valueOf(100)) >= 0){ //si selecciona presentar algun nivel de Ketonas
            //Set keysGSset = keto.getBloodGlucoseLevel().keySet();  //no se usa
            
            if(/*gsCurrently.compareTo(BigDecimal.valueOf(100))>=0 &&*/ gsCurrently.compareTo(BigDecimal.valueOf(180))<0){
                keyGS = "BG = 100-180 mg/dL";
                if(keto.getBloodGlucoseLevel().get(keyGS).containsKey("extraTdd")){ //verifica antes si contiene un Map con clave extraTdd
                    double extraTdd = Double.parseDouble(keto.getBloodGlucoseLevel().get(keyGS).get("extraTdd"));
                    correctionBolusRecommended = correctionBolusRecommended.add(tdd.multiply(BigDecimal.valueOf(extraTdd)));
                }
            }
            else if(gsCurrently.compareTo(BigDecimal.valueOf(180))>=0 && gsCurrently.compareTo(BigDecimal.valueOf(250))<0){
                 keyGS = "BG = 180-250 mg/dL";
                 if(keto.getBloodGlucoseLevel().get(keyGS).containsKey("extraTdd")){ //verifica antes si contiene un Map con clave extraTdd
                    double extraTdd = Double.parseDouble(keto.getBloodGlucoseLevel().get(keyGS).get("extraTdd"));
                    correctionBolusRecommended = correctionBolusRecommended.add(tdd.multiply(BigDecimal.valueOf(extraTdd)));
                }
            }
            else if(gsCurrently.compareTo(BigDecimal.valueOf(250))>=0 && gsCurrently.compareTo(BigDecimal.valueOf(400))<0){
                keyGS = "BG = 250-400 mg/dL";
                if(keto.getBloodGlucoseLevel().get(keyGS).containsKey("extraTdd")){ //verifica antes si contiene un Map con clave extraTdd
                    double extraTdd = Double.parseDouble(keto.getBloodGlucoseLevel().get(keyGS).get("extraTdd"));
                    correctionBolusRecommended = correctionBolusRecommended.add(tdd.multiply(BigDecimal.valueOf(extraTdd)));
                }
            }
            else if(gsCurrently.compareTo(BigDecimal.valueOf(400))>=0){
                keyGS = "BG > 400 mg/dL";
                if(keto.getBloodGlucoseLevel().get(keyGS).containsKey("extraTdd")){ //verifica antes si contiene un Map con clave extraTdd
                    double extraTdd = Double.parseDouble(keto.getBloodGlucoseLevel().get(keyGS).get("extraTdd"));
                    correctionBolusRecommended = correctionBolusRecommended.add(tdd.multiply(BigDecimal.valueOf(extraTdd)));
                }
            }
        }
        if(sports.equals(Boolean.TRUE)) //Si señala aplicar corrección y enseguida realizar deporte entonces poner la mitad de la dosis de correción sugerida
            correctionBolusRecommended = correctionBolusRecommended.multiply(BigDecimal.valueOf(0.50), new MathContext(3, RoundingMode.HALF_EVEN));
        System.out.println("Correction dose: "+correctionBolusRecommended);
          return correctionBolusRecommended;          
    }
    
    public Text[] TextFlowPrinter(){
        List<Text> textList = new ArrayList<>();
        Text[] textArray = new Text[]{};
        
        Text doseText = new Text(String.format("%s%n", "Bolo de Correción Recomendado (Correction dose): "));
        Text correctionDoseText = new Text(String.format("%s%n%n", (correctionBolusRecommended.compareTo(BigDecimal.ZERO)>=0)? correctionBolusRecommended + " unidades de insulina" : "No requiere ningún bolo de correción"));
        Text recomendationText = new Text(String.format("%s%n%s%n%n", "\"Cerciórese que glucosa en sangre haya disminuido de 2h a 3h posterior al bolo, de lo contrario considere administrar un segundo bolo de correción para llevar a glucosa objetivo\"","\"Si glucosa en sangre sigue igual o incluso más alta que antes, considere cambiar insulina, equipo de infusion o ambos y corregir idealmente mediante inyección de insulina.\""));
        doseText.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
        doseText.setFill(Color.CORNFLOWERBLUE);
        correctionDoseText.setFont(Font.font("Verdana", 15));
        correctionDoseText.setFill(Color.DEEPSKYBLUE);
        recomendationText.setFill(Color.DODGERBLUE);
        recomendationText.setFont(Font.font("Verdana", 15));
        
        Text dataText = new Text(String.format("%s%n%n", "Bolo calculado en base a:"));
        dataText.setFill(Color.MEDIUMSEAGREEN);
        dataText.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 17));
        
        Text fsiText = new Text(String.format("%s%n", "Factor de Sensibilidad a la Insulina(FSI) [Sensitivity Factor (ISF)]: "));
        Text fsiUserText = new Text(String.format("%s %s %s%n%n", (fsiKnown)? a1c_fsi : fsiCalculado, "mg/dL", "por cada 1U de insulina" ));
        fsiText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        fsiText.setFill(Color.OLIVE);
        fsiUserText.setFont(Font.font("Verdana", 13));
        fsiUserText.setFill(Color.OLIVEDRAB);
        
        Text gscText = new Text(String.format("%s%n", "Glucosa Actual (BG Current):"));
        Text gsCurrentlyText = new Text(String.format("%s %s%n%n", gsCurrently,"mg/dL" ));
        gscText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        gscText.setFill(Color.DARKORANGE);
        gsCurrentlyText.setFont(Font.font("Verdana", 13));
        gsCurrentlyText.setFill(Color.CORAL);
        
        Text gstText = new Text(String.format("%s%n", "Glucosa Objetivo (BG Target):"));
        //Muestra resultado en base a si se modero glucosa a corregir en 200 mg/dL, teniendo en cuenta si se uso tendencia de glucosa o no, ya que para calcular la dosis por tendecia se aumenta la GS solo si no sobrepasa el limite de 200 mg/dL de lo contrario se usa la formula de aumentar la dosis de corrección un %.
        Text gsTargetText = new Text(String.format("%s%n%s%s%n", (targetGlucoseLimited)? gsCurrently.subtract(BigDecimal.valueOf(200)) + "mg/dL (Limitado/Limited)" : gsTarget + " mg/dL", 
                (targetGlucoseLimited && trendArrow==null)? "Se limita a maximo 200 mg/dL la glucosa a reducir ya que (Glucemia Actual - Glucemia Objetivo) = " + gsCurrently.subtract(gsTarget) + " mg/dL > 200 mg/dL" + ", para evitar reducirla de golpe" : "", 
                (targetGlucoseLimited && trendArrow!=null)?  "Se limita a maximo 200 mg/dL la glucosa a reducir ya que (Glucemia Actual + Ajuste por Tendencia MCG) - Glucosa Objetivo = " + gsCurrently.add(BigDecimal.valueOf(trendArrow.getAddBG())).subtract(gsTarget) + " mg/dL > 200 mg/dL" + ", para evitar reducirla de golpe": ""
                /*(!targetGlucoseLimited)? : "")*/));
        Text gsLimitedText = new Text(String.format("%s%n%n", (targetGlucoseLimited)? "¡Objetivo Limitado!, Revisar glucosa 3h a 4h despues del bolo, según resultado considere segundo bolo de corrección para llevar glucosa a objetivo":""));
        gstText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        gstText.setFill(Color.STEELBLUE);
        gsTargetText.setFont(Font.font("Verdana", 13));
        gsTargetText.setFill(Color.DODGERBLUE);
        gsLimitedText.setFont(Font.font("Verdana", FontWeight.EXTRA_LIGHT, 13));
        gsLimitedText.setFill(Color.ROYALBLUE);
        
        Text tddText = new Text(String.format("%s%n", "Dosis Diaria Total (TDD):"));
        Text tddUserText = new Text(String.format("%s %s%n%n", tdd,"U/día" ));
        tddText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        tddText.setFill(Color.MEDIUMSLATEBLUE);
        tddUserText.setFont(Font.font("Verdana", 13));
        tddUserText.setFill(Color.VIOLET);
        
        Text sportText = new Text(String.format("%s%n", "¿Se ejercitará (de moderado a intenso) justo despues de la corrección? (Will perform aerobic/cardiovascular exercise?)"));
        Text sportUserText = new Text(String.format("%s%n", (sports)? "Sí, por lo que se calculó la mitad de la dosis de corrección habitual; no olvide beber agua." : "NO, pero recuerde que tan solo caminar 10-15 minutos tiene efecto inmediato en la glucosa"));
        Text sportKetoText = new Text(String.format("%s%n", (keto!=Ketoacidosis.NORMAL && keto != null)? "ADVERTENCIA: Abstenerse de realizar ejercicio cuando la glucemia se encuentra por encima de 250-290 mg/dl y las cetonas sean positivas > 0.6 mmol/L": ""));
        Text sportKetoTip = new Text(String.format("%s%n%n", (keto!=Ketoacidosis.NORMAL && keto != null)? "CONSEJO: Prefiera ejercicio de baja intensidad como caminar, pasear en bicicleta o nadar en lugar de alta intensidad como levantar pesas o correr" : ""));
        sportText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        sportText.setFill(Color.MEDIUMSLATEBLUE);
        sportUserText.setFont(Font.font("Verdana", 13));
        sportUserText.setFill(Color.BLUEVIOLET);
        sportKetoText.setFont(Font.font("Verdana", 13));
        sportKetoText.setFill(Color.STEELBLUE);
        sportKetoTip.setFont(Font.font("Verdana", 13));
        sportKetoTip.setFill(Color.VIOLET);
        
        Text lowText = new Text(String.format("%s%n", "¿Ha presentado hipoglicemia luego de un bolo de correción? (Frequent Hypoglycemia after correction?)"));
        Text lowUserText = new Text(String.format("%s%n%n", (low)? "SÍ, por lo que su FSI se ha aumentado un 10% para hacer su bolo de correción más pequeño" : "NO, por lo que su FSI no se ha modificado" ));
        lowText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        lowText.setFill(Color.CRIMSON);
        lowUserText.setFont(Font.font("Verdana", 13));
        lowUserText.setFill(Color.ORANGERED);
        
        Text highText = new Text(String.format("%s%n", "¿Ha continuado en hiperglicemia luego de 2h-3h de un bolo de correción? (Frequent Hyperglycemia after correction?)"));
        Text highUserText = new Text(String.format("%s%n%n", (high)? "SÍ, por lo que su FSI se ha reducido un 10% para hacer su bolo de correción más grande" : "NO, por lo que su FSI no se ha modificado" ));
        highText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        highText.setFill(Color.GOLDENROD);
        highUserText.setFont(Font.font("Verdana", 13));
        highUserText.setFill(Color.DARKGOLDENROD);
        
        Text trendText = new Text(String.format("%s%n", "Flechas de tendencia del MCG (Trend Arrows):"));
        Text trendUserText = new Text(String.format("%s%n%s%n%n", (trendArrow!=null)? trendArrow.getWhatsMean() : "NO APLICA" , (trendArrow!=null)? trendArrow.getAdjustDose() : "" ));
        trendText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        trendText.setFill(Color.HOTPINK);
        trendUserText.setFont(Font.font("Verdana", 13));
        trendUserText.setFill(Color.MEDIUMVIOLETRED);
        
        Text ketoText = new Text(String.format("%s%n", "Nivel de Cetonas en sangre u orina (Ketones level):"));
        Text ketoUserText = new Text(String.format("%s%n", (keto!=null)? keto.getBloodKetoneLevel() + ">> " +  keto.getWhatItMeans() : "NO APLICA" ));
        Text ketoActions = new Text(String.format("%s%n%n", (/*keto!=null && */keyGS != null)? keto.getBloodGlucoseLevel().get(keyGS).get("action") : ""));
        ketoText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        ketoText.setFill(Color.FIREBRICK);
        ketoUserText.setFont(Font.font("Verdana", 13));
        ketoUserText.setFill(Color.RED);
        ketoActions.setFont(Font.font("Verdana", 13));
        ketoActions.setFill(Color.CRIMSON);
        
        textList.add(doseText);
        textList.add(correctionDoseText);
        textList.add(recomendationText);
        textList.add(dataText);
        textList.add(fsiText);
        textList.add(fsiUserText);
        textList.add(gscText);
        textList.add(gsCurrentlyText);
        textList.add(gstText);
        textList.add(gsTargetText);
        textList.add(gsLimitedText);
        textList.add(sportText);
        textList.add(sportUserText);
        textList.add(sportKetoText);
        textList.add(sportKetoTip);
        textList.add(lowText);
        textList.add(lowUserText);
        textList.add(highText);
        textList.add(highUserText);
        textList.add(trendText);
        textList.add(trendUserText);
        textList.add(ketoText);
        textList.add(ketoUserText);
        textList.add(ketoActions);
        /*Text dataText = new Text(String.format("%s%n %s%n %s%n %s%n %s%n %s%s%n %s%s%n %s%s%n %s%s%n %s%s%n%n", "De acuerdo a:", 
                (fsiKnown)? "Factor de Sensibilidad a la Insulina (FSI): " + a1c_fsi + " mg/dL" :"Factor de Sensibilidad a la Insulina (FSI): " + fsiCalculado + " mg/dL",
                "Glucosa Actual: " + gsCurrently + " mg/dL",
                "Glucosa Objetivo: " + gsTarget + " mg/dL",
                "Dosis Diaria Total (TDD): " + tdd + " U/día",
                "¿Se ejercitara luego de la corrección? ", (sports)? "Sí":"No",
                "¿Ha presentado hipoglicemia luego de una correción? ", (low)?"Sí":"No",
                "¿Ha presentado hiperglicemia luego de una correción? ", (high)? "Sí":"No",
                "Flechas de tendencia del MCG: ",  (trendArrow!=null)? trendArrow.getWhatsMean() : "No Aplica" ,
                "Nivel de Cetonas en sangre u orina: ", (keto!=null)? keto.getWhatItMeans()+ " " + keto.getBloodKetoneLevel() : "No Aplica" ));
        dataText.setFill(Color.SEAGREEN);
        dataText.setFont(Font.font("Verdana", FontWeight.NORMAL, 15));
        
        textList.add(resultadoText);
        textList.add(dataText);*/
                
        textArray = textList.toArray(textArray);
        
        return textArray;
    }   
}