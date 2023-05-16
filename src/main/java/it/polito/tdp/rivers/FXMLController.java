/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.rivers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model = new Model();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxRiver"
    private ComboBox<River> boxRiver; // Value injected by FXMLLoader

    @FXML // fx:id="txtStartDate"
    private TextField txtStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtEndDate"
    private TextField txtEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumMeasurements"
    private TextField txtNumMeasurements; // Value injected by FXMLLoader

    @FXML // fx:id="txtFMed"
    private TextField txtFMed; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader
    
    @FXML
    void handleSimula(ActionEvent event) {
    	this.txtResult.clear();
    	try {
    		Integer k = Integer.parseInt(this.txtK.getText());
    		River river = this.boxRiver.getValue();
    		if (k > 0) {
    			this.txtResult.appendText("INIZIO SIMULAZIONE\n");
    			long tic = System.currentTimeMillis();
    			this.model.simula(river, k);
    			long toc = System.currentTimeMillis();
    			this.txtResult.appendText("Numero di giorni critici: " + this.model.getnGiorniCritici() + "\n");
    			this.txtResult.appendText("Occupazione media del bacino: " + this.model.getOccupazioneMedia() + "\n");
    			this.txtResult.appendText("TERMINE DELLA SIMULAZIONE\n");
    			this.txtResult.appendText("Tempo richiesto: " + (toc - tic) + " ms\n");
    		}
    		else {
    			this.txtResult.appendText("Errore nel formato di input. Il fattore di scala deve essere un numero intero positivo.\n");
    		}
    		
    	} catch (NumberFormatException nfe) {
    		this.txtResult.appendText("Errore nel formato di input. Il fattore di scala deve essere un valore numerico.\n");
    	}
    	
    }

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        this.boxRiver.getItems().setAll(this.model.getRiversIdMap().values());
        this.boxRiver.setOnAction((e) -> {
        	List<Flow> flowsRiver = this.model.getFlowsRiver(this.boxRiver.getValue());
            this.txtStartDate.setText(flowsRiver.get(0).getDay().toString());
            this.txtEndDate.setText(flowsRiver.get(flowsRiver.size() - 1).getDay().toString());
            this.txtNumMeasurements.setText(String.valueOf(flowsRiver.size()));
            this.txtFMed.setText(String.valueOf(this.model.avgFlow(flowsRiver)));
       });
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
