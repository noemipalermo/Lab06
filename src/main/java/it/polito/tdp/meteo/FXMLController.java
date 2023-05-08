/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {

	Model model;
	List<Citta> citta;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	
    	int mese = this.boxMese.getValue();
    	List<Citta> sequenzaCitta = model.trovaSequenza(mese);
    	
    	txtResult.appendText(sequenzaCitta + "\n");
	}
    
    
    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	
    	int mese = this.boxMese.getValue();
    	Map<String, Integer> umiditaCitta = new HashMap<>();
    	
    	umiditaCitta = model.getUmiditaMedia(mese);
    	
    	for(String citta : umiditaCitta.keySet()) {
    		this.txtResult.appendText("L'umidità media di "+citta+ " nel mese di "+this.nomeMese(mese)+" è stata di "+umiditaCitta.get(citta)+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

        for(int mese=1; mese<=12; mese++) {
        	this.boxMese.getItems().add(mese);
        }
    }

	public void setModel(Model model) {
		this.model=model;
		
	}

	public String nomeMese(int mese) {
		if(mese==1) {
			return "Gennaio";
		}else if(mese==2) {
			return "Febbraio";
		}else if(mese==3) {
			return "Marzo";
		}else if(mese==4) {
			return "Aprile";
		}else if(mese==5) {
			return "Maggio";
		}else if(mese==6) {
			return "Giugno";
		}else if(mese==7) {
			return "Luglio";
		}else if(mese==8) {
			return "Agosto";
		}else if(mese==9) {
			return "Settembre";
		}else if(mese==10) {
			return "Ottobre";
		}else if(mese==11) {
			return "Novembre";
		}else if(mese==12) {
			return "Dicembre";
		}
		return null;
	}
}

