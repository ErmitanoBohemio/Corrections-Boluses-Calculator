package com.deitel.diabetescorrectionsapp;

import com.diabetescorrectionapp.model.Correction;
import com.diabetescorrectionapp.model.KetoCells;
import com.diabetescorrectionapp.model.Ketoacidosis;
import com.diabetescorrectionapp.model.TrendArrows;
import com.diabetescorrectionapp.model.TrendArrowsCell;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.css.CssMetaData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class PrimaryController implements Initializable{

    @FXML
    private TextField tddTextField;
    @FXML
    private TextField gsCurrentTextField;
    @FXML
    private ComboBox gsTargetComboBox;
    @FXML
    private JFXToggleButton fsiOrA1cToggleButton;
    @FXML
    private Label fsiOrA1cLabel;
    @FXML
    private TextField fsiOrA1cTextField;
    @FXML
    private Tooltip fsiOra1cTooltip;
    @FXML
    private ComboBox trendArrowComboBox;
    @FXML
    private ComboBox ketoacidosisComboBox;
    @FXML
    private JFXToggleButton sportsToggleButton;
    @FXML
    private JFXToggleButton lowToggleButton;
    @FXML
    private JFXToggleButton highToggleButton;
    @FXML
    private Button calcularButton;
    @FXML
    private Button reiniciarButton;
    @FXML
    private TextFlow resultadoTextFlow;
    
    @FXML
    private ToggleGroup groupToggle;
   
    //Datos requeridos para calcular correción
    private Double gsTarget; //almacena valor de glucosa objetivo seleccionado por usuario (120 o 140 mg/dL)
    private TrendArrows trendArrow;
    private Ketoacidosis ketoLevel;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //resultadoTextFlow.
        
        //Para hacer que los botones en el ButtonBar no sean todos iguales, se pone propiedad en false y su modifica el prefSize de los botones al tamanio deseado
        /*By default all buttons are uniformly sized in a ButtonBar, meaning that all buttons take the width of 
        the widest button. It is possible to opt-out of this on a per-button basis, but calling the 
        setButtonUniformSize(Node, boolean) method with a boolean value of false. */
        ButtonBar.setButtonUniformSize(calcularButton, false);
        ButtonBar.setButtonUniformSize(reiniciarButton, false);

        //A cada JXToogleButton le asigno la variable Boolean que se usa en el evento de ChangeListener
        fsiOrA1cToggleButton.setUserData(Boolean.FALSE);
        fsiOrA1cToggleButton.setToggleLineColor(Color.HONEYDEW);
        fsiOrA1cToggleButton.setToggleColor(Color.CHARTREUSE);
        
        sportsToggleButton.setUserData(Boolean.FALSE);
        sportsToggleButton.setToggleColor(Color.BLUEVIOLET);
        sportsToggleButton.setToggleLineColor(Color.LAVENDER);
        
        lowToggleButton.setUserData(Boolean.FALSE);
        lowToggleButton.setToggleColor(Color.CRIMSON);
        lowToggleButton.setToggleLineColor(Color.MISTYROSE);
        
        highToggleButton.setUserData(Boolean.FALSE);
        highToggleButton.setToggleColor(Color.GOLD);
        highToggleButton.setToggleLineColor(Color.LIGHTYELLOW);
        
        gsTarget = 140.0; //valor de glucosa objetivo por defecto
        
        //Creo elementos y los añado al ComboBox Glucosa Objetivo
        String[] gsTargetsArray =  new String[]{"120 mg/dL", "140 mg/dL"};
        gsTargetComboBox.setItems(FXCollections.observableArrayList(gsTargetsArray));
        gsTargetComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldTarget, String newTarget) {
                if(newTarget.startsWith("120"))
                    gsTarget = 120.0;
                else
                    gsTarget = 140.0;
            }
        });
        
        //Creo elementos y los añado al ComboBox Linea de tendencia
        trendArrowComboBox.setItems(FXCollections.observableArrayList(null, TrendArrows.SLOWLY_RISING, TrendArrows.RISING, TrendArrows.RAPIDLY_RISING));
        trendArrowComboBox.getSelectionModel().selectFirst(); //Selecciona primer elementos del ComboBox
        trendArrowComboBox.valueProperty().addListener(new ChangeListener<TrendArrows>(){
            @Override
            public void changed(ObservableValue<? extends TrendArrows> ov, TrendArrows oldValue, TrendArrows newValue) {
                trendArrow = newValue;
            }
            
        });
        
        trendArrowComboBox.setCellFactory((listView) -> new TrendArrowsCell());
        
        //Creo elementos y los añado al ComboBox Ketocidosis
        ketoacidosisComboBox.setItems(FXCollections.observableArrayList(null, Ketoacidosis.NORMAL, Ketoacidosis.LIGERAMENTE_ELEVADO, Ketoacidosis.RIESGO_CETOACIDOSIS, Ketoacidosis.ACUDIR_A_URGENCIAS));
        ketoacidosisComboBox.getSelectionModel().selectFirst(); //Selecciona la primer elemento del ComboBox
        ketoacidosisComboBox.valueProperty().addListener(new ChangeListener<Ketoacidosis>(){
            @Override
            public void changed(ObservableValue<? extends Ketoacidosis> ov, Ketoacidosis oldValue, Ketoacidosis newValue) {
                ketoLevel = newValue; //almacena nivel de Ketonas seleccionado
                //if(sportsToggleButton.isSelected()){ //No se necesita este if, ya que sino permitiría mover boton "sports" en casos positivos de cetonas lo cual no es correcto
                    //Si Nivel de Cetonas es mayor o igual a 0.6 mmol/L
                    if(newValue == Ketoacidosis.LIGERAMENTE_ELEVADO || newValue == Ketoacidosis.RIESGO_CETOACIDOSIS || newValue == Ketoacidosis.ACUDIR_A_URGENCIAS){
                        sportsToggleButton.setSelected(false); //Deselecciona SportsToogleButton ya que no se recomienda actividad fisica con ese nivel de cetonas 
                        sportsToggleButton.setDisable(true); //deshabilita SportsToogleButton para evitar sea seleccionado
                    }
                //}
                    else if(newValue == Ketoacidosis.NORMAL || newValue == null)
                        sportsToggleButton.setDisable(false); //habilita nuevamente el SportsToogleButton
            }      
        });
        
        //Se usa para personalizarel aspecto que tendra cada celda de este ComboBox segun elemento.
        ketoacidosisComboBox.setCellFactory((listView)-> new KetoCells(ketoacidosisComboBox));
        /*ketoacidosisComboBox.buttonCellProperty().bind(Bindings.createObjectBinding(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                StackPane arrow = (StackPane) ketoacidosisComboBox.lookup(".arrow-button");
                if(arrow != null)
                    arrow.setBackground(ketoLevel.getColorKetoLevel());
            }
        }, ketoacidosisComboBox.valueProperty()));*/
        
       
        //Escucha evento que reacciona si se tiene valor de Sensililidad a la insulina o no
        fsiOrA1cToggleButton.selectedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldSelect, Boolean newSelect) {
                if(newSelect.equals(Boolean.TRUE))
                {
                    fsiOrA1cLabel.setText("Factor de Corrección o Sensilidad a la Insulina* (?)");
                    fsiOrA1cLabel.setTextFill(Color.TEAL);
                    fsiOrA1cLabel.setBackground(new Background(new BackgroundFill(Color.HONEYDEW, CornerRadii.EMPTY, Insets.EMPTY)));
                    fsiOrA1cTextField.setPromptText("mg/dl");
                    fsiOra1cTooltip.setText("Cuántos mg/dL una unidad de insulina rápida reduce la glucosa en un período de 2 a 3 horas"); //actualiza texto de Tooltip para el FSI
                    fsiOrA1cTextField.setText(""); //limpia texto del TextField
                    fsiOrA1cToggleButton.setText("SI");
                    fsiOrA1cToggleButton.setUserData(Boolean.TRUE);
                }
                else{
                    fsiOrA1cLabel.setText("Resultado de Hemoglobina Glicosilada Reciente (HbA1c)* (?)");
                    fsiOrA1cLabel.setTextFill(Color.PURPLE);
                    fsiOrA1cLabel.setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, CornerRadii.EMPTY, Insets.EMPTY)));
                    fsiOrA1cTextField.setPromptText("5.0 - 12.0 %");
                    fsiOra1cTooltip.setText("Usa resultado de HbA1c reciente para calcular Factor de sensibilidad a la insulina"); //actualiza texto de Tooltip para el HbA1c
                    fsiOrA1cTextField.setText(""); //limpia texto del TextField
                    fsiOrA1cToggleButton.setText("NO");
                    fsiOrA1cToggleButton.setUserData(Boolean.FALSE);
                }
            }
        });
        
        //Eventos de escucha de los JFXToogleButton
        //fsiOrA1cToggleButton.selectedProperty().addListener(new ToogleChangeListener(fsiOrA1cToggleButton)); //no se necesita por que se usa el ChangeListener de arriba
        //sportsToggleButton.selectedProperty().addListener(new ToogleChangeListener(sportsToggleButton));
        sportsToggleButton.selectedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if(newValue.equals(Boolean.TRUE) && (ketoLevel == null || ketoLevel == Ketoacidosis.NORMAL)){ //solo permite calcular el modo deporte solo si no se tienen cetonas o estan en un nivel normal 
                        sportsToggleButton.setText("SÍ");
                        sportsToggleButton.setUserData(Boolean.TRUE);
                    }
                else {
                    sportsToggleButton.setText("NO");
                    sportsToggleButton.setUserData(Boolean.FALSE);
                    }
            }
        });
        
        lowToggleButton.selectedProperty().addListener(new ToogleChangeListener(lowToggleButton));
        highToggleButton.selectedProperty().addListener(new ToogleChangeListener(highToggleButton));

        tddTextField.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>(
        ) {
            @Override
            public void handle(KeyEvent event) {
                if(!Character.isDigit(event.getCharacter().charAt(0)) && event.getCharacter().charAt(0)!='.') //Ignorar todo lo que no sea un digito (0...9) ó '.'
                    event.consume();
                if(event.getCharacter().charAt(0)=='.' && tddTextField.getText().contains(".")) //Si se teclea '.' y ya se ingreso '.' antes, ignorar
                    event.consume();
                if(tddTextField.getText().length()>5 && tddTextField.getText().matches("\\d{3}\\.\\d{2}")  ) //Si ya tiene contiene formato ###.## ignora cualquier caracter 
                {event.consume(); }
                if(tddTextField.getText().length()>4 && tddTextField.getText().matches("\\d{2}\\.\\d{2}") ) //Si ya tiene contiene formato ##.## ignora cualquier caracter
                {event.consume(); }
                if(tddTextField.getText().length()>3 && tddTextField.getText().matches("\\d{1}\\.\\d{2}")) //Si ya tiene contiene formato #.## ignora cualquier caracter
                { event.consume(); }
                if(tddTextField.getText().length()>2 && tddTextField.getText().matches("\\d{3}") && event.getCharacter().charAt(0)!= '.') //Si ya tiene contiene formato ### ignora cualquier caracter que no sea '.'
                { event.consume(); }
            }
        });
        
        gsCurrentTextField.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>(
        ) {
            @Override
            public void handle(KeyEvent event) {
                if(!Character.isDigit(event.getCharacter().charAt(0)))
                    event.consume();
                if(gsCurrentTextField.getText().length()>2)
                    event.consume();
            }
        });
        
        fsiOrA1cTextField.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>(
        ) {
            @Override
            public void handle(KeyEvent event) {
                char charTyped = event.getCharacter().charAt(0);
                String valorTyped = fsiOrA1cTextField.getText();
                int valorLength = fsiOrA1cTextField.getText().length();
                
                if(!Character.isDigit(charTyped) && charTyped != '.')
                    event.consume();
                if(fsiOrA1cToggleButton.isSelected()){ //Se conoce FSI solo acepta nuneros de hasta tres cifras sin puntos
                    if(valorLength > 2 || charTyped=='.') //Ignorar todo valor que no sea #, ## o ###, es decir q Si lo escrito en el campo A1c tiene más dos digitos (##..), no cotiene ya un "." además lo tecleado no es un "."
                        event.consume(); //ignore lo tecleado
                    }
                else{
                    /*if(valorTyped.matches("1") && charTyped>51 && charTyped!='.') //Verifica que solo se pueda escribir numeros menores a 14.0
                        event.consume();*/
                    if(valorLength<1 && charTyped=='.') //impide escribir '.' sin antes escribir un digito
                        event.consume();
                    if(valorTyped.contains(".") && charTyped == '.') //Si el campo A1c ya contiene un "." y se tecleo otro "."
                        event.consume(); //ignore lo tecleado
                    if(valorLength > 4 && valorTyped.matches("\\d{2}\\.\\d{2}"))
                        event.consume();
                    if(valorLength > 3 && valorTyped.matches("\\d{1}\\.\\d{2}") )
                        event.consume();
                    if(valorLength > 1 && charTyped != '.' && valorTyped.matches("\\d{2}")) //ignorar todo valor mayor a un digito diferente a un '.' y tenga la forma ## (osea que ya sea de dos digitos lo escrito en el campo)
                        event.consume();
                    }
                }
            });
    }
    
    @FXML
    private void calcularCorreccion(ActionEvent event){
        //Comprueba que se hayan ingresado algun valor para los datos Tdd, GS Actual y FSI_A1c 
        if( (!tddTextField.getText().isEmpty() || tddTextField.getText() != "" || !tddTextField.getText().isBlank())
               && (!gsCurrentTextField.getText().isEmpty() || gsCurrentTextField.getText() != "" ||  !gsCurrentTextField.getText().isBlank())
               && (!fsiOrA1cTextField.getText().isEmpty() || fsiOrA1cTextField.getText() != "" || !fsiOrA1cTextField.getText().isBlank()) ){
            
            double tdd = Double.valueOf(tddTextField.getText());
            double gsCurrently = Double.parseDouble(gsCurrentTextField.getText());
            double a1c_fsi = Double.parseDouble(fsiOrA1cTextField.getText());
            var fsiKnown = (Boolean) fsiOrA1cToggleButton.getUserData();
            var sports = (Boolean) sportsToggleButton.getUserData();
            var lows = (Boolean) lowToggleButton.getUserData();
            var highs = (Boolean) highToggleButton.getUserData();

            resultadoTextFlow.getChildren().clear(); //Limpia cualquier texto en el area TextFlow

            //Comprueba que los datos ingresados para Tdd y A1c_Fsi sean mayores a cero
            if( tdd > 0 && a1c_fsi > 0 ){

            Correction datosProporcionados = new Correction(BigDecimal.valueOf(tdd), BigDecimal.valueOf(gsCurrently), BigDecimal.valueOf(gsTarget), BigDecimal.valueOf(a1c_fsi), trendArrow, ketoLevel, fsiKnown, lows, highs, sports);
            resultadoTextFlow.getChildren().addAll(datosProporcionados.TextFlowPrinter());
            }
        }
        else { //Si falta datos indipensables para el calculo como son Tdd, GS actual, GS objetivo y FSI o HbA1c reciente.
            resultadoTextFlow.getChildren().clear(); //limpia area del resultado
            Text txt = new Text("Campos marcados con ( * ) son obligatorios");
            txt.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
            txt.setFill(Color.RED);
            resultadoTextFlow.getChildren().add(txt);
        }
    }
    
    @FXML
    private void reiniciar(ActionEvent event){
        tddTextField.setText("");
        gsCurrentTextField.setText("");
        fsiOrA1cTextField.setText("");
        gsTargetComboBox.getSelectionModel().selectLast(); //Selecciona 140 mg/dL como el GS Target por defecto
        
        fsiOrA1cToggleButton.setSelected(false); //Desactiva "En NO" FSI Or A1c Toggle Button 
        
        sportsToggleButton.setSelected(false); //Desactiva "En NO" Sports ToggleButton
        //sportsToggleButton.setUserData(false); //pone User Data en false (Valor por defecto)
        
        lowToggleButton.setSelected(false); //Desactiva "En NO" Low ToggleButton
        //lowToggleButton.setUserData(false); //pone User Data en false (Valor por defecto)
        
        highToggleButton.setSelected(false); //Desactiva "En NO" High ToggleButton
        //highToggleButton.setUserData(false); //pone User Data en false (Valor por defecto)
        
        trendArrowComboBox.getSelectionModel().selectFirst(); //Selecciona NO como opción por defecto
        ketoacidosisComboBox.getSelectionModel().selectFirst(); //Selecciona NO como opción por defecto
        resultadoTextFlow.getChildren().clear(); //Limpia del TextFlow Resultados de calculos anteriores
    }
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    
    
    //Clase  que se usa para los eventos de seleccion de los JFXToggleButtons de la app
    class ToogleChangeListener implements ChangeListener<Boolean>{

        JFXToggleButton tgButton;

        public ToogleChangeListener(JFXToggleButton tgButton) {
            this.tgButton = tgButton;
        }
        
        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean oldSelected, Boolean newSelected) {
            if(newSelected.equals(Boolean.TRUE)){
                tgButton.setText("SÍ");
                tgButton.setUserData(Boolean.TRUE);
                //get UserData del JFXToogleButton precionado, asi obtengo la variable de seleccion asociada (sport, low o high) al JFXToogleButton en el initializable method.
                //var selection = tgButton.getUserData(); //Uso var por que a partir de Java 10 puedo dejar que java infiera el tipo de la variable de forma automatica en este caso "Boolean"
                //selection = Boolean.TRUE;
            }
            else {
                tgButton.setText("NO");
                tgButton.setUserData(Boolean.FALSE);
                //get UserData del JFXToogleButton precionado, asi obtengo la variable de seleccion asociada (sport, low o high) al JFXToogleButton en el initializable method.
                //var selection = (Boolean) tgButton.getUserData(); //Uso var por que a partir de Java 10 puedo dejar que java infiera el tipo de la variable de forma automatica en este caso "Boolean"
                //selection = Boolean.FALSE;    
            }
        }
    }
}
